# PulsarDemo

This projects is a demo of Pulsar connectors.


## Functionality

After set up there will be two connectors running in the Pulsar instance.

One of them is a source connector which accepts HTTP POST requests and saves
the request payloads into topic entries.

The other one is a sink connector that is fed messages from the topic and
makes requests to a front-end web app to display those messages.

```
Request-->|Source|-->|Topic|-->|Sink|-->|Web|
```


## Set Up

- Deploy the Docker stack. This will run a web server on port 5000 and a
  standalone Pulsar instance.

    ```
    $ docker-compose up -d
    ```

- Deploy the Pulsar source connector. It will be running an HTTP server on port
  8000.

    ```
    $ docker-compose exec pulsar \
        bin/pulsar-admin sources create \
        --archive /connectors-dist/connectors-1.0.jar \
        --classname demo.connectors.HTTPSource \
        --name http_source \
        --tenant public \
        --namespace default  \
        --destination-topic-name demo
    ```

- Deploy the Pulsar sink connector.

    ```
    $ docker-compose exec pulsar \
        bin/pulsar-admin sinks create \
        --archive /connectors-dist/connectors-1.0.jar \
        --classname demo.connectors.HTTPSink \
        --name http_sink \
        --tenant public \
        --namespace default  \
        --inputs demo \
        --processing-guarantees EFFECTIVELY_ONCE
    ```


## Using the demo

- Use a browser to visit `http://localhost:5000/read-messages`. The page should
  auto-refresh to display new messages.

- Send messages to the source connector.

    ```
    $ curl -d 'Hello World!' -X POST http://localhost:8000/message
    $ curl -d 'Goodbye World!' -X POST http://localhost:8000/message
    ```

- Check the web page again. It should display the new messages.

- Stop the web app container to simulate site down-time.

    ```
    $ docker container stop pulsardemo_web_1
    ```


- Send a new message.

    ```
    $ curl -d 'Important message!!!' -X POST http://localhost:8000/message
    ```


- Start the web app container.

    ```
    $ docker container start pulsardemo_web_1
    ```


- Visit `http://localhost:5000/read-messages` again. The messages sent during
  down-time should be received and displayed after a little while.
