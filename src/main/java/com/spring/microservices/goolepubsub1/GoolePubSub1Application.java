package com.spring.microservices.goolepubsub1;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import org.springframework.cloud.gcp.pubsub.integration.outbound.PubSubMessageHandler;
import org.springframework.cloud.gcp.pubsub.support.GcpPubSubHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import com.google.cloud.pubsub.v1.AckReplyConsumer;

@SpringBootApplication
public class GoolePubSub1Application {

	// private static final Log LOGGER =
	// LogFactory.getLog(GoolePubSub1Application.class);

	public static void main(String[] args) {
		SpringApplication.run(GoolePubSub1Application.class, args);
	}
	// Inbound channel adapter.

	// tag::pubsubInputChannel[]
	@Bean
	public MessageChannel pubsubInputChannel() {
		return new DirectChannel();
	}
	// end::pubsubInputChannel[]

	// tag::messageChannelAdapter[]
	@Bean
	public PubSubInboundChannelAdapter messageChannelAdapter(
			@Qualifier("pubsubInputChannel") MessageChannel inputChannel, PubSubTemplate subscriberFactory) {
		PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(subscriberFactory, "testSubscription");
		adapter.setOutputChannel(inputChannel);
		//adapter.setAckMode(AckMode.MANUAL);

		return adapter;
	}
	// end::messageChannelAdapter[]

	// tag::messageReceiver[]
	@Bean
	@ServiceActivator(inputChannel = "pubsubInputChannel")
	public MessageHandler messageReceiver() {
		return message -> {

			byte[] payload = (byte[]) message.getPayload();
			try {
				String payloadformatted = new String(payload, "UTF8");
				System.out.println("Message arrived! Payload converted byte to string: " + payloadformatted);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			@SuppressWarnings("deprecation")
			AckReplyConsumer consumer = (AckReplyConsumer) message.getHeaders().get(GcpPubSubHeaders.ACKNOWLEDGEMENT);
			consumer.ack();
		};
	}
	// end::messageReceiver[]

	// Outbound channel adapter

	// tag::messageSender[]
	@Bean
	@ServiceActivator(inputChannel = "pubsubOutputChannel")
	public MessageHandler messageSender(PubSubTemplate pubsubTemplate) {
		PubSubMessageHandler outboundAdapter = new PubSubMessageHandler(pubsubTemplate, "testTopic");
		outboundAdapter.setTopic("testTopic");
		return outboundAdapter;
	}
	// end::messageSender[]

	// tag::messageGateway[]
	@MessagingGateway(defaultRequestChannel = "pubsubOutputChannel")
	public interface PubsubOutboundGateway {

		void sendToPubsub(String message);
	}
	// end::messageGateway[]
}
