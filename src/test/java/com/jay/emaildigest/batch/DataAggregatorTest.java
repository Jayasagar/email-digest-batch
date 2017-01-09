package com.jay.emaildigest.batch;

import com.jay.emaildigest.batch.model.Notification;
import com.jay.emaildigest.batch.model.User;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is independent local test.
 * TODO: Need to improve
 */
public class DataAggregatorTest {

    @Test
    public void messages_should_be_order_by_timestamp() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowMinusTen = now.minusMinutes(10);
        Notification notification1 = createNotification("jayasagar@gmail.com", "Jay", "Hi 1", nowMinusTen);
        Notification notification2 = createNotification("jayasagar@gmail.com", "Jay", "Hi 2", now);

        // Act - Purposely ordered wrong!
        Set<Map.Entry<User, String>> orderedByTimestamp = Arrays.asList(notification2, notification1)
                .parallelStream()
                .sorted(Comparator.comparing(Notification::getTimestamp))
                // Transfer data to Entry<User, Notification>
                .map(Notification::userNotificationEntry)
                // Group by email/user and map list of notification messages to single digest message!!
                .collect(Collectors.groupingBy(entry -> entry.getKey(),
                        Collectors.mapping(entry -> {
                            return String.format("%-35s%s", entry.getValue().getTimestamp().format(DateTimeFormatter.ofPattern("EEEE, HH:mm")), entry.getValue().getMessage());
                        }, Collectors.joining("\n"))))
                .entrySet();

        // Assert
        String[] splitMessage = orderedByTimestamp.iterator().next().getValue().split("\n");
        Assert.assertEquals(splitMessage[0], String.format("%-35s%s", nowMinusTen.format(DateTimeFormatter.ofPattern("EEEE, HH:mm")), "Hi 1"));
    }

    private Notification createNotification(String email, String name, String message, LocalDateTime timestamp) {
        Notification notification = new Notification();
        notification.setEmail(email);
        notification.setName(name);
        notification.setTimestamp(timestamp);
        notification.setMessage(message);
        return notification;
    }
}
