package ru.chrome.ext.api.observes;

import lombok.NoArgsConstructor;
import ru.chrome.ext.api.logger.IUseFileLogger;
import ru.chrome.ext.toExtension.Resolvers;
import ru.chrome.ext.toExtension.io.MessageReader;
import rx.Observable;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

import java.io.InterruptedIOException;
import java.util.concurrent.atomic.AtomicBoolean;

@NoArgsConstructor
public class ObservableCreator implements IUseFileLogger {

    private ConnectableObservable<String> target;

    public ObservableCreator create(AtomicBoolean interrompe) {
        var reader = new MessageReader();
        target = Observable
                .create((Observable.OnSubscribe<String>) subscriber -> {
                    subscriber.onStart();
                    try {
                        while (true) {
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

    public ConnectableObservable<String> mount(Resolvers resolvers) {
        target.observeOn(Schedulers.computation()).subscribe(new rx.Observer<>() {
            public void onCompleted() {
            }

            public void onError(Throwable throwable) {
            }

            public void onNext(String s) {

                log("Received: " + s);

                if (resolvers.$1(s)
                        || resolvers.$2(s)
                        || resolvers.$3(s)
                        || resolvers.$4(s)
                        || resolvers.rejectRptView(s)
                        || resolvers.rejectInistFso2(s)
                        || resolvers.$5(s)
                        || resolvers.$6(s)
                        || resolvers.$7(s)
                        || resolvers.$8(s)
                        || resolvers.$9(s)
                        || resolvers.$10(s)
                        || resolvers.$11(s)
                        || resolvers.$12(s)
                        || resolvers.$13(s)
                        || resolvers.$14(s)
                        || resolvers.$15(s)
                        || resolvers.$16(s)) {
                    return;
                }
                var sl = "{\"success\":false," + "\"message\":\"" + "invalido" + "\"}";
                log("Returning: " + sl);
            }
        });
        return target;
    }
}
