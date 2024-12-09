package com.project;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;

public class Manager {

    private static SessionFactory factory; 
    
    public static void createSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            
            // Agregar los archivos de mapeo en vez de clases anotadas
            configuration.addResource("Ciutat.hbm.xml");
            configuration.addResource("Ciutada.hbm.xml");

            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
                
            factory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) { 
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex); 
        }
    }

    public static void close () {
        factory.close();
    }
  
    public static Ciutat addCiutat(String nom, String pais, int poblacio, Set<Ciutada> ciutadans) {
        Session session = factory.openSession();
        Transaction tx = null;
        Ciutat result = null;
        try {
            tx = session.beginTransaction();
            result = new Ciutat(nom, pais, poblacio, ciutadans);
            session.persist(result);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace(); 
            result = null;
        } finally {
            session.close(); 
        }
        return result;
    }

    // Agregar un Ciutada (Item en el ejemplo original)
    public static Ciutada addCiutada(String name) {
        Session session = factory.openSession();
        Transaction tx = null;
        Ciutada result = null;
        try {
            tx = session.beginTransaction();
            result = new Ciutada(name);
            session.persist(result);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace(); 
            result = null;
        } finally {
            session.close(); 
        }
        return result;
    }

    // Actualizar un Ciutada (Item en el ejemplo original)
    public static void updateCiutada(long itemId, String name) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Ciutada obj = session.get(Ciutada.class, itemId); 
            obj.setName(name);
            session.merge(obj);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace(); 
        } finally {
            session.close(); 
        }
    }

    public static void updateCiutat(long ciutatId, String nom, String pais, int poblacio, Set<Ciutada> ciutadans) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            Ciutat ciutat = session.get(Ciutat.class, ciutatId);
            if (ciutat == null) {
                throw new RuntimeException("Ciutat not found with id: " + ciutatId);
            }
            
            ciutat.setNom(nom);
            ciutat.setPais(pais);
            ciutat.setPoblacio(poblacio);
            
            if (ciutat.getCiutadans() != null) {
                for (Ciutada oldCiutada : new HashSet<>(ciutat.getCiutadans())) {
                    ciutat.removeItem(oldCiutada);
                }
            }
            
            if (ciutadans != null) {
                for (Ciutada ciutada : ciutadans) {
                    Ciutada managedCiutada = session.get(Ciutada.class, ciutada.getCiutadaId());
                    if (managedCiutada != null) {
                        ciutat.addCiutada(managedCiutada);
                    }
                }
            }
            
            session.merge(ciutat);
            tx.commit();
            
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // Obtener una Ciutat con sus Ciutadanos (relación con Items)
    public static Ciutat getCiutatWithCiutadans(long ciutatId) {
        Ciutat ciutat;
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            ciutat = session.get(Ciutat.class, ciutatId);
            // Fetch eagerly the Ciutadans collection
            ciutat.getCiutadans().size();
            tx.commit();
        }
        return ciutat;
    }

    // Obtener un objeto por su ID genérico
    public static <T> T getById(Class<? extends T> clazz, long id) {
        Session session = factory.openSession();
        Transaction tx = null;
        T obj = null;
        try {
            tx = session.beginTransaction();
            obj = clazz.cast(session.get(clazz, id)); 
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace(); 
        } finally {
            session.close(); 
        }
        return obj;
    }

    // Eliminar un objeto por su ID genérico
    public static <T> void delete(Class<? extends T> clazz, Serializable id) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            T obj = clazz.cast(session.get(clazz, id));
            if (obj != null) {  // Only try to remove if the object exists
                session.remove(obj);
                tx.commit();
            }
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // Listar objetos de una clase
    public static <T> Collection<?> listCollection(Class<? extends T> clazz) {
        return listCollection(clazz, "");
    }

    // Listar objetos con un filtro
    public static <T> Collection<?> listCollection(Class<? extends T> clazz, String where) {
        Session session = factory.openSession();
        Transaction tx = null;
        Collection<?> result = null;
        try {
            tx = session.beginTransaction();
            if (where.length() == 0) {
                result = session.createQuery("FROM " + clazz.getName(), clazz).list(); // Usamos la clase en el query
            } else {
                result = session.createQuery("FROM " + clazz.getName() + " WHERE " + where, clazz).list();
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace(); 
        } finally {
            session.close(); 
        }
        return result;
    }

    // Convertir una colección a un string
    public static <T> String collectionToString(Class<? extends T> clazz, Collection<?> collection) {
        String txt = "";
        for (Object obj : collection) {
            T cObj = clazz.cast(obj);
            txt += "\n" + cObj.toString();
        }
        if (txt.length() > 0 && txt.substring(0, 1).compareTo("\n") == 0) {
            txt = txt.substring(1);
        }
        return txt;
    }

    // Actualizar con una consulta SQL nativa
    public static void queryUpdate(String queryString) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            NativeQuery<?> query = session.createNativeQuery(queryString, Void.class); // Actualizado a NativeQuery
            query.executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace(); 
        } finally {
            session.close(); 
        }
    }

    // Realizar una consulta nativa a la tabla
    public static List<Object[]> queryTable(String queryString) {
        Session session = factory.openSession();
        Transaction tx = null;
        List<Object[]> result = null;
        try {
            tx = session.beginTransaction();
            NativeQuery<Object[]> query = session.createNativeQuery(queryString, Object[].class); // Actualizado a NativeQuery
            result = query.getResultList(); // Cambiado de list() a getResultList()
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace(); 
        } finally {
            session.close(); 
        }
        return result;
    }

    // Convertir la tabla de resultados a string
    public static String tableToString(List<Object[]> rows) {
        String txt = "";
        for (Object[] row : rows) {
            for (Object cell : row) {
                txt += cell.toString() + ", ";
            }
            if (txt.length() >= 2 && txt.substring(txt.length() - 2).compareTo(", ") == 0) {
                txt = txt.substring(0, txt.length() - 2);
            }
            txt += "\n";
        }
        if (txt.length() >= 2) {
            txt = txt.substring(0, txt.length() - 1);
        }
        return txt;
    }
}
