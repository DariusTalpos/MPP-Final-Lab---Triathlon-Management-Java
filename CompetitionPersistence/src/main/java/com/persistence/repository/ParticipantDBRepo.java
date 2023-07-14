package com.persistence.repository;

import com.model.Participant;
import com.persistence.CommonUtils;
import com.persistence.template.IParticipantRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class ParticipantDBRepo implements IParticipantRepo {
    private CommonUtils commonUtils;
    private static final Logger logger= LogManager.getLogger();

    public ParticipantDBRepo(Properties properties)
    {
        logger.info("Initialising ParticipantDBRepo with properties: {}",properties);
        commonUtils =new CommonUtils(properties);
    }

    @Override
    public Participant save(Participant entity) {
        logger.traceEntry("saving task {}", entity);
        Connection connection= commonUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("insert into Participants(name,full_points) values (?,?)", Statement.RETURN_GENERATED_KEYS))
        {

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setInt(2, entity.getFullPoints());
            int result = preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            rs.next();
            Long id = rs.getLong(1);
            logger.trace("Saved {} instances", result);
            entity.setId(id);
            return entity;
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.err.println("Database error: "+e);
            return null;
        }
    }

    @Override
    public Participant update(Participant entity)
    {
        logger.traceEntry("updating task {}", entity);
        Connection connection= commonUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("update Participants set name=?,full_points=? where id=?"))
        {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setInt(2, entity.getFullPoints());
            preparedStatement.setLong(3, entity.getId());
            int result = preparedStatement.executeUpdate();
            logger.trace("Modified {} instances", result);
            return entity;
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.err.println("Database error: "+e);
            return null;
        }
    }

    @Override
    public Participant delete(Long id)
    {
        logger.traceEntry("deleting task {}", id);
        Participant participant = findOne(id);
        if (participant != null)
        {
            Connection connection = commonUtils.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement("delete from Participants where id=?"))
            {
                preparedStatement.setLong(1, id);
                int result = preparedStatement.executeUpdate();
                logger.trace("Deleted {} instances", result);
            }
            catch (SQLException e)
            {
                logger.error(e);
                System.err.println("Database error: " + e);
            }
        }
        return participant;
    }

    @Override
    public Participant findOne(Long id) {
        logger.traceEntry();
        Connection connection= commonUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from Participants where id=?"))
        {
            preparedStatement.setLong(1, id);
            try(ResultSet resultSet= preparedStatement.executeQuery())
            {
                resultSet.next();
                String name = resultSet.getString("name");
                int fullPoints = resultSet.getInt("full_points");
                Participant participant = new Participant(name,fullPoints);
                participant.setId(id);
                logger.traceExit(participant);
                return participant;
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.err.println("Database error: "+e);
            return null;
        }
    }

    @Override
    public Iterable<Participant> findAll()
    {
        logger.traceEntry();
        List<Participant> participants = new ArrayList<>();
        Connection connection= commonUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from Participants order by id"))
        {
            try(ResultSet resultSet= preparedStatement.executeQuery())
            {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    int fullPoints = resultSet.getInt("full_points");
                    Participant participant = new Participant(name,fullPoints);
                    participant.setId(id);
                    participants.add(participant);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.err.println("Database error: "+e);
        }
        logger.traceExit(participants);
        return participants;
    }
}

