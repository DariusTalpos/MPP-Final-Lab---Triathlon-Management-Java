package com.server.service;

import com.model.Round;
import com.persistence.template.IRoundRepo;

import java.util.List;

public class RoundService {
    private IRoundRepo roundRepo;

    public RoundService(IRoundRepo roundRepo) {
        this.roundRepo = roundRepo;
    }

    public List<Round> getRoundList() { return (List<Round>) roundRepo.findAll();}

    public Round getRoundWithName(String name) {return roundRepo.findRoundWithName(name);}

    public int save(String name)
    {
        Round round = new Round(name);
        if(roundRepo.save(round)!=null)
            return 0;
        return 1;
    }
}
