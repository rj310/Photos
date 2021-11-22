package view;

import java.io.IOException;
import java.util.ArrayList;

import Models.User;
import app.Photos;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * @author Rohan Joshi
 * @author Nicholas Cheniara
 */
public class LoginController {
	

	
	/**
	 * The current list of users as retrieved from the AdminController
	 */
	ArrayList<User> userlist = AdminController.getUsers();
	/**
	 * The list of usernames to check against the inputted username
	 */
	ArrayList<String> susers = new ArrayList<String>();
	/**
	 *  The current user for the applcation to be set upon login
	 */
	static User cur;
	
	@FXML TextField usernameField;
	@FXML Button quitButton;
	@FXML Button submitButton;

	/**
	 * Start method adds all the usernames to their respective list
	 * and adds listeners to the buttons
	 */
	public void start(){ 
		if(userlist!=null) {
			for (User u : userlist) {
				susers.add(u.getName());
				cur = u;
			}
		}

		
		quitButton.setOnAction((event) -> {
			quitButtonClicked();
        });
		
		submitButton.setOnAction((event) -> {
			submitButtonClicked(event);
        });
		
	}
	/**
	 * Exits the application after saving the 
	 * existing data
	 */
	public void quitButtonClicked() {
		Photos p = new Photos();
		p.users = AdminController.users;
		Photos.saveUsers(p);
		System.exit(0);
	}
	
	/**
	 * Checks to see if the username is either admin, or a valid username
	 * and advances the user to the appropriate screen
	 * @param ev The event of the button being clicked
	 */
	public void submitButtonClicked(Event ev) {
		String name = usernameField.getText();
		if(name.equals("") || name == null){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Error");
			alert.setContentText("No username entered!");
			alert.show();
			return;
		}
		if(name.toLowerCase().equals("admin")) {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader();   
			loader.setLocation(getClass().getResource("/view/Admin.fxml"));
			AnchorPane root;
			try {
				root = (AnchorPane)loader.load();

			AdminController admincontroller = 
					loader.getController();
			admincontroller.start();

			Scene scene = new Scene(root, 500, 435);
			((Node)ev.getSource()).getScene().getWindow().hide();
			stage.setScene(scene);
			
			stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(susers.contains(name)) {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader();   
			loader.setLocation(getClass().getResource("/view/UserLogin.fxml"));
			AnchorPane root;
			try {
				root = (AnchorPane)loader.load();

				UserLoginController usercontroller = 
					loader.getController();
			usercontroller.start(name);

			Scene scene = new Scene(root, 600, 500);
			((Node)ev.getSource()).getScene().getWindow().hide();
			stage.setScene(scene);
			
			stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(!susers.contains(name) && !name.toLowerCase().equals("admin")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Error");
			alert.setContentText("Username not found!");
			alert.show();
			usernameField.clear();
			return;
		}
		
	
	}


}
