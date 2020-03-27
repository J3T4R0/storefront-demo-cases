package com.storefront.controller;

import com.storefront.kafka.Sender;
import com.storefront.model.*;
import com.storefront.respository.CustomerCasesRepository;
import com.storefront.utilities.SampleData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/customers")
public class CustomerCasesController {

    private CustomerCasesRepository customerCasesRepository;

    private MongoTemplate mongoTemplate;

    @Value("${spring.kafka.topic.cases-cases}") //same thing as cases-get
    private String topic;

    private Sender sender;


    @Autowired
    public CustomerCasesController(CustomerCasesRepository customerCasesRepository,
                                    MongoTemplate mongoTemplate,
                                    Sender sender) {

        this.customerCasesRepository = customerCasesRepository;
        this.mongoTemplate = mongoTemplate;
        this.sender = sender;
    }

    @RequestMapping(path = "/sample/cases", method = RequestMethod.GET)
    public ResponseEntity<String> sampleCases() {

        List<CustomerCases> customerCasesList = customerCasesRepository.findAll();

        for (CustomerCases customerCases : customerCasesList) {
            customerCases.setCases(SampleData.createSampleCasesHistory());
        }

        customerCasesRepository.saveAll(customerCasesList);

        return new ResponseEntity("Sample cases added to customer cases", HttpStatus.OK);
    }

    @RequestMapping(path = "/summary", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Map<String, List<CustomerCases>>> customerSummary() {

        List<CustomerCases> customerCasesList = customerCasesRepository.findAll();
        return new ResponseEntity<>(Collections.singletonMap("customers", customerCasesList), HttpStatus.OK);
    }

    @RequestMapping(path = "/sample/fulfill", method = RequestMethod.GET)
    public ResponseEntity<String> fulfillSampleCases() {

        Criteria elementMatchCriteria = Criteria.where("cases.casesStatusEvents")
                .size(2)
                .elemMatch(Criteria.where("casesStatusType").is(CasesStatusType.CREATED))
                .elemMatch(Criteria.where("casesStatusType").is(CasesStatusType.APPROVED));
        Query query = Query.query(elementMatchCriteria);
        List<CustomerCases> customerCasesList = mongoTemplate.find(query, CustomerCases.class);

        log.info("customerCasesList size: " + customerCasesList.size() + '\n');


        for (CustomerCases customerCases : customerCasesList) {
            FulfillmentRequestEvent fulfillmentRequestEvent = new FulfillmentRequestEvent();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            fulfillmentRequestEvent.setTimestamp(timestamp.getTime());
            fulfillmentRequestEvent.setName(customerCases.getName());
            fulfillmentRequestEvent.setContact(customerCases.getContact());

            Address shippingAddress = customerCases.getAddresses()
                    .stream()
                    .filter(o -> o.getType().equals(AddressType.SHIPPING))
                    .findFirst()
                    .orElse(null);

            fulfillmentRequestEvent.setAddress(shippingAddress);

            try {
                // cases where the first cases status event in list is created...
                // cases where the last cases status event in list is approved...

                Cases pendingCases = customerCases.getCases()
                        .stream()
                        .filter(o -> o.getCasesStatusEvents()
                                .get(0)
                                .getCasesStatusType().equals(CasesStatusType.CREATED))
                        .filter(o -> o.getCasesStatusEvents()
                                .get(o.getCasesStatusEvents().size() - 1)
                                .getCasesStatusType().equals(CasesStatusType.APPROVED))
                        .findFirst()
                        .orElse(null);

                log.info("pending cases: " + pendingCases);

                fulfillmentRequestEvent.setCases(pendingCases);

                sender.send(topic, fulfillmentRequestEvent);

            } catch (NullPointerException ex) {
                log.info(ex.getMessage());
                return new ResponseEntity("No 'Approved' cases found", HttpStatus.NOT_FOUND);
            }

        }
        return new ResponseEntity("All 'Approved' cases sent for fulfillment", HttpStatus.OK);
    }
}
