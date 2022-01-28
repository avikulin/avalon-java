package networking;

import java.io.*;
import java.net.Socket;

public class FileTransmitter {
    /**
     * Стандартный размер буфера.
     */
    public static final int BUFFER_SIZE = 4096;

    public static void transmit(String server, int port, String fileName) {
        if (server == null || server.isEmpty()) {
            throw new IllegalArgumentException("Server name must be defined");
        }

        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("Name of the file to transfer must be defined");
        }
        File file = new File(fileName);
        if (!file.exists() || file.isDirectory()) {
            throw new IllegalArgumentException("Incorrect filename passed");
        }


        try (Socket clientSocket = new Socket(server, port);
             OutputStream networkOutputStream = clientSocket.getOutputStream();
             BufferedReader socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            byte[] buf = new byte[BUFFER_SIZE];

            System.out.println("Transfer started:");
            System.out.println(String.format("\t-endpoint: %s", clientSocket.getRemoteSocketAddress()));
            System.out.println(String.format("\t-file: %s", file.getAbsolutePath()));
            System.out.println(String.format("\t-size : %d bytes\n", file.length()));

            //#1. sending file name.
            byte[] bufFileName = fileName.getBytes();
            networkOutputStream.write(bufFileName, 0, bufFileName.length);

            //#2. sending file content.
            int bytesSent = 0;
            try (FileInputStream fis = new FileInputStream(file)) {

                while (true) {
                    int bytesRead = fis.read(buf);

                    // В конце файла/потока метод read() возвращает -1 (EOF).
                    if (bytesRead == -1) {
                        break;
                    }
                    networkOutputStream.write(buf, 0, bytesRead);
                    bytesSent += bytesRead;
                }
            }
            System.out.printf("All data have been sent (%d bytes).\n\n", bytesSent);

            //releasing the output stream
            clientSocket.shutdownOutput();

            //receiving ACK message from server.
            System.out.println(socketReader.readLine());
        } catch (IOException e) {
            System.err.println("File transfer error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: <server-address> <port-number> <file-name>");
        }
        int port = -1;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Error: incorrect port number param passed");
        }

        transmit(args[0], port, args[2]);
    }
}
