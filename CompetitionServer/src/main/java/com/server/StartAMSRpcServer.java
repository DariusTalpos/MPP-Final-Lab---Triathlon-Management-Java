package com.server;

import com.network.utils.AbstractServer;
import com.network.utils.CompetitionAMSConcurrentServer;
import com.network.utils.ServerException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartAMSRpcServer {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-server.xml");
        AbstractServer server=context.getBean("competitionTCPServer", CompetitionAMSConcurrentServer.class);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
