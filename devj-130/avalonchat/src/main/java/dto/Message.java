package dto;

import enums.Command;
import enums.MessageType;

import java.io.Serializable;

public class Message implements Serializable {
    private final String nickName;
    private final MessageType type;
    private final Command command;
    private final String payLoad;

    public Message(String nickName, String text) {
        if (nickName == null || nickName.isEmpty()){
            throw new IllegalArgumentException("Nickname must be non-null & non-empty string");
        }
        if (text == null){
            throw new IllegalArgumentException("Text must be non-null & non-empty string");
        }
        this.nickName = nickName;
        this.type = MessageType.TEXT;
        this.command = Command.SEND_TEXT;
        this.payLoad = text;
    }

    public Message(String nickName, Command cmd){
        if (nickName == null || nickName.isEmpty()){
            throw new IllegalArgumentException("Nickname must be non-null & non-empty string");
        }
        if (cmd == null){
            throw new IllegalArgumentException("Command object reference must be non-null");
        }
        this.nickName = nickName;
        this.type = MessageType.COMMAND;
        this.command = cmd;
        this.payLoad = "#"+cmd;
    }

    public String getNickName() {
        return nickName;
    }

    public MessageType getType() {
        return type;
    }

    public Command getCommand() {
        return command;
    }

    public String getPayLoad() {
        return payLoad;
    }
}
