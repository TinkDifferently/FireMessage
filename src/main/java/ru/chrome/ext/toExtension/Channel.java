package ru.chrome.ext.toExtension;

import ru.chrome.ext.api.logger.IUseFileLogger;
import ru.chrome.ext.api.observes.ObservableCreator;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


public class Channel implements IUseFileLogger, Runnable {
    private final AtomicBoolean interrompe;

    {
        this.interrompe = new AtomicBoolean(false);
    }


    public void run() {


        final var app = new Channel();

        app.log("Starting the app...");

        var resolvers = new Resolvers();

        var obs = new ObservableCreator().create(app.interrompe)
                .mount(resolvers);
        obs.connect();

        while (!app.interrompe.get()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                break;
            }
        }

        System.exit(0);
    }


}