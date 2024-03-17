import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;

public class ConsumerRebalanceListener {
    private final static Logger logger = LoggerFactory.getLogger(SyncCommitEveryTimeConsumer.class);
    private final static String TOPIC_NAME = "test";
    private final static String BOOTSTRAP_SERVER = "192.168.0.11:9092";
    private final static String GROUP_ID = "test-group";

    private static KafkaConsumer<String , String> consumer = null;
    private static Map<TopicPartition, OffsetAndMetadata> currentOffset = new HashMap<>();


    public static void main(String[] args) {
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);


        consumer = new KafkaConsumer<String, String>(config);
        consumer.subscribe(Arrays.asList(TOPIC_NAME), new ReBalanceListener());

        Set<TopicPartition> assignedTopicPartition = consumer.assignment();
        logger.error("ASSIGNMENT {}", assignedTopicPartition);
        while( true ) {
            ConsumerRecords<String , String > records = consumer.poll(Duration.ofSeconds(1));

            for ( ConsumerRecord<String , String> record: records ) {
                logger.info("{}", record);
                currentOffset.put(
                        new TopicPartition(record.topic(), record.partition()),
                        new OffsetAndMetadata(record.offset() + 1, null)
                );

                consumer.commitSync(currentOffset);
            }
        }

    }

    private static class ReBalanceListener implements org.apache.kafka.clients.consumer.ConsumerRebalanceListener {

        @Override
        public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
            logger.warn("Partitions are assigned");
        }

        @Override
        public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
            logger.warn("Partitions are revoked");
            consumer.commitSync(currentOffset);
        }
    }


}


