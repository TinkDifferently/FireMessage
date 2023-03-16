package ru.chrome.ext.api.app;

import ru.chrome.ext.api.session.SessionData;
import ru.chrome.ext.toApp.Channel;

import java.nio.file.Path;

public class ToApp {
    public static void main(String[] args) {
        SessionData.getInstance().$("session", "{\"cmdId\":12,\"type\":\"resp\",\"c\":1,\"n\":1,\"colonyId\":0,\"msg\":\"[\\\"success\\\",{\\\"Session\\\":\\\"1544626471148645247\\\",\\\"Command\\\":1,\\\"RememberPacket\\\":false}]\",\"ext\":\"polkfpcimpfhcflopocaobbbnphilcbi\"}");
        var channel = new Channel(
                "C:\\Users\\tinkd\\AppData\\Roaming\\Inist\\FireWyrmNativeMessageHost.proxy.exe",
                "polkfpcimpfhcflopocaobbbnphilcbi",
                "{\"cmd\":\"create\",\"mimetype\":\"application/x-bicryptplugin2\",\"ext\":\"polkfpcimpfhcflopocaobbbnphilcbi\"}",
                Path.of("sign.txt"));
        channel.run();
        System.out.println((String) SessionData.getInstance().$("login"));
    }
}
