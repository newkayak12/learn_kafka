import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

/**
 * Created on 2024-03-25
 * Project learn_kafka
 */
public class SingleFileSinkConnectorConfig extends AbstractConfig {

    public static final String DIR_FILE_NAME = "file";
    private static final String DIR_FILE_NAME_DEFAULT_VALUE = "/tmp/kafka.txt";
    private static final String DIR_FILE_NAME_DOC = "FILE NAME";

    public static ConfigDef CONFIG = new ConfigDef().define(DIR_FILE_NAME, ConfigDef.Type.STRING, DIR_FILE_NAME_DEFAULT_VALUE, ConfigDef.Importance.HIGH, DIR_FILE_NAME_DOC);


    public SingleFileSinkConnectorConfig(Map<String, String> props) {
        super(CONFIG, props);
    }
}
