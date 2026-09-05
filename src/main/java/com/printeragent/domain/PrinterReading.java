package com.printeragent.domain;

import java.time.LocalDateTime;

public class PrinterReading {

    private String id;
    private String printerId;
    private String serialNumber;
    private Long totalPageCount;
    private Integer tonerCapacity;
    private Integer tonerLevel;
    private Integer drumCapacity;
    private Integer drumLevel;
    private LocalDateTime collectedAt;
    private CollectionType collectionType;
    private CollectionStatus status;
    private String failureMessage;

    public PrinterReading() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrinterId() {
        return printerId;
    }

    public void setPrinterId(String printerId) {
        this.printerId = printerId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Long getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(Long totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public Integer getTonerCapacity() {
        return tonerCapacity;
    }

    public void setTonerCapacity(Integer tonerCapacity) {
        this.tonerCapacity = tonerCapacity;
    }

    public Integer getTonerLevel() {
        return tonerLevel;
    }

    public void setTonerLevel(Integer tonerLevel) {
        this.tonerLevel = tonerLevel;
    }

    public Integer getDrumCapacity() {
        return drumCapacity;
    }

    public void setDrumCapacity(Integer drumCapacity) {
        this.drumCapacity = drumCapacity;
    }

    public Integer getDrumLevel() {
        return drumLevel;
    }

    public void setDrumLevel(Integer drumLevel) {
        this.drumLevel = drumLevel;
    }

    public LocalDateTime getCollectedAt() {
        return collectedAt;
    }

    public void setCollectedAt(LocalDateTime collectedAt) {
        this.collectedAt = collectedAt;
    }

    public CollectionType getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(CollectionType collectionType) {
        this.collectionType = collectionType;
    }

    public CollectionStatus getStatus() {
        return status;
    }

    public void setStatus(CollectionStatus status) {
        this.status = status;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

}
