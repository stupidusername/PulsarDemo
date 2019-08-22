package demo.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Callable;

public class GetHandler implements HttpHandler {

    private Callable<String> getResponse;

    public GetHandler(Callable<String> getResponse) {
        this.getResponse = getResponse;
    }

    @Override
    public void handle(HttpExchange he) throws IOException {
        // Send response.
        int status = 200;
        String response = null;
        try {
            response = this.getResponse.call();
        } catch (Exception e) {
            status = 500;
            response = "ERROR";
        }
        he.sendResponseHeaders(status, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
