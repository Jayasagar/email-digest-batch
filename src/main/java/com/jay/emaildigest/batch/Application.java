package com.jay.emaildigest.batch;

import com.jay.emaildigest.batch.model.Notification;
import com.jay.emaildigest.batch.repo.NotificationRepo;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableScheduling
public class Application {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(Application.class).run(args);
        //saveRecords(ctx);

        LOG.info(String.format("Started batch processing: %s", LocalDateTime.now().toString()));
    }

    private static void saveRecords(ConfigurableApplicationContext ctx) {
        NotificationRepo notificationRepo = ctx.getBean(NotificationRepo.class);

        int j = 0;
        // One million records Saved
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
