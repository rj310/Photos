package Models;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import view.AdminController;

/**
 * @author Rohan Joshi
 * @author Nicholas Cheniara
 *
 */
public class Photo implements Serializable{
	/**
	 * The Image inside the photo
	 */
	transient Image image;
	/**
	 * The caption of the photo
	 */
	private String caption;
	/**
	 * The tags of the photo
	 */
	private HashMap<String,ArrayList<String>> tags = new HashMap<String,ArrayList<String>>();
	/**
	 * The list of tagtypes for the user to chose from
	 */
	public static ArrayList<String> tagtypes = new ArrayList<String>();
	/**
	 * Creation date of the photo
	 */
	private Calendar makedate;
	static final long serialVersionUID = 1L;
	
	/**
	 * No arg constructor, initializes all values
	 */
	public Photo() {
		caption = "";
	}
	

	
	/**
	 * Returns the image inside the photo
	 * @return Image the image of the photo
	 */
	public Image getImage() {
		return image;
	}
	/**
	 * Sets the image in the photo
	 * @param image the image to set
	 */
	public void setImage(Image image) {
		this.image = image;
	}
	/**
	 * Returns the caption inside the photo
	 * @return String the caption of the photo
	 */
	public String getCaption() {
		return caption;
	}
	/**
	 * Sets the caption in the photo
	 * @param caption the caption of the photo to be set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	/**
	 * Returns the creation date inside the photo
	 * @return Calendar the makedate of the photo
	 */
	public Calendar getMakedate() {
		return makedate;
	}
	/**
	 * Sets the creation date of photos added by the user
	 * @param fp The file chose by the user
	 */
	public void setMakedate(File fp) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		sdf.format(fp.lastModified());
		makedate = sdf.getCalendar();
		makedate.set(Calendar.MILLISECOND,0);
	}
	/**
	 * Deserializes Images specially
	 * @param ois
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        image = SwingFXUtils.toFXImage(ImageIO.read(ois), null);
    }
	
	/**
	 * Serializes Images specially
	 * @param oos
	 * @throws IOException
	 */
	private void writeObject(ObjectOutputStream oos) throws IOException {
	     oos.defaultWriteObject();
	     if(image != null){
	       	ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", oos);
	     }
	}
	/**
	 * Adds a tag to the tag list
	 * @param type The type of tag added
	 * @param value The value of tag added
	 * @return String Whether the tag was added or duplicate
	 */
	public String addTag(String type, String value) {
			if(tags.containsKey(type)) {
				if(tags.get(type).contains(value)) {
					return "duplicate";
				}
				else {
					tags.get(type).add(value);
					return "Added";
				}
			}
			else {
				ArrayList<String> temp1 = new ArrayList<String>();
				temp1.add(value);
				tags.put(type, temp1);
				if(AdminController.tagtypes!=null) {
					AdminController.tagtypes.add(type);
				}
				else {
					AdminController.tagtypes = new ArrayList<String>();
					AdminController.tagtypes.add(type);

				}
				return "Added";
			}
			
		
		
	}
	/**
	 * Deletes a tag from a photo
	 * @param type The type to be deleted
	 * @param value The value to be deleted
	 */
	public void deleteTag(String type, String value) {
		if(tags.containsKey(type)) {
			if(tags.get(type).size()==1) {
				tags.remove(type);
			}
			else {
				tags.get(type).remove(value);
			}
		}
	}
	/**
	 * Returns the list of tags
	 * @return HashMap(String,ArrayList(String)) the list of tags
	 */
	public HashMap<String,ArrayList<String>> getTags() {
		return tags;
	}



	/**
	 * Sets the list of tags
	 * @param tags the list of tags to set
	 */
	public void setTags(HashMap<String, ArrayList<String>> tags) {
		this.tags = tags;
	}



	/**
	 * Sets creation date of the photo
	 * @param makedate the creation date to sets
	 */
	public void setMakedate(Calendar makedate) {
		this.makedate = makedate;
	}
	


	
}
