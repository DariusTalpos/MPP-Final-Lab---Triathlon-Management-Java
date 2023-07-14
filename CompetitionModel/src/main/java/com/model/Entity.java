package com.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@MappedSuperclass
public class Entity<ID extends Serializable>
{
    private ID id;

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public ID getId()
    {
        return id;
    }
    public void setId(ID id)
    {
        this.id = id;
    }
}
