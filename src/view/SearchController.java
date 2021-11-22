package view;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


import Models.Album;
import Models.Photo;
import Models.User;
import app.Photos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
/**
 * @author Rohan Joshi
 * @author Nicholas Cheniara
 */
public class SearchController {
	@FXML ScrollPane scrollpane;
	@FXML TilePane resultpane;
	@FXML DatePicker firstdatefield;
	@FXML DatePicker seconddatefield;
	@FXML TextField tag1field;
	@FXML TextField tag2field;
	@FXML TextField tag3field;
	@FXML TextField tag4field;
	@FXML Button newalbutton;
	@FXML Button quitbutton;
	@FXML Button logoutbutton;
	@FXML Button backbutton;
	@FXML Button datesubmit;
	@FXML Button tagsubmit;
	@FXML ComboBox<String> selbox;		
	/**
	 * The current user of the application
	 */
	User current;
	/**
	 * The list of albums of the current user
	 */
	ArrayList<Album> als;
	/**
	 * The list of photos returned by the search
	 */
	ArrayList<Photo> results;
	
	
	/**
	 * Initializes all fields, sets the visibility of certain nodes,
	 * adds listeners to button, and adds the photos in results to the display 
	 * structures
	 * @param cur the current user to search on
	 */
	public void start(User cur) {
		ObservableList<String> obsList = FXCollections.observableArrayList();
		obsList.add("AND");
		obsList.add("OR");
		selbox.setItems(obsList);
		tag1field.clear();
		tag2field.clear();
		tag3field.clear();
		tag4field.clear();
		firstdatefield.setValue(null);
		seconddatefield.setValue(null);
		
		current=cur;
		als = current.getAlbums();
		
		datesubmit.setOnAction((event) -> {
			searchDates();
        });
		
		tagsubmit.setOnAction((event) -> {
			searchTags();
        });
		quitbutton.setOnAction((event) -> {
			quit();
        });
		logoutbutton.setOnAction((event) -> {
			logout(event);
        });
		backbutton.setOnAction((event) -> {
			back(event);
        });
		newalbutton.setOnAction((event) -> {
			createAlbum(event);
        });
//		
		if(results==null) {
			newalbutton.setDisable(true);
			return;
		}else {
			newalbutton.setDisable(false);
		}
		if(results.size()==0) {
			newalbutton.setDisable(true);
		}else {
			newalbutton.setDisable(false);
		}
		ArrayList<ImageView> images = new ArrayList<ImageView>();
		for(int i=0;i<results.size();i++) {
			images.add(new ImageView(results.get(i).getImage()));
		}
		
		scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Horizontal
		scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll bar
		scrollpane.setFitToWidth(true);
		scrollpane.setContent(resultpane);
		resultpane.setPadding(new Insets(15,15,15,15));
		resultpane.setHgap(15);
		resultpane.setVgap(40);
		resultpane.getChildren().clear();
		for (int i = 0; i < images.size(); i++){
			images.get(i).setFitHeight(75);
			images.get(i).setFitWidth(100);
			images.get(i).setPreserveRatio(true);
			TilePane temp = new TilePane();
			Label caption = new Label();
			caption.setText(results.get(i).getCaption());
			//resultPane.getChildren().add(images.get(i));
			temp.setMaxSize(100, 100);
			caption.setMaxWidth(100);
			temp.getChildren().addAll(images.get(i),caption);
			resultpane.getChildren().add(temp);
		 }
		
	}
	
	/**
	 * Searches the list of tags for all photos based on
	 * the inputted tag search values of the user
	 */
	public void searchTags(){
		ArrayList<Photo> ans = new ArrayList<Photo>();
		if(selbox.getSelectionModel().isEmpty()) {
			String type = tag1field.getText();
			String value = tag2field.getText();
			for(Album a : als) {
				for(Photo p: a.getPhotolist()) {
					HashMap<String,ArrayList<String>> tags = p.getTags();
					if(tags.containsKey(type)) {
						if(tags.get(type).contains(value)) {
							ans.add(p);
						}
					}
				}
			}
		}
		else {
			String conj = (String) selbox.getValue().toString();
			String type1 = tag1field.getText();
			String value1 = tag2field.getText();
			String type2 = tag3field.getText();
			String value2 = tag4field.getText();
			
			if(conj.equals("OR")) {
				for(Album a : als) {
					for(Photo p: a.getPhotolist()) {
						HashMap<String,ArrayList<String>> tags = p.getTags();
						if(tags.containsKey(type1)) {
							if(tags.get(type1).contains(value1)) {
								ans.add(p);
								continue;
							}
						}
						if(tags.containsKey(type2)) {
							if(tags.get(type2).contains(value2)) {
								ans.add(p);
								continue;
							}
							
						}
					
					}
				}
				
				
			}
			if(conj.equals("AND")) {
				for(Album a : als) {
					for(Photo p: a.getPhotolist()) {
						HashMap<String,ArrayList<String>> tags = p.getTags();
						if(tags.containsKey(type1)&&tags.containsKey(type2)) {
							if(tags.get(type1).contains(value1)&&tags.get(type2).contains(value2)) {
								ans.add(p);
							}
						}

					
					}
				}
				
			}
			
		}
		results=ans;
		selbox.setValue(null);
		start(current);
	}
	
	/**
	 * Searchs the dates for all photos owned by the User and returns
	 * any found between the interval set by the user at search
	 */
	public void searchDates() {
		if(!(firstdatefield.getValue()!=null)) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Error");
			alert.setContentText("You left the first date blank!");
			alert.show();
			return;
		}
		if(!(seconddatefield.getValue()!=null)) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Error");
			alert.setContentText("You left the second date blank!");
			alert.show();
			return;
		}
		ArrayList<Photo> ans = new ArrayList<Photo>();
		Date date = Date.from(firstdatefield.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
		Calendar first = Calendar.getInstance();
		first.setTime(date);
		Date date2 = Date.from(seconddatefield.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
		Calendar second = Calendar.getInstance();
		second.setTime(date2);
		
		for(Album a : als) {
			for(Photo p: a.getPhotolist()) {
				Calendar temp = p.getMakedate();
				if(temp.before(second)&&temp.after(first)) {
					ans.add(p);
				}
			}
		}
		results=ans;
		selbox.setValue(null);
		start(current);
		
		
//		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//		sdf.format(date);
//		System.out.println(sdf.getCalendar().getTime());
		
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
	 * Returns the user to the previous screen
	 * @param ev The event of the mouse clicking on the button
	 */
	public void back(Event ev) {
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(getClass().getResource("/view/UserLogin.fxml"));
		AnchorPane root;
		try {
			root = (AnchorPane)loader.load();

			UserLoginController usercontroller = 
				loader.getController();
		usercontroller.start(current.getName());

		Scene scene = new Scene(root, 600, 500);
		((Node)ev.getSource()).getScene().getWindow().hide();
		stage.setScene(scene);
		
		stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Creates a new album containing the search results
	 * and adds it to the user
	 * @param e
	 */
	public void createAlbum(Event e) {
		Album added = new Album();
		added.setPhotolist(results);
		added.setName("Search Results");
		current.addAlbum(added);
		back(e);
	}
	
	

}
	