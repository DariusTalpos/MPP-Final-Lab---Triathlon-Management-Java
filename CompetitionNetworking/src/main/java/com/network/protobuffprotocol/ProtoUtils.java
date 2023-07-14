package com.network.protobuffprotocol;

import com.model.Participant;
import com.model.Round;
import com.model.Score;
import com.model.User;

import java.util.ArrayList;
import java.util.List;

public class ProtoUtils {
        public static CompetitionProtobuffs.Request createLoginRequest(User user)
        {
            CompetitionProtobuffs.User userDTO = CompetitionProtobuffs.User.newBuilder().setPassword(user.getPassword()).setUsername(user.getUsername()).build();
            CompetitionProtobuffs.Request request = CompetitionProtobuffs.Request.newBuilder().setType(CompetitionProtobuffs.Request.Type.LOGIN).setUser(userDTO).build();
            return request;
        }

        public static CompetitionProtobuffs.Request createLogoutRequest(User user)
        {
            CompetitionProtobuffs.User userDTO = CompetitionProtobuffs.User.newBuilder().setPassword(user.getPassword()).setUsername(user.getUsername()).build();
            CompetitionProtobuffs.Request request = CompetitionProtobuffs.Request.newBuilder().setType(CompetitionProtobuffs.Request.Type.LOGOUT).setUser(userDTO).build();
            return request;
        }

        public static CompetitionProtobuffs.Request createParticipantsRequest()
        {
            CompetitionProtobuffs.Request request = CompetitionProtobuffs.Request.newBuilder().setType(CompetitionProtobuffs.Request.Type.GET_PARTICIPANTS).build();
            return request;
        }

        public static CompetitionProtobuffs.Request createRoundsRequest()
        {
            CompetitionProtobuffs.Request request = CompetitionProtobuffs.Request.newBuilder().setType(CompetitionProtobuffs.Request.Type.GET_ROUNDS).build();
            return request;
        }

        public static CompetitionProtobuffs.Request createScoresRequest(String roundName)
        {
            CompetitionProtobuffs.Request request = CompetitionProtobuffs.Request.newBuilder().setType(CompetitionProtobuffs.Request.Type.GET_SCORES).setRoundName(roundName).build();
            return request;
        }

        public static CompetitionProtobuffs.Request createAddRoundScoreRequest(String roundName, Participant participant, int points)
        {
            CompetitionProtobuffs.Round roundDTO = CompetitionProtobuffs.Round.newBuilder().setName(roundName).build();
            CompetitionProtobuffs.Participant participantDTO = CompetitionProtobuffs.Participant.newBuilder().setId(participant.getId()).setName(participant.getName())
                    .setFullPoints(participant.getFullPoints()).build();
            CompetitionProtobuffs.Score scoreDTO = CompetitionProtobuffs.Score.newBuilder().setRound(roundDTO).setParticipant(participantDTO).setPoints(points).build();
            CompetitionProtobuffs.Request request = CompetitionProtobuffs.Request.newBuilder().setType(CompetitionProtobuffs.Request.Type.SEND_SCORE).setScore(scoreDTO).build();
            return request;
        }

        public static String getError(CompetitionProtobuffs.Response response){
            String errorMessage=response.getError();
            return errorMessage;
        }

        public static List<Participant> getParticipantList(CompetitionProtobuffs.Response response)
        {
            List<CompetitionProtobuffs.Participant> participantList = response.getParticipantsList();
            List<Participant> participants = new ArrayList<>();
            for (CompetitionProtobuffs.Participant p: participantList)
            {
                Participant participant = new Participant(p.getName(),p.getFullPoints());
                participant.setId(p.getId());
                participants.add(participant);
            }
            return participants;
        }

    public static List<Round> getRoundList(CompetitionProtobuffs.Response response)
    {
        List<CompetitionProtobuffs.Round> roundList = response.getRoundsList();
        List<Round> rounds = new ArrayList<>();
        for (CompetitionProtobuffs.Round r: roundList)
        {
            Round round = new Round(r.getName());
            round.setId(r.getId());
            rounds.add(round);
        }
        return rounds;
    }

    public static List<Score> getScoreList(CompetitionProtobuffs.Response response)
    {
        List<CompetitionProtobuffs.Score> scoreList = response.getScoresList();
        List<Score> scores = new ArrayList<>();
        for (CompetitionProtobuffs.Score s: scoreList)
        {
            Participant participant = new Participant(s.getParticipant().getName(),s.getParticipant().getFullPoints());
            participant.setId(s.getParticipant().getId());
            Round round = new Round(s.getRound().getName());
            round.setId(s.getRound().getId());
            Score score = new Score(participant,round,s.getPoints());
            score.setId(s.getId());
            scores.add(score);
        }
        return scores;
    }

    public static Score getScore(CompetitionProtobuffs.Response response)
    {
        CompetitionProtobuffs.Score s = response.getScore();

        Participant participant = new Participant(s.getParticipant().getName(),s.getParticipant().getFullPoints());
        participant.setId(s.getParticipant().getId());
        Round round = new Round(s.getRound().getName());
        round.setId(s.getRound().getId());
        Score score = new Score(participant,round,s.getPoints());
        score.setId(s.getId());
        return score;
    }
}
