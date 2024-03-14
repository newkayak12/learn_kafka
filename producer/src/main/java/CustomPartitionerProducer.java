import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.InvalidRecordException;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class CustomPartitionerProducer {

    private final static Logger logger = LoggerFactory.getLogger(SimpleProducer.class);
    private final static String TOPIC_NAME = "test";
    private final static String BOOTSTRAP_SERVER = "192.168.0.11:9092";

    public static void main(String[] args) {
        Properties configs = new Properties();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class);

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(configs);


        String messageValue = "testMsg";
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, "pangyo", messageValue);
        //messageKey는 null

        try {
            producer.send(record, new ProducerCallback());
            logger.info("--------------------------------");

            RecordMetadata metadata = producer.send(record).get();
            logger.info("METADATA {}", metadata);

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        producer.flush();
        producer.close();

        /**
         * root@6b1197d09376:/# kafka-console-consumer.sh \
         * --bootstrap-server localhost:9092 \
         * --topic test \
         * --property print.key=true \
         * --property key.separator="-" \
         * --from-beginning
         *
         * pangyo-testMsg
         * null-testMsg
         * pangyo-testMsg
         */
    }


    public static class CustomPartitioner implements Partitioner {
        @Override
        public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
            //레코드를 기반으로 파티션을 정하는 로직이 포함된다.

            if (Objects.isNull(keyBytes)) throw new InvalidRecordException("Need MsgKey");
            if ("pangyo".equals((String) key)) return 0;

            List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
            int numPartitions = partitions.size();
            return Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitions;


        }

        @Override
        public void close() {

        }

        @Override
        public void configure(Map<String, ?> configs) {

        }
    }
    public static class ProducerCallback implements Callback {
        private final static Logger logger = LoggerFactory.getLogger(SimpleProducer.class);
        @Override
        public void onCompletion(RecordMetadata metadata, Exception exception) {
            if( Objects.nonNull(exception)) logger.error(exception.getMessage(), exception);
            else logger.info(metadata.toString());
        }
    }
}
