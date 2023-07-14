package com.model;

import jakarta.persistence.Table;

@jakarta.persistence.Entity
@Table(name = "rounds")
public class Round extends Entity<Long>{
    private String name;

    public Round(String name) {
        this.name = name;
    }

    public Round() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Round{" +
                "name='" + name + '\'' +
                '}';
    }
}

