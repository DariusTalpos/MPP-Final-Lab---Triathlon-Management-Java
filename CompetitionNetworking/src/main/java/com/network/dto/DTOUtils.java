package com.network.dto;

import com.model.Participant;
import com.model.Score;
import com.model.User;
import com.model.Round;

import java.util.ArrayList;
import java.util.List;

public class DTOUtils {

    public static User getFromDTO(UserDTO uDTO)
    {
        String username = uDTO.getUsername();
        String password = uDTO.getPassword();
        return new User(username,password);
    }

    public static UserDTO getDTO(User user)
    {
        String username = user.getUsername();
        String password = user.getPassword();
        return new UserDTO(username,password);
    }

    public static Participant getFromDTO(ParticipantDTO pDTO)
    {
        String name = pDTO.getName();
        int fullPoints = pDTO.getFullPoints();
        Long id = pDTO.getId();
        Participant participant = new Participant(name,fullPoints);
        participant.setId(id);
        return participant;
    }

    public static ParticipantDTO getDTO(Participant participant)
    {
        String name = participant.getName();
        int fullPoints = participant.getFullPoints();
        Long id = participant.getId();
        return new ParticipantDTO(name,fullPoints,id);
    }

    public static List<Participant> getFromDTOParticipantList(List<ParticipantDTO> pDTO)
    {
        List<Participant> participants = new ArrayList<>();
        for(ParticipantDTO participant: pDTO)
            participants.add(getFromDTO(participant));
        return participants;
    }

    public static List<ParticipantDTO> getDTOParticipantList(List<Participant> participants)
    {
        List<ParticipantDTO> pDTO = new ArrayList<>();
        for(Participant participant: participants)
            pDTO.add(getDTO(participant));
        return pDTO;
    }

    public static Round getFromDTO(RoundDTO rDTO)
    {
        String name = rDTO.getName();
        Long id = rDTO.getId();
        Round round =new Round(name);
        round.setId(id);
        return round;
    }

    public static RoundDTO getDTO(Round round)
    {
        String name = round.getName();
        Long id = round.getId();
        return new RoundDTO(name,id);
    }

    public static List<RoundDTO> getDTORoundList(List<Round> rounds)
    {
        List<RoundDTO> roundDTOS = new ArrayList<>();
        for(Round round: rounds)
            roundDTOS.add(getDTO(round));
        return roundDTOS;
    }

    public static List<Round> getFromDTORoundList(List<RoundDTO> roundDTOS)
    {
        List<Round> rounds = new ArrayList<>();
        for(RoundDTO roundDTO: roundDTOS)
            rounds.add(getFromDTO(roundDTO));
        return rounds;
    }

    public static ScoreDTO getDTO(Score score)
    {
        ParticipantDTO participantDTO = getDTO(score.getParticipant());
        RoundDTO roundDTO = getDTO(score.getRound());
        int points = score.getPoints();
        Long id = score.getId();
        return new ScoreDTO(participantDTO,roundDTO,points,id);
    }

    public static Score getFromDTO(ScoreDTO sDTO)
    {
        Participant participant = getFromDTO(sDTO.getParticipant());
        Round round = getFromDTO(sDTO.getRound());
        int points = sDTO.getPoints();
        Long id = sDTO.getId();
        Score score = new Score(participant,round,points);
        score.setId(id);
        return score;
    }

    public static List<Score> getFromDTOScoreList(List<ScoreDTO> scoreDTOS)
    {
        List<Score> scores = new ArrayList<>();
        for(ScoreDTO scoreDTO: scoreDTOS)
            scores.add(getFromDTO(scoreDTO));
        return scores;
    }

    public static List<ScoreDTO> getDTOScoreList(List<Score> scores)
    {
        List<ScoreDTO> scoreDTOS = new ArrayList<>();
        for(Score score: scores)
            scoreDTOS.add(getDTO(score));
        return scoreDTOS;
    }

}
