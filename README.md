# email-digest-batch
Batch processor to aggregate and send email

#### How to run
* Check out the code from https://github.com/Jayasagar/email-digest-batch
* Run **/.gradlew build** It should produce executable jar under build/libs
* Create a dummy gmail account, modify email and password the **application.properties** under project checkout root folder

##### Run as a service
* sudo ln -s /var/email-digest/email-digest-batch-0.1.jar /etc/init.d/email-digest-batch
* sudo /etc/init.d/email-digest-batch start|stop|restart

##### Run a jar
* java -jar build/libs/email-digest-batch-0.1.jar --spring.config.location= $CHECKOUT_ROOT_PATH/application.properties

### Current execution flow and solution implemented using Spring Schedular
* Every hour run the batch job
* Pull all distinct email Ids 
* Create a HourlyEmailTask, which is responsible for the aggregating each user message and send an email.
* Delete the records once email sent.