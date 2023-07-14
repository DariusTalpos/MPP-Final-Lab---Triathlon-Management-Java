package com.persistence.repository;

import com.model.Round;
import com.persistence.CommonUtils;
import com.persistence.template.IRoundRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RoundDBRepo implements IRoundRepo {

    private CommonUtils commonUtils;
    private static final Logger logger = LogManager.getLogger();

    public RoundDBRepo(Properties properties)
    {
        logger.info("Initialising RoundDBRepo with properties: {}",properties);
        commonUtils =new CommonUtils(properties);
    }

    @Override
    public Round save(Round entity) {
        logger.traceEntry("saving task {}", entity);
        Connection connection= commonUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("insert into Rounds(name) values (?)"))
        {
            preparedStatement.setString(1, entity.getName());
            int result = preparedStatement.executeUpdate();
            logger.trace("Saved {} instances", result);
            return null;
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.err.println("Database error: "+e);
            return entity;
        }
    }

    @Override
    public Round update(Round entity) {
        logger.traceEntry("updating task {}", entity);
        Connection connection= commonUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("update Rounds set name=? where id=?"))
        {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setLong(2, entity.getId());
            int result = preparedStatement.executeUpdate();
            logger.trace("Modified {} instances", result);
            return null;
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.err.println("Database error: "+e);
            return entity;
        }
    }

    @Override
    public Round delete(Long id) {
        logger.traceEntry("deleting task {}", id);
        Round round = findOne(id);
        if(round!=null)
        {
            Connection connection= commonUtils.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement("delete from Rounds where id=?"))
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
        return round;
    }

    @Override
    public Round findOne(Long id) {
        logger.traceEntry();
        Connection connection= commonUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from Rounds where id=?"))
        {
            preparedStatement.setLong(1, id);
            try(ResultSet resultSet= preparedStatement.executeQuery())
            {
                resultSet.next();
                String name = resultSet.getString("name");
                Round round = new Round(name);
                round.setId(id);
                logger.traceExit(round);
                return round;
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
    public Iterable<Round> findAll() {
        logger.traceEntry();
        List<Round> rounds = new ArrayList<>();
        Connection connection= commonUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from Rounds"))
        {
            try(ResultSet resultSet= preparedStatement.executeQuery())
            {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    Round round = new Round(name);
                    round.setId(id);
                    rounds.add(round);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.err.println("Database error: "+e);
        }
        logger.traceExit(rounds);
        return rounds;
    }

    @Override
    public Round findRoundWithName(String name) {
        logger.traceEntry();
        Connection connection= commonUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from Rounds where name=?"))
        {
            preparedStatement.setString(1, name);
            try(ResultSet resultSet= preparedStatement.executeQuery())
            {
                if(resultSet.next())
                {
                    Long id = resultSet.getLong("id");
                    Round round = new Round(name);
                    round.setId(id);
                    logger.traceExit(round);
                    return round;
                }
                logger.traceExit(null);
                return null;
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.err.println("Database error: "+e);
            return null;
        }
    }
}
