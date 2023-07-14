package com.server.service;

import com.model.Participant;
import com.persistence.template.IParticipantRepo;

import java.util.List;

public class ParticipantService {
    private IParticipantRepo participantRepo;

    public ParticipantService(IParticipantRepo participantRepo) {
        this.participantRepo = participantRepo;
    }

    public List<Participant> getParticipantList() { return (List<Participant>) participantRepo.findAll(); }

    public int updatePoints(Participant participant,int points)
    {
        participant.setFullPoints(participant.getFullPoints()+points);
        if(participantRepo.update(participant)!=null)
            return 1;
        return 0;
    }
}
