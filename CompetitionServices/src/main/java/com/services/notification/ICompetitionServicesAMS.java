package com.services.notification;

import com.model.Participant;
import com.model.Round;
import com.model.Score;
import com.model.User;
import com.services.CompetitionException;

import java.util.List;

public interface ICompetitionServicesAMS {
    void login(User user) throws CompetitionException;
    void logout(User user) throws CompetitionException;
    List<Participant> getParticipantList() throws CompetitionException;
    List<Round> getRoundList() throws CompetitionException;
    void addRoundScore(String roundName, Participant participant,int points) throws CompetitionException;
    List<Score> getScoreListFromRound(String roundName) throws CompetitionException;
}
