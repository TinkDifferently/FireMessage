package ru.chrome.ext.toApp.observes;

import lombok.NoArgsConstructor;
import ru.chrome.ext.api.logger.IUseFileLogger;
import ru.chrome.ext.api.session.SessionData;
import ru.chrome.ext.toApp.io.MessageReader;
import ru.chrome.ext.toApp.io.MessageWriter;
import ru.chrome.ext.toApp.types.Messages;
import rx.Observable;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.concurrent.atomic.AtomicBoolean;

@NoArgsConstructor
public class ObservableCreator implements IUseFileLogger {

    private ConnectableObservable<String> target;
    private AtomicBoolean interrompe;

    public ObservableCreator create(InputStream stream, AtomicBoolean interrompe) {
        this.interrompe = interrompe;
        var reader = new MessageReader(stream);
        target = Observable
                .create((Observable.OnSubscribe<String>) subscriber -> {
                    subscriber.onStart();
                    try {
                        while (!interrompe.get()) {
                            String _s = reader.get().unwrap();
                            subscriber.onNext(_s);
                        }
                    } catch (Exception e) {
                        if (e.getClass() != InterruptedIOException.class) {
                            subscriber.onError(e);
                        } else {
                            log("Blocked communication");
                        }
                    }
                    subscriber.onCompleted();
                }).subscribeOn(Schedulers.io()).publish();

        target.subscribe(new Observer(interrompe));
        return this;
    }

    public ConnectableObservable<String> mount(MessageWriter writer, Messages messages, AtomicBoolean call) {
        target.observeOn(Schedulers.computation()).subscribe(new NextObserver(interrompe,messages,writer));
        return target;
    }
}
