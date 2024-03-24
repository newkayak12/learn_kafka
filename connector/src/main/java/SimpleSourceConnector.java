import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleSourceConnector extends SourceConnector {

    private Map<String, String> configProperties;

    @Override
    public void start(Map<String, String> props) {
        this.configProperties = props;
        try {
            new SingleFileConnectorConfig(props);
        } catch (Exception e) {
            throw new ConnectException(e.getMessage());
        }

    }

    @Override
    public Class<? extends Task> taskClass() {
        return SingleFileSourceTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        List<Map<String, String>> taskConfigs = new ArrayList<>();
        Map<String, String> taskProps = new HashMap<>();
        taskProps.putAll(configProperties);

        for ( int i = 0; i < maxTasks; i ++ ) {
            taskConfigs.add(taskProps);
        }

        return taskConfigs;
    }

    @Override
    public void stop() {

    }

    @Override
    public ConfigDef config() {
        return SingleFileConnectorConfig.CONFIG;
    }

    @Override
    public String version() {
        return "1.0";
    }
}
