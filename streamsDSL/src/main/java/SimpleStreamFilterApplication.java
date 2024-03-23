import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class SimpleStreamFilterApplication {
    private final static Logger logger = LoggerFactory.getLogger(SimpleStreamFilterApplication.class);
    private final static String APPLICATION_NAME = "streams-application";
    private final static String BOOTSTRAP_SERVER = "192.168.0.11:9092";
    private final static String STREAM_LOG = "stream_log";
    private final static String STREAM_LOG_COPY = "stream_log_copy";


    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_NAME);
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> streamLog = builder.stream(STREAM_LOG);
        KStream<String, String> filteredLog  = streamLog.filter((key, value) -> value.length() > 5);
        filteredLog.to(STREAM_LOG_COPY);

        KafkaStreams streams = new KafkaStreams(builder.build(), properties);
        streams.start();
    }
}
