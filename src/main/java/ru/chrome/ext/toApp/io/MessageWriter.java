package ru.chrome.ext.toApp.io;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.chrome.ext.api.Session;
import ru.chrome.ext.api.interfaces.IConsumeMessage;
import ru.chrome.ext.api.interfaces.IMessage;
import ru.chrome.ext.api.logger.IUseFileLogger;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class MessageWriter implements IConsumeMessage, IUseFileLogger {

    private final PrintStream out;

    private final Session session;

    private String updateCmd(String msg) {
        var cmdIdMatcher = Pattern.compile("\"cmdId\":\\d+")
                .matcher(msg);
        if (cmdIdMatcher.find()) {
            var typeMatcher = Pattern.compile("\"type\":\"([^\"]+)\"")
                    .matcher(msg);
            if (typeMatcher.find()) {
                switch (typeMatcher.group(1)) {
                    case "resp":
                        return cmdIdMatcher.replaceFirst("\"cmdId\":" + session.getToAppResp());
                    case "cmd":
                        return cmdIdMatcher.replaceFirst("\"cmdId\":" + session.getToAppCmd());
                    default:
                }
            }
        }
        return msg;
    }

    @SneakyThrows
    @Override
    public void accept(IMessage iMessage) {
        var message = iMessage.unwrap();
        message = updateCmd(message);
        var bytes = message.getBytes(StandardCharsets.UTF_8);
        out.write(getBytes(bytes.length));
        out.write(bytes);
        out.flush();
        log("Sent: " + message);
    }

    public byte[] getBytes(int length) {
        var bytes = new byte[4];
        bytes[0] = (byte) (length & 0xFF);
        bytes[1] = (byte) ((length >> 8) & 0xFF);
        bytes[2] = (byte) ((length >> 16) & 0xFF);
        bytes[3] = (byte) ((length >> 24) & 0xFF);
        return bytes;
    }
}
