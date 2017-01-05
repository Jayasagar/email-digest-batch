package com.jay.emaildigest.batch.repo;

import com.jay.emaildigest.batch.model.Notification;

import java.util.List;

public interface CustomNotificationRepository {

    /**
     * Gets the all distinct emails from Db, so that we can process one by one.
     */
    List<String> getAllUnProcessedDistinctEmails();

    /**
     * Get all the notifications by the email Id, so that we can combine all user records into email body.
     */
    List<Notification> getUserNotifications(String email);
}
