package com.andrejanesic.cads.homework1.cli.output.impl;

import com.andrejanesic.cads.homework1.cli.output.ICLOutput;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.SimpleDateFormat;
import java.util.Date;

@Singleton
public class CLOutput extends ICLOutput {


    @Inject
    public CLOutput() {
    }

    @Override
    public void info(String message) {
        log(MessageType.INFO, message);
    }

    @Override
    public void warning(String message) {
        log(MessageType.WARNING, message);
    }

    @Override
    public void error(String message) {
        log(MessageType.ERROR, message);
    }

    /**
     * Logs a message to the user.
     *
     * @param type    Message type.
     * @param message Message contents.
     */
    private void log(MessageType type, String message) {
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

    @Override
    public void afterStart() {

    }

    @Override
    public void main() {

    }

    @Override
    public void beforeEnd() {

    }
}
