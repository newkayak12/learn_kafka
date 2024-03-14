

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Properties;

public class SimpleProducer {
    private final static Logger logger = LoggerFactory.getLogger(SimpleProducer.class);
    private final static String TOPIC_NAME = "test";
    private final static String BOOTSTRAP_SERVER = "192.168.0.11:9092";

    public static void main(String[] args) {
        Properties configs = new Properties();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(configs);


        String messageValue = "testMsg";
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, "pangyo", messageValue);
        //messageKey는 null

        producer.send(record);
        //send가 바로되는 것은 아니다.record를 프로듀서 내부에 가지고 있다가 배치 형태로 묶에서 브로커에 전송한다.
        //이를 배치 전송이라고 한다.
        logger.info("{}", record);
        producer.flush();
        //flush로 내부 버퍼에 있던 레코드 배치를 브로커에 전송한다.
        producer.close();
        //kafka를 정리한다.


        /**
         *    > ./bin/kafka-topics.sh \
         *      --bootstrap-server \
         *      localhost:9092 \
         *      --create \
         *      --topic test \
         *      --partitions 3
         *
         * [main] INFO org.apache.kafka.clients.producer.ProducerConfig - ProducerConfig values:
         * 	acks = 1
         * 	batch.size = 16384
         * 	bootstrap.servers = [192.168.0.11:9092]
         * 	buffer.memory = 33554432
         * 	client.dns.lookup = default
         * 	client.id = producer-1
         * 	compression.type = none
         * 	connections.max.idle.ms = 540000
         * 	delivery.timeout.ms = 120000
         * 	enable.idempotence = false
         * 	interceptor.classes = []
         * 	key.serializer = class org.apache.kafka.common.serialization.StringSerializer
         * 	linger.ms = 0
         * 	max.block.ms = 60000
         * 	max.in.flight.requests.per.connection = 5
         * 	max.request.size = 1048576
         * 	metadata.max.age.ms = 300000
         * 	metadata.max.idle.ms = 300000
         * 	metric.reporters = []
         * 	metrics.num.samples = 2
         * 	metrics.recording.level = INFO
         * 	metrics.sample.window.ms = 30000
         * 	partitioner.class = class org.apache.kafka.clients.producer.internals.DefaultPartitioner
         * 	receive.buffer.bytes = 32768
         * 	reconnect.backoff.max.ms = 1000
         * 	reconnect.backoff.ms = 50
         * 	request.timeout.ms = 30000
         * 	retries = 2147483647
         * 	retry.backoff.ms = 100
         * 	sasl.client.callback.handler.class = null
         * 	sasl.jaas.config = null
         * 	sasl.kerberos.kinit.cmd = /usr/bin/kinit
         * 	sasl.kerberos.min.time.before.relogin = 60000
         * 	sasl.kerberos.service.name = null
         * 	sasl.kerberos.ticket.renew.jitter = 0.05
         * 	sasl.kerberos.ticket.renew.window.factor = 0.8
         * 	sasl.login.callback.handler.class = null
         * 	sasl.login.class = null
         * 	sasl.login.refresh.buffer.seconds = 300
         * 	sasl.login.refresh.min.period.seconds = 60
         * 	sasl.login.refresh.window.factor = 0.8
         * 	sasl.login.refresh.window.jitter = 0.05
         * 	sasl.mechanism = GSSAPI
         * 	security.protocol = PLAINTEXT
         * 	security.providers = null
         * 	send.buffer.bytes = 131072
         * 	ssl.cipher.suites = null
         * 	ssl.enabled.protocols = [TLSv1.2]
         * 	ssl.endpoint.identification.algorithm = https
         * 	ssl.key.password = null
         * 	ssl.keymanager.algorithm = SunX509
         * 	ssl.keystore.location = null
         * 	ssl.keystore.password = null
         * 	ssl.keystore.type = JKS
         * 	ssl.protocol = TLSv1.2
         * 	ssl.provider = null
         * 	ssl.secure.random.implementation = null
         * 	ssl.trustmanager.algorithm = PKIX
         * 	ssl.truststore.location = null
         * 	ssl.truststore.password = null
         * 	ssl.truststore.type = JKS
         * 	transaction.timeout.ms = 60000
         * 	transactional.id = null
         * 	value.serializer = class org.apache.kafka.common.serialization.StringSerializer
         *
         * [main] INFO org.apache.kafka.common.utils.AppInfoParser - Kafka version: 2.5.0
         * [main] INFO org.apache.kafka.common.utils.AppInfoParser - Kafka commitId: 66563e712b0b9f84
         * [main] INFO org.apache.kafka.common.utils.AppInfoParser - Kafka startTimeMs: 1710334011117
         * [kafka-producer-network-thread | producer-1] INFO org.apache.kafka.clients.Metadata - [Producer clientId=producer-1] Cluster ID: CihwpILxSJCziJY-haxLMg
         * [main] INFO SimpleProducer - ProducerRecord(topic=test, partition=null, headers=RecordHeaders(headers = [], isReadOnly = true), key=null, value=testMsg, timestamp=null)
         * // record 출력
         * [main] INFO org.apache.kafka.clients.producer.KafkaProducer - [Producer clientId=producer-1] Closing the Kafka producer with timeoutMillis = 9223372036854775807 ms.
         *
         *
         *
         *     > kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning
         * testMsg
         */
    }



}

