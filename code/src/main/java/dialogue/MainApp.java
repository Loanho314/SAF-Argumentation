package dialogue;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
		Parent root = loader.load();
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("Darg");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
