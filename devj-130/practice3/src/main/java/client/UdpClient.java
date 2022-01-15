package client;

import dto.DtoNetworkMessage;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpClient {
    private static final int PORT = 20220;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length < 1){
            System.out.println("Usage: <udp-client> <server-address>");
            return;
        }

        System.out.println("Enter text to send to server: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String text = reader.readLine();

        DtoNetworkMessage message = new DtoNetworkMessage(text);
        message.markProcessingStartTime();

        byte[] buf = message.toBytes();
        DatagramPacket packet = new DatagramPacket(buf, 0, buf.length, InetAddress.getByName(args[0]), PORT);
        try (DatagramSocket socket = new DatagramSocket()){
            socket.send(packet);
            buf = new byte[4096];
            packet = new DatagramPacket(buf, buf.length);
            ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(buf, 0, buf.length));
            DtoNetworkMessage msg = (DtoNetworkMessage) inputStream.readObject();
            System.out.println("Msg processing time: "+message.getProcessingStartTime());
        }

    }
}
