package com.persistence.hibernate;

import com.model.Round;
import com.persistence.template.IRoundRepo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoundHibernateRepo implements IRoundRepo {

    private SessionFactory sessionFactory = SessionFactorySingleton.getSessionFactory();

    public Round save(Round entity)
    {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                session.persist(entity);
                tx.commit();
                return null;
            } catch (RuntimeException ex)
            {
                if(tx!=null)
                    tx.rollback();
            }
        }
        return entity;
    }

    @Override
    public Round update(Round entity) {
        return null;
    }

    @Override
    public Round delete(Long aLong) {
        return null;
    }

    public Iterable<Round> findAll()
    {
        try(Session session = sessionFactory.openSession())
        {
            Transaction tx=null;
            try
            {
                tx = session.beginTransaction();
                List<Round> rounds = session.createQuery("from Round", Round.class).list();
                tx.commit();
                return rounds;
            }
            catch (RuntimeException ex)
            {
                if(tx!=null)
                    tx.rollback();
            }
        }
        return null;
    }

    public Round findOne(Long id) {
        try (Session session = sessionFactory.openSession())
        {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Round r = session.createQuery("from Round round where round.id = :id", Round.class).setParameter("id", id).setMaxResults(1).uniqueResult();
                tx.commit();
                return r;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        return null;
    }

    public Round findRoundWithName(String name) {
        try (Session session = sessionFactory.openSession())
        {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Round r = session.createQuery("from Round round where round.name = :name", Round.class).setParameter("name", name).setMaxResults(1).uniqueResult();
                tx.commit();
                return r;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        return null;
    }


}
