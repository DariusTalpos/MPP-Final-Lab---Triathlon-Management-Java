package com.services.notification;

import com.model.Participant;
import com.model.Round;
import com.model.Score;
import com.model.User;
import com.services.CompetitionException;

import java.util.List;

public interface ICompetitionNotificationServices {

    void loggedIn(User user);

    void loggedOut(User user);

    void addedRound(Round round);

    void addedScore(Score score);
}
