import controller.ChatController;
import exceptions.ChatException;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TestServer {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try (ChatController controller = new ChatController("localhost","avikulin")){
            while (true){

            }
        } catch (ChatException e) {
            e.printStackTrace();
        }
    }
}
