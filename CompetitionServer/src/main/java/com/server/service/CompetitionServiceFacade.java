package com.server.service;

import com.model.Participant;
import com.model.Round;
import com.model.Score;
import com.model.User;
import com.persistence.repository.ParticipantDBRepo;
import com.persistence.repository.RoundDBRepo;
import com.persistence.repository.ScoreDBRepo;
import com.persistence.repository.UserDBRepo;
import com.persistence.template.IRoundRepo;
import com.services.CompetitionException;
import com.services.ICompetitionObserver;
import com.services.ICompetitionServices;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CompetitionServiceFacade implements ICompetitionServices {

    private UserService userService;
    private ParticipantService participantService;
    private RoundService roundService;
    private ScoreService scoreService;
    private Map<String, ICompetitionObserver> loggedUsers;
    private final int defaultThreadsNo=5;

    public CompetitionServiceFacade(UserDBRepo userDBRepo, ParticipantDBRepo participantDBRepo, IRoundRepo roundDBRepo, ScoreDBRepo scoreDBRepo) {
        userService = new UserService(userDBRepo);
        participantService = new ParticipantService(participantDBRepo);
        roundService = new RoundService(roundDBRepo);
        scoreService = new ScoreService(scoreDBRepo);
        loggedUsers= new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void login(User user, ICompetitionObserver client) throws CompetitionException {
        User result = userService.userExists(user.getUsername(), user.getPassword());
        if(result!=null) {
            if (loggedUsers.get(user.getUsername()) != null)
                throw new CompetitionException("User is already logged in.");
            loggedUsers.put(user.getUsername(), client);
        }
        else
            throw new CompetitionException("There is no account with this username and password");
    }

    @Override
    public synchronized void logout(User user, ICompetitionObserver client) throws CompetitionException {
        ICompetitionObserver localClient = loggedUsers.remove(user.getUsername());
        if(localClient==null)
            throw new CompetitionException("User "+user.getUsername()+" is not logged in.");
    }

    @Override
    public List<Participant> getParticipantList() throws CompetitionException {
        return participantService.getParticipantList();
    }

    @Override
    public List<Round> getRoundList() throws CompetitionException {
        return roundService.getRoundList();
    }

    @Override
    public List<Score> getScoreListFromRound(String roundName) throws CompetitionException {
        return scoreService.getScoreListFromRound(roundName);
    }

    @Override
    public void addRoundScore(String roundName, Participant participant, int points) throws CompetitionException {
        Round round = roundService.getRoundWithName(roundName);
        if(round == null)
        {
            roundService.save(roundName);
            round = roundService.getRoundWithName(roundName);
            loggedUsers.forEach((key,value)->
            {
                try {
                    value.newRound();
                } catch (CompetitionException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        Score score = scoreService.save(round,participant,points);
        participantService.updatePoints(participant,points);
        loggedUsers.forEach((key,value)->
        {
            try {
                value.newScore(score);
            } catch (CompetitionException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
