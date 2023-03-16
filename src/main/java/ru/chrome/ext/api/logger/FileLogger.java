package ru.chrome.ext.api.logger;

import ru.chrome.ext.api.config.Config;
import ru.chrome.ext.api.interfaces.ILogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLogger implements ILogger {
    private final Path path;

    public FileLogger() {
        path = Path.of(Config.getLogFile());
        createIfNotExists();
    }

    void createIfNotExists() {
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            //System.out.println("Unresolvable panic");
        }
    }

    @Override
    public void log(String text) {
        try {

            Files.writeString(path,LocalDateTime.now().format(DateTimeFormatter.ofPattern("[MM.dd-hh:mm:ss] "))+text+"\n", StandardOpenOption.APPEND);
        } catch (IOException e) {
            //System.out.println("Unresolvable panic");
        }
    }


}
