package com.printeragent.persistence;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaManager {

   private static EntityManagerFactory entityManagerFactory;

   public static void initialize() {

      if (entityManagerFactory == null) {
         entityManagerFactory = Persistence.createEntityManagerFactory("printerAgentPU");
      }

   }

   public static EntityManagerFactory getEntityManagerFactory() {
      if (entityManagerFactory == null) {
         throw new IllegalStateException("EntityManagerFactory não foi inicializado.");
      }

      return entityManagerFactory;
   }

}
