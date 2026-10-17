package com.printeragent.persistence;

import com.printeragent.domain.PrinterReading;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;

public class ReadingRepositoryJPA implements ReadingRepository {

    @Override
    public void save(PrinterReading reading) {

        EntityManager entityManager = JpaManager.getEntityManagerFactory().createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();

        try {

            transaction.begin();

            entityManager.persist(reading);

            transaction.commit();

        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }

    }

}