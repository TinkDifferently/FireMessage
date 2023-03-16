package ru.chrome.ext.api.observes;

import lombok.RequiredArgsConstructor;
import ru.chrome.ext.api.logger.IUseFileLogger;

import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
class Observer implements rx.Observer<String>, IUseFileLogger {
    private final AtomicBoolean interrompe;

    @Override
    public void onCompleted() {
        log("App closed.");
        interrompe.set(true);
    }

    @Override
    public void onError(Throwable throwable) {
        log("Unexpected error!");
        interrompe.set(true);
    }

    @Override
    public void onNext(String s) {

    }
}
