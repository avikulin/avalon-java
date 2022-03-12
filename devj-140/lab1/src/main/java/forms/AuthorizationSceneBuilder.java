package forms;

import contracts.SceneBuilder;
import controllers.UserSessionController;
import exceptions.UserException;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthorizationSceneBuilder implements SceneBuilder {
    private Stage primary;

    public AuthorizationSceneBuilder(Stage primary) {
        if (primary == null){
            throw new IllegalArgumentException("Primary stage must be set");
        }

        this.primary = primary;
    }

    @Override
    public Scene build() {
        GridPane mainBoard = new GridPane();

        VBox left = new VBox();
        VBox right = new VBox();
        VBox center = new VBox();

        mainBoard.add(left, 0,0);
        mainBoard.add(center, 1,0);
        mainBoard.add(right, 2,0);

        ColumnConstraints leftMarginConstraint = new ColumnConstraints();
        leftMarginConstraint.setPercentWidth(10.0);
        leftMarginConstraint.setHgrow(Priority.ALWAYS);
        leftMarginConstraint.setFillWidth(true);


        ColumnConstraints bodyConstraint = new ColumnConstraints();
        bodyConstraint.setPercentWidth(80.0);
        bodyConstraint.setHgrow(Priority.ALWAYS);
        bodyConstraint.setFillWidth(true);
        
        mainBoard.getColumnConstraints().add(leftMarginConstraint);
        mainBoard.getColumnConstraints().add(bodyConstraint);
        mainBoard.getColumnConstraints().add(leftMarginConstraint);

        center.setAlignment(Pos.CENTER);

        HBox paneTitle = new HBox();
        paneTitle.setAlignment(Pos.BOTTOM_CENTER);

        Label labelTop = new Label("Authorization");
        labelTop.getStyleClass().add("welcome-title");
        paneTitle.getChildren().add(labelTop);
        center.getChildren().add(paneTitle);

        GridPane paneMain = new GridPane();
        paneMain.setHgap(5);
        paneMain.setVgap(5);

        ColumnConstraints labels = new ColumnConstraints();
        labels.setPercentWidth(30.0);
        labels.setHgrow(Priority.SOMETIMES);

        ColumnConstraints values = new ColumnConstraints();
        values.setPercentWidth(70.0);
        values.setHgrow(Priority.SOMETIMES);
        values.setFillWidth(true);

        paneMain.getColumnConstraints().addAll(labels, values);

        Label nameLabel = new Label("Login: ");
        TextField nameField = new TextField();
        paneMain.add(nameLabel, 0, 0);
        paneMain.add(nameField, 1, 0);

        Label panePassword = new Label("Password: ");
        PasswordField pasField = new PasswordField();
        paneMain.add(panePassword, 0, 1);
        paneMain.add(pasField, 1, 1);
        center.getChildren().add(paneMain);

        HBox paneErrorMsg = new HBox();
        paneErrorMsg.setAlignment(Pos.CENTER_LEFT);

        Label lbErrorMsg = new Label();
        lbErrorMsg.getStyleClass().add("error-info");

        paneErrorMsg.getChildren().add(lbErrorMsg);
        center.getChildren().add(paneErrorMsg);

        HBox paneSubmit = new HBox();
        paneSubmit.setAlignment(Pos.CENTER_RIGHT);
        Button btnSignIn = new Button("Sing in");
        btnSignIn.getStyleClass().add("button-rich-blue");
        btnSignIn.setOnAction((e) -> {
            lbErrorMsg.setText("");
            String name = nameField.getText();
            String pass = pasField.getText();
            try {
                UserSessionController.getInstance().createSession(name, pass);

                lbErrorMsg.setText("Loading...");
                this.primary.setScene(new MainSceneBuilder(this.primary).build());
            } catch (UserException | IOException ex) {
                lbErrorMsg.setText(ex.getMessage());
            }
        });
        paneSubmit.getChildren().add(btnSignIn);
        center.getChildren().add(paneSubmit);
        Scene res = new Scene(mainBoard);
        res.getStylesheets().add(this.getClass().getResource("/css/buttons.css").toExternalForm());
        res.getStylesheets().add(this.getClass().getResource("/css/text.css").toExternalForm());
        return res;
    }
}
