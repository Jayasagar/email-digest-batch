package com.jay.emaildigest.batch.service;

import com.jay.emaildigest.batch.model.User;
import com.jay.emaildigest.batch.model.Notification;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Component
public class BatchSchedularService {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(BatchSchedularService.class);

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private Executor taskExecutor;

    @Autowired
    private EmailService emailService;

    /**
     * Pull all distinct the records by last 1 hour range records.
     * Using streams process the data, group them and construct the email body.
     */
    //@Scheduled(cron = "0 0 * * * *") //Every hour of every day
    @Scheduled(cron = "0 0/1 * * * *") // Every 1 minutes for testing
    public void sendHourlyDigestEmail() {
        LOG.info(String.format("Hourly schedular, time now: %s", LocalDateTime.now().toString()));

        List<String> allUnProcessedUsers = notificationService.getAllUnProcessedUsers();

        allUnProcessedUsers
                .stream()
                .forEach(email -> {
                    // User un processed emails
                    List<Notification> userUnProcessedNotifications = notificationService.getUnProcessedUserNotifications(email);

                    // Submit to send an email
                    taskExecutor.execute(new HourlyEmailTask(userUnProcessedNotifications, emailService, notificationService));
                });
    }

    /**
     * It is thread worker task, going to execute set of specific user un processed messages and send an email.
     */
    class HourlyEmailTask implements Runnable {

        private List<Notification> userUnProcessedNotifications;
        private EmailService emailService;
        private NotificationService notificationService;

        public HourlyEmailTask(List<Notification> userUnProcessedNotifications, EmailService emailService, NotificationService notificationService) {
            this.userUnProcessedNotifications = userUnProcessedNotifications;
            this.emailService = emailService;
            this.notificationService = notificationService;
        }

        @Override
        public void run() {
            userUnProcessedNotifications
                    .parallelStream()
                    // Transfer data to Entry<User, Notification>
                    .map(Notification::userNotificationEntry)
                    // Group by email/user and map list of notification messages to single digest message!!
                    .collect(Collectors.groupingBy(entry -> entry.getKey(),
                            Collectors.mapping(entry -> {
                                return String.format("%-35s%s", entry.getValue().getTimestamp().format(DateTimeFormatter.ofPattern("EEEE, HH:mm")), entry.getValue().getMessage());
                            }, Collectors.joining("\n"))))
                    .entrySet()
                    .parallelStream()
                    .forEach(entry -> {
                        StringBuilder body = new StringBuilder()
                                .append(String.format("Hi %s, your friends are active!", entry.getKey().getName()))
                                .append("\n\n")
                                .append(entry.getValue());

                        emailService.sendMailMessage(entry.getKey().getEmail(), body.toString());
                    });

            // TODO : Delete the processed records
            notificationService.deleteProcessedNotifications(userUnProcessedNotifications);
        }
    }

    /**
     * Pull all the records by last 1 hour range records.
     * Using streams process the data, group them and construct the email body.
     */
    @Deprecated
    //@Scheduled(cron = "0 0 * * * *") //Every hour of every day
    //@Scheduled(cron = "0 0/15 * * * *") // Every 15 minutes for testing
    public void sendHourlyDigestEmailSolution2() {
        LOG.info(String.format("Hourly schedular, time now: %s", LocalDateTime.now().toString()));

        Map<User, String> userHourlyDigestBody = notificationService.lastOneHourNotifications();

        userHourlyDigestBody
                .entrySet()
                .stream()
                .forEach(entry -> {
                    StringBuilder body = new StringBuilder()
                            .append(String.format("Hi %s, your friends are active!", entry.getKey().getName()))
                            .append("\n\n")
                            .append(entry.getValue());

                    emailService.sendMailMessage(entry.getKey().getEmail(), body.toString());
                });
    }
}
