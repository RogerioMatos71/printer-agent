package com.printeragent.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;

public class JpaManagerTest {

    @Test
    void initializeShouldCreateEntityManagerFactory() {

        JpaManager.initialize();

        EntityManager entityManager = JpaManager.getEntityManagerFactory().createEntityManager();

        assertNotNull(entityManager);

        entityManager.close();

    }

    @Test 
    void shouldConnectToDatabase() {

        JpaManager.initialize();

        EntityManager entityManager = JpaManager.getEntityManagerFactory().createEntityManager();

        Object result = entityManager.createNativeQuery("Select 1").getSingleResult();

        entityManager.createNativeQuery("Select 1");

        assertEquals(1, ((Number) result).intValue());

        entityManager.close();

        
    }
    

}
