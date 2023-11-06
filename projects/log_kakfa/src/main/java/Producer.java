import java.io.*;
import java.util.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class Producer extends Thread {

    static String TOPIC_NAME = "TestTopic00";

    private File targetFile;
    private String fileName;
    private BufferedReader reader;
    private String[] buffer = new String[5];
    private long updateTime; 
    private Properties configs;
    private KafkaProducer<String, String> producer;

    public Producer(String _fileName) {
        fileName = _fileName;
        configs = new Properties();
        configs.put("bootstrap.servers", "localhost:9092");
        configs.put("acks", "all");
        configs.put("block.on.buffer.full", "true");
        configs.put("compression.type", "gzip");
        configs.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");       
        configs.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");     
        producer = new KafkaProducer<String, String>(configs);
    }

    public void read() {
        try {
            File file = new File(fileName);
            updateTime = file.lastModified();
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String line = "";
            int count = 0;
            while(true) {
                line = reader.readLine();

                if(line == null) {
                    Thread.sleep(10);
                    continue;
                }
                ProducerRecord<String,String> prodRecord = new ProducerRecord<String, String>(TOPIC_NAME, line);
                try {
                    producer.send(prodRecord);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(reader != null) {reader.close();}
            } catch(Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public void run() {
        read();
    }

    public static void main(String[] args) {
        new Producer(args[0]).start();
    }
}
