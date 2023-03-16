package ru.chrome.ext.api.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import ru.chrome.ext.api.exceptions.LoggingException;
import ru.chrome.ext.api.interfaces.IMessage;

public class JsonMessageOut implements IMessage {

    @SneakyThrows
    @Override
    public String unwrap() {
        try {
            var mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Throwable any){
            throw new LoggingException(this.getClass().getCanonicalName(),any);
        }
    }
}
