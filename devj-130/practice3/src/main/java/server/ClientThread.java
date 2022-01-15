package server;

import dto.DtoNetworkMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket clientSocket;

    public ClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        super.run();
        try (ObjectOutputStream outputStream = new ObjectOutputStream (clientSocket.getOutputStream());
             ObjectInputStream inputStream = new ObjectInputStream (clientSocket.getInputStream())){

            DtoNetworkMessage msg = (DtoNetworkMessage) inputStream.readObject();
            System.out.println(
                    String.format(
                            "Message received: \"%s\" (from %s)",
                            msg.getMessage(),
                            clientSocket.getRemoteSocketAddress()
                    )
            );

            msg.markProcessingStartTime();
            System.out.println(
                    String.format("\tstart processing: %s (origin = %s)",
                            msg.getProcessingStartTime(),
                            clientSocket.getRemoteSocketAddress()
                    )
            );
            Thread.sleep(15_000); //2 sec
            msg.markProcessingEndTime();
            System.out.println(
                    String.format("\tend processing: %s (origin = %s)\n",
                            msg.getProcessingEndTime(),
                            clientSocket.getRemoteSocketAddress()
                    )
            );

            outputStream.writeObject(msg);
        } catch (InterruptedException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
