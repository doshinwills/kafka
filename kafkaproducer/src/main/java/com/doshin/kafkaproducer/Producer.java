package com.doshin.kafkaproducer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This producer will send a bunch of messages to topic "fast-messages". Every
 * so often, it will send a message to "slow-messages". This shows how messages
 * can be sent to multiple topics. On the receiving end, we will see both kinds
 * of messages but will also see how the two topics aren't really synchronized.
 */
public class Producer {
	public static void main(String[] args) throws IOException {
		// set up the producer
		KafkaProducer<String, String> producer;

		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		producer = new KafkaProducer(props);

		try {

			for (int i = 0; i < 10; i++) {
				producer.send(new ProducerRecord<String, String>("rep-test",
						String.format("{\"type\":\"test\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i)));
				Thread.currentThread().sleep(1000l);

			}
		} catch (Throwable throwable) {
			System.out.printf("%s", throwable.getStackTrace());
		} finally {
			producer.close();
		}

	}
}