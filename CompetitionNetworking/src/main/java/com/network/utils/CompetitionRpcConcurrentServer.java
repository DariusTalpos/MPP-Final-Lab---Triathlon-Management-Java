package com.network.utils;

import com.network.rpcprotocol.CompetitionClientRpcWorker;
import com.services.ICompetitionServices;

import java.net.Socket;

public class CompetitionRpcConcurrentServer extends AbstractConcurrentServer {

    private ICompetitionServices competitionServer;

    public CompetitionRpcConcurrentServer(int port, ICompetitionServices competitionServer)
    {
        super(port);
        this.competitionServer = competitionServer;
        System.out.println("Competition");
    }


    @Override
    protected Thread createWorker(Socket client) {

        CompetitionClientRpcWorker worker = new CompetitionClientRpcWorker(competitionServer, client);
        Thread thread = new Thread(worker);
        return thread;
    }

    @Override
    public void stop() {
        System.out.println("Stopping services ...");
    }
}
