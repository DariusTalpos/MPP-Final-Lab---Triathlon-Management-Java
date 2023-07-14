package com.server.service;

import com.model.Participant;
import com.model.Round;
import com.model.Score;
import com.persistence.template.IScoreRepo;

import java.util.List;

public class ScoreService {
    private IScoreRepo scoreRepo;

    public ScoreService(IScoreRepo scoreRepo) {
        this.scoreRepo = scoreRepo;
    }

    public List<Score> getScoreListFromRound(String roundName) { return (List<Score>) scoreRepo.findAllWithPointsInRound(roundName);}

    public Score save(Round round, Participant participant, int points)
    {
        Score score = new Score(participant,round,points);
        if(scoreRepo.save(score)!=null)
            return null;
        return score;
    }
}
