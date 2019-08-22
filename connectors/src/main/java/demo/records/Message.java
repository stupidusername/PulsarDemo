package demo.records;

import org.apache.pulsar.functions.api.Record;

public class Message implements Record<String> {

    private String content;

    public Message(String content) {
        this.content = content;
    }

    public String getValue() {
        return content;
    }
}
