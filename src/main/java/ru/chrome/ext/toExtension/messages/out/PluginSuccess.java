package ru.chrome.ext.toExtension.messages.out;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.chrome.ext.api.messages.JsonMessageOut;

public class PluginSuccess extends JsonMessageOut {
    @JsonProperty
    String plugin = "inist.ru/BicryptPlugin2";
    @JsonProperty
    String status = "success";
}
