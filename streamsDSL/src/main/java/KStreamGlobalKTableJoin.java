import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KStreamGlobalKTableJoin {
    private final static Logger logger = LoggerFactory.getLogger(SimpleStreamFilterApplication.class);
    private final static String APPLICATION_NAME = "global-table-join-application";
    private final static String BOOTSTRAP_SERVER = "192.168.0.11:9092";
    private final static String ADDRESS_GLOBAL_TABLE = "address_v2";
    private final static String ORDER_TABLE = "order";
    private final static String ORDER_JOIN_TABLE = "order_join";

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_NAME);
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        GlobalKTable<String, String> addressTable = builder.globalTable(ADDRESS_GLOBAL_TABLE);
        KStream<String, String> orderStream = builder.stream(ORDER_TABLE);

        orderStream.join(addressTable, (orderKey, orderValue) -> orderKey,  (order, address) -> order + " send to " + address)
                .to(ORDER_JOIN_TABLE);

        KafkaStreams streams = new KafkaStreams(builder.build(), properties);
        streams.start();

    }
}
