package ru.chrome.ext.toApp.observes;

import lombok.RequiredArgsConstructor;
import ru.chrome.ext.api.logger.IUseFileLogger;
import ru.chrome.ext.api.session.SessionData;
import ru.chrome.ext.toApp.io.MessageWriter;
import ru.chrome.ext.toApp.types.Messages;

import java.util.concurrent.atomic.AtomicBoolean;
@RequiredArgsConstructor
public class NextObserver implements rx.Observer<String>, IUseFileLogger {

    private final AtomicBoolean interruptMe;
    private final Messages messages;
    private final MessageWriter writer;
    public void onCompleted() {
    }

    public void onError(Throwable throwable) {
    }

    public void onNext(String s) {
        log("Received: " + s);
        var outMessages = messages.remove(s);
        if (outMessages == null) {
            var group = messages.entrySet().stream().filter(entry -> entry.getKey()
                    .matches("[a-zA-Z\\-]+")).findFirst();
            if (group.isEmpty()) {
                log("ERROR QUIT");
                throw new RuntimeException("Unknown message");
            }
            SessionData.getInstance().$(group.get().getKey(), s);
            interruptMe.set(true);
            log(group.get().getKey() + ":" + s);
            outMessages = messages.remove(group.get().getKey());
        }
        outMessages.forEach(writer);
        if (messages.isEmpty()) {
            interruptMe.set(true);
            throw new RuntimeException("Unknown message");
        }
    }
}
