package com.network.rpcprotocol;

import com.model.Participant;
import com.model.Round;
import com.model.Score;
import com.model.User;
import com.network.dto.*;
import com.services.CompetitionException;
import com.services.ICompetitionObserver;
import com.services.ICompetitionServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CompetitionServicesRpcProxy implements ICompetitionServices {
    private String host;
    private int port;
    private ICompetitionObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private BlockingQueue<Response> responseQueue;
    private volatile boolean finished;

    public CompetitionServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        responseQueue = new LinkedBlockingQueue<Response>();
    }

    private void initializeConnection() throws CompetitionException {
        try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
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

    private void sendRequest(Request request)throws CompetitionException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new CompetitionException("Error sending object "+e);
        }
    }

    private Response readResponse() throws CompetitionException {
        Response response=null;
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
        UserDTO userDTO = DTOUtils.getDTO(user);
        Request request = new Request.Builder().type(RequestType.LOGIN).data(userDTO).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type()== ResponseType.OK){
            this.client=client;
            return;
        }
        if (response.type()==ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new CompetitionException(err);
        }
    }

    @Override
    public void logout(User user, ICompetitionObserver client) throws CompetitionException {
        UserDTO userDTO= DTOUtils.getDTO(user);
        Request request =new Request.Builder().type(RequestType.LOGOUT).data(userDTO).build();
        sendRequest(request);
        Response response=readResponse();
        closeConnection();
        if (response.type()== ResponseType.ERROR) {
            String err = response.data().toString();
            throw new CompetitionException(err);
        }
    }

    @Override
    public List<Participant> getParticipantList() throws CompetitionException {
        Request request = new Request.Builder().type(RequestType.GET_PARTICIPANTS).build();
        sendRequest(request);
        Response response=readResponse();
        if(response.type()== ResponseType.OK)
        {
            return DTOUtils.getFromDTOParticipantList((List<ParticipantDTO>) response.data());
        }
        return null;
    }

    @Override
    public List<Round> getRoundList() throws CompetitionException {
        Request request = new Request.Builder().type(RequestType.GET_ROUNDS).build();
        sendRequest(request);
        Response response=readResponse();
        if(response.type()== ResponseType.OK)
        {
            return DTOUtils.getFromDTORoundList((List<RoundDTO>) response.data());
        }
        return null;
    }

    @Override
    public List<Score> getScoreListFromRound(String roundName) throws CompetitionException {
        Request request= new Request.Builder().type(RequestType.GET_SCORES).data(roundName).build();
        sendRequest(request);
        Response response=readResponse();
        if(response.type()== ResponseType.OK)
        {
            return DTOUtils.getFromDTOScoreList((List<ScoreDTO>) response.data());
        }
        return null;
    }

    @Override
    public void addRoundScore(String roundName, Participant participant, int points) throws CompetitionException {
        Request request = new Request.Builder().type(RequestType.SEND_SCORE).data(DTOUtils.getDTO(new Score(participant,new Round(roundName), points))).build();
        sendRequest(request);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR) {
            String err = response.data().toString();
            throw new CompetitionException(err);
        }
    }

    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private boolean isUpdate(Response response)
    {
        return response.type()==ResponseType.NEW_SCORE || response.type()==ResponseType.NEW_ROUND;
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (isUpdate((Response)response)){
                        handleUpdate((Response)response);
                    }else{

                        try {
                            responseQueue.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }

    private void handleUpdate(Response response) {
        if(response.type()==ResponseType.NEW_ROUND)
        {
            System.out.println("New round added!");
            try
            {
                client.newRound();
            }
            catch (CompetitionException e)
            {
                e.printStackTrace();
            }
        }
        if(response.type()==ResponseType.NEW_SCORE)
        {
            System.out.println("New score added!");
            try
            {
                Score score = DTOUtils.getFromDTO((ScoreDTO) response.data());
                client.newScore(score);
            }
            catch (CompetitionException e)
            {
                e.printStackTrace();
            }
        }
    }

}
