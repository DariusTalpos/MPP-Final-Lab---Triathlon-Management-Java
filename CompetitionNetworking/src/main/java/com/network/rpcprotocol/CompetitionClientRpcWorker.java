package com.network.rpcprotocol;

import com.model.Round;
import com.model.Score;
import com.model.User;
import com.network.dto.DTOUtils;
import com.network.dto.ScoreDTO;
import com.network.dto.UserDTO;
import com.services.CompetitionException;
import com.services.ICompetitionObserver;
import com.services.ICompetitionServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CompetitionClientRpcWorker implements Runnable, ICompetitionObserver {

    private ICompetitionServices server;

    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();

    public CompetitionClientRpcWorker(ICompetitionServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try
        {
            output= new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input= new ObjectInputStream(connection.getInputStream());
            connected = true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void run()
    {
        while (connected)
        {
            try
            {
                Object request = input.readObject();
                Response response = handleRequest((Request)request);
                if (response!=null)
                    sendResponse(response);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            try
            {
               Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            input.close();
            output.close();
            connection.close();
        }
        catch (IOException e)
        {
            System.out.println("Error "+ e);
        }
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }

    private Response handleRequest(Request request) throws CompetitionException {
        Response response=null;
        if(request.type()==RequestType.LOGIN)
        {
            System.out.println("Login request ..."+request.type());
            UserDTO udto=(UserDTO)request.data();
            User user= DTOUtils.getFromDTO(udto);
            try {
                server.login(user, this);
                return okResponse;
            } catch (CompetitionException e) {
                connected=false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type()==RequestType.LOGOUT)
        {
            System.out.println("Logout request ..."+request.type());
            UserDTO udto=(UserDTO)request.data();
            User user= DTOUtils.getFromDTO(udto);
            try {
                server.logout(user, this);
                connected=false;
                return okResponse;
            } catch (CompetitionException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type()==RequestType.GET_PARTICIPANTS)
        {
            System.out.println("Get participants request ..."+request.type());
            try
            {
                return new Response.Builder().type(ResponseType.OK).data(DTOUtils.getDTOParticipantList(server.getParticipantList())).build();
            } catch (CompetitionException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type()==RequestType.GET_ROUNDS)
        {
            System.out.println("Get rounds request ..."+request.type());
            try
            {
                return new Response.Builder().type(ResponseType.OK).data(DTOUtils.getDTORoundList(server.getRoundList())).build();
            } catch (CompetitionException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type()==RequestType.GET_SCORES)
        {
            System.out.println("Get scores request ..."+request.type());
            try
            {
                String roundName = (String) request.data();
                return new Response.Builder().type(ResponseType.OK).data(DTOUtils.getDTOScoreList(server.getScoreListFromRound(roundName))).build();
            }
            catch (CompetitionException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type()==RequestType.SEND_SCORE)
        {
            System.out.println("Send scores request ..."+request.type());
            try
            {
                Score score = DTOUtils.getFromDTO((ScoreDTO) request.data());
                server.addRoundScore(score.getRound().getName(),score.getParticipant(),score.getPoints());
                return new Response.Builder().type(ResponseType.OK).build();
            }
            catch (CompetitionException e)
            {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        return response;
    }

    @Override
    public void newRound() throws CompetitionException {
        Response response = new Response.Builder().type(ResponseType.NEW_ROUND).data(null).build();
        System.out.println("New round added");
        try {
            sendResponse(response);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void newScore(Score score) throws CompetitionException {
        ScoreDTO scoreDTO = DTOUtils.getDTO(score);
        Response response = new Response.Builder().type(ResponseType.NEW_SCORE).data(scoreDTO).build();
        System.out.println("New score added");
        try {
            sendResponse(response);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
