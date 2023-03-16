package ru.chrome.ext.api.messages;

import lombok.NoArgsConstructor;
import ru.chrome.ext.api.interfaces.IMessage;

@NoArgsConstructor(staticName = "create")
public class JsonMessageBuilder {
    private final StringBuilder builder = new StringBuilder();

    private boolean comma = false;

    private void comma() {
        if (!comma) {
            comma = true;
            builder.append("{");
            return;
        }
        builder.append(",");
    }

    private JsonMessageBuilder property(String key, String value) {
        comma();
        builder.append('"')
                .append(key)
                .append('"')
                .append(":")
                .append(value);
        return this;
    }

    public JsonMessageBuilder boolProp(String key, boolean value) {
        return property(key, String.valueOf(value));
    }

    public JsonMessageBuilder intProp(String key, int value) {
        return property(key, String.valueOf(value));
    }

    public JsonMessageBuilder stringProp(String key, String value) {
        return property(key, String.format("\"%s\"", value));
    }

    public JsonMessageBuilder openObjProp(String key) {
        property(key, "{");
        comma = false;
        return this;
    }

    public JsonMessageBuilder closeObj() {
        builder.append("}");
        comma = true;
        return this;
    }

    public JsonMessageBuilder openArrProp(String key) {
        property(key, "[");
        comma = false;
        return this;
    }

    public JsonMessageBuilder closeArr() {
        builder.append("]");
        comma = true;
        return this;
    }

    public JsonMessageBuilder stringValue(String value) {
        comma();
        builder.append('"').append(value).append('"');
        return this;
    }

    public JsonMessageBuilder intValue(int value) {
        comma();
        builder.append(value);
        return this;
    }

    public JsonMessageBuilder boolValue(int value) {
        comma();
        builder.append(value);
        return this;
    }

    public JsonMessageBuilder objValue() {
        comma();
        builder.append("{");
        return this;
    }

    public JsonMessageBuilder arrValue() {
        comma();
        builder.append("[");
        return this;
    }

    public String build() {
        if (builder.length() == 0) {
            return "{}";
        }
        return builder.append("}").toString();
    }


}
