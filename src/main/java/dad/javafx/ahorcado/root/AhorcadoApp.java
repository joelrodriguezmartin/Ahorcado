package dad.javafx.ahorcado.root;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AhorcadoApp extends Application {
	RootController controller;
	@Override
	public void start(Stage primaryStage) throws Exception {
		controller = new RootController();
		Scene scene = new Scene(controller.getRoot(), 320, 320);
		primaryStage.setTitle("Ahorcado");
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(e -> controller.writeFiles());
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}

}
