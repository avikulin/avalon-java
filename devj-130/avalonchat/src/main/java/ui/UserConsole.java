package ui;

import controller.ChatController;
import enums.ChatMode;
import exceptions.ChatException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserConsole {
    private static String CMD_TERMINATE = "#bye";
    private final ChatMode mode;
    private final String hostName;


    public UserConsole(ChatMode chatMode, String hostname) {
        if (chatMode == null) {
            throw new IllegalArgumentException("Chat mode must be not-null");
        }
        if (hostname == null || hostname.isEmpty()){
            throw new IllegalArgumentException("Host name must be non-null and non-empty string");
        }
        this.mode = chatMode;
        this.hostName = hostname;
    }

    public void run() throws IOException {
        System.out.println("\n\n"+
                " █████╗ ██╗   ██╗ █████╗ ██╗      ██████╗ ███╗   ██╗         ██████╗██╗  ██╗ █████╗ ████████╗\n" +
                "██╔══██╗██║   ██║██╔══██╗██║     ██╔═══██╗████╗  ██║        ██╔════╝██║  ██║██╔══██╗╚══██╔══╝\n" +
                "███████║██║   ██║███████║██║     ██║   ██║██╔██╗ ██║        ██║     ███████║███████║   ██║   \n" +
                "██╔══██║╚██╗ ██╔╝██╔══██║██║     ██║   ██║██║╚██╗██║        ██║     ██╔══██║██╔══██║   ██║   \n" +
                "██║  ██║ ╚████╔╝ ██║  ██║███████╗╚██████╔╝██║ ╚████║        ╚██████╗██║  ██║██║  ██║   ██║   \n" +
                "╚═╝  ╚═╝  ╚═══╝  ╚═╝  ╚═╝╚══════╝ ╚═════╝ ╚═╝  ╚═══╝         ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝   ");
        System.out.printf("\nStart chatting session (%s)...\n[ print \"%s\" to quite ]\n\n", this.mode, CMD_TERMINATE);
        System.out.print("Enter your nick-name: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String nickName = reader.readLine();
        try (ChatController controller = new ChatController(this.hostName, nickName)){
            switch (this.mode){
                case CLIENT: controller.client();break;
                case SERVER: controller.server();break;
            }
            System.out.println("Waiting for connect...");

            //---waiting for INTRO to start session
            while (true){
                String incomingMsg = controller.getMessage();
                if (incomingMsg != null && !incomingMsg.isEmpty()){
                    System.out.println(incomingMsg);
                    break;
                }
            }
            System.out.println("Connected...");
            System.out.println("Press \"ENTER\" to check incoming messages");
            while (true){

                while (true) { //---printing all the messages received
                    String incomingMsg = controller.getMessage();
                    if (incomingMsg ==null){
                        break;
                    }
                    if (!incomingMsg.isEmpty()){
                        System.out.println(incomingMsg);
                    }
                }

                System.out.print(String.format("[%s] ▶ ", nickName));
                String outgoingMsg = reader.readLine();
                if (outgoingMsg.isEmpty()){
                    System.out.println("checking in-box...");
                }
                if (outgoingMsg.trim().toLowerCase().equals(CMD_TERMINATE)){
                    System.out.println("Closing the chat session");
                    break;
                }
                controller.sendMessage(outgoingMsg);
            }

        } catch (ChatException e) {
            System.err.println("Unexpected error happens:");
            e.printStackTrace();
        }
    }
}
