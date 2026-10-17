package com.printeragent.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Enumerated;

import com.printeragent.domain.CollectionStatus;
import com.printeragent.domain.CollectionType;

import com.printeragent.domain.PrinterReading;

public class ReadingRepositoryJPATest {

    @Test
    void shouldSaveinDataBase() {

        JpaManager.initialize();

        ReadingRepositoryJPA repository = new ReadingRepositoryJPA();

        PrinterReading reading = new PrinterReading();

        reading.getId();
        reading.setPrinterId("123456");
        reading.setSerialNumber("K0N245789");
        reading.setTotalPageCount(217658);
        reading.setTonerCapacity(-2);
        reading.setTonerLevel(-3);
        reading.setDrumCapacity(60000);
        reading.setDrumLevel(35000);
        reading.setCollectedAt(LocalDateTime.now());
        reading.setCollectionType(CollectionType.REGULAR);
        reading.setStatus(CollectionStatus.SUCCESS);
        reading.setFailureMessage("Failure message");

        repository.save(reading);

        EntityManager entityManager = JpaManager.getEntityManagerFactory().createEntityManager();

        PrinterReading saveReading = entityManager.find(PrinterReading.class, reading.getId());

        assertNotNull(saveReading);

        assertNotNull(saveReading.getId());
        assertEquals(reading.getPrinterId(), saveReading.getPrinterId());
        assertEquals(reading.getSerialNumber(), saveReading.getSerialNumber());
        assertEquals(reading.getTotalPageCount(), saveReading.getTotalPageCount());
        assertEquals(reading.getTonerCapacity(), saveReading.getTonerCapacity());
        assertEquals(reading.getTonerLevel(), saveReading.getTonerLevel());
        assertEquals(reading.getDrumCapacity(), saveReading.getDrumCapacity());
        assertEquals(reading.getDrumLevel(), saveReading.getDrumLevel());
        assertEquals(
                reading.getCollectedAt().truncatedTo(ChronoUnit.MINUTES),
                saveReading.getCollectedAt().truncatedTo(ChronoUnit.MINUTES));
        assertEquals(reading.getCollectionType(), saveReading.getCollectionType());
        assertEquals(reading.getStatus(), saveReading.getStatus());
        assertEquals(reading.getFailureMessage(), saveReading.getFailureMessage());

    }

}
