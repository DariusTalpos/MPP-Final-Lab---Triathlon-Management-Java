package com.network.protobuffprotocol;

import com.model.User;
import com.model.Participant;
import com.model.Score;
import com.model.Round;

import com.network.rpcprotocol.Response;
import com.services.ICompetitionServices;
import com.services.ICompetitionObserver;
import com.services.CompetitionException;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProtoCompetitionProxy implements ICompetitionServices{

    private String host;
    private int port;

    private ICompetitionObserver client;
    private InputStream input;
    private OutputStream output;
    private Socket connection;

    private BlockingQueue<CompetitionProtobuffs.Response> responseQueue;

    private volatile boolean finished;

    public ProtoCompetitionProxy(String host, int port) {
        this.host = host;
        this.port = port;
        responseQueue=new LinkedBlockingQueue<CompetitionProtobuffs.Response>();
    }

    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private void initializeConnection() throws CompetitionException{
        try {
            connection=new Socket(host,port);
            output=connection.getOutputStream();
            //output.flush();
            input=connection.getInputStream();
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(CompetitionProtobuffs.Request request)throws CompetitionException{
        try {
            System.out.println("Sending request ..."+request);
            //request.writeTo(output);
            request.writeDelimitedTo(output);
            output.flush();
            System.out.println("Request sent.");
        } catch (IOException e) {
            throw new CompetitionException("Error sending object "+e);
        }

    }

    private CompetitionProtobuffs.Response readResponse() throws CompetitionException{
        CompetitionProtobuffs.Response response=null;
        try{
            response=responseQueue.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public void login(User user, ICompetitionObserver client) throws CompetitionException {
        initializeConnection();
        System.out.println("Login request ...");
        sendRequest(ProtoUtils.createLoginRequest(user));
        CompetitionProtobuffs.Response response=readResponse();
        if (response.getType()==CompetitionProtobuffs.Response.Type.OK){
            this.client=client;
            return;
        }
        if (response.getType()==CompetitionProtobuffs.Response.Type.ERROR){
            String errorText=ProtoUtils.getError(response);
            closeConnection();
            throw new CompetitionException(errorText);
        }
    }

    @Override
    public void logout(User user, ICompetitionObserver client) throws CompetitionException {
        sendRequest(ProtoUtils.createLogoutRequest(user));
        CompetitionProtobuffs.Response response=readResponse();
        closeConnection();
        if (response.getType()==CompetitionProtobuffs.Response.Type.ERROR){
            String errorText=ProtoUtils.getError(response);
            throw new CompetitionException();
        }
    }

    @Override
    public List<Participant> getParticipantList() throws CompetitionException {
        sendRequest(ProtoUtils.createParticipantsRequest());
        CompetitionProtobuffs.Response response = readResponse();
        if(response.getType()==CompetitionProtobuffs.Response.Type.OK)
        {
            return ProtoUtils.getParticipantList(response);
        }
        return null;
    }

    @Override
    public List<Round> getRoundList() throws CompetitionException {
        sendRequest(ProtoUtils.createRoundsRequest());
        CompetitionProtobuffs.Response response = readResponse();
        if(response.getType()==CompetitionProtobuffs.Response.Type.OK)
        {
            return ProtoUtils.getRoundList(response);
        }
        return null;
    }

    @Override
    public void addRoundScore(String roundName, Participant participant, int points) throws CompetitionException {
        sendRequest(ProtoUtils.createAddRoundScoreRequest(roundName,participant,points));
        CompetitionProtobuffs.Response response = readResponse();
        if (response.getType()==CompetitionProtobuffs.Response.Type.ERROR){
            String errorText=ProtoUtils.getError(response);
            throw new CompetitionException();
        }
    }

    @Override
    public List<Score> getScoreListFromRound(String roundName) throws CompetitionException {
        sendRequest(ProtoUtils.createScoresRequest(roundName));
        CompetitionProtobuffs.Response response = readResponse();
        if(response.getType()==CompetitionProtobuffs.Response.Type.OK)
        {
            return ProtoUtils.getScoreList(response);
        }
        return null;
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    CompetitionProtobuffs.Response response=CompetitionProtobuffs.Response.parseDelimitedFrom(input);
                    System.out.println("response received "+response);
                    if (isUpdateResponse(response.getType())){
                        handleUpdate(response);
                    }else{

                        try {
                            responseQueue.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }

    private boolean isUpdateResponse(CompetitionProtobuffs.Response.Type type){
        switch (type){
            case NEW_ROUND, NEW_SCORE -> {
                return true;
            }
        }
        return false;
    }

    private void handleUpdate(CompetitionProtobuffs.Response updateResponse) {
        switch (updateResponse.getType())
        {
            case NEW_SCORE -> {
                Score score = ProtoUtils.getScore(updateResponse);
                System.out.println("New score added "+score);
                try{
                    client.newScore(score);
                } catch (CompetitionException e)
                {
                    e.printStackTrace();
                }
                break;
            }

            case NEW_ROUND -> {
                try{
                    client.newRound();
                } catch (CompetitionException e)
                {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


}
