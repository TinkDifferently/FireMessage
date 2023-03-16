package ru.chrome.ext.toExtension.io;

import lombok.SneakyThrows;
import ru.chrome.ext.api.interfaces.IConsumeMessage;
import ru.chrome.ext.api.interfaces.IMessage;
import ru.chrome.ext.api.logger.IUseFileLogger;

import java.nio.charset.StandardCharsets;

public class MessageWriter implements IConsumeMessage, IUseFileLogger {

    @SneakyThrows
    @Override
    public void accept(IMessage iMessage) {
        var message = iMessage.unwrap();
        System.out.write(getBytes(message.length()));
        System.out.write(message.getBytes(StandardCharsets.UTF_8));
        System.out.flush();
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
