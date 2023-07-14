package com.server.service;

import com.model.Participant;
import com.model.Round;
import com.model.Score;
import com.model.User;
import com.notification.Notification;
import com.notification.NotificationType;
import com.services.CompetitionException;
import com.services.notification.ICompetitionNotificationServices;
import org.springframework.jms.core.JmsOperations;

import java.util.List;

public class NotificationService implements ICompetitionNotificationServices {

    private JmsOperations jmsOperations;

    public NotificationService(JmsOperations jmsOperations) {
        this.jmsOperations = jmsOperations;
    }

    @Override
    public void loggedIn(User user){
        System.out.println("User logged in notification");
        Notification notification = new Notification(NotificationType.LOGGED_IN, user.getUsername());
        jmsOperations.convertAndSend(notification);
        System.out.println("Sent message to ActiveMQ... " +notification);
    }

    @Override
    public void loggedOut(User user){
        System.out.println("User logged out notification");
        Notification notification = new Notification(NotificationType.LOGGED_OUT, user.getUsername());
        jmsOperations.convertAndSend(notification);
        System.out.println("Sent message to ActiveMQ... " +notification);
    }

    @Override
    public void addedRound(Round round) {
        System.out.println("New round added notification");
        Notification notification = new Notification(NotificationType.NEW_ROUND, round.getName());
        jmsOperations.convertAndSend(notification);
        System.out.println("Sent message to ActiveMQ... " +notification);
    }

    @Override
    public void addedScore(Score score) {
        System.out.println("New score added notification");
        Notification notification = new Notification(NotificationType.NEW_SCORE, score.toString());
        jmsOperations.convertAndSend(notification);
        System.out.println("Sent message to ActiveMQ... " +notification);
    }
}
