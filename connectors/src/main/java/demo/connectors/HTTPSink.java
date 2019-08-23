package demo.connectors;

import org.apache.pulsar.functions.api.Record;
import org.apache.pulsar.io.core.Sink;
import org.apache.pulsar.io.core.SinkContext;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HTTPSink implements Sink<String> {

    @Override
    public void open(Map<String, Object> config, SinkContext sinkContext) throws Exception {
        // Do nothing.
    }

    @Override
    public void close() throws Exception {
        // Do nothing.
    }

    @Override
    public void write(Record<String> record) throws Exception {
        String messageContent = record.getValue();
        try {
            post("http://web:5000/message", messageContent);
            record.ack();
        } catch (IOException e) {
            record.fail();
        }
    }

    private static String post(String url, String data) throws IOException {
        // Create connection.
        HttpURLConnection urlConn;
        URL mUrl = new URL(url);
        urlConn = (HttpURLConnection) mUrl.openConnection();
        urlConn.setDoOutput(true);
        // Send request body.
        if (data != null) {
            urlConn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            urlConn.getOutputStream().write(data.getBytes(StandardCharsets.UTF_8));
        }
        // Get response content.
        BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String responseLine = null;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }
        return response.toString();
    }
}
