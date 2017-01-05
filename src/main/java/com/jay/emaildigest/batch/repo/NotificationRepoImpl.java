package com.jay.emaildigest.batch.repo;

import com.jay.emaildigest.batch.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificationRepoImpl implements CustomNotificationRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<String> getAllUnProcessedDistinctEmails() {
        return mongoTemplate.getCollection("notification")
                .distinct("email");//, new BasicDBObject("isProcessed", new BasicDBObject("$eq", true)));
    }

    public List<Notification> getUserNotifications(String email) {
        return mongoTemplate.find(new Query()
                .addCriteria(Criteria.where("email").is(email)), Notification.class);
    }
}
