package com.spring.microservices.goolepubsub1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.microservices.goolepubsub1.GoolePubSub1Application.PubsubOutboundGateway;

@RestController
public class WebAppController {

    // tag::autowireGateway[]
    @Autowired
    private PubsubOutboundGateway messagingGateway;
    // end::autowireGateway[]
    
    @PostMapping("/publishMessage")
    public String publishMessage(@RequestBody MyAppGCPMessage  message) {
        messagingGateway.sendToPubsub(message.toString());
        return "message publised successfully";
        
    }
    
 /*// Consume from the PubSub topic (fetch first message)
    // Ref. https://cloud.google.com/pubsub/docs/subscriber#pubsub-create-subscription-java
    @RequestMapping("/fetch")
    public PubSubMessage fetch() {
        Subscription subscription = pubSub.getSubscription(subscriptionName);
        if (subscription == null) {
            logger.info("Subscription is null.  Creating one (name: " + subscriptionName + ")");
            subscription = pubSub.create(SubscriptionInfo.of(topicName, subscriptionName));
            String subStatus = (subscription == null ? "FAILED" : "SUCCESS");
            logger.info("Status: " + subStatus);
        } else {
            logger.info("Got subscription \"" + subscriptionName + "\"");
        }
        // Pick up and return at most one message per REST call
        Iterator<ReceivedMessage> messageIterator = subscription.pull(1);
        PubSubMessage rv;
        if (messageIterator.hasNext()) {
            ReceivedMessage recd = messageIterator.next();
            String id = recd.getId();
            long pubTime = recd.getPublishTime();
            rv = new PubSubMessage(id, pubTime, recd.getPayloadAsString());
            recd.ack();
        } else {
            rv = new PubSubMessage("none", -1, "No Message in Queue");
        }
        return rv;
    }*/

   }