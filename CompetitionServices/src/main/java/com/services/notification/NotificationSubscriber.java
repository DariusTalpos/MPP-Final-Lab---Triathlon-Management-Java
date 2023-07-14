package com.services.notification;

import com.notification.Notification;

public interface NotificationSubscriber {
    void notificationReceived(Notification notif);
}
