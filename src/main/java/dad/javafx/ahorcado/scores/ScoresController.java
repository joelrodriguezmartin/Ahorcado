package dad.javafx.ahorcado.scores;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

public class ScoresController implements Initializable{
	@FXML
	BorderPane root;
	@FXML
	ListView<String> scoresList;
	
	//model
	public ListProperty<String> scoresListProperty = new SimpleListProperty<String>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		scoresListProperty.bindBidirectional(scoresList.itemsProperty());
	}
	
	public void orderList() {
		Collections.sort(scoresListProperty);
		Collections.reverse(scoresListProperty);
	}

	public ScoresController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ScoresTab.fxml"));
		loader.setController(this);
		loader.load();
	}
	
	public BorderPane getRoot() {
		return root;
	}
}
