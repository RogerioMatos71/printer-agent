package com.printeragent.domain;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table (name = "printer_readings")
public class PrinterReading {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String printerId;
    private String serialNumber;
    private Integer totalPageCount;
    private Integer tonerCapacity;
    private Integer tonerLevel;
    private Integer drumCapacity;
    private Integer drumLevel;
    private LocalDateTime collectedAt;

    @Enumerated (EnumType.STRING)
    private CollectionType collectionType;

    @Enumerated (EnumType.STRING)
    private CollectionStatus status;

    private String failureMessage;

    public PrinterReading() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(Integer totalPageCount) {
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
