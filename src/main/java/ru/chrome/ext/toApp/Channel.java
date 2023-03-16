package ru.chrome.ext.toApp;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.chrome.ext.toApp.io.MessageWriter;
import ru.chrome.ext.toApp.observes.ObservableCreator;

import java.io.PrintStream;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
@RequiredArgsConstructor
public class Channel implements Runnable{

    private final String path;
    private final String extension;

    private final String initialMessage;

    private final Path sourcePath;

    @SneakyThrows
    @Override
    public void run() {
        var interrompe = new AtomicBoolean(false);
        var process = new ProcessBuilder().command(path, "chrome-extension://"+extension+"/ --parent-window=0")
                .start();
        var inputStream = process.getInputStream();
        var outputStream = process.getOutputStream();
        var stream = new PrintStream(outputStream);
        var writer = new MessageWriter(stream);
        var connector = new ObservableCreator()
                .create(inputStream, interrompe)
                .mount(writer, MessageParser.getInstance().parseMessages(sourcePath));
        connector.connect();
        var message = initialMessage;
        writer.accept(() -> message);
        while (!interrompe.get()) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
