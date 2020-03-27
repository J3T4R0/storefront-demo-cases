package com.storefront.model;

import lombok.Data;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

@Data
public class Cases {

    @NonNull
    private String guid;

    @NonNull
    private List<CasesStatusEvent> casesStatusEvents;

    @NonNull
    private List<CasesItem> casesItems;

    public Cases() {

        this.guid = UUID.randomUUID().toString();
    }

    public Cases(List<CasesStatusEvent> casesStatusEvents, List<CasesItem> casesItems) {

        this.guid = UUID.randomUUID().toString();
        this.casesStatusEvents = casesStatusEvents;
        this.casesItems = casesItems;
    }
}
