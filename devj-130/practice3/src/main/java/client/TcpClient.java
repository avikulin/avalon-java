package client;

import dto.DtoNetworkMessage;

import java.io.*;
import java.net.Socket;

public class TcpClient {
    public final static int PORT = 20220;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length < 1){
            System.out.println("Usage: <udp-client> <server-address>");
            return;
        }

        System.out.println("Enter text to send to server: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String text = reader.readLine();

        DtoNetworkMessage message = new DtoNetworkMessage(text);

        try(Socket socket = new Socket(args[0], PORT)){
            try (ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream())){
                outputStream.writeObject(message);
                message = (DtoNetworkMessage)inputStream.readObject();
                System.out.println(
                        String.format("Message processing time: %s",
                                message.getProcessPeriod()
                        )
                );
            }
        }
    }
}
