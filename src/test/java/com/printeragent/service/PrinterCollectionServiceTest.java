package com.printeragent.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

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

@ExtendWith(MockitoExtension.class)
public class PrinterCollectionServiceTest {

        @Mock
        private SnmpReader snmpReader;

        private PrinterProfileResolver profileResolver;
        private PrinterCollectionService service;
        private Printer printer;
        private BrotherPrinterProfile profile;

        @BeforeEach
        void setUp() {

                profileResolver = new PrinterProfileResolver();
                service = new PrinterCollectionService(snmpReader, profileResolver);

                printer = new Printer();
                printer.setId("printer-1");
                printer.setManufacturer("Brother");

                profile = new BrotherPrinterProfile();

        }

        @Test
        void shouldCollectPrinterReadingSuccessfully() {

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

                when(snmpReader.readOid(printer, profile.getTonerDefinition().getCapacityOid()))
                                .thenReturn(new SnmpResult(profile.getTonerDefinition().getCapacityOid(), "-2", true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTonerDefinition().getLevelOid()))
                                .thenReturn(new SnmpResult(profile.getTonerDefinition().getLevelOid(), "-3", true,
                                                null));

                when(snmpReader.readOid(printer, profile.getDrumDefinition().getCapacityOid()))
                                .thenReturn(new SnmpResult(profile.getDrumDefinition().getCapacityOid(), "50000", true,
                                                null));

                when(snmpReader.readOid(printer, profile.getDrumDefinition().getLevelOid()))
                                .thenReturn(new SnmpResult(profile.getDrumDefinition().getLevelOid(), "37033", true,
                                                null));

                PrinterReading reading = service.collect(printer, CollectionType.REGULAR);

                assertEquals("printer-1", reading.getPrinterId());
                assertEquals("U64198M1N776620", reading.getSerialNumber());
                assertEquals(272617, reading.getTotalPageCount());
                assertEquals(CollectionType.REGULAR, reading.getCollectionType());
                assertEquals(CollectionStatus.SUCCESS, reading.getStatus());
                assertEquals(-2, reading.getTonerCapacity());
                assertEquals(-3, reading.getTonerLevel());
                assertEquals(50000, reading.getDrumCapacity());
                assertEquals(37033, reading.getDrumLevel());
                assertNotNull(reading.getCollectedAt());
                assertNull(reading.getFailureMessage());
        }

        @Test
        void shouldFailWhenSerialNumberCannotBeRead() {

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

        @Test
        void shouldFailWhenTonerCapacityCannotBeRead() {
                when(snmpReader.readOid(printer, profile.getSerialNumberOid()))
                                .thenReturn(new SnmpResult(profile.getSerialNumberOid(),
                                                "U64198M1N776620",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTotalPageCountOid()))
                                .thenReturn(new SnmpResult(
                                                profile.getTotalPageCountOid(),
                                                "272617",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTonerDefinition().getCapacityOid()))
                                .thenReturn(new SnmpResult(profile.getTonerDefinition().getCapacityOid(),
                                                null,
                                                false,
                                                "Não foi possível obter a capacidade do toner."));

                PrinterReading reading = service.collect(printer, CollectionType.REGULAR);

                assertEquals(CollectionStatus.FAILURE, reading.getStatus());
                assertEquals("U64198M1N776620", reading.getSerialNumber());
                assertEquals(272617, reading.getTotalPageCount());
                assertNull(reading.getTonerCapacity());
                assertEquals("Não foi possível obter a capacidade do toner.", reading.getFailureMessage());

        }

        @Test
        void shouldFailWhenTonerCapacityIsInvalid() {
                when(snmpReader.readOid(printer, profile.getSerialNumberOid()))
                                .thenReturn(new SnmpResult(profile.getSerialNumberOid(),
                                                "U64198M1N776620",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTotalPageCountOid()))
                                .thenReturn(new SnmpResult(
                                                profile.getTotalPageCountOid(),
                                                "272617",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTonerDefinition().getCapacityOid()))
                                .thenReturn(new SnmpResult(profile.getTonerDefinition().getCapacityOid(),
                                                "valor-invalido",
                                                true,
                                                "Capacidade do toner inválida."));

                PrinterReading reading = service.collect(printer, CollectionType.REGULAR);

                assertEquals(CollectionStatus.FAILURE, reading.getStatus());
                assertEquals("U64198M1N776620", reading.getSerialNumber());
                assertEquals(272617, reading.getTotalPageCount());
                assertNull(reading.getTonerCapacity());
                assertEquals("Capacidade do toner inválida.", reading.getFailureMessage());
        }

        @Test
        void shouldFailWhenTonerLevelCannotBeRead() {
                when(snmpReader.readOid(printer, profile.getSerialNumberOid()))
                                .thenReturn(new SnmpResult(profile.getSerialNumberOid(),
                                                "U64198M1N776620",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTotalPageCountOid()))
                                .thenReturn(new SnmpResult(
                                                profile.getTotalPageCountOid(),
                                                "272617",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTonerDefinition().getCapacityOid()))
                                .thenReturn(new SnmpResult(profile.getTonerDefinition().getCapacityOid(),
                                                "-2",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTonerDefinition().getLevelOid()))
                                .thenReturn(new SnmpResult(profile.getTonerDefinition().getLevelOid(),
                                                null,
                                                false,
                                                "Não foi possível obter o nível do toner."));

                PrinterReading reading = service.collect(printer, CollectionType.REGULAR);

                assertEquals(CollectionStatus.FAILURE, reading.getStatus());
                assertEquals("U64198M1N776620", reading.getSerialNumber());
                assertEquals(272617, reading.getTotalPageCount());
                assertEquals(-2, reading.getTonerCapacity());
                assertNull(reading.getTonerLevel());

                assertEquals("Não foi possível obter o nível do toner.", reading.getFailureMessage());

        }

        @Test
        void shouldFailWhenTonerLevelIsInvalid() {
                when(snmpReader.readOid(printer, profile.getSerialNumberOid()))
                                .thenReturn(new SnmpResult(profile.getSerialNumberOid(),
                                                "U64198M1N776620",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTotalPageCountOid()))
                                .thenReturn(new SnmpResult(
                                                profile.getTotalPageCountOid(),
                                                "272617",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTonerDefinition().getCapacityOid()))
                                .thenReturn(new SnmpResult(profile.getTonerDefinition().getCapacityOid(),
                                                "-2",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTonerDefinition().getLevelOid()))
                                .thenReturn(new SnmpResult(profile.getTonerDefinition().getLevelOid(),
                                                "valor-invalido",
                                                true,
                                                "Nível do toner inválido."));

                PrinterReading reading = service.collect(printer, CollectionType.REGULAR);

                assertEquals(CollectionStatus.FAILURE, reading.getStatus());
                assertEquals("U64198M1N776620", reading.getSerialNumber());
                assertEquals(272617, reading.getTotalPageCount());
                assertNull(reading.getTonerLevel());
                assertEquals("Nível do toner inválido.", reading.getFailureMessage());

        }

        @Test
        void shouldFailWhenDrumCapacityCannotBeRead() {
                when(snmpReader.readOid(printer, profile.getSerialNumberOid()))
                                .thenReturn(new SnmpResult(profile.getSerialNumberOid(),
                                                "U64198M1N776620",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTotalPageCountOid()))
                                .thenReturn(new SnmpResult(
                                                profile.getTotalPageCountOid(),
                                                "272617",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTonerDefinition().getCapacityOid()))
                                .thenReturn(new SnmpResult(profile.getTonerDefinition().getCapacityOid(),
                                                "-2",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTonerDefinition().getLevelOid()))
                                .thenReturn(new SnmpResult(profile.getTonerDefinition().getLevelOid(),
                                                "-3",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getDrumDefinition().getCapacityOid()))
                                .thenReturn(new SnmpResult(profile.getDrumDefinition().getCapacityOid(),
                                                null,
                                                false,
                                                "Não foi possível obter a capacidade do cilindro."));

                PrinterReading reading = service.collect(printer, CollectionType.REGULAR);

                assertEquals(CollectionStatus.FAILURE, reading.getStatus());
                assertEquals("U64198M1N776620", reading.getSerialNumber());
                assertEquals(272617, reading.getTotalPageCount());
                assertEquals(-2, reading.getTonerCapacity());
                assertEquals(-3, reading.getTonerLevel());
                assertNull(reading.getDrumCapacity());
                assertEquals("Não foi possível obter a capacidade do cilindro.", reading.getFailureMessage());

        }

        @Test
        void shouldFailWhenDrumCapacityIsInvalid() {
                when(snmpReader.readOid(printer, profile.getSerialNumberOid()))
                                .thenReturn(new SnmpResult(profile.getSerialNumberOid(),
                                                "U64198M1N776620",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTotalPageCountOid()))
                                .thenReturn(new SnmpResult(
                                                profile.getTotalPageCountOid(),
                                                "272617",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTonerDefinition().getCapacityOid()))
                                .thenReturn(new SnmpResult(profile.getTonerDefinition().getCapacityOid(),
                                                "-2",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTonerDefinition().getLevelOid()))
                                .thenReturn(new SnmpResult(profile.getTonerDefinition().getLevelOid(),
                                                "-3",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getDrumDefinition().getCapacityOid()))
                                .thenReturn(new SnmpResult(profile.getDrumDefinition().getCapacityOid(),
                                                "valor-invalido",
                                                true,
                                                "Capacidade do cilindro inválida."));

                PrinterReading reading = service.collect(printer, CollectionType.REGULAR);

                assertEquals(CollectionStatus.FAILURE, reading.getStatus());
                assertEquals("U64198M1N776620", reading.getSerialNumber());
                assertEquals(272617, reading.getTotalPageCount());
                assertEquals(-2, reading.getTonerCapacity());
                assertEquals(-3, reading.getTonerLevel());
                assertNull(reading.getDrumCapacity());
                assertEquals("Capacidade do cilindro inválida.", reading.getFailureMessage());

        }

        @Test
        void shouldFailWhenDrumLevelCannotBeRead() {
                when(snmpReader.readOid(printer, profile.getSerialNumberOid()))
                                .thenReturn(new SnmpResult(profile.getSerialNumberOid(),
                                                "U64198M1N776620",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTotalPageCountOid()))
                                .thenReturn(new SnmpResult(
                                                profile.getTotalPageCountOid(),
                                                "272617",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTonerDefinition().getCapacityOid()))
                                .thenReturn(new SnmpResult(profile.getTonerDefinition().getCapacityOid(),
                                                "-2",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTonerDefinition().getLevelOid()))
                                .thenReturn(new SnmpResult(profile.getTonerDefinition().getLevelOid(),
                                                "-3",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getDrumDefinition().getCapacityOid()))
                                .thenReturn(new SnmpResult(profile.getDrumDefinition().getCapacityOid(),
                                                "50000",
                                                true,
                                                null));

                 when(snmpReader.readOid(printer, profile.getDrumDefinition().getLevelOid()))
                                .thenReturn(new SnmpResult(profile.getDrumDefinition().getLevelOid(),
                                                null,
                                                false,
                                                "Não foi possível obter o nível do cilindro."));                                



                PrinterReading reading = service.collect(printer, CollectionType.REGULAR);

                assertEquals(CollectionStatus.FAILURE, reading.getStatus());
                assertEquals("U64198M1N776620", reading.getSerialNumber());
                assertEquals(272617, reading.getTotalPageCount());
                assertEquals(-2, reading.getTonerCapacity());
                assertEquals(-3, reading.getTonerLevel());
                assertEquals(50000, reading.getDrumCapacity());
                assertNull(reading.getDrumLevel());
                assertEquals("Não foi possível obter o nível do cilindro.", reading.getFailureMessage());

        }

         @Test
        void shouldFailWhenDrumLevelIsInvalid() {
                when(snmpReader.readOid(printer, profile.getSerialNumberOid()))
                                .thenReturn(new SnmpResult(profile.getSerialNumberOid(),
                                                "U64198M1N776620",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTotalPageCountOid()))
                                .thenReturn(new SnmpResult(
                                                profile.getTotalPageCountOid(),
                                                "272617",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTonerDefinition().getCapacityOid()))
                                .thenReturn(new SnmpResult(profile.getTonerDefinition().getCapacityOid(),
                                                "-2",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getTonerDefinition().getLevelOid()))
                                .thenReturn(new SnmpResult(profile.getTonerDefinition().getLevelOid(),
                                                "-3",
                                                true,
                                                null));

                when(snmpReader.readOid(printer, profile.getDrumDefinition().getCapacityOid()))
                                .thenReturn(new SnmpResult(profile.getDrumDefinition().getCapacityOid(),
                                                "50000",
                                                true,
                                                null));

                 when(snmpReader.readOid(printer, profile.getDrumDefinition().getLevelOid()))
                                .thenReturn(new SnmpResult(profile.getDrumDefinition().getLevelOid(),
                                                "valor-invalido",
                                                true,
                                                "Nível do cilindro inválido."));                                



                PrinterReading reading = service.collect(printer, CollectionType.REGULAR);

                assertEquals(CollectionStatus.FAILURE, reading.getStatus());
                assertEquals("U64198M1N776620", reading.getSerialNumber());
                assertEquals(272617, reading.getTotalPageCount());
                assertEquals(-2, reading.getTonerCapacity());
                assertEquals(-3, reading.getTonerLevel());
                assertEquals(50000, reading.getDrumCapacity());
                assertNull(reading.getDrumLevel());
                assertEquals("Nível do cilindro inválido.", reading.getFailureMessage());

        }

}
