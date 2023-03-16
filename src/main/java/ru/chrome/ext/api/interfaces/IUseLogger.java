package ru.chrome.ext.api.interfaces;

public interface IUseLogger extends ILogger{
    ILogger getLogger();

    default void log(String text){
        getLogger().log(text);
    }
}
