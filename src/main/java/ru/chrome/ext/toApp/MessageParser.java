package ru.chrome.ext.toApp;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import ru.chrome.ext.api.interfaces.IMessage;
import ru.chrome.ext.api.session.SessionData;
import ru.chrome.ext.toApp.types.Messages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageParser {
    @RequiredArgsConstructor
    private enum MessageType {
        IN(true),
        OUT(false),
        FROM_CACHE(false),
        TO_CACHE(true);

        private final boolean in;
    }

    private final static class Message {
        private final MessageType messageType;
        @Getter
        private final String message;

        private Message(@NotNull String message) {
            if (message.startsWith("[in]")) {
                messageType = MessageType.IN;
                this.message = message.substring(4);
                return;
            }
            if (message.matches("\\[in:[^]]+]")) {
                messageType = MessageType.TO_CACHE;
                this.message = message.substring(4, message.length() - 1);
                return;
            }
            if (message.matches("\\[[a-zA-Z\\-]+]")) {
                messageType = MessageType.FROM_CACHE;
                this.message = message.substring(1, message.length() - 1);
                return;
            }
            messageType = MessageType.OUT;
            this.message = message;
        }
    }

    @Getter
    private final static MessageParser instance = new MessageParser();

    public Messages parseMessages(Path path) {
        try {
            var parsed = Files.readAllLines(path).stream()
                    .filter(line -> !line.isBlank())
                    .map(Message::new)
                    .collect(Collectors.toList());
            var groups = new Messages();
            var outs = new ArrayList<IMessage>();
            var in = "";
            for (var message : parsed) {
                if (message.messageType.in) {
                    if (in.isBlank()) {
                        in = message.message;
                        continue;
                    }
                    groups.put(in, outs);
                    in = message.message;
                    outs = new ArrayList<>();
                    continue;
                }
                IMessage iMessage=message.messageType == MessageType.OUT
                        ? message::getMessage
                        : ()-> SessionData.getInstance().$(message.getMessage());
                outs.add(iMessage);
            }
            if (!in.isBlank()) {
                groups.put(in, outs);
            }
            return groups;
        } catch (IOException e) {
            throw new RuntimeException(String.format("Не удалось прочесть сообщения по пути '%s'", path.toAbsolutePath()), e);
        }
    }
}
