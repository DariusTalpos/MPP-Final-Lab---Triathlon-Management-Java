package com.network.dto;

import java.io.Serializable;

public class ParticipantDTO implements Serializable {
    private String name;
    private int fullPoints;

    private Long id;

    public ParticipantDTO(String name, int fullPoints, Long id) {
        this.name = name;
        this.fullPoints = fullPoints;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getFullPoints() {
        return fullPoints;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFullPoints(int fullPoints) {
        this.fullPoints = fullPoints;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserDTO["+ name +"; points="+fullPoints+"]";
    }
}
