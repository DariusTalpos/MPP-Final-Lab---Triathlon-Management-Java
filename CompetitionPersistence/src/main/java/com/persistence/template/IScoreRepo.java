package com.persistence.template;

import com.model.Score;

public interface IScoreRepo extends IGenericRepo<Long, Score> {
    public Iterable<Score> findAllWithPointsInRound(String roundName);
}
