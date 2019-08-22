package demo.connectors;

import com.sun.net.httpserver.HttpServer;
import demo.handlers.GetHandler;
import org.apache.pulsar.functions.api.Record;
import org.apache.pulsar.io.core.Sink;
import org.apache.pulsar.io.core.SinkContext;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Map;

public class HTTPSink implements Sink<String> {

    private HttpServer server;
    private ArrayList<String> messages = new ArrayList<>();

    @Override
    public void open(Map<String, Object> config, SinkContext sinkContext) throws Exception {
        this.server = HttpServer.create(new InetSocketAddress(8008), 0);
        this.server.createContext("/read", new GetHandler(this::getReadContent));
        this.server.start();
    }

    @Override
    public void close() throws Exception {
        this.server.stop(0);
    }

    @Override
    public void write(Record<String> record) throws Exception {
        messages.add(record.getValue());
        record.ack();
    }

    private String getReadContent() {
        StringBuilder content = new StringBuilder("<html><head><title>Messages</title></head><body><ol>");
        content.append("</ol></body></html>");
        for (String message : messages) {
            content.append("<li>").append(message).append("</li>");
        }
        // Empty list.
        messages = new ArrayList<>();
        return content.toString();
    }
}
