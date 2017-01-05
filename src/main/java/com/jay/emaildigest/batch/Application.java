package com.jay.emaildigest.batch;

import com.jay.emaildigest.batch.service.BatchSchedularService;
import com.jay.emaildigest.batch.repo.NotificationRepo;
import com.jay.emaildigest.batch.repo.NotificationRepoImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {

        // Enable MongoDB logging in general
        System.setProperty("DEBUG.MONGO", "true");

// Enable DB operation tracing
        System.setProperty("DB.TRACE", "true");

        Logger mongoLogger = Logger.getLogger( "com.mongodb" );
        mongoLogger.setLevel(Level.SEVERE);

        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(Application.class).run(args);
        //saveRecords(ctx);

        NotificationRepoImpl notificationRepo = ctx.getBean(NotificationRepoImpl.class);
        List<String> users = notificationRepo.getAllUnProcessedDistinctEmails();
        System.out.println("users:" + users);
        //BatchSchedularService emailService = ctx.getBean(BatchSchedularService.class);
        //emailService.sendHourlyDigestEmail();
        System.out.println(notificationRepo.getUserNotifications("jayasagar@gmail.com"));

        BatchSchedularService batchSchedularService = ctx.getBean(BatchSchedularService.class);
        batchSchedularService.sendHourlyDigestEmail();
    }

    private static void saveRecords(ConfigurableApplicationContext ctx) {
        NotificationRepo notificationRepo = ctx.getBean(NotificationRepo.class);
//        notificationRepo.save(Notification.builder()
//                .email("jayasagar1@gmail.com")
//                .message("Date now cron test 1")
//                .isProcessed(true)
//                .timestamp(LocalDateTime.now())
//                .build());
//        notificationRepo.save(Notification.builder()
//                .email("jayasagar1@gmail.com")
//                .message("Date now cron test 2")
//                .isProcessed(false)
//                .timestamp(LocalDateTime.now().minusHours(1))
//                .build());
//        notificationRepo.save(Notification.builder()
//                .email("jayasagar2@gmail.com")
//                .message("Date now cron test 3")
//                .isProcessed(false)
//                .timestamp(LocalDateTime.now().minusHours(2))
//                .build());
//
//        notificationRepo.save(Notification.builder()
//                .email("sri1@gmail.com")
//                .message("Date now cron test 4")
//                .timestamp(LocalDateTime.now().minusHours(2))
//                .build());
    }
}
