package forms;

import contracts.SceneBuilder;
import dal.RepositoryImpl;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.TaskItem;

public class MainSceneBuilder implements SceneBuilder {
    private Stage primary;

    public MainSceneBuilder(Stage primary) {
        if (primary == null){
            throw new IllegalArgumentException("Primary stage must be set");
        }

        this.primary = primary;
    }

    @Override
    public Scene build() {
        BorderPane mainBoard = new BorderPane();
        VBox topPlaceHolder = new VBox();
        topPlaceHolder.setId("top-placeholder");

        GridPane searchPane = new GridPane();
        ColumnConstraints searchTextConstraint = new ColumnConstraints();
        searchTextConstraint.setPercentWidth(90.0);
        searchTextConstraint.setHgrow(Priority.ALWAYS);
        searchTextConstraint.setFillWidth(true);

        ColumnConstraints searchBtnConstraint = new ColumnConstraints();
        searchBtnConstraint.setPercentWidth(10.0);
        searchBtnConstraint.setHgrow(Priority.SOMETIMES);
        searchBtnConstraint.setFillWidth(true);

        searchPane.getColumnConstraints().add(searchTextConstraint);
        searchPane.getColumnConstraints().add(searchBtnConstraint);

        TextField tfSearchQuery = new TextField();
        tfSearchQuery.setId("search-box");

        ImageView imgSearch = new ImageView(this.getClass().getResource("/img/search_icon.png").toExternalForm());
        Button btnSearch = new Button();
        btnSearch.setGraphic(imgSearch);

        searchPane.add(tfSearchQuery, 0,0);
        searchPane.add(btnSearch, 1,0);
        searchPane.setId("search-pane");

        topPlaceHolder.getChildren().add(searchPane);

        mainBoard.setTop(topPlaceHolder);

        TableView<TaskItem> tableView = new TableView<>();

        TableColumn<TaskItem, String> firstNameCol = new TableColumn<>("#");
        firstNameCol.setPrefWidth(50);
        tableView.getColumns().add(firstNameCol);

        TableColumn<TaskItem, String> priorityCol = new TableColumn<>("Priority");
        priorityCol.setPrefWidth(50);
        tableView.getColumns().add(priorityCol);

        TableColumn<TaskItem, Integer> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setPrefWidth(270);
        tableView.getColumns().add(descriptionCol);

        TableColumn<TaskItem, Integer> dueDateCol = new TableColumn<>("Date");
        dueDateCol.setPrefWidth(80);
        tableView.getColumns().add(dueDateCol);

        RepositoryImpl<TaskItem> repo = new RepositoryImpl<>();
        repo.bind(tableView);

        mainBoard.setCenter(tableView);

        VBox bottomPlaceHolder = new VBox();
        bottomPlaceHolder.setId("top-placeholder");

        GridPane actionsPane = new GridPane();
        ColumnConstraints actionColumns = new ColumnConstraints();
        actionColumns.setPercentWidth(50.0);
        actionColumns.setHgrow(Priority.ALWAYS);
        actionColumns.setFillWidth(true);

        actionsPane.getColumnConstraints().add(actionColumns);
        actionsPane.getColumnConstraints().add(actionColumns);

        Button btnNewItem = new Button("New");
        btnNewItem.getStyleClass().add("button-glass-grey");
        btnNewItem.setMaxWidth(Double.MAX_VALUE);

        Button btnEditItem = new Button("Edit");
        btnEditItem.getStyleClass().add("button-glass-grey");
        btnEditItem.setMaxWidth(Double.MAX_VALUE);

        VBox leftColumn = new VBox();
        leftColumn.getStyleClass().add("action-placeholder");
        leftColumn.getChildren().add(btnNewItem);

        VBox rightColumn = new VBox();
        rightColumn.getStyleClass().add("action-placeholder");
        rightColumn.getChildren().add(btnEditItem);

        actionsPane.add(leftColumn, 0,0);
        actionsPane.add(rightColumn, 1,0);

        bottomPlaceHolder.getChildren().add(actionsPane);
        mainBoard.setBottom(bottomPlaceHolder);

        Scene res = new Scene(mainBoard);
        res.getStylesheets().add(this.getClass().getResource("/css/buttons.css").toExternalForm());
        res.getStylesheets().add(this.getClass().getResource("/css/text.css").toExternalForm());
        res.getStylesheets().add(this.getClass().getResource("/css/textbox.css").toExternalForm());
        return res;
    }
}
