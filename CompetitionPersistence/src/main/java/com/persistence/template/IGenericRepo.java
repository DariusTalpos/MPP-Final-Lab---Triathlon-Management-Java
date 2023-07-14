package com.persistence.template;

import com.model.Entity;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public interface IGenericRepo<ID extends Serializable, E extends Entity<ID>> {
    E save(E entity);
    E update(E entity);
    E delete(ID id);
    E findOne(ID id);
    Iterable<E> findAll();
}
