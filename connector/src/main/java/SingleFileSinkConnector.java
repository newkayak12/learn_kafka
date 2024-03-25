import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2024-03-25
 * Project learn_kafka
 */
public class SingleFileSinkConnector extends SinkConnector {
    private Map<String, String> configProperties;

    @Override
    public void start(Map<String, String> map) {
        this.configProperties = map;
        try {
            new SingleFileSinkConnectorConfig(map);
        }catch (Exception e) {
            throw new ConnectException(e);
        }
    }

    @Override
    public Class<? extends Task> taskClass() {
        return SingleFileSinkTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        List<Map<String, String>> taskConfigs = new ArrayList<>();

        Map<String, String> taskProps = new HashMap<>();
        taskProps.putAll(configProperties);

        for( int i = 0; i < maxTasks; i ++ ) taskConfigs.add(taskProps);


        return taskConfigs;
    }

    @Override
    public void stop() {

    }

    @Override
    public ConfigDef config() {
        return SingleFileSinkConnectorConfig.CONFIG;
    }

    @Override
    public String version() {
        return "1.0";
    }
}
