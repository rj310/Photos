package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import Models.Album;
import Models.Photo;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UserLoginController {
	
	@FXML ListView<String> albumlistview;
	@FXML Button openbutton;
	@FXML Button createbutton;
	@FXML Button renamebutton;
	@FXML Button deletebutton;
	@FXML TextArea albuminfo;
	@FXML Button searchbutton;
	@FXML TextField createfield;
	@FXML TextField renamefield;
	@FXML Button quitbutton;
	@FXML Button logbutton;
	/**
	 * The observable list created to hold the list of Album names
	 * to be shown in the list view
	 */
	ObservableList<String> obsList = FXCollections.observableArrayList();
	/**
	 * The current list of users on the application
	 */
	ArrayList<User> users = AdminController.getUsers();
	/**
	 * The list of albums of the current user of the application
	 */
	ArrayList<Album> albums;
	/**
	 * The current user of the album
	 */
	public static User cur;
	
	
	/**
	 * Sets the current user of the album,
	 * sets the list of preset tags,
	 * adds the list of album names to the list view,
	 * and adds listeners to all the buttons
	 * @param name
	 */
	public void start(String name) {
		Photo.tagtypes.add("location");
		Photo.tagtypes.add("person");
		Photo.tagtypes.add("vibe");
		Photo.tagtypes.add("hobby");
		Photo.tagtypes.add("subject");
		Photo.tagtypes.add("photographer");
		for(User u : users) {
			if(u.getName().equals(name)) {
				albums = u.getAlbums();
				cur = u;
			}
		}
		
		
		albuminfo.setEditable(false);
		
		for(int i=0;i<albums.size();i++) {
			obsList.add(albums.get(i).getName());
		}
		albumlistview.setItems(obsList);
		albumlistview.getSelectionModel().select(0);
		
		
		openbutton.setOnAction((event) -> {
			if(albumlistview.getSelectionModel().getSelectedItem()!=null) {
				open(event);
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Error");
				alert.setContentText("Select an album to open!");
				alert.show();
			}
        });
		
		renamebutton.setOnAction((event) -> {
			rename();
        });
		
		deletebutton.setOnAction((event) -> {
			deleteAlbum();
        });
		
		createbutton.setOnAction((event) -> {
			createAlbum();
        });
		
		logbutton.setOnAction((event) -> {
			logout(event);
        });
		quitbutton.setOnAction((event) -> {
			quit();
        });
		searchbutton.setOnAction((event) -> {
			search(event);
        });
		
		albumlistview.getSelectionModel().select(0);
		if(obsList.size()>0) {
			showItem();
		}
		
		albumlistview
		.getSelectionModel()
		.selectedIndexProperty()
		.addListener(
				(obs, oldVal, newVal) -> 
				showItem());
		
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
	/**
	 * Displays the details of each album
	 * in the textfield under the listview
	 * when they are clicked
	 */
	private void showItem() {
		if(albumlistview.getSelectionModel().getSelectedItem()!=null) {
			String alname = albumlistview.getSelectionModel().getSelectedItem();
			Album temp=null;
			for(Album a : albums) {
				if(a.getName().equals(alname)) {
					temp = a;
				}
			}
			String nametext="";
			String numtext="";
			String newtext="";
			String oldtext="";
			String createtext="";
			
			if(temp.getName()!=null) {
				nametext = temp.getName();
			}else {nametext="Does not Exist";}
			
			if(temp.getPhotolist()!=null) {
				numtext += temp.getPhotolist().size();
			}else {numtext="Does not Exist";}
			
			if(temp.getNewest()!=null) {
				newtext = temp.getNewest().getTime().toString();
			}else {newtext="Does not Exist";}
			
			if(temp.getOldest()!=null) {
				oldtext = temp.getOldest().getTime().toString();
			}else {oldtext="Does not Exist";}
			
			if(temp.getMakedate()!=null) {
				createtext = temp.getMakedate().getTime().toString();
			}else {createtext="Does not Exist";}
			
			
			String content = "Name: "+nametext + 
					"\nNumber of Photos: " + numtext+
					"\n Newest Photo Made: " + newtext+
					"\n Oldest Photo Made: " + oldtext+
					"\n Creation Date: " + createtext;
			albuminfo.setText(content);
		}
	}
	
	/**
	 * Creates a new album,
	 * adds it to the User and the album
	 * list view
	 */
	public void createAlbum() {
		String newname = createfield.getText();
		if(newname.trim().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Error");
			alert.setContentText("Enter an album name!");
			alert.show();
			return;
		}
		Album newalb = new Album();
		newalb.setName(newname);
		newalb.setMakedate(Calendar.getInstance());
		obsList.add(newname);
		albums.add(newalb);
		cur.setAlbums(albums);
		albumlistview.setItems(obsList);
		createfield.clear();
		albumlistview.refresh();
		int index = obsList.indexOf(newname);
		albumlistview.getSelectionModel().select(index);
	}
	
	/**
	 * Deletes an album from the User, and removes it from the list view
	 */
	public void deleteAlbum() {
		if(albumlistview.getSelectionModel().getSelectedItem()!=null) {
			int index = albumlistview.getSelectionModel().getSelectedIndex();
			obsList.remove(index);
			albums.remove(index);
			cur.setAlbums(albums);
			albumlistview.setItems(obsList);
			albumlistview.refresh();
			 if(obsList.size()==0) {
				 albuminfo.clear();
			 }
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Error");
			alert.setContentText("Select an album to copy to!");
			alert.show();
			return;
		}
		
	}
	
	/**
	 * Renames an album based on the user inputted new name
	 */
	public void rename() {
		if(albumlistview.getSelectionModel().getSelectedItem()!=null&&!renamefield.getText().trim().isEmpty()) {
			String newname = renamefield.getText();
			int index = albumlistview.getSelectionModel().getSelectedIndex();
			obsList.set(index, newname);
			albums.get(index).setName(newname);
			albumlistview.setItems(obsList);
			albumlistview.refresh();
			showItem();
			renamefield.clear();
		}else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Error");
			alert.setContentText("No Album selected or Blank new name!");
			alert.show();
			return;
		}

	}
	
	/**
	 * Opens an album by sending the user 
	 * to the album open page using the 
	 * UserResultsController
	 * @param ev The event of the mouse clicking the open buttons
	 */
	public void open(Event ev) {
		int index = albumlistview.getSelectionModel().getSelectedIndex();
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(getClass().getResource("/view/UserResults.fxml"));
		AnchorPane root;
		try {
			root = (AnchorPane)loader.load();

			UserResultsController userresultscontroller = 
				loader.getController();
		userresultscontroller.start(albums.get(index));

		Scene scene = new Scene(root, 800, 600);
		((Node)ev.getSource()).getScene().getWindow().hide();
		stage.setScene(scene);
		
		stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Sends the user to the search page using the
	 * SearchController
	 * @param ev The event of the search button being clicked
	 */
	public void search(Event ev) {
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(getClass().getResource("/view/Search.fxml"));
		AnchorPane root;
		try {
			root = (AnchorPane)loader.load();

			SearchController searchcontroller = 
				loader.getController();
			searchcontroller.start(cur);

		Scene scene = new Scene(root, 800, 500);
		((Node)ev.getSource()).getScene().getWindow().hide();
		stage.setScene(scene);
		
		stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
