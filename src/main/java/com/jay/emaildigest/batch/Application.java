package com.jay.emaildigest.batch;

import com.jay.emaildigest.batch.model.Notification;
import com.jay.emaildigest.batch.service.BatchSchedularService;
import com.jay.emaildigest.batch.repo.NotificationRepo;
import com.jay.emaildigest.batch.repo.NotificationRepoImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(Application.class).run(args);
        //saveRecords(ctx);

        NotificationRepoImpl notificationRepo = ctx.getBean(NotificationRepoImpl.class);
        //List<String> users = notificationRepo.getAllUnProcessedDistinctEmails();
        //System.out.println("users:" + users.size());

        //System.out.println(notificationRepo.getUserNotifications("jayasagar1@gmail.com").size());

        System.out.println("Start:" + Instant.now());
        BatchSchedularService batchSchedularService = ctx.getBean(BatchSchedularService.class);
        batchSchedularService.sendHourlyDigestEmail();
        System.out.println("End:" + Instant.now());
    }

    private static void saveRecords(ConfigurableApplicationContext ctx) {
        NotificationRepo notificationRepo = ctx.getBean(NotificationRepo.class);

        int j = 0;
        // One milliion records Saved
        for (int i = 0; i< 1000000;i++) {

            if (j == 10000) {
                j = 0;
            }
            Notification notification = new Notification();
            notification.setMessage("Messsage " + i);
            notification.setEmail("jayasagar" + j + "@gmail.com");
            notification.setTimestamp(LocalDateTime.now());
            notification.setName("User" + j);

            notificationRepo.save(notification);

            j++;
        }
    }
}
