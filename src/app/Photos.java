package app;
/**
 * @author Rohan Joshi
 * @author Nicholas Cheniara
 *
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import Models.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.AdminController;
import view.LoginController;

public class Photos extends Application implements Serializable {
	
	
	/**
	 * The directory in which to store the serialized data
	 */
	public static final String storeDir = "data";
	
	/**
	 * The file path in which to store the serialized data
	 */
	public static final String storeFile = "users.data";
	
	/**
	 * The list of users containing all the albums and photos for the application
	 * created here so that a Photos instance can be serialized and then loaded 
	 * for keeping data across session
	 */
	public ArrayList<User> users;
	
	/**
	 * The list of tagtypes so that users can select from pre selected tagtypes 
	 */
	public ArrayList<String> tagtypes;
	
	/**
	 * Start method starts the application, loads the initial controller,
	 * initial stage and scene, and displays the login window
	 * 
	 * @param primaryStage The initial stage to display to the user
	 * @throws Exception
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(
				getClass().getResource("/view/login.fxml"));
		AnchorPane root = (AnchorPane)loader.load();

		LoginController logincontroller = 
				loader.getController();
		logincontroller.start();

		Scene scene = new Scene(root, 500, 435);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show(); 

	}

	/**
	 * The main method of the application first reads data from the previous
	 * session if it exists, then launches the application, finally saving the data 
	 * on application close.
	 * 
	 * @param args The command line arguments taken in as a String array
	 */
	public static void main(String[] args) {
		Photos p = new Photos();
		if (readUsers() != null) {
			p = readUsers();
			AdminController.users = p.users;
		}
		launch(args);
		p.users = AdminController.getUsers();
		saveUsers(p);
	}
	/**
	 * This method serializes a photos object holding the
	 * list of users from the last session
	 * 
	 * @param p the Photos object to serialize
	 */
	public static void saveUsers(Photos p) {
			ObjectOutputStream oos;
			try {
				oos = new ObjectOutputStream(
				new FileOutputStream(storeDir + File.separator + storeFile));
				oos.writeObject(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	/**
	 * Converts the serialized data back into usable Photos object
	 * 
	 * @return Photos the deserialized photos object
	 */
	public static Photos readUsers() {
		ObjectInputStream ois = null;
		Photos p = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
			p = (Photos)ois.readObject();
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			try { ois.close(); } catch (IOException e1) {}
			return null;
		}
		try { ois.close(); } catch (IOException e1) {}
		return p;
	}
	
	/**
	 * Utility print method to see if the correct list of users is saved
	 */
	public void print() {
		for(User u : AdminController.users) {
			System.out.println(u.getName());
		}
	}

}
