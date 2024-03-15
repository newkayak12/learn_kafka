import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerExactPartition {
    private final static Logger logger = LoggerFactory.getLogger(SyncCommitEveryTimeConsumer.class);
    private final static String TOPIC_NAME = "test";
    private final static String BOOTSTRAP_SERVER = "192.168.0.11:9092";
    private final static String GROUP_ID = "test-group";
    private final static Integer PARTITION_NUMBER = 0;

    public static void main(String[] args) {
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        KafkaConsumer<String , String> consumer = new KafkaConsumer<String, String>(config);;
        consumer.assign(Collections.singleton(new TopicPartition(TOPIC_NAME, PARTITION_NUMBER)));

        while ( true ) {
            ConsumerRecords<String , String > records = consumer.poll(Duration.ofSeconds(1));

            for ( ConsumerRecord<String, String> record: records) {
                logger.info("{}", record);
            }

            consumer.commitSync();
        }

    }
}
