package com.server.service;

import com.model.Participant;
import com.model.Round;
import com.model.Score;
import com.model.User;
import com.network.protobuffprotocol.CompetitionProtobuffs;
import com.persistence.repository.ParticipantDBRepo;
import com.persistence.repository.ScoreDBRepo;
import com.persistence.repository.UserDBRepo;
import com.persistence.template.IRoundRepo;
import com.services.CompetitionException;
import com.services.ICompetitionObserver;
import com.services.ICompetitionServices;
import com.services.notification.ICompetitionNotificationServices;
import com.services.notification.ICompetitionServicesAMS;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CompetitionServiceFacadeAMS implements ICompetitionServicesAMS {
    private UserService userService;
    private ParticipantService participantService;
    private RoundService roundService;
    private ScoreService scoreService;
    private Map<String, User> loggedUsers;
    private ICompetitionNotificationServices notificationService;
    private final int defaultThreadsNo=5;
    public CompetitionServiceFacadeAMS(UserDBRepo userDBRepo, ParticipantDBRepo participantDBRepo, IRoundRepo roundDBRepo, ScoreDBRepo scoreDBRepo, ICompetitionNotificationServices service) {
        userService = new UserService(userDBRepo);
        participantService = new ParticipantService(participantDBRepo);
        roundService = new RoundService(roundDBRepo);
        scoreService = new ScoreService(scoreDBRepo);
        loggedUsers= new ConcurrentHashMap<>();
        this.notificationService = service;
    }


    public void login(User user) throws CompetitionException {
        User result = userService.userExists(user.getUsername(), user.getPassword());
        if(result!=null) {
            if (loggedUsers.get(user.getUsername()) != null)
                throw new CompetitionException("User is already logged in.");
            loggedUsers.put(user.getUsername(), user);
            notificationService.loggedIn(user);
        }
        else
            throw new CompetitionException("There is no account with this username and password");
    }


    public void logout(User user) throws CompetitionException {
       User localClient = loggedUsers.remove(user.getUsername());
        if(localClient==null)
            throw new CompetitionException("User "+user.getUsername()+" is not logged in.");
        else
            notificationService.loggedOut(user);
    }


    public List<Participant> getParticipantList() throws CompetitionException {
        return participantService.getParticipantList();
    }


    public List<Round> getRoundList() throws CompetitionException {
        return roundService.getRoundList();
    }


    public void addRoundScore(String roundName, Participant participant, int points) throws CompetitionException {
        Round round = roundService.getRoundWithName(roundName);
        if(round == null)
        {
            roundService.save(roundName);
            round = roundService.getRoundWithName(roundName);
            notificationService.addedRound(round);
        }

        Score score = scoreService.save(round,participant,points);
        participantService.updatePoints(participant,points);
        notificationService.addedScore(score);
    }


    public List<Score> getScoreListFromRound(String roundName) throws CompetitionException {
        return scoreService.getScoreListFromRound(roundName);
    }
}
