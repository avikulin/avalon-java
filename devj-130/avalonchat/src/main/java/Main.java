import enums.ChatMode;
import ui.UserConsole;

import java.io.IOException;

public class Main {
    private static void help(){
        System.out.println("Usage: \n" +
                "#\tfor client session: avalon-chat -client <REMOTE HOSTNAME>\n" +
                "#\tfor server session: avalon-chat -server");
    }

    private static void startSession(UserConsole ui){
        if (ui == null){
            System.out.println("Improper reference to chat controller");
            return;
        }

        try {
            ui.run();
        } catch (IOException e) {
            System.out.println("Unexpected error occur: ".concat(e.getMessage()));
            e.printStackTrace();
            return;
        }
    }

    public static void main(String[] args) {
        if (args.length==0){
            System.out.println("ERROR: No mode defined.");
            help();
            return;
        }

        ChatMode mode = ChatMode.fromString(args[0]);
        if (mode == ChatMode.SERVER){
            startSession(new UserConsole(mode, "localhost"));
        }
        if (mode == ChatMode.CLIENT){
            if (args.length < 2){
                System.out.println("ERROR: Hostname parameter missing");
                help();
                return;
            }
            String hostName = args[1];
            startSession(new UserConsole(mode, hostName));
        }
    }
}
