import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

public class Consumer {

    static String TOPIC_NAME = "TestTopic00";

    public static void main(String[] args) {

        Properties configs = new Properties();
        configs.put("bootstrap.servers", "localhost:9092");
        configs.put("group.id", "TestTopic");
        configs.put("session.timeout.ms", "10000");
        configs.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        configs.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(configs);
        consumer.subscribe(Arrays.asList(TOPIC_NAME));

        try {
            while (true) {
                // 계속 loop를 돌면서 producer의 message를 consume
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records)
                    System.out.println("offset: " + record.offset() + ", key: " + record.key() + ", value: " + record.value());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}
