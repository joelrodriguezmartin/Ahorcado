package dad.javafx.ahorcado.words;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;

public class WordsController implements Initializable{
	@FXML
	BorderPane root;
	@FXML
	ListView<String> wordsList;
	@FXML
	Button addButton;
	@FXML
	Button removeButton;
	
	//model
	public ListProperty<String> wordsListProperty = new SimpleListProperty<String>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO button onactions
		wordsListProperty.bindBidirectional(wordsList.itemsProperty());
		
		addButton.setOnAction(e -> onAddAction());
		removeButton.setOnAction(e -> onRemoveAction());
	}
	
	private void onRemoveAction() {
		wordsList.getItems().remove(wordsList.getSelectionModel().getSelectedItem());
	}

	private void onAddAction() {
		TextInputDialog addWord = new TextInputDialog();
		addWord.setTitle("Add a word");
		addWord.setHeaderText("Enter a word:");
		addWord.setContentText("Word:");

		Optional<String> result = addWord.showAndWait();

		result.ifPresent(word -> {
			this.wordsListProperty.add(word);
		});
	}

	public WordsController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/WordsTab.fxml"));
		loader.setController(this);
		loader.load();
	}
	
	public BorderPane getRoot() {
		return root;
	}
}
