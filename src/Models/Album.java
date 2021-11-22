package Models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author Rohan Joshi
 * @author Nicholas Cheniara
 *
 */
public class Album implements Serializable{
	/**
	 *  The name of the album
	 */
	private String name;
	/**
	 * The list of photos that the album contains
	 */
	private ArrayList<Photo> photolist;
	/**
	 * The date of the oldest photo in the album
	 */
	private Calendar oldest;
	/**
	 * Date of the newest photo in the album
	 */
	private Calendar newest;
	/**
	 * The date/time the album was created
	 */
	private Calendar makedate;
	static final long serialVersionUID = 1L;
	
	/**
	 * No arguments constructor initializing all values
	 */
	public Album() {
		name = "";
		photolist = new ArrayList<Photo>();
		makedate = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		sdf.format(makedate.getTime());
		makedate = sdf.getCalendar();
		makedate.set(Calendar.MILLISECOND,0);
		
	}
	/**
	 * Constructor using a new album name and list of photos
	 * 
	 * @param name The new name of the album
	 * @param pl The list of photos to set to the album
	 */
	public Album(String name, ArrayList<Photo> pl) {
		this.name = name;
		photolist = pl;
	}
	/**
	 * Gets the name of the album
	 * @return String
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets the Album name
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * returns the list of photos
	 * @return ArrayList(Photo) 
	 */
	public ArrayList<Photo> getPhotolist() {
		return photolist;
	}
	/**
	 * Sets the list of photos
	 * @param photolist the list of photos to set
	 */
	public void setPhotolist(ArrayList<Photo> photolist) {
		this.photolist = photolist;
	}
	/**
	 * Returns the date of the oldest photo in the album
	 * @return Calendar the date of the oldest photo
	 */
	public Calendar getOldest() {
		if(photolist.isEmpty()) {
			return null;
		}
		oldest = photolist.get(0).getMakedate();
		for(Photo p: photolist) {
			if(p.getMakedate().before(oldest)) {
				oldest = p.getMakedate();
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		sdf.format(oldest.getTime());
		oldest = sdf.getCalendar();
		oldest.set(Calendar.MILLISECOND,0);
		return oldest;
	}
	/**
	 * Returns the date of the newest photo in the album
	 * @return Calendar the date of the newest photo
	 */
	public Calendar getNewest() {
		if(photolist.isEmpty()) {
			return null;
		}
		newest = photolist.get(0).getMakedate();
		for(Photo p: photolist) {
			if(p.getMakedate().after(newest)) {
				newest = p.getMakedate();
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		sdf.format(newest.getTime());
		newest = sdf.getCalendar();
		newest.set(Calendar.MILLISECOND,0);
		return newest;
	}
	/**
	 * @param newest
	 */
	public void setNewest(Calendar newest) {
		this.newest = newest;
	}
	/**
	 * @return
	 */
	public Calendar getMakedate() {
		return makedate;
	}
	/**
	 * @param makedate
	 */
	public void setMakedate(Calendar makedate) {
		this.makedate = makedate;
	}
	/**
	 * Adds a photo to the album
	 * @param p the photo to be added
	 */
	public void addPhoto(Photo p) {
		photolist.add(p);
	}
	/**
	 * Removes a photo from the album
	 * @param p the photo to be removed
	 */
	public void removePhoto(Photo p) {
		photolist.remove(p);
	}
	
	
	
}
