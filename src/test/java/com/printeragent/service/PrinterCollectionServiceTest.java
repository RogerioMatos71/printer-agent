package com.printeragent.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import com.printeragent.domain.CollectionStatus;
import com.printeragent.domain.CollectionType;
import com.printeragent.domain.Printer;
import com.printeragent.domain.PrinterReading;
import com.printeragent.snmp.BrotherPrinterProfile;
import com.printeragent.snmp.PrinterProfileResolver;
import com.printeragent.snmp.SnmpReader;
import com.printeragent.snmp.SnmpResult;



public class PrinterCollectionServiceTest {


        

        @Test
        void shouldCollectPrinterReadingSuccessfully() {

                SnmpReader snmpReader = mock(SnmpReader.class);
                PrinterProfileResolver profileResolver = new PrinterProfileResolver();
                PrinterCollectionService service = new PrinterCollectionService(snmpReader, profileResolver);

                Printer printer = new Printer();
                printer.setId("printer-1");
                printer.setManufacturer("Brother");

                BrotherPrinterProfile profile = new BrotherPrinterProfile();
                

                when(snmpReader.readOid(printer, profile.getSerialNumberOid()))
                                .thenReturn(new SnmpResult(
                                                profile.getSerialNumberOid(),
                                                "U64198M1N776620",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTotalPageCountOid()))
                                .thenReturn(new SnmpResult(
                                                profile.getTotalPageCountOid(),
                                                "272617",
                                                true,
                                                null));

                PrinterReading reading = service.collect(printer, CollectionType.REGULAR);

                assertEquals("printer-1", reading.getPrinterId());
                assertEquals("U64198M1N776620", reading.getSerialNumber());
                assertEquals(272617L, reading.getTotalPageCount());
                assertEquals(CollectionType.REGULAR, reading.getCollectionType());
                assertEquals(CollectionStatus.SUCCESS, reading.getStatus());
                assertNotNull(reading.getCollectedAt());
                assertNull(reading.getFailureMessage());
        }

        @Test
        void shouldFailWhenSerialNumberCannotBeRead() {

                SnmpReader snmpReader = mock(SnmpReader.class);
                PrinterProfileResolver profileResolver = new PrinterProfileResolver();
                PrinterCollectionService service = new PrinterCollectionService(snmpReader, profileResolver);

                Printer printer = new Printer();
                printer.setId("printer-1");
                printer.setManufacturer("Brother");

                BrotherPrinterProfile profile = new BrotherPrinterProfile();
               

                when(snmpReader.readOid(printer, profile.getSerialNumberOid()))
                                .thenReturn(new SnmpResult(
                                                profile.getSerialNumberOid(),
                                                null,
                                                false,
                                                "A impressora não respondeu."));

                

                PrinterReading reading = service.collect(printer, CollectionType.REGULAR);

                 

                assertEquals(CollectionStatus.FAILURE, reading.getStatus());
                assertEquals("A impressora não respondeu.", reading.getFailureMessage());
                assertNull(reading.getSerialNumber());
                assertNull(reading.getTotalPageCount());

                verify(snmpReader).readOid(printer, profile.getSerialNumberOid());

                verify(snmpReader, never()).readOid(
                                printer, profile.getTotalPageCountOid());

               
        }

        @Test
        void shouldFailWhenTotalPageCountCannotBeRead() {

                SnmpReader snmpReader = mock(SnmpReader.class);
                PrinterProfileResolver profileResolver = new PrinterProfileResolver();
                PrinterCollectionService service = new PrinterCollectionService(snmpReader, profileResolver);

                Printer printer = new Printer();
                printer.setId("printer-1");
                printer.setManufacturer("Brother");

                BrotherPrinterProfile profile = new BrotherPrinterProfile();
                

                when(snmpReader.readOid(printer, profile.getSerialNumberOid()))
                                .thenReturn(new SnmpResult(
                                                profile.getSerialNumberOid(),
                                                "U64198M1N776620",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTotalPageCountOid()))
                                .thenReturn(new SnmpResult(
                                                profile.getTotalPageCountOid(),
                                                null,
                                                false,
                                                "Não foi possível ler o contador."));

                PrinterReading reading = service.collect(printer, CollectionType.REGULAR);

               

                assertEquals(CollectionStatus.FAILURE, reading.getStatus());
                assertEquals("U64198M1N776620", reading.getSerialNumber());
                assertNull(reading.getTotalPageCount());
                assertEquals("Não foi possível ler o contador.", reading.getFailureMessage());
        }

        @Test
        void shouldFailWhenTotalPageCountIsInvalid() {

                SnmpReader snmpReader = mock(SnmpReader.class);
                PrinterProfileResolver profileResolver = new PrinterProfileResolver();
                PrinterCollectionService service = new PrinterCollectionService(snmpReader, profileResolver);

                Printer printer = new Printer();
                printer.setId("printer-1");
                printer.setManufacturer("Brother");

                BrotherPrinterProfile profile = new BrotherPrinterProfile();
                
                when(snmpReader.readOid(printer, profile.getSerialNumberOid()))
                                .thenReturn(new SnmpResult(
                                                profile.getSerialNumberOid(),
                                                "U64198M1N776620",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTotalPageCountOid()))
                                .thenReturn(new SnmpResult(
                                                profile.getTotalPageCountOid(),
                                                "valor-invalido",
                                                true,
                                                null));

                PrinterReading reading = service.collect(printer, CollectionType.REGULAR);

                assertEquals(CollectionStatus.FAILURE, reading.getStatus());
                assertEquals("U64198M1N776620", reading.getSerialNumber());
                assertNull(reading.getTotalPageCount());
                assertEquals("Contador total inválido.", reading.getFailureMessage());
        }

}
