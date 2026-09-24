package com.printeragent.service;

import java.time.LocalDateTime;

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

        SnmpResult counterResult = snmpReader.readOid(
                printer,
                profile.getTotalPageCountOid());

        if (!counterResult.isSuccessful()) {

            reading.setStatus(
                    CollectionStatus.FAILURE);

            reading.setFailureMessage(
                    counterResult.getError());

            return reading;
        }

        try {

            long totalPageCount = Long.parseLong(
                    counterResult.getValue());

            reading.setTotalPageCount(
                    totalPageCount);

        } catch (NumberFormatException e) {

            reading.setStatus(
                    CollectionStatus.FAILURE);

            reading.setFailureMessage(
                    "Contador total inválido.");

            return reading;
        }

        reading.setStatus(
                CollectionStatus.SUCCESS);

        SupplyDefinition tonerDefinition = profile.getTonerDefinition();

        SnmpResult tonerSnmpResult = snmpReader.readOid(printer, tonerDefinition.getCapacityOid());

        if (!tonerSnmpResult.isSuccessful()) {
            reading.setStatus(CollectionStatus.FAILURE);

            reading.setFailureMessage(tonerSnmpResult.getError());

            return reading;
        }

        reading.setStatus(CollectionStatus.SUCCESS);

        try {

            Integer tonerCapacity = Integer.parseInt(tonerSnmpResult.getValue());

            reading.setTonerCapacity(tonerCapacity);

        } catch (NumberFormatException e) {
            reading.setStatus(CollectionStatus.FAILURE);

            reading.setFailureMessage("Capacidade do toner inválida.");

            return reading;
        }

        reading.setStatus(CollectionStatus.SUCCESS);

        tonerSnmpResult = snmpReader.readOid(printer, tonerDefinition.getLevelOid());

        if (!tonerSnmpResult.isSuccessful()) {
            reading.setStatus(CollectionStatus.FAILURE);

            reading.setFailureMessage(tonerSnmpResult.getError());

            return reading;
        }

        reading.setStatus(CollectionStatus.SUCCESS);

        try {

            Integer tonerLevel = Integer.parseInt(tonerSnmpResult.getValue());

            reading.setTonerLevel(tonerLevel);

        } catch (NumberFormatException e) {
            reading.setStatus(CollectionStatus.FAILURE);

            reading.setFailureMessage("Nível do toner inválido.");

            return reading;
        }

        reading.setStatus(CollectionStatus.SUCCESS);

        return reading;

    }
}
