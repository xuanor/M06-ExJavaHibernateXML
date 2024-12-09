package com.project;

import java.io.Serializable;

public class Ciutada implements Serializable {

	private long ciutadaId;
	private String name;
    private Ciutat cart;

    public Ciutada() {}

    public Ciutada(String name) {
        this.name = name;
    }

    public long getCiutadaId() {
        return ciutadaId;
    }

    public void setciutadaId(long ciutadaId) {
        this.ciutadaId = ciutadaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Ciutat getCart() {
        return cart;
    }

    public void setCart(Ciutat cart) {
        this.cart = cart;
    }

    @Override
    public String toString() {
        return this.getCiutadaId() + ": " + this.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Ciutada item = (Ciutada) o;
        return ciutadaId == item.ciutadaId;
    }
    
    @Override
    public int hashCode() {
        return Long.hashCode(ciutadaId);
    }    
}