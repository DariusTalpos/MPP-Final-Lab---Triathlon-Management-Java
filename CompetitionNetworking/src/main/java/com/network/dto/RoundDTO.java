package com.network.dto;

import java.io.Serializable;

public class RoundDTO implements Serializable {
    private String name;

    private Long id;

    public RoundDTO(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RoundDTO["+name+"]";
    }
}
