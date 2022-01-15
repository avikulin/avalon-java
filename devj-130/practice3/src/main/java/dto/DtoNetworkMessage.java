package dto;

import java.io.*;
import java.util.Date;

public class DtoNetworkMessage implements Serializable {
    private final String message;
    private Date processingStartTime;
    private Date processingEndTime;

    public DtoNetworkMessage(String message) {
        if (message == null){
            throw new IllegalArgumentException("Message can't be null");
        }
        this.message = message;
    }

    public void markProcessingStartTime(){
        if (processingStartTime !=null){
            throw new IllegalStateException("Processing start time is already set");
        }

        this.processingStartTime = new Date();
    }

    public void markProcessingEndTime(){
        if (processingEndTime !=null){
            throw new IllegalStateException("Processing start time is already set");
        }
        if (processingStartTime ==null){
            throw new IllegalStateException("Processing start time is not set");
        }

        this.processingEndTime = new Date();
    }

    public String getMessage() {
        return message;
    }

    public Date getProcessingStartTime() {
        return processingStartTime;
    }

    public Date getProcessingEndTime() {
        return processingEndTime;
    }

    public String getProcessPeriod(){
        return String.format("Processing from %s to %s",
                processingStartTime.toString(),
                processingEndTime.toString()
        );
    }

    public byte[] toBytes() {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream stream = new ObjectOutputStream(bytes);
            stream.writeObject(this);
            return bytes.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static DtoNetworkMessage fromBytes(byte[] source) throws IOException, ClassNotFoundException {
        try(ObjectInputStream inputStream = new ObjectInputStream(
                new ByteArrayInputStream(source, 0, source.length))
        ){
            DtoNetworkMessage msg = (DtoNetworkMessage) inputStream.readObject();
            return msg;
        }
    }
}
