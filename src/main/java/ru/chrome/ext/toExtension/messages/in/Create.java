package ru.chrome.ext.toExtension.messages.in;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Create {
    @JsonProperty
    String cmd;
    @JsonProperty
    String mimetype;
    @JsonProperty
    String ext;
}
