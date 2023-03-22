package ru.chrome.ext.api;

import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC, staticName = "create")
public class Session {
    @Getter
    private final String sessionId;

    private int toAppCmd = 1;
    private int toAppResp = 1;
    private int fromAppCmd = 0;
    private int fromAppResp = 0;


    public int getToAppCmd() {
        return toAppCmd++;
    }

    public int getToAppResp() {
        return toAppResp++;
    }

    public int getFromAppCmd() {
        return fromAppCmd++;
    }

    public int getFromAppResp() {
        return fromAppResp++;
    }

}
