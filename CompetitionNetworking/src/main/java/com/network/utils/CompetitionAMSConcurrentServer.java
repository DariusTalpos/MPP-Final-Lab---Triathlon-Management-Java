package com.network.utils;

import com.network.rpcprotocol.ams.CompetitionClientAMSRpcWorker;
import com.services.notification.ICompetitionServicesAMS;

import java.net.Socket;

public class CompetitionAMSConcurrentServer extends AbstractConcurrentServer{

    private ICompetitionServicesAMS competitionServer;
    public CompetitionAMSConcurrentServer(int port, ICompetitionServicesAMS competitionServer) {
        super(port);
        this.competitionServer = competitionServer;
        System.out.println("Competition - CompetitionRPCConcurrentServer port"+port);
    }

    @Override
    protected Thread createWorker(Socket client) {
        CompetitionClientAMSRpcWorker worker = new CompetitionClientAMSRpcWorker(competitionServer, client);
        Thread tw=new Thread(worker);
        return tw;
    }
}
