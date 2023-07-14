package com.network.dto;

import java.io.Serializable;

public class ScoreDTO implements Serializable {
    private ParticipantDTO participant;
    private RoundDTO round;
    private int points;
    private Long id;

    public ScoreDTO(ParticipantDTO participant, RoundDTO round, int points,Long id) {
        this.participant = participant;
        this.round = round;
        this.points = points;
        this.id = id;
    }

    public ParticipantDTO getParticipant() {
        return participant;
    }

    public void setParticipant(ParticipantDTO participant) {
        this.participant = participant;
    }

    public RoundDTO getRound() {
        return round;
    }

    public void setRound(RoundDTO round) {
        this.round = round;
    }

    public int getPoints() {
        return points;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
