package dad.javafx.ahorcado.round;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class RoundController implements Initializable{
	@FXML
	BorderPane root;
	@FXML
	ImageView hangedImage;
	@FXML
	Label wordLabel;
	@FXML
	Label scoreLabel;
	@FXML
	Label letterLabel;
	@FXML
	TextField inputText;
	@FXML
	Button letterButton;
	@FXML
	Button solveButton;
	
	//utilities
	Alert alert;
	
	//model
	public StringProperty secretWord = new SimpleStringProperty();
	public StringProperty wordSpaces = new SimpleStringProperty();
	public StringProperty failedLetters = new SimpleStringProperty();
	public IntegerProperty score = new SimpleIntegerProperty();
	public IntegerProperty tries = new SimpleIntegerProperty();
	public BooleanProperty defeat = new SimpleBooleanProperty();
	public BooleanProperty win = new SimpleBooleanProperty();


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		letterButton.setOnAction(e -> onLetterButton());
		solveButton.setOnAction(e -> onSolveButton());
		
		tries.addListener((obs, ov, nv) -> {
			hangedImage.setImage(new Image(getClass().getResource("/images/"+nv+".png").toString()));
		});
		
		tries.set(1);
		score.set(0);
		failedLetters.set("");
		defeat.set(false);
		win.set(false);
		
		letterLabel.textProperty().bindBidirectional(failedLetters);
		wordLabel.textProperty().bindBidirectional(wordSpaces);
		scoreLabel.textProperty().bind(score.asString());
		tries.addListener((obs, ov, nv) -> {
			hangedImage.setImage(new Image(getClass().getResource("/images/"+nv+".png").toString()));
		});
	}
	
	private void onSolveButton() {
		if(inputText.getText().isEmpty()) {
			alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Error");
			alert.setContentText("Debe introducir algo");
			alert.showAndWait();
		}else {
			if (tries.get() < 9) { 
				if (inputText.getText().equals(secretWord.get())){
					alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("Felicidades");
					alert.setContentText("Has ganado");
					alert.showAndWait();
					score.set(score.get()+50);
					win.set(true);
				}else {
					tries.set(tries.get() + 1);
				}
			}else {
				alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Error");
				alert.setContentText("Has perdido");
				alert.showAndWait();
				defeat.set(true);
			}
		}
	}

	private void onLetterButton() {
		boolean encontrada = false;
		if(inputText.getText().isEmpty() || inputText.getText().length() > 1) {
			alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Error");
			alert.setContentText("Debe introducir 1 letra");
			alert.showAndWait();
		}else {
			if (tries.get() < 9) {
				char letter = inputText.getText().charAt(0);
				for (int i = 0; i < secretWord.get().length(); i++) {
					if (secretWord.get().charAt(i) == letter || secretWord.get().charAt(i) == Character.toUpperCase(letter)) {
						score.set(score.get() + 5);
						System.out.println(score.get());
						StringBuilder newSpaces = new StringBuilder(wordSpaces.get());
						newSpaces.setCharAt(i, letter);
						wordSpaces.set(newSpaces.toString());
						encontrada = true;
					}
				}
				if (encontrada == false) {
					StringBuilder newFailedLetters = new StringBuilder(failedLetters.get());
					String letterString = "";
					letterString += letter;
					if (!newFailedLetters.toString().contains(letterString)) {
						newFailedLetters.append(letter);
						newFailedLetters.append(" ");
						failedLetters.set(newFailedLetters.toString());
						score.set(score.get() - 1);
						tries.set(tries.get() + 1);
					}
				}
				if (wordSpaces.get().equals(secretWord.get())) {
					alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("Felicidades");
					alert.setContentText("Has ganado");
					alert.showAndWait();
					win.set(true);
				}
			}else {
				alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Error");
				alert.setContentText("Has perdido");
				alert.showAndWait();
				defeat.set(true);
			}
		}
	}

	public RoundController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RoundTab.fxml"));
		loader.setController(this);
		loader.load();
	}
	
	public BorderPane getRoot() {
		return root;
	}
}
