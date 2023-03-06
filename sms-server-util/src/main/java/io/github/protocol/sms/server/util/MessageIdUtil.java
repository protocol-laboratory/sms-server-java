package io.github.protocol.sms.server.util;

import java.util.UUID;

public class MessageIdUtil {

    public static String generateMessageId() {
        return UUID.randomUUID().toString();
    }

}
