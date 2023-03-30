package com.andrejanesic.cads.homework1.cli.commons;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Writes output to the user.
 */
public class OutputWriter {

    @Inject
    public OutputWriter() {
    }

    /**
     * Logs a message to the user.
     *
     * @param type    Message type.
     * @param message Message contents.
     */
    public void log(MessageType type, String message) {
        String prefix = "";
        if (!type.equals(MessageType.INFO)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date now = new Date();
            prefix = "[" + sdf.format(now) + "]" +
                    (type.equals(MessageType.WARNING) ? "[Warning]" : "[ERROR]");
        }

        System.out.println(prefix + message);
    }

    public enum MessageType {
        INFO,
        WARNING,
        ERROR,
    }
}
