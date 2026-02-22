package javafxpractice.controllers;
import javafxpractice.utils.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;


import java.io.IOException;



public class MainController {
	
	
	@FXML
	private TextField Username;
	@FXML
	private TextField Password;
	@FXML
	private Button LoginButton;
	@FXML
	private Label ErrorLabel;
	@FXML
	private Button ttButton;
	
	
	@FXML
    private void buttonPushed(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/javafxpractice/views/nextScene.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
	
	
	@FXML
	private void onLoginPushed(javafx.event.ActionEvent event) throws IOException {
		
		final String uN = Username.getText().trim();
		final String pW = Password.getText().trim();
		
		accountSystem aS = new accountSystem();
		accountValidator aV = new accountValidator(aS);
		
		
		if(uN.isEmpty() || pW.isEmpty()) {
			ErrorLabel.setText("Please Provide A Username And Password");
			ErrorLabel.setStyle("-fx-text-fill: red;");
			ErrorLabel.setVisible(true);
			
		}
		else if( (!aV.checkUNL(uN))) {
			ErrorLabel.setText("Username is not unique, please try again");
			ErrorLabel.setStyle("-fx-text-fill: red;");
			ErrorLabel.setVisible(true);
		}
		else if((!aV.isUsernameUnique(uN))) {
			ErrorLabel.setText("Please make sure that the username is at least 10 characters");
			ErrorLabel.setStyle("-fx-text-fill: red;");
			ErrorLabel.setVisible(true);
		}
		else if((!aV.checkPWAR(pW))) {
			ErrorLabel.setText("Please ensure that the password is at least 11 characters and contains one digit");
			ErrorLabel.setVisible(true);
		}
		
		else {
			ErrorLabel.setVisible(false);
			aS.createUser(uN, pW);
			Parent root = FXMLLoader.load(getClass().getResource("/javafxpractice/views/nextScene.fxml"));
	        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        Scene scene = new Scene(root);
	        stage.setScene(scene);
	        stage.show();
		}
		
	}
	
	
	
	public void goToTT(javafx.event.ActionEvent event) throws IOException {
		//go to the new TTscreen
		Parent root = FXMLLoader.load(getClass().getResource("/javafxpractice/views/tt.fxml"));
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
		
		
	}
	
	
	

}
