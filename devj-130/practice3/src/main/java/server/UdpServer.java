package server;

import dto.DtoNetworkMessage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpServer {
    public static final int PORT = 20220;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("Server started...\n");
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            byte[] buf = new byte[4096];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            while (true) {
                socket.receive(packet);

                DtoNetworkMessage msg = DtoNetworkMessage.fromBytes(buf);
                System.out.println(
                        String.format(
                                "Message received: \"%s\" (%s)",
                                msg.getMessage(),
                                msg.getProcessingStartTime().toString()
                        )
                );

                byte[] buf2 = msg.toBytes();
                DatagramPacket packet1 = new DatagramPacket(
                        buf2,
                        0,
                        buf2.length,
                        packet.getAddress(),
                        packet.getPort()
                );
                socket.send(packet1);
            }
        }
    }
}
