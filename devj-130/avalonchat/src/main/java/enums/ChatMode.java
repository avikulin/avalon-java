package enums;

public enum ChatMode {
    SERVER("-server"),
    CLIENT("-client");
    private final String command;

    ChatMode(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public static ChatMode fromString(String s){
        String strCmd = s.trim().toLowerCase();
        if (strCmd.equals(SERVER.getCommand())){
            return SERVER;
        }
        return CLIENT;
    }
}
