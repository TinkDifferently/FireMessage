package ru.chrome.ext.toExtension;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.chrome.ext.api.interfaces.IMessage;
import ru.chrome.ext.api.logger.IUseFileLogger;
import ru.chrome.ext.api.messages.JsonMessageBuilder;
import ru.chrome.ext.toExtension.io.MessageWriter;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

@NoArgsConstructor
public class Resolvers implements IUseFileLogger {

    private final MessageWriter writer;

    {
        writer = new MessageWriter();
    }


    @SneakyThrows
    private boolean $(@NotNull String expected, String actual, String... responses) {
        if (expected.equals(actual)) {
            Arrays.stream(responses).<IMessage>map(response -> () -> response)
                    .forEach(writer);
            return true;
        }
        return false;
    }

    @SneakyThrows
    private boolean $$(String message, @NotNull Stream<Predicate<JsonNode>> predicates, IMessage... responses) {
        var mapper = new ObjectMapper();
        var tree = mapper.readTree(message);
        if (predicates.allMatch(predicate -> predicate.test(tree))) {
            Arrays.stream(responses)
                    .forEach(writer);
            return true;
        }
        return false;
    }

    @Contract(pure = true)
    private @NotNull Predicate<JsonNode> stringEquals(String key, String expected) {
        return tree -> {
            var node = tree.get(key);
//            if ((node != null && node.isTextual()) && !node.asText().equals(expected)){
//                log(node.asText());
//                log(expected);
//            }
            return (node != null && node.isTextual()) && node.asText().equals(expected);
        };
    }

    @Contract(pure = true)
    private @NotNull Predicate<JsonNode> intEquals(String key, int expected) {
        return tree -> {
            var node = tree.get(key);
            return node != null && node.asInt() == expected;
        };
    }

    @Contract(pure = true)
    private @NotNull Predicate<JsonNode> cmdIdEquals(int cmdId) {
        return intEquals(
                "cmdId", cmdId
        );
    }

    @Contract(pure = true)
    private @NotNull Predicate<JsonNode> typeEqualsResp() {
        return stringEquals(
                "type", "resp"
        );
    }

    @Contract(pure = true)
    private @NotNull Predicate<JsonNode> typeEqualsCmd() {
        return stringEquals(
                "type", "cmd"
        );
    }

    @RequiredArgsConstructor(staticName = "create")
    private static class Resolver {
        private final String message;
        private final Stream.Builder<Predicate<JsonNode>> validatorBuilder = Stream.builder();
        private final Stream.Builder<IMessage> messagesBuilder = Stream.builder();

        public Resolver validator(Predicate<JsonNode> predicate) {
            validatorBuilder.add(predicate);
            return this;
        }

        @SafeVarargs
        public final Resolver validators(Predicate<JsonNode>... predicate) {
            Arrays.stream(predicate).forEach(this::validator);
            return this;
        }


        public Resolver message(IMessage message) {
            messagesBuilder.add(message);
            return this;
        }

        public Resolver messages(IMessage... messages) {
            Arrays.stream(messages).forEach(this::message);
            return this;
        }

        @SneakyThrows
        boolean resolve(MessageWriter writer) {
            var mapper = new ObjectMapper();
            var tree = mapper.readTree(message);
            if (validatorBuilder.build().allMatch(predicate -> predicate.test(tree))) {
                messagesBuilder.build()
                        .forEach(writer);
                return true;
            }
            return false;
        }
    }

    @SneakyThrows
    public boolean $1(String message) {
        return Resolver.create(message)
                .validators(
                        stringEquals("cmd", "create"),
                        stringEquals("mimetype", "application/x-bicryptplugin2"),
                        stringEquals("ext", "polkfpcimpfhcflopocaobbbnphilcbi")
                )
                .message(() -> JsonMessageBuilder.create()
                        .stringProp("plugin", "inist.ru/BicryptPlugin2")
                        .stringProp("status", "success")
                        .build())
                .resolve(writer);
    }

    public boolean $2(String message) {
        return Resolver.create(message)
                .validators(
                        cmdIdEquals(1),
                        typeEqualsCmd(),
                        intEquals("c", 1),
                        stringEquals("msg", "[\"New\",\"application/x-bicryptplugin2\",{}]")
                )
                .message(
                        () -> JsonMessageBuilder.create()
                                .intProp("c", 1)
                                .intProp("cmdId", 1)
                                .intProp("colonyId", 0)
                                .stringProp("msg", "[\\\"New\\\",\\\"browser\\\",{}]")
                                .intProp("n", 1)
                                .stringProp("type", "cmd")
                                .build()
                )
                .resolve(writer);
    }

    public boolean $3(String message) {
        return Resolver.create(message)
                .validators(
                        cmdIdEquals(1),
                        typeEqualsResp(),
                        stringEquals("msg", "[\"success\",1]")
                )
                .message(
                        () -> JsonMessageBuilder.create()
                                .intProp("c", 1)
                                .intProp("cmdId", 1)
                                .intProp("colonyId", 0)
                                .stringProp("msg", "[\\\"success\\\",1]")
                                .intProp("n", 1)
                                .stringProp("type", "resp")
                                .build()
                )
                .message(
                        () -> JsonMessageBuilder.create()
                                .intProp("c", 1)
                                .intProp("cmdId", 2)
                                .intProp("colonyId", 0)
                                .stringProp("msg", "[\\\"Enum\\\",1,0]")
                                .intProp("n", 1)
                                .stringProp("type", "cmd")
                                .build()
                )
                .resolve(writer);
    }

    public boolean $4(String message) {
        return Resolver.create(message)
                .validators(
                        cmdIdEquals(2),
                        typeEqualsResp(),
                        stringEquals("msg", "[\"success\",[\"eval\",\"getDocument\",\"getWindow\",\"invokeWithDelay\",\"readArray\",\"readObject\"]]")
                )
                .resolve(writer);
    }

    public boolean $5(String message) {
        return Resolver.create(message)
                .validators(
                        cmdIdEquals(2),
                        typeEqualsCmd(),
                        stringEquals("msg", "[\"Enum\",1,0]")
                )
                .messages(
                        () -> JsonMessageBuilder.create()
                                .intProp("c", 1)
                                .intProp("cmdId", 3)
                                .intProp("colonyId", 0)
                                .stringProp("msg", "[\\\"Invoke\\\",1,0,\\\"getWindow\\\",[]]")
                                .intProp("n", 1)
                                .stringProp("type", "cmd")
                                .build(),
                        () -> JsonMessageBuilder.create()
                                .intProp("c", 1)
                                .intProp("cmdId", 4)
                                .intProp("colonyId", 0)
                                .stringProp("msg", "[\\\"Invoke\\\",1,0,\\\"getDocument\\\",[]]")
                                .intProp("n", 1)
                                .stringProp("type", "cmd")
                                .build(),
                        () -> JsonMessageBuilder.create()
                                .intProp("c", 1)
                                .intProp("cmdId", 2)
                                .intProp("colonyId", 0)
                                .stringProp("msg", "[\\\"success\\\",[\\\"AddKeyParam\\\",\\\"BankName\\\",\\\"CheckKey\\\",\\\"CheckVersion\\\",\\\"ClearKeyParams\\\",\\\"ClientCode\\\",\\\"ClientName\\\",\\\"Command\\\",\\\"CompressFiles\\\",\\\"CreatePacket\\\",\\\"CryptString\\\",\\\"DecompressFile\\\",\\\"DecryptPackage\\\",\\\"DecryptPackageBase64\\\",\\\"DecryptPackageBuf\\\",\\\"DocType\\\",\\\"FileSystemKeyStore\\\",\\\"FileSystemKeyStoreOnly\\\",\\\"ForceRegister\\\",\\\"GenSignKey\\\",\\\"GeneratedKeyInfo\\\",\\\"GetFileContentEx\\\",\\\"GetFileInfoByPath\\\",\\\"GetFileLines\\\",\\\"GetFileUTF8\\\",\\\"GetPluginProperties\\\",\\\"Gost2012\\\",\\\"InitToken\\\",\\\"KeyAkt\\\",\\\"KeyBody\\\",\\\"KeyInfo\\\",\\\"KeyParam\\\",\\\"Language\\\",\\\"MaxTrash\\\",\\\"MaxTrashPause\\\",\\\"MinTrash\\\",\\\"MinTrashPause\\\",\\\"NGOTPMode\\\",\\\"NGOTPTimeout\\\",\\\"Packet\\\",\\\"PacketCount\\\",\\\"PrintKeyAkt\\\",\\\"ProfileName\\\",\\\"RememberPacket\\\",\\\"RemoveKeyParam\\\",\\\"SaveFileUTF8\\\",\\\"SaveToFile\\\",\\\"SaveToFileFirst\\\",\\\"SaveToFileLast\\\",\\\"SaveToFileNext\\\",\\\"SaveToFileUTF16\\\",\\\"SelectMultiFiles\\\",\\\"Session\\\",\\\"SetCommand\\\",\\\"SetPin\\\",\\\"SetPluginProperties\\\",\\\"SetSession\\\",\\\"UseNGOTP\\\",\\\"UserName\\\",\\\"VirtualKeyboardOnly\\\",\\\"createPacketFromFile2\\\",\\\"getAttribute\\\",\\\"getFileInfo\\\",\\\"getPacket\\\",\\\"selectCatalog\\\",\\\"selectCatalogEx\\\",\\\"selectFiles\\\",\\\"selectFilesEx\\\",\\\"setAttribute\\\",\\\"set_SOPass\\\",\\\"set_UserDefaultPin\\\",\\\"toString\\\",\\\"valid\\\",\\\"value\\\",\\\"version\\\"]]")
                                .intProp("n", 1)
                                .stringProp("type", "resp")
                                .build()

                )
                .resolve(writer);
    }

    public boolean $6(String message) {
        return Resolver.create(message)
                .validators(
                        cmdIdEquals(3),
                        typeEqualsCmd(),
                        stringEquals("msg", "[\"GetP\",1,0,\"version\"]")
                )
                .message(
                        () -> JsonMessageBuilder.create()
                                .intProp("c", 1)
                                .intProp("cmdId", 3)
                                .intProp("colonyId", 0)
                                .stringProp("msg", "[\\\"success\\\",\\\"1.0.0.32\\\"]")
                                .intProp("n", 1)
                                .stringProp("type", "resp")
                                .build()
                )
                .resolve(writer);
    }

    public boolean $7(String message) {
        return Resolver.create(message)
                .validators(
                        cmdIdEquals(3),
                        typeEqualsResp(),
                        stringEquals("msg", "[\"success\",{\"$type\":\"ref\",\"data\":[1,1]}]")
                )
                .message(
                        () -> JsonMessageBuilder.create()
                                .intProp("c", 1)
                                .intProp("cmdId", 5)
                                .intProp("colonyId", 0)
                                .stringProp("msg", "[\\\"Enum\\\",1,1]")
                                .intProp("n", 1)
                                .stringProp("type", "cmd")
                                .build()
                )
                .resolve(writer);
    }

    public boolean $8(String message) {
        return Resolver.create(message)
                .validators(
                        cmdIdEquals(4),
                        typeEqualsResp(),
                        stringEquals("msg", "[\"success\",{\"$type\":\"ref\",\"data\":[1,2]}]")
                )
                .message(
                        () -> JsonMessageBuilder.create()
                                .intProp("c", 1)
                                .intProp("cmdId", 6)
                                .intProp("colonyId", 0)
                                .stringProp("msg", "[\\\"Enum\\\",1,2]")
                                .intProp("n", 1)
                                .stringProp("type", "cmd")
                                .build()
                )
                .resolve(writer);
    }

    public boolean $9(String message) {
        return Resolver.create(message)
                .validators(
                        cmdIdEquals(5),
                        typeEqualsResp(),
                        node -> {
                            var msg = node.get("msg");
                            return msg != null && msg.isTextual() && msg.asText().startsWith("[\"success\",[\"0\",\"1\",\"window\",\"self\",\"document\",\"name\",\"location\",\"customElements\",\"history\",\"navigation\"");
                        }
                )
                .resolve(writer);
    }

    public boolean $10(String message) {
        return Resolver.create(message)
                .validators(
                        cmdIdEquals(6),
                        typeEqualsResp(),
                        node -> {
                            var msg = node.get("msg");
                            return msg != null && msg.isTextual() && msg.asText().startsWith("[\"success\",[\"location\",\"");
                        }
                )
                .resolve(writer);
    }

    public boolean $11(String message) {
        return Resolver.create(message)
                .validators(
                        cmdIdEquals(4),
                        typeEqualsCmd(),
                        stringEquals("msg", "[\"GetP\",1,0,\"SetPluginProperties\"]")
                )
                .message(
                        () -> JsonMessageBuilder.create()
                                .intProp("c", 1)
                                .intProp("cmdId", 4)
                                .intProp("colonyId", 0)
                                .stringProp("msg", "[\\\"success\\\",{\\\"$type\\\":\\\"ref\\\",\\\"data\\\":[0,1]}]")
                                .intProp("n", 1)
                                .stringProp("type", "resp")
                                .build()
                )
                .resolve(writer);
    }

    public boolean $12(String message) {
        return Resolver.create(message)
                .validators(
                        cmdIdEquals(5),
                        typeEqualsCmd(),
                        stringEquals("msg", "[\"Enum\",0,1]")
                )
                .message(
                        () -> JsonMessageBuilder.create()
                                .intProp("c", 1)
                                .intProp("cmdId", 5)
                                .intProp("colonyId", 0)
                                .stringProp("msg", "[\\\"success\\\",[]]")
                                .intProp("n", 1)
                                .stringProp("type", "resp")
                                .build()
                )
                .resolve(writer);
    }

    public boolean $13(String message) {
        return Resolver.create(message)
                .validators(
                        cmdIdEquals(6),
                        typeEqualsCmd(),
                        stringEquals("msg", "[\"Invoke\",0,1,\"\",[{\"$type\":\"ref\",\"data\":[0,1]}]]")
                )
                .messages(
                        () -> JsonMessageBuilder.create()
                                .intProp("c", 1)
                                .intProp("cmdId", 7)
                                .intProp("colonyId", 0)
                                .stringProp("msg", "[\\\"Enum\\\",0,1]")
                                .intProp("n", 1)
                                .stringProp("type", "cmd")
                                .build(),
                        () -> JsonMessageBuilder.create()
                                .intProp("c", 1)
                                .intProp("cmdId", 8)
                                .intProp("colonyId", 0)
                                .stringProp("msg", "[\\\"Invoke\\\",1,0,\\\"readObject\\\",[{\\\"$type\\\":\\\"local-ref\\\",\\\"data\\\":[0,1]}]]")
                                .intProp("n", 1)
                                .stringProp("type", "cmd")
                                .build()
                )
                .resolve(writer);
    }

    public boolean $14(String message) {
        return Resolver.create(message)
                .validators(
                        cmdIdEquals(7),
                        typeEqualsResp(),
                        stringEquals("msg", "[\"success\",[\"DocType\",\"BankName\",\"ProfileName\",\"Language\",\"RegistryKeyStore\",\"RememberPacket\",\"Packet\",\"VirtualKeyboardOnly\"]]")
                )
                .resolve(writer);
    }

    public boolean $15(String message) {
        return Resolver.create(message)
                .validators(
                        cmdIdEquals(8),
                        typeEqualsResp(),
                        node -> {
                            var msg = node.get("msg");
                            return msg != null && msg.isTextual() && msg.asText().startsWith("[\"success\",{\"DocType\":\"\",\"BankName\":\"\",\"ProfileName\":\"Profile_Name\",\"Language\":0,\"RegistryKeyStore\":false,\"RememberPacket\":true,\"Packet\":");
                        }
                )
                .message(
                        () -> JsonMessageBuilder.create()
                                .intProp("c", 1)
                                .intProp("cmdId", 6)
                                .intProp("colonyId", 0)
                                .stringProp("msg", "[\\\"success\\\",true]")
                                .intProp("n", 1)
                                .stringProp("type", "resp")
                                .build()
                )
                .resolve(writer);
    }

    public boolean $16(String message) {
        return Resolver.create(message)
                .validators(
                        cmdIdEquals(7),
                        typeEqualsCmd(),
                        stringEquals("msg", "[\"RelObj\",0,1]")
                )
                .message(
                        () -> JsonMessageBuilder.create()
                                .intProp("c", 1)
                                .intProp("cmdId", 7)
                                .intProp("colonyId", 0)
                                .stringProp("msg", "[\\\"success\\\",null]")
                                .intProp("n", 1)
                                .stringProp("type", "resp")
                                .build()
                )
                .resolve(writer);
    }


//    public boolean $5(String message) {
//        String expected = "{\"cmdId\":2,\"type\":\"resp\",\"c\":1,\"n\":1,\"colonyId\":0,\"msg\":\"[\\\"success\\\",[\\\"eval\\\",\\\"getDocument\\\",\\\"getWindow\\\",\\\"invokeWithDelay\\\",\\\"readArray\\\",\\\"readObject\\\"]]\",\"ext\":\"polkfpcimpfhcflopocaobbbnphilcbi\"}";
//        String response1 = "{\"c\":1,\"cmdId\":3,\"colonyId\":0,\"msg\":\"[\\\"Invoke\\\",1,0,\\\"getWindow\\\",[]]\",\"n\":1,\"type\":\"cmd\"}";
//        String response2 = "{\"c\":1,\"cmdId\":4,\"colonyId\":0,\"msg\":\"[\\\"Invoke\\\",1,0,\\\"getDocument\\\",[]]\",\"n\":1,\"type\":\"cmd\"}";
//        return $(expected, message, response1, response2);
//    }
//
//    public boolean $6(String message) {
//        String expected = "{\"cmdId\":3,\"type\":\"cmd\",\"c\":1,\"n\":1,\"colonyId\":0,\"msg\":\"[\\\"GetP\\\",1,0,\\\"version\\\"]\",\"ext\":\"polkfpcimpfhcflopocaobbbnphilcbi\"}";
//        String response = "{\"c\":1,\"cmdId\":3,\"colonyId\":0,\"msg\":\"[\\\"success\\\",\\\"1.0.0.32\\\"]\",\"n\":1,\"type\":\"resp\"}";
//        return $(expected, message, response);
//    }
//
//    public boolean $7(String message) {
//        var expected = "{\"cmdId\":3,\"type\":\"resp\",\"c\":1,\"n\":1,\"colonyId\":0,\"msg\":\"[\\\"success\\\",{\\\"$type\\\":\\\"ref\\\",\\\"data\\\":[1,1]}]\",\"ext\":\"polkfpcimpfhcflopocaobbbnphilcbi\"}";
//        var response = "{\"c\":1,\"cmdId\":5,\"colonyId\":0,\"msg\":\"[\\\"Enum\\\",1,1]\",\"n\":1,\"type\":\"cmd\"}";
//        return $(expected, message, response);
//    }
//
//    public boolean $8(String message) {
//        var expected = "{\"cmdId\":4,\"type\":\"resp\",\"c\":1,\"n\":1,\"colonyId\":0,\"msg\":\"[\\\"success\\\",{\\\"$type\\\":\\\"ref\\\",\\\"data\\\":[1,2]}]\",\"ext\":\"polkfpcimpfhcflopocaobbbnphilcbi\"}";
//        var response = "{\"c\":1,\"cmdId\":6,\"colonyId\":0,\"msg\":\"[\\\"Enum\\\",1,2]\",\"n\":1,\"type\":\"cmd\"}";
//        return $(expected, message, response);
//    }


    public boolean rejectRptView(String message) {
        String rejected = "{\"cmd\":\"create\",\"mimetype\":\"application/x-rprtview2\",\"ext\":\"polkfpcimpfhcflopocaobbbnphilcbi\"}";
        String response = "{\"message\":\"No registered plugins detected\",\"status\":\"error\"}";
        return $(rejected, message, response);
    }

    public boolean rejectInistFso2(String message) {
        String rejected = "{\"cmd\":\"create\",\"mimetype\":\"application/x-inistfso2\",\"ext\":\"polkfpcimpfhcflopocaobbbnphilcbi\"}";
        String response = "{\"message\":\"No registered plugins detected\",\"status\":\"error\"}";
        return $(rejected, message, response);
    }


}
