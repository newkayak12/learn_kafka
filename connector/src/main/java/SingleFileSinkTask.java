import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;

import java.io.File;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Created on 2024-03-25
 * Project learn_kafka
 */
public class SingleFileSinkTask extends SinkTask {

    private SingleFileSinkConnectorConfig config;
    private File file;
    private FileWriter fileWriter;

    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public void start(Map<String, String> map) {

        try {
            config = new SingleFileSinkConnectorConfig(map);
            file  = new File(config.getString(config.DIR_FILE_NAME));
            fileWriter = new FileWriter(file, true);
        } catch (Exception e) {
            throw new ConnectException(e);
        }

    }

    @Override
    public void put(Collection<SinkRecord> collection) {
        try {
            for ( SinkRecord r : collection) {
                fileWriter.write(r.value().toString() + "\n");
            }
        } catch (Exception e) {
            throw new ConnectException(e);
        }
    }

    @Override
    public void flush(Map<TopicPartition, OffsetAndMetadata> currentOffsets) {
        try {
            fileWriter.flush();
        } catch (IOException e) {
            throw new ConnectException(e);
        }
    }


    @Override
    public void stop() {
        try {
            fileWriter.close();
        } catch (IOException e) {
            throw new ConnectException(e);
        }
    }
}
