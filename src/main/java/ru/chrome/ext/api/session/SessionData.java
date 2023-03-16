package ru.chrome.ext.api.session;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionData {
    @Getter
    private static final SessionData instance = new SessionData();

    private final Map<String, Object> map = new ConcurrentHashMap<>();

    public void $(String key, Object value) {
        map.put(key, value);
    }

    public <T> T $(String key) {
        if (map.containsKey(key)) {
            return (T) map.get(key);
        }
        throw new RuntimeException(String.format("No such data '%s'", key));
    }
}
