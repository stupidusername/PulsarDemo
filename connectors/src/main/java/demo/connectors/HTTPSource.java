package demo.connectors;

import com.sun.net.httpserver.HttpServer;
import demo.handlers.PostHandler;
import demo.records.Message;
import org.apache.pulsar.io.core.PushSource;
import org.apache.pulsar.io.core.SourceContext;

import java.net.InetSocketAddress;
import java.util.Map;

public class HTTPSource extends PushSource<String> {

    private HttpServer server;

    @Override
    public void open(Map<String, Object> config, SourceContext sourceContext) throws Exception {
        server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/message", new PostHandler(this::sendString));
        server.start();
    }

    @Override
    public void close() throws Exception {
        server.stop(0);
    }


    private void sendString(String content) {
        consume(new Message(content));
    }
}
