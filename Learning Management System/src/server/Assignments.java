package server;

import java.io.Serializable;

/**
 * This is a class to represent all the data for a assignment that the professor  creates 
 * or the student can view
 * @author louis rae
 * @version 1.0
 * @since April 11, 2018
 *
 */
public class Assignments implements Serializable
{
	/**
	 * This represents a table in the database
	 * The table holds all the information
	 * about a submission from each student
	 */
	DropBox dropbox;
	/**
	 * This field represents the path where the assignment is saved. When a assignment is 
	 * uploaded it is sent to the server's machine, there it will be downloaded onto the server
	 * machine. The path keeps track of where it is downloaded, so it can be retrieved when a
	 * student requests to view that assignment the server can send that file to the student.
	 */
	String path;
	/**
	 * the title of the assignment
	 */
	String title;
	/**
	 * the id of the assignment
	 */
	int ID;
	
	public Assignments(DropBox d, String p, String title)
	{
		this.title = title;
		dropbox = d;
		path = p;
	}
	
	/**
	 * method that returns assignment title
	 */
	@Override
	public String toString()
	{
		return title;
	}
}
