package com.jay.emaildigest.batch.service;

import com.jay.emaildigest.batch.model.Notification;
import com.jay.emaildigest.batch.model.User;
import com.jay.emaildigest.batch.repo.NotificationRepo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationRepo notificationRepo;

    public List<Notification> getUnProcessedUserNotifications(String email) {
        return notificationRepo.getUserNotifications(email);
    }

    public void deleteProcessedNotifications(List<Notification> notifications) {
        notificationRepo.delete(notifications);
    }

    public List<String> getAllUnProcessedUsers() {
        return notificationRepo.getAllUnProcessedDistinctEmails();
    }

    public Map<User, String> lastOneHourNotifications() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime hourBack = LocalDateTime.now().minusHours(1);

        List<Notification> notifications = notificationRepo.lastOneHourNotifications(hourBack, now);

        // Transfer notification to Entry with <user, notification>
        // Group by user(email) and map to message using joiner
        Map<User, String> userHourlyDigestBody = notifications
                .stream()
                .map(Notification::userNotificationEntry)
                .collect(Collectors.groupingBy(entry -> entry.getKey(),
                        Collectors.mapping(entry -> {
                            return String.format("%-35s%s", entry.getValue().getTimestamp().format(DateTimeFormatter.ofPattern("EEEE, HH:mm")), entry.getValue().getMessage());
                        }, Collectors.joining("\n"))));

        return userHourlyDigestBody;
    }
}
