package com.server;

import com.network.utils.AbstractServer;
import com.network.utils.CompetitionRpcConcurrentServer;
import com.network.utils.ServerException;
import com.persistence.hibernate.RoundHibernateRepo;
import com.persistence.repository.ParticipantDBRepo;
import com.persistence.repository.RoundDBRepo;
import com.persistence.repository.ScoreDBRepo;
import com.persistence.repository.UserDBRepo;
import com.server.service.CompetitionServiceFacade;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {

    private static int defaultPort=55555;
    public static void main(String[] args)
    {
        Properties serverProps=new Properties();

        try{
            serverProps.load(StartRpcServer.class.getResourceAsStream("/competitionserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        }
        catch (IOException e)
        {
            System.out.println("Cannot find competitionserver.properties "+e);
            return;
        }

        UserDBRepo userDBRepo = new UserDBRepo(serverProps);
        ParticipantDBRepo participantDBRepo = new ParticipantDBRepo(serverProps);
        RoundDBRepo roundDBRepo = new RoundDBRepo(serverProps);
        RoundHibernateRepo roundHibernateRepo = new RoundHibernateRepo();
        ScoreDBRepo scoreDBRepo = new ScoreDBRepo(serverProps);
        //CompetitionServiceFacade service = new CompetitionServiceFacade(userDBRepo, participantDBRepo, roundDBRepo, scoreDBRepo);
        CompetitionServiceFacade service = new CompetitionServiceFacade(userDBRepo, participantDBRepo, roundHibernateRepo, scoreDBRepo);
        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("competition.server.port"));
        }
        catch (NumberFormatException e)
        {
            System.err.println("Wrong Port Number" + e.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: "+ serverPort);
        AbstractServer server = new CompetitionRpcConcurrentServer(serverPort,service);
        try
        {
            server.start();
        }
        catch (ServerException e)
        {
            System.out.println("Error starting the server " + e.getMessage());
        }
        finally {
            try
            {
                server.stop();
            }
            catch (ServerException e)
            {
                System.out.println("Error stopping server "+ e.getMessage());
            }
        }

    }
}