package ru.chrome.ext.toApp.io;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.chrome.ext.api.interfaces.IMessage;
import ru.chrome.ext.api.interfaces.ISupplyMessage;
import ru.chrome.ext.api.logger.IUseFileLogger;

import java.io.InputStream;
import java.io.InterruptedIOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class MessageReader implements ISupplyMessage, IUseFileLogger {

    private final InputStream in;

    @Contract(pure = true)
    private int getInt(byte @NotNull [] bytes) {
        return (bytes[3] << 24) & 0xff000000 | (bytes[2] << 16) & 0x00ff0000
                | (bytes[1] << 8) & 0x0000ff00 | (bytes[0] << 0) & 0x000000ff;
    }

    @Contract(" -> new")
    @SneakyThrows
    private @NotNull String read() {
        byte[] b = new byte[4];
        in.read(b);

        int size = getInt(b);

        if (size == 0) {
            throw new InterruptedIOException("Blocked communication");
        }

        b = new byte[size];
        in.read(b);

        return new String(b, StandardCharsets.UTF_8);
    }

    @SneakyThrows
    @Override
    public IMessage get() {
        return this::read;
    }
}
