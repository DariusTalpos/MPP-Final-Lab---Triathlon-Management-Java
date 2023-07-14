package com.model;

public class Score extends Entity<Long>{
    private Participant participant;
    private Round round;
    private int points;

    public Score(Participant participant,Round round, int points) {
        this.participant = participant;
        this.round = round;
        this.points = points;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getParticipantName() {return participant.getName(); }

    @Override
    public String toString() {
        return "Score{" +
                "participant=" + participant +
                ", round=" + round +
                ", points=" + points +
                '}';
    }
}
