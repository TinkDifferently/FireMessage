package ru.chrome.ext.api.logger;

import ru.chrome.ext.api.interfaces.IUseLogger;
import ru.chrome.ext.api.interfaces.ILogger;

public interface IUseFileLogger extends IUseLogger, ILogger {
    FileLogger logger = new FileLogger();
    @Override
    default ILogger getLogger() {
        return logger;
    }
}
