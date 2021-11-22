package view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import Models.Album;
import Models.Photo;
import Models.User;
import app.Photos;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @author Rohan Joshi
 * @author Nicholas Cheniara
 *
 */
public class UserResultsController {
	
	@FXML TilePane resultPane;
	@FXML ScrollPane scrollpane;
	@FXML Button addbutton;
	@FXML Button removebutton;
	@FXML Button captionbutton;
	@FXML Button edittagsbutton;
	@FXML Button addtagsbutton;
	@FXML Button removetagsbutton;
	@FXML Pane bigimagePane;
	@FXML Button quitbutton;
	@FXML Button logbutton;
	@FXML Button backbutton;
	@FXML Label bigcaption;
	@FXML TextField newcapfield;
	@FXML ListView<String> taglistview;
	@FXML TextField newtagfield;
	@FXML ComboBox<String> tagdropbox;
	@FXML TextField qualifiednewtagfield;
	@FXML Button slidebutton;
	@FXML Label timelabel;
	@FXML Button movebutton;
	@FXML Button copybutton;
	@FXML ComboBox<String> albumselectbox;
	
	/**
	 * The observable list made to hold the list of tags for a photo
	 * and display them in a list view
	 */
	ObservableList<String> obsList = FXCollections.observableArrayList();
	/**
	 * The observable list made to hold the list of tag types for a photo
	 * and display them in a combobox
	 */
	ObservableList<String> typelist = FXCollections.observableArrayList();
	/**
	 * The observable list made to hold the list of albums for a user
	 * and display them in a combobox
	 */
	ObservableList<String> albumlist = FXCollections.observableArrayList();
	/**
	 * The observable list made to hold the list of tag types for a photo
	 * and display them in a combobox
	 */
	ObservableList<String> tagtypelist = FXCollections.observableArrayList();
    /**
     * The currently displayed album
     */
    Album cur;
    /**
     * The current user of the application
     */
    User current = LoginController.cur;
    /**
     * The currently selected image
     */
    ImageView curImage;
    /**
     * The index of the currently selected image
     */
    int currentImageViewIndex;
    
	/**
	 * First, clears all user input fields and adds listeners to relevant buttons.
	 * Then, sets relevant list views and comboboxes. Finally, adds the photos 
	 * in the inputted album to the display area and adds listeners to each photo, 
	 * used for editing tags, captions, and displaying the in large display area
	 * @param al The inputted album to show
	 */
	public void start(Album al) {
		albumselectbox.getItems().clear();
		tagdropbox.getItems().clear();
		cur = al;
		ArrayList<Photo> photos = new ArrayList<Photo>();
		photos = al.getPhotolist();
		addbutton.setOnAction((event) -> {
			addphoto();
        });
		quitbutton.setOnAction((event) -> {
			quit();
        });
		logbutton.setOnAction((event) -> {
			logout(event);
        });
		backbutton.setOnAction((event) -> {
			back(event);
        });
		slidebutton.setOnAction((event) -> {
			if(cur.getPhotolist().isEmpty()) {
				return;
			}
			slide(al,event);
        });
		for(Album a: current.getAlbums()) {
			if(a.getName().equals(al.getName())) {
				continue;
			}
			albumlist.add(a.getName());
		}
		albumselectbox.setItems(albumlist);
		
		for(String s : Photo.tagtypes) {
			tagtypelist.add(s);
		}
		tagdropbox.setItems(tagtypelist);
		
		
		if(photos.isEmpty()){
			return;
		}
	
		
		
		ArrayList<ImageView> images = new ArrayList<ImageView>();
		
		for(int i=0;i<photos.size();i++) {
			images.add(new ImageView(photos.get(i).getImage()));
		}
		
		 
		scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Horizontal
		scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll bar
		scrollpane.setFitToWidth(true);
		scrollpane.setContent(resultPane);
		resultPane.setPadding(new Insets(15,15,15,15));
		resultPane.setHgap(15);
		resultPane.setVgap(40);
		 
		resultPane.getChildren().clear();
		
		
		for(int i=0;i<images.size();i++) {
			ImageView current = images.get(i);
			int curindex = i;
			 images.get(i).setFitHeight(75);
			 images.get(i).setFitWidth(100);
			 images.get(i).setPreserveRatio(true);
			 images.get(i).setOnMouseClicked(new EventHandler<MouseEvent>(){
			     
					@Override
					public void handle(MouseEvent event) {
						// TODO Auto-generated method stub
						
						if(event.getClickCount() == 2){
							Album temp = searchForAlbum(al.getName());
							temp.getPhotolist().remove(curindex);
							LoginController.cur.getAlbums().remove(al);
							LoginController.cur.getAlbums().add(temp);
							resultPane.getChildren().clear();
							bigimagePane.getChildren().clear();
							bigcaption.setText(null);
							timelabel.setText(null);
							taglistview.getItems().clear();
							start(temp);
						}
						
						if(event.getClickCount()==1) {
							curImage = current;
							obsList.clear();
							Album tempal = searchForAlbum(al.getName());
							ImageView temp = new ImageView(current.getImage());
							temp.setFitHeight(200);
							temp.setFitWidth(200);
							temp.setPreserveRatio(true);
							bigimagePane.getChildren().clear();
							bigimagePane.getChildren().add(temp);
							bigcaption.setText("Caption: "+tempal.getPhotolist().get(curindex).getCaption());
							timelabel.setText("Date Taken: "+tempal.getPhotolist().get(curindex).getMakedate().getTime().toString());
							HashMap<String,ArrayList<String>> temptags = tempal.getPhotolist().get(curindex).getTags();

							if(temptags!=null) {
								for(String key: temptags.keySet()) {
									ArrayList<String> list = temptags.get(key);
									for(String s : list) {
										String fulltag = key+":"+s;
										obsList.add(fulltag);
									}
			
									
								}
								taglistview.setItems(obsList);
								taglistview.refresh();
							}
							
							
							
							captionbutton.setOnAction((event2) -> {
								caption(curindex);
					        });
							addtagsbutton.setOnAction((event3) -> {
								addtagtophoto(curindex);
					        });
							removetagsbutton.setOnAction((event3) -> {
								deletetag(curindex);
					        });
							copybutton.setOnAction((event4) -> {
								if(tagdropbox.getValue()!=null) {
									copy(curindex);
								}
								
								else {
									Alert alert = new Alert(AlertType.ERROR);
									alert.setHeaderText("Error");
									alert.setContentText("Select an album to copy to!");
									alert.show();
								}
					        });
							movebutton.setOnAction((event5) -> {
								if(tagdropbox.getValue()!=null) {
									move(curindex);
								}
								else {
									Alert alert = new Alert(AlertType.ERROR);
									alert.setHeaderText("Error");
									alert.setContentText("Select an album to copy to!");
									alert.show();
								}
								
					        });
							
						}
					 
					 
					}});
		}
		
		
		for (int i = 0; i < images.size(); i++){
			TilePane temp = new TilePane();
			Label caption = new Label();
			caption.setText(photos.get(i).getCaption());
			//resultPane.getChildren().add(images.get(i));
			temp.setMaxSize(100, 100);
			caption.setMaxWidth(100);
			temp.getChildren().addAll(images.get(i),caption);
			resultPane.getChildren().add(temp);
		 }
		
		
	}
	
	
	
	/**
	 * Adds a photo to the album
	 * by opening a file chooser for the user,
	 * filtering possible extensions
	 * and passing the selected file to addPhotoFromFile
	 */
	public void addphoto() {
		Stage stage = new Stage();
		stage.setTitle("File Chooser Sample");
	 
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("View Pictures");
        fileChooser.setInitialDirectory(
            new File(System.getProperty("user.home"))
        );                 
        fileChooser.getExtensionFilters().addAll(
        	  new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpeg", "*.gif","*.bmp")
        );
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            addPhotoFromFile(file);
        }
        start(cur);
	}
	
	
	/**
	 * Take the user selected file, converts it to 
	 * a buffered image and then a javafx image, and then adds
	 * it to the album, and starts the display again
	 * @param file The user selected file containing the photo to be added
	 */
	public void addPhotoFromFile(File file) {
        try {
        	
            Photo newPhoto = new Photo();
            BufferedImage bi = ImageIO.read(file);
            Image image = SwingFXUtils.toFXImage(bi, null);
//            if(checkDuplicateImage(image)) {
//            	Alert alert = new Alert(AlertType.ERROR);
//            	alert.setTitle("Error");
//            	alert.setHeaderText("Duplicate Image!");
//            	alert.setContentText("Please try again or try a different file.");
//            	alert.showAndWait();
//            	return;
//            }
//            else {
//            	newPhoto.setImage(image);
//                newPhoto.setMakedate(file);
//                cur.addPhoto(newPhoto);
//            }
            newPhoto.setImage(image);
            newPhoto.setMakedate(file);
            cur.addPhoto(newPhoto);
 
        } catch (IOException ex) {
        	
        	Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Error");
        	alert.setHeaderText("Could not open specified file.");
        	alert.setContentText("Please try again or try a different file.");

        	alert.showAndWait();
        }
    }
	
	/**
	 * Searches for and gets a specific album based on the name of the album
	 * @param name The name of the album to find
	 * @return Album the album with the inputted name if it exists
	 */
	public Album searchForAlbum(String name){
		User currentUser = LoginController.cur;
		for(int i = 0; i < currentUser.getAlbums().size(); i++){
			if(currentUser.getAlbums().get(i).getName().equals(name)){
				return currentUser.getAlbums().get(i);
			}
		}
		return null;
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
		usercontroller.start(LoginController.cur.getName());

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
	 * Changes or adds a user inputed caption
	 * to the currently selected photo based on the
	 * index of the currently selected photo in the photo list
	 * @param index The index of the currently selected photo
	 */
	public void caption(int index) {
		cur.getPhotolist().get(index).setCaption(newcapfield.getText());
		resultPane.getChildren().clear();
		bigcaption.setText(newcapfield.getText());
		if(newcapfield.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Warning");
			alert.setContentText("You entered a blank caption! Make sure you want this.");
			alert.show();
		}
		newcapfield.clear();

		
		start(cur);
	}
	/**
	 * Adds a tag to the currently selected photo
	 * based on the index of the photo and the values
	 * entered by the user in the new tag fields
	 * @param index The index in the photo list of the current image
	 */
	public void addtagtophoto(int index) {
		if(!tagdropbox.getSelectionModel().isEmpty()) {
			if(qualifiednewtagfield.getText().trim().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Error");
				alert.setContentText("You didnt enter a value!");
				alert.show();
				return;
			}
			String input = qualifiednewtagfield.getText();
			qualifiednewtagfield.clear();
			String result = cur.getPhotolist().get(index).addTag(tagdropbox.getValue(), input);
			if(result.equals("Added")) {
				obsList.add(tagdropbox.getValue()+":"+input);
				taglistview.setItems(obsList);
				taglistview.refresh();
				tagdropbox.setValue(null);
				newtagfield.clear();
				return;
			}
		}
		
		
		String input = newtagfield.getText();
		newtagfield.clear();
		String[] split = input.split(":");
		if(split.length!=2) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Error");
			alert.setContentText("Invalid Format!");
			alert.show();
			return;
		}
		String result = cur.getPhotolist().get(index).addTag(split[0], split[1]);
		if(result.equals("Added")) {
			tagtypelist.add(split[0]);
			tagdropbox.setItems(tagtypelist);
			obsList.add(input);
			taglistview.setItems(obsList);
			taglistview.refresh();
		}
		if(result.equals("duplicate")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Error");
			alert.setContentText("Duplicate tag!");
			alert.show();
			return;
		}
	}
	
	/**
	 * Delete a tag from the currently selected photo
	 * based on the index of the current photo
	 * @param index The index in the photo list of the current image
	 */
	public void deletetag(int index) {
		if(taglistview.getSelectionModel().getSelectedItem() != null) {
			String todelete = taglistview.getSelectionModel().getSelectedItem();
			int delindex = taglistview.getSelectionModel().getSelectedIndex();
			String[] split = todelete.split(":");
			cur.getPhotolist().get(index).deleteTag(split[0], split[1]);
			obsList.remove(delindex);
			taglistview.setItems(obsList);
			taglistview.refresh();
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Error");
			alert.setContentText("You didnt select a tag to delete!");
			alert.show();
			return;
		}
		
		
		
	}
	/**
	 * Sends the user to the slide show page of the currently
	 * displayed album using the SlideShowController
	 * @param al The album to use in the slide show
	 * @param ev The event of the mouse being clicked on the slideshow button
	 */
	public void slide(Album al, Event ev) {
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(getClass().getResource("/view/slildeshow.fxml"));
		AnchorPane root;
		try {
			root = (AnchorPane)loader.load();

			SlideShowController slidecontroller = 
				loader.getController();
			slidecontroller.start(al);

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
	 * Copies a photo to another album based on the 
	 * album selected by the user in the combobox
	 * @param curindex The index in the photo list of the photo to copy
	 */
	public void copy(int curindex) {
		Album temp = searchForAlbum(cur.getName());
		Photo selected = temp.getPhotolist().get(curindex);
		Album selectedal = searchForAlbum(albumselectbox.getValue());
		selectedal.addPhoto(selected);
		
	}
	/**
	 * Moves a photo to another album based on the 
	 * album selected by the user in the combobox
	 * @param curindex The index in the photo list of the photo to move
	 */
	public void move(int curindex) {
		Album temp = searchForAlbum(cur.getName());
		Photo selected = temp.getPhotolist().get(curindex);
		Album selectedal = searchForAlbum(albumselectbox.getValue());
		selectedal.addPhoto(selected);
		temp.getPhotolist().remove(curindex);
		resultPane.getChildren().clear();
		bigimagePane.getChildren().clear();
		bigcaption.setText(null);
		timelabel.setText(null);
		taglistview.getItems().clear();
		start(cur);
	}
	
	/**
	 * Checks if 2 images are equivalent to prevent
	 * duplicate photos being added
	 * @param check The image to check as a duplicate
	 * @return Boolean whether or not the photo is a duplicate
	 */
	public boolean checkDuplicateImage(Image check) {
		 ArrayList<Photo> temp = cur.getPhotolist();
         for(Photo p: temp) {
         	for (int i = 0; i < check.getWidth(); i++)
             {
               for (int j = 0; j < check.getHeight(); j++)
               {
                 if(!check.getPixelReader().getColor(i, j).equals(p.getImage().getPixelReader().getColor(i, j))) {
                 	return false;
                 }
               }
             }
         }
         return true;
	}
	

}
