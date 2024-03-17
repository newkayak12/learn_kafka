import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

public class SimpleConsumer {
    private final static Logger logger = LoggerFactory.getLogger(SimpleConsumer.class);
    private final static String TOPIC_NAME = "test";
    private final static String BOOTSTRAP_SERVER = "192.168.0.11:9092";
    private final static String GROUP_ID = "test-group";

    public static void main(String[] args) {
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        /**
         * 컨슈머 그룹 이름을 선언한다. 컨슈머 그룹을 통해 컨슈머의 목적을 구분할 수 있다.
         * group으로 지정해서 동일한 역할을 하는 컨슈머를 묶어 관리할 수 있다.
         * 컨슈머 그룹을 기준으로 컨슈머 오프셋을 관리하기 때문에 subscribe() 메소드를 사용해서
         * 토픽을 구독하는 경우에는 컨슈머 그룹을 선언해야한다.
         *
         * 컨슈머가 중단하거나 재시작되더라도 컨슈머의 컨슈머 오프셋을 기준으로 데이터 처리를 하기 떄문이다.
         */
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(config);
        consumer.subscribe(Arrays.asList(TOPIC_NAME));

        Set<TopicPartition> assignedTopicPartition = consumer.assignment();
        logger.error("ASSIGNMENT {}", assignedTopicPartition);


        while ( true ) {
            ConsumerRecords<String , String > records = consumer.poll(Duration.ofSeconds(1));

            for ( ConsumerRecord<String, String> record: records) {
                logger.info("{}", record);
            }


        }


    }
}
