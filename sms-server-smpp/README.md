# sms-server-smpp

This module provides a simple, easy-to-use SMPP (Short Message Peer-to-Peer) server implementation. It supports custom
message processors for handling submit_sm PDUs, and includes a built-in mock processor for testing purposes.

## Features

- Customizable SMPP server configuration
- Support for SSL/TLS encryption
- Custom submit_sm processors
- Built-in mock processor for testing

## Getting Started

First, add the module as a dependency to your project.

## Using SmppSubmitMockProcessor

`SmppSubmitMockProcessor` is a built-in processor that simulates the delivery of messages without actually sending them.
It's useful for testing and debugging your application. This processor can be configured to delay the delivery reports
and customize the delivery status.

To use `SmppSubmitMockProcessor`, follow these steps:

1. Create a new SmppConfig instance.
    ```java
    SmppConfig config=new SmppConfig()
            .host("0.0.0.0")
            .port(2775)
            .acceptorThreadsNum(2)
            .ioThreadsNum(4);
    ```

2. Create a new `SmppSubmitMockProcessor` instance with the desired delay and delivery status.
    ```java
    long mockDelayInMill = 1000; // Delay in milliseconds
    String mockReportStatus = "DELIVRD"; // Delivery report status
    
    SmppSubmitMockProcessor mockProcessor = new SmppSubmitMockProcessor(mockDelayInMill, mockReportStatus);
    ```

3. Set the `SmppSubmitMockProcessor` as the `submitSmProcessor` for the `SmppConfig`
    ```java
    config.submitSmProcessor(mockProcessor);
    ```

4. Create a new `SmppServer` instance with the `SmppConfig` and start the server.
    ```java
    SmppServer server = new SmppServer(config);
    server.start();
    ```

   Now, the SMPP server will use the `SmppSubmitMockProcessor` to handle submit_sm PDUs. The processor will generate
   delivery reports with the configured delay and status.

   Remember to close the `SmppSubmitMockProcessor` when you're done using it:
    ```java
    mockProcessor.close();
    ```

## Using Custom SubmitSmProcessor

If you need custom behavior for handling submit_sm PDUs, you can implement the `SmppSubmitSmProcessor` interface and
override the `process` method. Then, set your custom processor as the `submitSmProcessor` in the `SmppConfig`.

For example:

```java
public class CustomSmppSubmitProcessor implements SmppSubmitSmProcessor {
    // Implement custom logic
}

    CustomSmppSubmitProcessor customProcessor = new CustomSmppSubmitProcessor();
    config.submitSmProcessor(customProcessor);
```

Make sure to handle resource cleanup in your custom processors if necessary.
