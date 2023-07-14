package com.services;

import com.model.Participant;
import com.model.Round;
import com.model.Score;
import com.model.User;

import java.util.List;

public interface ICompetitionServices {

    void login(User user,ICompetitionObserver client)  throws CompetitionException;

    void logout(User user, ICompetitionObserver client) throws CompetitionException;

    List<Participant> getParticipantList() throws CompetitionException;

    List<Round> getRoundList() throws CompetitionException;

    void addRoundScore(String roundName, Participant participant,int points) throws CompetitionException;

    List<Score> getScoreListFromRound(String roundName) throws CompetitionException;
}
