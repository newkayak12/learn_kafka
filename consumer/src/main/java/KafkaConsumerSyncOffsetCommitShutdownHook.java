import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class KafkaConsumerSyncOffsetCommitShutdownHook {
    private final static Logger logger = LoggerFactory.getLogger(SyncCommitEveryTimeConsumer.class);
    private final static String TOPIC_NAME = "test";
    private final static String BOOTSTRAP_SERVER = "192.168.0.11:9092";
    private final static String GROUP_ID = "test-group";
    private static KafkaConsumer<String , String> consumer;
    private static Map<TopicPartition, OffsetAndMetadata> currentOffset = new HashMap<>();




    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new ShutdownThread());

        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);


        consumer = new KafkaConsumer<String, String>(config);
        consumer.subscribe(Arrays.asList(TOPIC_NAME));

        try  {
            while( true ) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                for(ConsumerRecord<String, String> record: records) logger.info("{}", record);
            }

        } catch (WakeupException e) {
            logger.warn("WAKEUP CONSUMER");
        } finally {
            consumer.close();
        }
    }

    static class ShutdownThread extends Thread {
        public void run () {
            logger.info("SHUTDOWN");
            consumer.wakeup();
        }
    }
}
