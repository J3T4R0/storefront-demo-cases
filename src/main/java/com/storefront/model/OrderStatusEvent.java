package com.storefront.model;

import lombok.Data;
import lombok.NonNull;

import java.sql.Timestamp;

@Data
public class CasesStatusEvent {

    @NonNull
    private Long timestamp;

    @NonNull
    private CasesStatusType casesStatusType;

    private String note;

    public CasesStatusEvent() {

        this.timestamp = new Timestamp(System.currentTimeMillis()).getTime();
    }

    public CasesStatusEvent(CasesStatusType casesStatusType) {

        this.timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        this.casesStatusType = casesStatusType;
    }

    public CasesStatusEvent(CasesStatusType casesStatusType, String note) {

        this.timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        this.casesStatusType = casesStatusType;
        this.note = note;
    }
}

