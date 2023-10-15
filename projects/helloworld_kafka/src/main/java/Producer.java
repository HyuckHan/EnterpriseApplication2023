import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class Producer {
    static String TOPIC_NAME = "TestTopic00";

    public static void main(String[] args) throws IOException {

        Properties configs = new Properties();
        configs.put("bootstrap.servers", "localhost:9092");
        configs.put("acks", "all");
        configs.put("block.on.buffer.full", "true");
        configs.put("compression.type", "gzip");
        configs.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        configs.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // producer 생성
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(configs);

        // log에 남길 시간
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = now.format(format);

        // 시작 시간 저장
        long start = System.currentTimeMillis();

        // message 전달 by for문
        for (int i = 0; i < 100; i++) {
            String data = (formatDateTime + ":" + i + "번째 " + "테스트용 메시지 전송 by somsom");
            ProducerRecord<String,String> prodRecord = new ProducerRecord<String, String>(TOPIC_NAME, data);
            try {
                producer.send(prodRecord);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("전송까지 걸린 시간 :" + String.valueOf(System.currentTimeMillis() - start));

        // 종료
        producer.flush();
        producer.close();
    }
}
