package Models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Rohan Joshi
 * @author Nicholas Cheniara
 */
public class User implements Serializable {
	
	/**
	 * The username of the user
	 */
	private String username;
	/**
	 * The albums that the user has
	 */
	private ArrayList<Album> albums;
	static final long serialVersionUID = 1L;
	
	/**
	 * No arg constructor intializes all fields
	 */
	public User() {
		username = "";
		albums = new ArrayList<Album>();
	}
	/**
	 * 2 Arg constructor to set the username and album list
	 * @param user the username to set
	 * @param al the list of albums to set
	 */
	public User(String user, ArrayList<Album> al) {
		username = user;
		albums = al;
	}
	/**
	 * Gets the name of the user
	 * @return String the username
	 */
	public String getName() {
		return username;
	}
	/**
	 * Gets the list of albums
	 * @return ArrayList(Album) the list of albums to return
	 */
	public ArrayList<Album> getAlbums(){
		return albums;
	}
	/**
	 * Sets the username of the user
	 * @param user the name to set
	 */
	public void setName(String user) {
		username = user;
	}
	/**
	 * Sets the list of albums
	 * @param al the list of albums to set
	 */
	public void setAlbums(ArrayList<Album> al){
		albums = al;
	}
	/**
	 * Adds an album to the lst
	 * @param al the album to add
	 */
	public void addAlbum(Album al) {
		albums.add(al);
	}
	

	
}
