package com.printeragent.service;

import java.time.LocalDateTime;
import java.util.function.Consumer;

import com.printeragent.domain.CollectionStatus;
import com.printeragent.domain.CollectionType;
import com.printeragent.domain.Printer;
import com.printeragent.domain.PrinterReading;
import com.printeragent.snmp.PrinterProfile;
import com.printeragent.snmp.PrinterProfileResolver;
import com.printeragent.snmp.SnmpReader;
import com.printeragent.snmp.SnmpResult;
import com.printeragent.snmp.SupplyDefinition;

public class PrinterCollectionService {

    private final SnmpReader snmpReader;
    private final PrinterProfileResolver profileResolver;

    public PrinterCollectionService(
            SnmpReader snmpReader,
            PrinterProfileResolver profileResolver) {

        this.snmpReader = snmpReader;
        this.profileResolver = profileResolver;
    }

    public PrinterReading collect(
            Printer printer,
            CollectionType collectionType) {

        PrinterReading reading = new PrinterReading();

        reading.setPrinterId(printer.getId());

        reading.setCollectionType(collectionType);

        reading.setCollectedAt(LocalDateTime.now());

        PrinterProfile profile = profileResolver.resolve(printer);

        SnmpResult serialResult = snmpReader.readOid(
                printer,
                profile.getSerialNumberOid());

        if (!serialResult.isSuccessful()) {

            reading.setStatus(
                    CollectionStatus.FAILURE);

            reading.setFailureMessage(
                    serialResult.getError());

            return reading;
        }

        reading.setSerialNumber(
                serialResult.getValue());

        if (!readIntegerValue(reading, printer, profile.getTotalPageCountOid(),
                "Contador total inválido.", value -> reading.setTotalPageCount(value))) {
            return reading;
        }

        SupplyDefinition tonerDefinition = profile.getTonerDefinition();

        if (!readIntegerValue(reading, printer, tonerDefinition.getCapacityOid(),
                "Capacidade do toner inválida.", value -> reading.setTonerCapacity(value))) {
            return reading;
        }

        if (!readIntegerValue(reading, printer, tonerDefinition.getLevelOid(),
                "Nível do toner inválido.", value -> reading.setTonerLevel(value))) {
            return reading;
        }

        SupplyDefinition drumDefinition = profile.getDrumDefinition();

        if (!readIntegerValue(reading, printer, drumDefinition.getCapacityOid(),
                "Capacidade do cilindro inválida.", value -> reading.setDrumCapacity(value))) {
            return reading;
        }

        if (!readIntegerValue(reading, printer, drumDefinition.getLevelOid(),
                "Nível do cilindro inválido.", value -> reading.setDrumLevel(value))) {
            return reading;
        }

        reading.setStatus(CollectionStatus.SUCCESS);

        return reading;

    }

    public boolean readIntegerValue(PrinterReading reading, Printer printer, String oid, String failureMessage,
            Consumer<Integer> action) {

        SnmpResult snmpResult = snmpReader.readOid(printer, oid);

        if (!snmpResult.isSuccessful()) {
            reading.setStatus(CollectionStatus.FAILURE);

            reading.setFailureMessage(snmpResult.getError());

            return false;

        }

        try {
            Integer value = Integer.parseInt(snmpResult.getValue());

            action.accept(value);
        } catch (NumberFormatException e) {
            reading.setStatus(CollectionStatus.FAILURE);

            reading.setFailureMessage(failureMessage);

            return false;

        }
        return true;

    }
}
