package com.services.notification;

import com.model.Score;
import com.services.CompetitionException;

public interface ICompetitionClient {
    public void newRound() throws CompetitionException;
    public void newScore(Score score) throws CompetitionException;
}
