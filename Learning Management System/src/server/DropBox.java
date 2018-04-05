package server;
import java.util.ArrayList;

/**
 * This class represents a table in the database, each assignment 
 * the professor uploads will have a specific table. The table holds all the information
 * about a submission from each student
 */
public class DropBox
{
	/**
	 * The field to keep track of submissions for that assignment
	 * is an ArrayList so the number of submissions can vary from assignment to assignment
	 */
	ArrayList <Submissions> submissions;
	
	public DropBox()
	{
		submissions = new <Submissions> ArrayList();
	}

}