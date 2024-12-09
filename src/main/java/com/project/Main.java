package com.project;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Main {
   public static void main(String[] args) {
       // Creem el directori data si no existeix
       String basePath = System.getProperty("user.dir") + "/data/";
       File dir = new File(basePath);
       if (!dir.exists()) {
           if (!dir.mkdirs()) {
               System.out.println("Error creating 'data' folder");
           }
       }

       // Inicialitzem la connexió amb Hibernate
       Manager.createSessionFactory();

       // CREATE - Creem les ciutats
       Ciutat refCiutat1 = Manager.addCiutat("Ciutat 1", "País A", 1000000, new HashSet<>());
       Ciutat refCiutat2 = Manager.addCiutat("Ciutat 2", "País B", 500000, new HashSet<>());
       Ciutat refCiutat3 = Manager.addCiutat("Ciutat 3", "País C", 750000, new HashSet<>());

       // CREATE - Creem els ciutadans
       Ciutada refCiutada1 = Manager.addCiutada("Ciutada 1");
       Ciutada refCiutada2 = Manager.addCiutada("Ciutada 2");
       Ciutada refCiutada3 = Manager.addCiutada("Ciutada 3");
       Ciutada refCiutada4 = Manager.addCiutada("Ciutada 4");
       Ciutada refCiutada5 = Manager.addCiutada("Ciutada 5");
       Ciutada refCiutada6 = Manager.addCiutada("Ciutada 6");

       // Associe les ciutats amb els seus ciutadans
       Set<Ciutada> ciutadansCiutat1 = new HashSet<>();
       ciutadansCiutat1.add(refCiutada1);
       ciutadansCiutat1.add(refCiutada2);
       Manager.updateCiutat(refCiutat1.getCiutatId(), refCiutat1.getNom(), refCiutat1.getPais(), refCiutat1.getPoblacio(), ciutadansCiutat1);

       Set<Ciutada> ciutadansCiutat2 = new HashSet<>();
       ciutadansCiutat2.add(refCiutada3);
       ciutadansCiutat2.add(refCiutada4);
       Manager.updateCiutat(refCiutat2.getCiutatId(), refCiutat2.getNom(), refCiutat2.getPais(), refCiutat2.getPoblacio(), ciutadansCiutat2);

       Set<Ciutada> ciutadansCiutat3 = new HashSet<>();
       ciutadansCiutat3.add(refCiutada5);
       ciutadansCiutat3.add(refCiutada6);
       Manager.updateCiutat(refCiutat3.getCiutatId(), refCiutat3.getNom(), refCiutat3.getPais(), refCiutat3.getPoblacio(), ciutadansCiutat3);

       // READ - Mostrem totes les ciutats i els seus ciutadans
       System.out.println("Punt 1: Després de la creació inicial d'elements");
       System.out.println(Manager.collectionToString(Ciutat.class, Manager.listCollection(Ciutat.class, "")));
       System.out.println(Manager.collectionToString(Ciutada.class, Manager.listCollection(Ciutada.class, "")));

       // DELETE - Esborrem el segon ciutadà de cada ciutat
       // Eliminen el segon ciutadà de cada ciutat
       Set<Ciutada> updatedCiutadans1 = new HashSet<>(ciutadansCiutat1);
       updatedCiutadans1.remove(refCiutada2);
       Manager.updateCiutat(refCiutat1.getCiutatId(), refCiutat1.getNom(), refCiutat1.getPais(), refCiutat1.getPoblacio(), updatedCiutadans1);

       Set<Ciutada> updatedCiutadans2 = new HashSet<>(ciutadansCiutat2);
       updatedCiutadans2.remove(refCiutada4);
       Manager.updateCiutat(refCiutat2.getCiutatId(), refCiutat2.getNom(), refCiutat2.getPais(), refCiutat2.getPoblacio(), updatedCiutadans2);

       Set<Ciutada> updatedCiutadans3 = new HashSet<>(ciutadansCiutat3);
       updatedCiutadans3.remove(refCiutada6);
       Manager.updateCiutat(refCiutat3.getCiutatId(), refCiutat3.getNom(), refCiutat3.getPais(), refCiutat3.getPoblacio(), updatedCiutadans3);

       // DELETE - Esborrem la segona ciutat
       Manager.delete(Ciutat.class, refCiutat2.getCiutatId());

       // READ - Mostrem l'estat després d'esborrar el segon ciutadà i la segona ciutat
       System.out.println("Punt 2: Després d'esborrar ciutadans i ciutat");
       System.out.println(Manager.collectionToString(Ciutat.class, Manager.listCollection(Ciutat.class, "")));
       System.out.println(Manager.collectionToString(Ciutada.class, Manager.listCollection(Ciutada.class, "")));

       // Tanquem la connexió amb Hibernate
       Manager.close();
   }
}
