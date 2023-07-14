package com.client.gui.ams;

import com.notification.Notification;
import com.services.notification.NotificationReceiver;
import com.services.notification.NotificationSubscriber;
import org.springframework.jms.core.JmsOperations;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NotificationReceiverImplementation implements NotificationReceiver {

    private JmsOperations jmsOperations;
    private boolean running;
    public NotificationReceiverImplementation(JmsOperations operations) {
        jmsOperations=operations;
    }
    ExecutorService service;
    NotificationSubscriber subscriber;

    @Override
    public void start(NotificationSubscriber subscriber) {
        System.out.println("Starting notification receiver ...");
        running=true;
        this.subscriber=subscriber;
        service = Executors.newSingleThreadExecutor();
        service.submit(()->{run();});
    }

    private void run(){
        while(running){
            Notification notif=(Notification)jmsOperations.receiveAndConvert();
            System.out.println("Received Notification... "+notif);
            subscriber.notificationReceived(notif);

        }
    }

    @Override
    public void stop() {
        running=false;
        try {
            service.awaitTermination(100, TimeUnit.MILLISECONDS);
            service.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stopped notification receiver");
    }
}
