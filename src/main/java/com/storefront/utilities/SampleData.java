package com.storefront.utilities;

import com.storefront.model.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class SampleData {

    public SampleData() {

    }

    public static List<Cases> createSampleCasesHistory() {


        // Random Cases #1
        List<CasesItem> casesItems = getRandomCasesItems();
        List<Cases> casesList = new ArrayList<>();
        List<CasesStatusEvent> casesStatusEventList = new ArrayList<>();
        casesStatusEventList.add(new CasesStatusEvent(CasesStatusType.CREATED));
        casesStatusEventList.add(new CasesStatusEvent(CasesStatusType.REJECTED, "Unable to get data. It was rejected"));
        casesList.add(new Cases(casesStatusEventList, casesItems));

        // Random Cases #2
        casesItems = getRandomCasesItems();
        casesStatusEventList = new ArrayList<>();
        casesStatusEventList.add(new CasesStatusEvent(CasesStatusType.CREATED));
        casesStatusEventList.add(new CasesStatusEvent(CasesStatusType.APPROVED));
        casesStatusEventList.add(new CasesStatusEvent(CasesStatusType.PROCESSING));
        casesStatusEventList.add(new CasesStatusEvent(CasesStatusType.SHIPPED));
        casesStatusEventList.add(new CasesStatusEvent(CasesStatusType.IN_TRANSIT));
        casesStatusEventList.add(new CasesStatusEvent(CasesStatusType.RECEIVED));
        casesList.add(new Cases(casesStatusEventList, casesItems));

        // Random Cases #3
        casesItems = getRandomCasesItems();
        casesStatusEventList = new ArrayList<>();
        casesStatusEventList.add(new CasesStatusEvent(CasesStatusType.CREATED));
        casesStatusEventList.add(new CasesStatusEvent(CasesStatusType.APPROVED));
        casesStatusEventList.add(new CasesStatusEvent(CasesStatusType.PROCESSING));
        casesStatusEventList.add(new CasesStatusEvent(CasesStatusType.ON_HOLD, "Still waiting for data"));
        casesStatusEventList.add(new CasesStatusEvent(CasesStatusType.CANCELLED, "Fetch for data has been cancelled"));
        casesList.add(new Cases(casesStatusEventList, casesItems));

        // Random Cases #4
        casesItems = getRandomCasesItems();
        casesStatusEventList = new ArrayList<>();
        casesStatusEventList.add(new CasesStatusEvent(CasesStatusType.CREATED));
        casesStatusEventList.add(new CasesStatusEvent(CasesStatusType.APPROVED));
        casesStatusEventList.add(new CasesStatusEvent(CasesStatusType.PROCESSING));
        casesStatusEventList.add(new CasesStatusEvent(CasesStatusType.SHIPPED));
        casesStatusEventList.add(new CasesStatusEvent(CasesStatusType.IN_TRANSIT));
        casesStatusEventList.add(new CasesStatusEvent(CasesStatusType.RECEIVED));
        casesStatusEventList.add(new CasesStatusEvent(CasesStatusType.RETURNED, "Data did not make it back in one piece"));
        casesList.add(new Cases(casesStatusEventList, casesItems));

        // Random Cases #5 Pending fulfillment...
        casesItems = getRandomCasesItems();
        casesStatusEventList = new ArrayList<>();
        casesStatusEventList.add(new CasesStatusEvent(CasesStatusType.CREATED));
        casesStatusEventList.add(new CasesStatusEvent(CasesStatusType.APPROVED));
        casesList.add(new Cases(casesStatusEventList, casesItems));

        return casesList;
    }

    private static List<CasesItem> getRandomCasesItems() {

        List<Product> productList = createSampleProducts();
        List<CasesItem> casesItems = new ArrayList<>();
        for (int i = 0; i < getRandomProductQuantity(); i++) {
            casesItems.add(new CasesItem(productList.get(getRandomProductListIndex()), getRandomProductQuantity()));
        }
        return casesItems;
    }
}