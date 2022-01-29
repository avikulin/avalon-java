package controller;

import contracts.ISimpleChat;
import dto.Message;
import enums.ChatMode;
import enums.Command;
import exceptions.ChatException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class ChatController implements ISimpleChat {
    private ChatMode mode;
    private ServerSocket serverSocket;
    private Socket socket;
    private final String hostName;
    private final String nickName;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private final BlockingDeque<String> outputQueue;
    private final BlockingDeque<String> inputQueue;
    private Thread sender;
    private Thread receiver;
    private boolean isReady;

    public ChatController(String hostName, String nickName) {
        if (hostName == null || hostName.isEmpty()){
            throw new IllegalArgumentException("Hostname must be non-null & non-empty string");
        }

        if (nickName == null || nickName.isEmpty()){
            throw new IllegalArgumentException("Nickname must be non-null & non-empty string");
        }
        this.hostName = hostName;
        this.nickName = nickName;
        this.inputQueue = new LinkedBlockingDeque<>();
        this.outputQueue = new LinkedBlockingDeque<>();
        this.isReady = false;
        this.mode = null;
    }

    @Override
    public void client() throws ChatException {
        try{
            this.socket = new Socket(hostName, SERVER_PORT);
            this.writer = new ObjectOutputStream(socket.getOutputStream());
            this.reader = new ObjectInputStream(socket.getInputStream());
            this.mode = ChatMode.CLIENT;
            init();
        } catch (IOException e) {
            throw new ChatException("Can't connect to host: ".concat(e.getMessage()));
        }
    }

    @Override
    public void server() throws ChatException {
        try{
            this.serverSocket = new ServerSocket(SERVER_PORT);
            this.socket = serverSocket.accept();
            this.reader = new ObjectInputStream(socket.getInputStream());
            this.writer = new ObjectOutputStream(socket.getOutputStream());
            this.mode = ChatMode.SERVER;
            init();
        } catch (IOException e) {
            throw new ChatException("Communication error occur: ".concat(e.getMessage()));
        }
    }

    private void init() throws IOException {
        Message msg = new Message(nickName, Command.CMD_INTRO);
        this.writer.writeObject(msg);
        this.receiver = new ChatTreadHandler(this.inputQueue, this.reader);
        this.receiver.start();
        this.sender = new ChatTreadHandler(this.nickName, this.outputQueue, this.writer);
        this.sender.start();
        this.isReady = true;
    }

    @Override
    public String getMessage() throws ChatException {
        if (!isReady){
            throw new ChatException("Chat controller is not properly initialized");
        }
        try {
            return this.inputQueue.poll();
        }catch (Exception e) {
            throw new ChatException("Error in receiving message occur: ".concat(e.getMessage()));
        }
    }

    @Override
    public void sendMessage(String message) throws ChatException {
        if (!isReady){
            throw new ChatException("Chat controller is not properly initialized");
        }
        try {
            this.outputQueue.put(message);
        } catch (Exception e) {
            throw new ChatException("Error in sending message occur: ".concat(e.getMessage()));
        }
    }

    @Override
    public void close() throws ChatException {
        try {
            if (this.sender != null){
                this.sender.interrupt();
            }

            if (this.receiver != null){
                this.receiver.interrupt();
            }
            if (this.writer != null) {
                if ((this.mode == ChatMode.SERVER && !this.serverSocket.isBound()) ||
                        (this.mode==ChatMode.CLIENT && !this.socket.isBound())) {
                    this.writer.writeObject(new Message(nickName, Command.CMD_TERMINATE));
                }
                this.writer.close();
            }

            if (this.reader != null) {
                this.reader.close();
            }

            if (socket != null){
                socket.close();
            }

            if (serverSocket != null){
                serverSocket.close();
            }
        }catch (IOException e){
            throw new ChatException("Error in closing session: ".concat(e.getMessage()));
        }
    }

    public ChatMode getMode() {
        return mode;
    }

    public boolean isReady() {
        return isReady;
    }
}
