package view;

import java.io.IOException;

import Models.Album;
import app.Photos;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
/**
* @author Rohan Joshi
* @author Nicholas Cheniara
*/
public class SlideShowController {
	@FXML Button prevbutton;
	@FXML Button nextbutton;
	@FXML Button logbutton;
	@FXML Button quitbutton;
	@FXML Button exitbutton;
	@FXML Pane displayPane;
	@FXML Label captionlabel;
	/**
	 * The current user of the applcation
	 */
	Album cur;
	/**
	 * This integer keeps track of the index of the 
	 * currently displayed photo in the slideshow
	 */
	int i=0;
	
	/**
	 * Starts the slideshow on the inputted album displaying
	 * the first picture in the photo list
	 * @param al The album to display in the slideshow
	 */
	public void start(Album al) {
		if(i>=al.getPhotolist().size()-1) {
			nextbutton.setVisible(false);
		}
		else {
			nextbutton.setVisible(true);
		}
		if(i<=0) {
			prevbutton.setVisible(false);
		}
		else {
			prevbutton.setVisible(true);
		}
		cur = al;
		
		exitbutton.setOnAction((event) -> {
			back(event);
        });
		nextbutton.setOnAction((event) -> {
			nextpic();
        });
		prevbutton.setOnAction((event) -> {
			prevpic();
        });
		logbutton.setOnAction((event) -> {
			logout(event);
        });
		quitbutton.setOnAction((event) -> {
			quit();
        });
		
		ImageView display = new ImageView();
		display.setPreserveRatio(true);
		display.setFitWidth(200);
		display.setFitHeight(250);
		
		displayPane.getChildren().clear();
		display.setImage(cur.getPhotolist().get(i).getImage());
		displayPane.getChildren().add(display);
		captionlabel.setText(cur.getPhotolist().get(i).getCaption());
		captionlabel.setMaxWidth(100);
		
	
		
		
		
		
	}
	/**
	 * Takes the slideshow to the next pictures
	 * by increasing the index
	 */
	public void nextpic() {
		i+=1;
		start(cur);
	}
	/**
	 * Takes the slideshow to the previous pictures
	 * by decreasing the index
	 */
	public void prevpic() {
		i-=1;
		start(cur);
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
		loader.setLocation(getClass().getResource("/view/UserResults.fxml"));
		AnchorPane root;
		try {
			root = (AnchorPane)loader.load();

			UserResultsController userresultscontroller = 
				loader.getController();
		userresultscontroller.start(cur);

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
