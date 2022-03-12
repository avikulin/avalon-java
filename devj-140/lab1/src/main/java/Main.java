import forms.AuthorizationSceneBuilder;
import forms.MainSceneBuilder;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Helpful notes");
        primaryStage.setMinWidth(300);
        primaryStage.setMinHeight(650);
        primaryStage.setScene(new AuthorizationSceneBuilder(primaryStage).build());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}