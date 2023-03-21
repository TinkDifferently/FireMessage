package ru.chrome.ext.toApp;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.chrome.ext.toApp.io.MessageWriter;
import ru.chrome.ext.toApp.observes.ObservableCreator;
import ru.chrome.ext.toApp.types.Messages;

import java.io.PrintStream;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class FireServer implements Runnable {
    private final String processName;
    private final String extensionId;

    private final AtomicBoolean interruptMe = new AtomicBoolean();
    private AtomicReference<Process> process = new AtomicReference<>();

    private final Messages messages=new Messages();

    private Process getProcess() {
        if (process.get() == null) {
            throw new IllegalStateException();
        }
        return process.get();
    }


    @SneakyThrows
    @Override
    public void run() {
        var process = new ProcessBuilder().command(processName, "chrome-extension://" + extensionId + "/ --parent-window=0")
                .start();
        this.process.set(process);
    }

    public void mountObserver(String initialMessage, Path sourcePath) {
        interruptMe.set(false);
        var inputStream = getProcess().getInputStream();
        var outputStream = getProcess().getOutputStream();
        var stream = new PrintStream(outputStream);
        var writer = new MessageWriter(stream);
        var messages=MessageParser.getInstance().parseMessages(sourcePath);
        this.messages.putAll(messages);
        AtomicBoolean call=new AtomicBoolean(true);
        var connector = new ObservableCreator()
                .create(inputStream, interruptMe)
                .mount(writer,this.messages,call);
        connector.connect();
        writer.accept(() -> initialMessage);
        while (!interruptMe.get()) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
        call.set(false);
    }

    public void refreshObserver(String initialMessage, Path sourcePath){
        interruptMe.set(false);
        var messages=MessageParser.getInstance().parseMessages(sourcePath);
        this.messages.putAll(messages);
        var outputStream = getProcess().getOutputStream();
        var stream = new PrintStream(outputStream);
        var writer = new MessageWriter(stream);
        writer.accept(() -> initialMessage);
        while (!interruptMe.get()) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
