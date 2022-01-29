package controller;

import dto.Message;
import enums.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatTreadHandler extends Thread {
    private String nickName;
    private BlockingDeque<String> queue;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private boolean isReady;

    public ChatTreadHandler(BlockingDeque<String> queue, ObjectInputStream reader) {
        setQueue(queue);
        setReader(reader);
        this.isReady = true;
    }

    public ChatTreadHandler(String nickName, BlockingDeque<String> queue, ObjectOutputStream writer) {
        setNickName(nickName);
        setQueue(queue);
        setWriter(writer);
        this.isReady = true;
    }

    private void setNickName(String nickName) {
        if (nickName == null || nickName.isEmpty()) {
            throw new IllegalArgumentException("Nickname must be non-null & non-empty string");
        }
        this.nickName = nickName;
    }

    private void setQueue(BlockingDeque<String> queue) {
        if (queue == null) {
            throw new IllegalArgumentException("Deque object reference must be set");
        }
        this.queue = queue;
    }

    private void setReader(ObjectInputStream reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Input stream reader object reference must be set");
        }
        this.reader = reader;
    }

    private void setWriter(ObjectOutputStream writer) {
        if (writer == null) {
            throw new IllegalArgumentException("Output stream writer reference must be set");
        }
        this.writer = writer;
    }

    public void dispatch(Message msg) {
        if (msg.getType() == MessageType.COMMAND) {
            switch (msg.getCommand()) {
                case CMD_INTRO: {
                    this.queue.add(String.format("User \"%s\" enters the chat", msg.getNickName()));
                    return;
                }
                case CMD_TERMINATE: {
                    this.queue.add(String.format("User \"%s\" leaves the chat. End of transmit.", msg.getNickName()));
                    this.isReady = false;
                    return;
                }
            }
        }

        this.queue.add(String.format("[%s] ▶ %s", msg.getNickName(), msg.getPayLoad()));
    }

    @Override
    public void run() {
        if (reader != null) {
            try {
                while (true) {
                    Message msg = (Message) reader.readObject();
                    if (msg != null) {
                        dispatch(msg);
                    }
                    Thread.sleep(1000);
                }
            } catch (IOException | ClassNotFoundException ignored) {
            } catch (InterruptedException e) {
                return;
            }
        }

        if (writer != null) {
            try {
                while (true) {
                    if (!queue.isEmpty()) {
                        String msg = queue.poll();
                        if (!msg.isEmpty() && this.isReady) {
                            writer.writeObject(new Message(this.nickName, msg));
                        }
                    }
                    Thread.sleep(1000);
                }
            } catch (IOException ignored) {
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
