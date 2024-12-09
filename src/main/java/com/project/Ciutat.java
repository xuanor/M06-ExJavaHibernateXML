package com.project;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Ciutat implements Serializable {

    private long ciutatId;
    private String nom;
    private String pais;
    private int poblacio;
    private Set<Ciutada> ciutadans = new HashSet<>();

    public Ciutat() {}

    public Ciutat(String nom) {
        this.nom = nom;
    }

    public Ciutat(String nom, String pais, int poblacio, Set<Ciutada> ciutadans) {
        this.nom = nom;
        this.pais = pais;
        this.poblacio = poblacio;
        if (ciutadans != null) {
            ciutadans.forEach(this::addCiutada);
        }
    }

    public long getCiutatId() {
        return ciutatId;
    }

    public void setCiutatId(long ciutatId) {
        this.ciutatId = ciutatId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public int getPoblacio() {
        return poblacio;
    }

    public void setPoblacio(int poblacio) {
        this.poblacio = poblacio;
    }

    public Set<Ciutada> getCiutadans() {
        return ciutadans;
    }

    public void setCiutadans(Set<Ciutada> ciutadans) {
        if (ciutadans != null) {
            ciutadans.forEach(this::addCiutada);
        }
    }



    public void addCiutada(Ciutada ciutada) {
        ciutadans.add(ciutada);
        // Ensure proper bidirectional relationship setup if needed
        ciutada.setCart(this);
    }

    public void removeItem(Ciutada ciutada) {
        ciutadans.remove(ciutada);
        // Ensure proper bidirectional relationship teardown if needed
        // ciutada.setCart(null);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Ciutada ciutada : ciutadans) {
            if (str.length() > 0) {
                str.append(" | ");
            }
            str.append(ciutada.getName());
        }
        return this.getCiutatId() + ": " + this.getNom() + ", Pais: " + this.getPais() +
                ", Poblacio: " + this.getPoblacio() + ", Items: [" + str + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ciutat cart = (Ciutat) o;
        return ciutatId == cart.ciutatId;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(ciutatId);
    }
}
