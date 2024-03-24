import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

public class SingleFileConnectorConfig extends AbstractConfig {
    public static final String DIR_FILE_NAME = "file";
    private static final String DIR_FILE_NAME_DEFAULT_VALUE = "/tmp/kafka.txt";
    private static final String DIR_FILE_NAME_DOC = "path, fileName";

    public static final String TOPIC_NAME = "topic";
    private static final String TOPIC_DEFAULT_VALUE = "test";
    private static final String TOPIC_DOC = "topic name";

    public static final ConfigDef CONFIG = new ConfigDef()
            .define(DIR_FILE_NAME, ConfigDef.Type.STRING, DIR_FILE_NAME_DEFAULT_VALUE, ConfigDef.Importance.HIGH, DIR_FILE_NAME_DOC)
            .define(TOPIC_NAME, ConfigDef.Type.STRING, TOPIC_DEFAULT_VALUE, ConfigDef.Importance.HIGH, TOPIC_DOC);


    public SingleFileConnectorConfig(Map<String, String > props) {
        super(CONFIG, props);
    }
}
