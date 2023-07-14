package com.model;

public class Participant extends Entity<Long>{
    private String name;
    private int fullPoints;

    public Participant()
    {

    }
    public Participant(String name, int fullPoints) {
        super();
        this.name = name;
        this.fullPoints = fullPoints;
    }

    public Participant(String name) {
        super();
        this.name = name;
        this.fullPoints = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFullPoints() {
        return fullPoints;
    }

    public void setFullPoints(int fullPoints) {
        this.fullPoints = fullPoints;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "name='" + name + '\'' +
                ", fullPoints=" + fullPoints +
                '}';
    }
}

