package com.example.pocketadvisor2;

/**
 * ToDo.java
 * Tegu on klassiga mis sisaldab endas ToDo isendimuutujaid ja meetodeid
 * @version 0.1
 * @author  Urmas Hoogma
 * @since   1.5
 */

public class ToDo {
	/** ToDo identifikaator */
	private int id;
	
	/** ToDo kuupa''ev */
	private String date;
	
	/** ToDo tiitel */
	private String title;
	
	/** ToDo kirjeldus */
	private String description;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/** Klassi ToDo konstruktor
    * @param id ToDo id kasutatakse konkreetse meelespea valimiseks 
    * listist
    * @param date ToDo kuupa''ev
    * @param title ToDo tiitel
    * @param description ToDo kirjeldus
    */
	public ToDo(int id, String date, String title, String description) {
		this.id = id;
		this.date = date;
		this.title = title;
		this.description = description;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
}
