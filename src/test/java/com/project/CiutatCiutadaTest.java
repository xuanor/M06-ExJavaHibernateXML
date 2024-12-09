package com.project;

import org.junit.jupiter.api.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CiutatCiutadaTest {

    private static Ciutat testCiutat;
    private static Ciutada testCiutada1;
    private static Ciutada testCiutada2;

    @BeforeAll
    public static void setup() {
        // Inicialitzar Hibernate
        Manager.createSessionFactory();
    }

    @AfterAll
    public static void cleanup() {
        // Tancar la sessió de Hibernate
        Manager.close();
    }

    @Test
    @Order(1)
    public void testCreateCiutat() {
        // Provar la creació d'una nova ciutat
        testCiutat = Manager.addCiutat("Ciutat de Prova", "País Prova", 100000, new HashSet<>());
        assertNotNull(testCiutat, "La ciutat no hauria de ser null després de crear-la");
        assertTrue(testCiutat.getCiutatId() > 0, "La ciutat hauria de tenir un ID vàlid després de crear-la");
        assertEquals("Ciutat de Prova", testCiutat.getNom(), "El nom de la ciutat hauria de coincidir amb l'entrada");
        assertEquals("País Prova", testCiutat.getPais(), "El país de la ciutat hauria de coincidir amb l'entrada");
        assertEquals(100000, testCiutat.getPoblacio(), "La població hauria de coincidir amb l'entrada");
        assertTrue(testCiutat.getCiutadans().isEmpty(), "La nova ciutat hauria de tenir el conjunt de ciutadans buit");
    }

    @Test
    @Order(2)
    public void testCreateCiutadans() {
        // Provar la creació de nous ciutadans
        testCiutada1 = Manager.addCiutada("Ciutada Prova 1");
        testCiutada2 = Manager.addCiutada("Ciutada Prova 2");

        assertNotNull(testCiutada1, "El ciutadà 1 no hauria de ser null després de crear-lo");
        assertNotNull(testCiutada2, "El ciutadà 2 no hauria de ser null després de crear-lo");
        assertTrue(testCiutada1.getCiutadaId() > 0, "El ciutadà 1 hauria de tenir un ID vàlid");
        assertTrue(testCiutada2.getCiutadaId() > 0, "El ciutadà 2 hauria de tenir un ID vàlid");
    }

    @Test
    @Order(3)
    public void testAddCiutadansToCiutat() {
        // Crear un conjunt de ciutadans
        Set<Ciutada> ciutadans = new HashSet<>();
        ciutadans.add(testCiutada1);
        ciutadans.add(testCiutada2);

        // Actualitzar la ciutat amb els nous ciutadans
        Manager.updateCiutat(testCiutat.getCiutatId(), testCiutat.getNom(), testCiutat.getPais(), testCiutat.getPoblacio(), ciutadans);

        // Obtenir la ciutat actualitzada de la base de dades
        Ciutat updatedCiutat = Manager.getCiutatWithCiutadans(testCiutat.getCiutatId());

        assertNotNull(updatedCiutat, "La ciutat actualitzada no hauria de ser null");
        assertEquals(2, updatedCiutat.getCiutadans().size(), "La ciutat hauria de tenir 2 ciutadans");
        assertTrue(updatedCiutat.getCiutadans().contains(testCiutada1), "La ciutat hauria de contenir el ciutadà 1");
        assertTrue(updatedCiutat.getCiutadans().contains(testCiutada2), "La ciutat hauria de contenir el ciutadà 2");
    }

    @Test
    @Order(4)
    public void testUpdateCiutada() {
        // Actualitzar el nom del ciutadà
        String newName = "Ciutada Actualitzat 1";
        Manager.updateCiutada(testCiutada1.getCiutadaId(), newName);

        // Obtenir el ciutadà actualitzat
        Ciutada updatedCiutada = Manager.getById(Ciutada.class, testCiutada1.getCiutadaId());
        assertEquals(newName, updatedCiutada.getName(), "El nom del ciutadà hauria d'estar actualitzat");
    }

    @Test
    @Order(5)
    public void testListCiutadansAndCiutats() {
        // Provar llistar tots els ciutadans i ciutats
        Collection<?> ciutadans = Manager.listCollection(Ciutada.class);
        Collection<?> ciutats = Manager.listCollection(Ciutat.class);

        assertNotNull(ciutadans, "La col·lecció de ciutadans no hauria de ser null");
        assertNotNull(ciutats, "La col·lecció de ciutats no hauria de ser null");
        assertTrue(ciutadans.size() >= 2, "Hauria d'haver-hi almenys 2 ciutadans");
        assertTrue(ciutats.size() >= 1, "Hauria d'haver-hi almenys 1 ciutat");
    }

    @Test
    @Order(6)
    public void testRemoveCiutadaFromCiutat() {
        // Obtenir ciutat amb ciutadans
        Ciutat ciutat = Manager.getCiutatWithCiutadans(testCiutat.getCiutatId());
        Set<Ciutada> ciutadans = new HashSet<>(ciutat.getCiutadans());

        // Eliminar un ciutadà
        ciutadans.remove(testCiutada1);
        Manager.updateCiutat(ciutat.getCiutatId(), ciutat.getNom(), ciutat.getPais(), ciutat.getPoblacio(), ciutadans);

        // Verificar l'actualització
        Ciutat updatedCiutat = Manager.getCiutatWithCiutadans(testCiutat.getCiutatId());
        assertEquals(1, updatedCiutat.getCiutadans().size(), "La ciutat hauria de tenir 1 ciutadà després de l'eliminació");
        assertFalse(updatedCiutat.getCiutadans().contains(testCiutada1), "La ciutat no hauria de contenir el ciutadà eliminat");
    }

    @Test
    @Order(7)
    public void testDeleteCiutada() {
        // Eliminar ciutadans
        Manager.delete(Ciutada.class, testCiutada1.getCiutadaId());
        Manager.delete(Ciutada.class, testCiutada2.getCiutadaId());

        // Verificar l'eliminació
        assertNull(Manager.getById(Ciutada.class, testCiutada1.getCiutadaId()), "El ciutadà 1 hauria d'estar eliminat");
        assertNull(Manager.getById(Ciutada.class, testCiutada2.getCiutadaId()), "El ciutadà 2 hauria d'estar eliminat");
    }

    @Test
    @Order(8)
    public void testDeleteCiutat() {
        // Eliminar ciutat
        Manager.delete(Ciutat.class, testCiutat.getCiutatId());

        // Verificar l'eliminació
        assertNull(Manager.getById(Ciutat.class, testCiutat.getCiutatId()), "La ciutat hauria d'estar eliminada");
    }
}
