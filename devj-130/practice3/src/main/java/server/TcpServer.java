package server;

import java.io.IOException;
import java.net.ServerSocket;

public class TcpServer {
    private static final int PORT = 20220;

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        System.out.println("Server started...\n");
        try (ServerSocket socket = new ServerSocket(PORT)){
            while (true){
                new ClientThread(socket.accept()).run();
            }
        }
    }
}
