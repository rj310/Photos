package view;

import java.io.IOException;
import java.util.ArrayList;

import Models.User;
import app.Photos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;




/**
 * @author Rohan Joshi
 * @author Nicholas Cheniara
 *
 */
public class AdminController {
	
	/**
	 *  The list of users, maintained here as used the most here
	 */
	public static ArrayList<User> users;
	/**
	 * List of tag types
	 */
	public static ArrayList<String> tagtypes;
	/**
	 * @return ArrayList(User) returns the list of users
	 */
	public static ArrayList<User> getUsers(){
		return users;
	}
	/**
	 * @return ArrayList(String) returns the list of tag types
	 */
	public static ArrayList<String> getTagtypes(){
		return tagtypes;
	}
	
	@FXML ListView<String> userlistview;
	@FXML Button createButton;
	@FXML Button deleteButton;
	@FXML Button createsubmit;
	@FXML Button deletesubmit;
	@FXML Button quitbutton;
	@FXML TextField newuserfield;
	@FXML Button logbutton;
	ObservableList<String> obsList = FXCollections.observableArrayList();
	
	/**
	 * The start method intializes all necessary values, sets the listview to the list
	 * of users, and adds listeners to all buttons
	 */
	public void start(){   
		
		deletesubmit.setVisible(false);
		createsubmit.setVisible(false);
		newuserfield.setVisible(false);
		if(users!=null) {
			for(int i=0;i<users.size();i++) {
				obsList.add(users.get(i).getName());
			}
		}
		userlistview.setItems(obsList);
		userlistview.getSelectionModel().select(0);
		
//		userlistview
//		.getSelectionModel()
//		.selectedIndexProperty()
//		.addListener(
//				(obs, oldVal, newVal) -> 
//				System.out.println("lol"));
		
		createsubmit.setOnAction((event) -> {
			create();
        });
		createButton.setOnAction((event) -> {
			createClicked();
        });
		deleteButton.setOnAction((event) -> {
			delete();
        });
		logbutton.setOnAction((event) -> {
			logout(event);
        });
		quitbutton.setOnAction((event) -> {
			quit();
        });
	}
	
	/**
	 * Makes fields for creating a new user visible
	 */
	public void createClicked() {
		newuserfield.setVisible(true);
		createsubmit.setVisible(true);
		
	}
	/**
	 * Makes field for deleting a user visible
	 */
	public void deleteClicked() {
		deletesubmit.setVisible(true);
		
	}
	/**
	 * Creates a new user and adds it to the listview
	 */
	public void create() {
		String newname = newuserfield.getText();
		if(newname.trim().equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Error");
			alert.setContentText("Blank username!");
			alert.show();
			return;
		}
		for(User u: users) {
			if(newname.equals(u.getName())) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Error");
				alert.setContentText("Duplicate username!");
				alert.show();
				createsubmit.setVisible(false);
				newuserfield.setVisible(false);
				newuserfield.clear();
				return;
			}
		}
		User newuser = new User();
		newuser.setName(newname);
		if(users != null) {
			users.add(newuser);
		}
		else {
			users = new ArrayList<User>();
			users.add(newuser);
		}
		obsList.add(newuser.getName());
		userlistview.setItems(obsList);
		userlistview.refresh();
		createsubmit.setVisible(false);
		newuserfield.setVisible(false);
		newuserfield.clear();
		
	}
	
	/**
	 * Deletes a user from the userlist and listview
	 */
	public void delete() {
		if(userlistview.getSelectionModel().getSelectedItem()!=null) {
			int index = userlistview.getSelectionModel().getSelectedIndex();
			users.remove(index);
			obsList.remove(index);
			userlistview.setItems(obsList);
			userlistview.refresh();
		}else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Error");
			alert.setContentText("Duplicate tag!");
			alert.show();
		}
	}
	
	/**
	 * Logs out the user and returns them to login screen
	 * @param e The event of the mouse being clicked on the button
	 */
	public void logout(Event e) {
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(
				getClass().getResource("/view/login.fxml"));
		AnchorPane root;
		try {
			root = (AnchorPane)loader.load();
			LoginController logincontroller = 
					loader.getController();
			logincontroller.start();

			Scene scene = new Scene(root, 500, 435);
			((Node)e.getSource()).getScene().getWindow().hide();
			stage.setScene(scene);
			stage.show();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	/**
	 * Exits the application after saving the 
	 * existing data
	 */
	public void quit() {
		Photos p = new Photos();
		p.users = AdminController.users;
		Photos.saveUsers(p);
		System.exit(0);
	}
	
	
	


}
