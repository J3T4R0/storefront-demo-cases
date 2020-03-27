package com.storefront.model;

import lombok.Data;
import lombok.NonNull;

import java.sql.Timestamp;

@Data
public class CasesStatusEvent {

    @NonNull
    private Long timestamp;

    @NonNull
    private CasesStatusType orderStatusType;

    private String note;

    public CasesStatusEvent() {

        this.timestamp = new Timestamp(System.currentTimeMillis()).getTime();
    }

    public CasesStatusEvent(CasesStatusType orderStatusType) {

        this.timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        this.orderStatusType = orderStatusType;
    }

    public CasesStatusEvent(CasesStatusType orderStatusType, String note) {

        this.timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        this.orderStatusType = orderStatusType;
        this.note = note;
    }
}

