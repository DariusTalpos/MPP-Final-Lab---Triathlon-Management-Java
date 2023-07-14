package com.services.notification;

public interface NotificationReceiver {
    void start(NotificationSubscriber subscriber);
    void stop();
}
