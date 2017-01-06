# email-digest-batch
Batch processor to aggregate and send email

### Installations
* Install JDK 8
* MongoDB

#### How to run
* Check out the code from https://github.com/Jayasagar/email-digest-batch
* Run **/.gradlew build** It should produce executable jar under build/libs
* Create a dummy gmail account, modify email and password the **application.properties** under project checkout root folder

##### Run as a service
* sudo mkdir /var/email-digest-batch
* sudo cp $CHECK_OUT/build/libs/email-digest-batch-0.1.jar /var/email-digest-batch
* sudo cp $CHECK_OUT/application.properties /var/email-digest-batch
* sudo ln -s /var/email-digest/email-digest-batch-0.1.jar /etc/init.d/email-digest-batch
* sudo /etc/init.d/email-digest-batch start|stop|restart

##### Run a jar
* java -jar build/libs/email-digest-batch-0.1.jar --spring.config.location= $CHECKOUT_ROOT_PATH/application.properties

### Current execution flow and solution implemented using Spring Schedular
* Every hour run the batch job
* Pull all distinct email Ids 
* Create a HourlyEmailTask (which will be submitted to thread pool), which is responsible for the aggregating each user message and send an email.
* Delete the records once email sent.

#### Quick simple Performance test
* I have tested the batch job with 1 million records in MongoDB, my observation is without send email, it took almost 6 minutes to process
* 10000 user email and 100 message for each user is the setup I made :) 