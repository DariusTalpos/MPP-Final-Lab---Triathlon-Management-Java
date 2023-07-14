package com.persistence.repository;

import com.model.User;
import com.persistence.CommonUtils;
import com.persistence.template.IUserRepo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserDBRepo implements IUserRepo {

    private CommonUtils commonUtils;
    private static final Logger logger= LogManager.getLogger();

    public UserDBRepo(Properties properties)
    {
        logger.info("Initialising UserDBRepo with properties: {}",properties);
        commonUtils =new CommonUtils(properties);
    }

    @Override
    public User save(User entity)
    {
        logger.traceEntry("saving task {}", entity);
        Connection connection= commonUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("insert into Users(username,password) values (?,?)"))
        {
                preparedStatement.setString(1, entity.getUsername());
                preparedStatement.setString(2, entity.getPassword());
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
    public User update(User entity)
    {
        logger.traceEntry("updating task {}", entity);
        Connection connection= commonUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("update Users set username=?,password=? where id=?"))
        {
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getPassword());
            preparedStatement.setLong(3, entity.getId());
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
    public User delete(Long id)
    {
        logger.traceEntry("deleting task {}",id);
        User user = findOne(id);
        if(user!=null)
        {
            Connection connection= commonUtils.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement("delete from Users where id=?"))
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
        return user;
    }

    @Override
    public User findOne(Long id)
    {
        logger.traceEntry();
        Connection connection= commonUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from Users where id=?"))
        {
            preparedStatement.setLong(1, id);
            try(ResultSet resultSet= preparedStatement.executeQuery())
            {
                resultSet.next();
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                User user = new User(username,password);
                user.setId(id);
                logger.traceExit(user);
                return user;
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
    public Iterable<User> findAll()
    {
        logger.traceEntry();
        List<User> users = new ArrayList<>();
        Connection connection= commonUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from Users"))
        {
            try(ResultSet resultSet= preparedStatement.executeQuery())
            {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    User user = new User(username, password);
                    user.setId(id);
                    users.add(user);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.err.println("Database error: "+e);
        }
        logger.traceExit(users);
        return users;
    }


    @Override
    public User findUserWithNameAndPassword(String username, String password) {
        logger.traceEntry();
        Connection connection= commonUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from Users where username=? and password=?"))
        {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            try(ResultSet resultSet= preparedStatement.executeQuery())
            {
                if(resultSet.next())
                {
                    Long id = resultSet.getLong("id");
                    User user = new User(username,password);
                    user.setId(id);
                    logger.traceExit(user);
                    return user;
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
