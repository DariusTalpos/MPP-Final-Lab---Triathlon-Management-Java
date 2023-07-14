package com.services;

import com.model.Round;
import com.model.Score;

public interface ICompetitionObserver {
    void newRound() throws CompetitionException;

    void newScore(Score score) throws CompetitionException;
}
