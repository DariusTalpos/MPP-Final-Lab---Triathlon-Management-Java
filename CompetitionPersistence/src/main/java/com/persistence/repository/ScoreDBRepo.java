package com.persistence.repository;

import com.model.Participant;
import com.model.Round;
import com.model.Score;
import com.persistence.CommonUtils;
import com.persistence.template.IScoreRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ScoreDBRepo implements IScoreRepo {
    private CommonUtils commonUtils;
    private static final Logger logger= LogManager.getLogger();

    public ScoreDBRepo(Properties properties)
    {
        logger.info("Initialising ScoreDBRepo with properties: {}",properties);
        commonUtils =new CommonUtils(properties);
    }
    @Override
    public Score save(Score entity)
    {
        logger.traceEntry("saving task {}", entity);
        Connection connection= commonUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("insert into Scores(participant_id,round_id,points) values (?,?,?)"))
        {
            preparedStatement.setLong(1, entity.getParticipant().getId());
            preparedStatement.setLong(2, entity.getRound().getId());
            preparedStatement.setInt(3, entity.getPoints());
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
    public Score update(Score entity)
    {
        logger.traceEntry("updating task {}", entity);
        Connection connection= commonUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("update Socres set participant_id=?,round_id=?,points=? where id=?"))
        {
            preparedStatement.setLong(1, entity.getParticipant().getId());
            preparedStatement.setLong(2, entity.getRound().getId());
            preparedStatement.setInt(2, entity.getPoints());
            preparedStatement.setLong(4,entity.getId());
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
    public Score delete(Long id)
    {
        logger.traceEntry("deleting task {}",id);
        Score score = findOne(id);
        if(score!=null)
        {
            Connection connection= commonUtils.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement("delete from Scores where id=?"))
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
        return score;
    }

    @Override
    public Score findOne(Long id)
    {
        logger.traceEntry();
        List<Score> scores = new ArrayList<>();
        Connection connection= commonUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("select r.id as 'round_id',r.name as 'round_name',p.id as 'participant_id',p.name as 'participant_name',p.full_points as 'participant_points', s.id as 'score_id', s.points as 'points' from Scores s inner join Rounds r on r.round_id=s.id inner join Participants p on p.id=s.participant_id where s.id=?"))
        {
            preparedStatement.setLong(1, id);
            try(ResultSet resultSet= preparedStatement.executeQuery())
            {
                String roundName = resultSet.getString("round_name");
                Round round = new Round(roundName);
                round.setId(id);
                Long participantID = resultSet.getLong("participant_id");
                String participantName = resultSet.getString("name");
                int participantPoints = resultSet.getInt("participant_points");
                Participant participant = new Participant(participantName,participantPoints);
                participant.setId(participantID);
                Long scoreID = resultSet.getLong("score_id");
                int points = resultSet.getInt("points");
                Score score = new Score(participant,round,points);
                score.setId(scoreID);
                return score;
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
    public Iterable<Score> findAll()
    {
        logger.traceEntry();
        List<Score> scores = new ArrayList<>();
        Connection connection= commonUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("select r.id as 'round_id',r.name as 'round_name',p.id as 'participant_id',p.name as 'participant_name',p.full_points as 'participant_points', s.id as 'score_id', s.points as 'points' from Scores s inner join Rounds r on r.id=s.round_id inner join Participants p on p.id=s.participant_id "))
        {
            try(ResultSet resultSet= preparedStatement.executeQuery())
            {
                while (resultSet.next()) {
                    long roundID = resultSet.getLong("round_id");
                    String roundName = resultSet.getString("round_name");
                    Round round = new Round(roundName);
                    round.setId(roundID);
                    Long participantID = resultSet.getLong("participant_id");
                    String participantName = resultSet.getString("name");
                    int participantPoints = resultSet.getInt("participant_points");
                    Participant participant = new Participant(participantName,participantPoints);
                    participant.setId(participantID);
                    Long scoreID = resultSet.getLong("score_id");
                    int points = resultSet.getInt("points");
                    Score score = new Score(participant,round,points);
                    score.setId(scoreID);
                    scores.add(score);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.err.println("Database error: "+e);
        }
        logger.traceExit(scores);
        return scores;
    }

    @Override
    public Iterable<Score> findAllWithPointsInRound(String roundName) {
        logger.traceEntry();
        List<Score> scores = new ArrayList<>();
        Connection connection= commonUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("select r.id as round_id,p.name as name,p.id as participant_id,p.full_points as participant_points, s.points as points, s.id as score_id from Scores s inner join Rounds r on r.id=s.round_id inner join Participants p on p.id=s.participant_id where r.name=? order by s.points DESC "))
        {
            preparedStatement.setString(1, roundName);
            try(ResultSet resultSet= preparedStatement.executeQuery())
            {
                while (resultSet.next()) {
                    Long roundID = resultSet.getLong("round_id");
                    Round round = new Round(roundName);
                    round.setId(roundID);
                    Long participantID = resultSet.getLong("participant_id");
                    String participantName = resultSet.getString("name");
                    int participantPoints = resultSet.getInt("participant_points");
                    Participant participant = new Participant(participantName,participantPoints);
                    participant.setId(participantID);
                    Long scoreID = resultSet.getLong("score_id");
                    int points = resultSet.getInt("points");
                    Score score = new Score(participant,round,points);
                    score.setId(scoreID);
                    scores.add(score);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.err.println("Database error: "+e);
        }
        logger.traceExit(scores);
        return scores;
    }
}
