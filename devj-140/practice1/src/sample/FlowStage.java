package sample;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class FlowStage extends Stage {
    public void init(){
        FlowPane flowPane = new FlowPane();

        Button button1 = new Button("Button 1");
        Button button2 = new Button("Button 2");
        Button button3 = new Button("Button 3");
        Button button4 = new Button("Button 4");
        Button button5 = new Button("Button 5");
        Button button6 = new Button("Button 6");
        Button button7 = new Button("Button 7");
        Button button8 = new Button("Button 8");
        Button button9 = new Button("Button 9");
        Button button10 = new Button("Button 10");

        ObservableList<Node> elements = flowPane.getChildren();
        elements.add(button1);
        elements.add(button2);
        elements.add(button3);
        elements.add(button4);
        elements.add(button5);
        elements.add(button6);
        elements.add(button7);
        elements.add(button8);
        elements.add(button9);
        elements.add(button10);

        flowPane.setVgap(10);
        flowPane.setHgap(10);
        flowPane.setAlignment(Pos.CENTER);
        Scene scene3 = new Scene(flowPane, 400, 300);
        setScene(scene3);
        show();
    }
}
