package ru.chrome.ext.toExtension.messages;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.chrome.ext.toExtension.messages.out.PluginSuccess;

class PluginSuccessTest {
    @Test
    public void test(){
        Assertions.assertEquals("{\"plugin\":\"inist.ru/BicryptPlugin2\",\"status\":\"success\"}",new PluginSuccess().unwrap());
    }
}