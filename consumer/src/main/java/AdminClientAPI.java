import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeConfigsResult;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.config.ConfigResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class AdminClientAPI {
    private final static Logger logger = LoggerFactory.getLogger(AsyncCommitConsumer.class);
    private final static String TOPIC_NAME = "test";
    private final static String BOOTSTRAP_SERVER = "192.168.0.11:9092";
    private final static String GROUP_ID = "test-group";
    private static AdminClient adminClient = null;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties configs = new Properties();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        adminClient = AdminClient.create(configs);

//        brokerInfo();
        topicInfo();

        adminClient.close();
    }

    private static void brokerInfo () throws ExecutionException, InterruptedException {
        logger.info("== Get broker information");
        for( Node node : adminClient.describeCluster().nodes().get() ) {
            logger.info("node : {}", node);
            ConfigResource cr = new ConfigResource(ConfigResource.Type.BROKER, node.idString());
            DescribeConfigsResult describeConfigs = adminClient.describeConfigs(Collections.singleton(cr));

            describeConfigs.all().get().forEach((broker, config) -> {
                config.entries().forEach(configEntry -> logger.info(configEntry.name() + "= " + configEntry.value()));
            });
        }
    }

    private static void topicInfo () throws ExecutionException, InterruptedException {
        logger.info("== Get topic information");
        Map<String, TopicDescription> topicInformation = adminClient.describeTopics(Collections.singletonList("test")).all().get();
        logger.info("{}", topicInformation);
    }
}
