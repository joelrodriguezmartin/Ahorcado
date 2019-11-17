package dad.javafx.ahorcado.root;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.javafx.ahorcado.round.RoundController;
import dad.javafx.ahorcado.scores.ScoresController;
import dad.javafx.ahorcado.words.WordsController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;

public class RootController implements Initializable{
	@FXML
	TabPane root;
	@FXML 
	Tab roundTab;
	@FXML
	Tab wordsTab;
	@FXML
	Tab scoresTab;
	
	//controllers
	RoundController roundController = new RoundController(); 
	WordsController wordsController = new WordsController(); 
	ScoresController scoresController = new ScoresController(); 
	
	//persistence
	ArrayList<String> words = new ArrayList<String>();
	ArrayList<String> scores = new ArrayList<String>();
	
	//utilities
	Alert alert;
	
	//model
	ListProperty<String> wordsListProperty = new SimpleListProperty<String>();
	ListProperty<String> scoresListProperty = new SimpleListProperty<String>();
	StringProperty usernameProperty = new SimpleStringProperty();
	StringProperty secretWord = new SimpleStringProperty();
	StringProperty wordSpaces = new SimpleStringProperty();
	public IntegerProperty tries = new SimpleIntegerProperty();
	public BooleanProperty defeat = new SimpleBooleanProperty();
	public BooleanProperty win = new SimpleBooleanProperty();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO leer los archivos .dat y pasarlos a properties
		wordsListProperty.bindBidirectional(wordsController.wordsListProperty);
		scoresListProperty.bindBidirectional(scoresController.scoresListProperty);
		
		tries.bindBidirectional(roundController.tries);
		win.bindBidirectional(roundController.win);
		defeat.bindBidirectional(roundController.defeat);
		secretWord.bindBidirectional(roundController.secretWord);
		wordSpaces.bindBidirectional(roundController.wordSpaces);
		
		defeat.addListener(e -> onDefeat());
		win.addListener(e -> onWin());
		
		readFiles();
		newRound();
	}
	
	private void onWin() {
		String newScore = roundController.score.get() +":"+ usernameProperty.get(); 
		scoresListProperty.add(newScore);
		newRound();
	}

	private void onDefeat() {
		newRound();
	}

	private void newRound() {
		tries.set(1);
		defeat.set(false);
		win.set(false);
		roundController.score.set(0);
		int random =(int)(Math.random() * (wordsListProperty.getSize()  ));
		secretWord.set(wordsListProperty.get(random));
		System.out.println(secretWord.get());
		String wordSize = "";
		for (int i = 0; i < secretWord.get().length(); i++) {
			wordSize += "_";
		}
		wordSpaces.set(wordSize);
	}

	private void readFiles() {
		FileReader reader;
		BufferedReader buffered;
		
		String line = "";
		
		try {
			reader = new FileReader("words.txt");
			buffered = new BufferedReader(reader);
			while((line = buffered.readLine()) != null) {
				words.add(line);
			}
			reader = new FileReader("scores.txt");
			buffered = new BufferedReader(reader);
			while((line = buffered.readLine()) != null) {
				scores.add(line);
			}
			reader.close();
			buffered.close();
		}catch(IOException e) {
			System.out.println("Error abriendo archivo");
		}
		
		for (int i = 0; i < words.size(); i++) {
			wordsListProperty.add(words.get(i));
		}
		
		for (int i = 0; i < scores.size(); i++) {
			scoresListProperty.add(scores.get(i));
		}
		scoresController.orderList();
	}
	
	public RootController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RootView.fxml"));
		loader.setController(this);
		loader.load();
		
		login();
		
		roundTab.setContent(roundController.getRoot());
		wordsTab.setContent(wordsController.getRoot());
		scoresTab.setContent(scoresController.getRoot());
	}
	
	private void login() {
		TextInputDialog userLogin = new TextInputDialog();
		userLogin.setTitle("Login");
		userLogin.setHeaderText("Enter your name:");
		userLogin.setContentText("Name:");
		 
		Optional<String> result = userLogin.showAndWait();
		 
		result.ifPresent(name -> {
		    this.usernameProperty.set(name);
		});
	}

	public TabPane getRoot() {
		return root;
	}

	public void writeFiles() {
		FileWriter writer;
		BufferedWriter buffered;
		try {
			writer = new FileWriter("words.txt");
			buffered = new BufferedWriter(writer);
			buffered.write("");
			for (int i = 0; i < wordsListProperty.getSize(); i++) {
				buffered.append(wordsListProperty.get(i));
				buffered.newLine();
			}
			buffered.close();
			
			writer = new FileWriter("scores.txt");
			buffered = new BufferedWriter(writer);
			buffered.write("");
			for (int i = 0; i < scoresListProperty.getSize(); i++) {
				buffered.append(scoresListProperty.get(i));
				buffered.newLine();
			}
			buffered.close();
		}catch (IOException e) {
			System.out.println("Error de archivos");
		}catch (IndexOutOfBoundsException e) {
			System.out.println("Tiene que haber al menos 1 score y 1 word");
		}
	}
}
