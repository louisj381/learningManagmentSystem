package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import dbManagers.AssignmentManager;
import dbManagers.CourseManager;

/**
 * handler responsible for assignment/database related actions, including:
 * adding an assignment to database,
 * @author louis rae
 * @version 1.0
 * @since April 11, 2018
 */
public class AssignmentHandler 
{
	/**
	 * objectinputstream to receive objects
	 */
	ObjectInputStream in = null;
	/**
	 * objectoutputstream to send objects
	 */
	ObjectOutputStream out = null;
		AssignmentHandler(ObjectInputStream in, ObjectOutputStream out)
		{
			this.in = in;
			this.out = out;
		}
		
	/**
	 * method that updates database when a assignment is added
	 */
	public void addHandler()
	{
		try
		{
		String ex = (String)in.readObject();
		String title = (String)in.readObject();
		Course course = (Course)in.readObject();
		CourseManager c = new CourseManager();
		AssignmentManager a = new AssignmentManager();
		int courseid = c.findCourseID(course.name);
		a.addItem(courseid, title, false);
		int assignmentid = a.recentID();
		String path = recieveFile(Integer.toString(assignmentid), ex);
		a.addPath(assignmentid, path);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * method that updates the Jlist in assignment panel
	 * this is for the professor side, shows both active and inactive assignments 
	 */
	public void updateList()
	{
		try {
			System.out.println("in updatelist");
			String courseName = (String)in.readObject();
			CourseManager courseDB = new CourseManager();
			int CourseID = courseDB.findCourseID(courseName);
			AssignmentManager assignDB = new AssignmentManager();
			ArrayList<Assignments> assignments = assignDB.getAssignments(CourseID);
			//send the arraylist
			out.writeObject(assignments);
			
			
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * method that allows students to download the assignment they clicked on in the Jlist 
	 * on assignment panel
	 */
	public void downloadAssign()
	{
		try
		{
			String title = (String)in.readObject();
			Course course = (Course)in.readObject();
			AssignmentManager assignDB = new AssignmentManager();
			CourseManager cm = new CourseManager();
			int courseID = cm.findCourseID(course.name);
			int AssignID = assignDB.GETAssignID(title, courseID);
			String path = new String();
			path = assignDB.getPath(AssignID);
			sendFile(path);
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * method that updates the Jlist in assignment panel
	 * this is for the student side, shows only active assignments 
	 */
	public void updateActiveList()
	{
		try {
			String courseName = (String)in.readObject();
			CourseManager courseDB = new CourseManager();
			int CourseID = courseDB.findCourseID(courseName);
			AssignmentManager assignDB = new AssignmentManager();
			ArrayList<Assignments> assignments = assignDB.getActiveAssignments(CourseID);
			//send the arraylist
			out.writeObject(assignments);
			
			
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * method allows profs to change assignment from active to inactive
	 * or inactive to active 
	 */
	public void updateActive()
	{
		try {
			String title = (String)in.readObject();
			Course course = (Course)in.readObject();
			AssignmentManager assignDB = new AssignmentManager();
			CourseManager cm = new CourseManager();
			int courseID = cm.findCourseID(course.name);
			
			int AssignID = assignDB.GETAssignID(title, courseID);
			boolean active = assignDB.isActive(AssignID);
			 if (active)//assignment is active
			 {
				int choice = JOptionPane.showConfirmDialog(null, "This assignment is ACTIVE.\n Do you want to deactivate it?", "deactivate", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION)
				{
					assignDB.changeActive(0, AssignID);
				}
				else
				{
					return;
				}
				
			 }
			 else	//assignment is not active
			 {
				int choice = JOptionPane.showConfirmDialog(null, "This assignment is NOT ACTIVE.\n Do you want to activate it?", "activate", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION)
				{
					assignDB.changeActive(1, AssignID);
				}
				else
				{
					return;
				}
			 }
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * method that receives a path to the file then sends that file over the socket
	 */
	public void sendFile(String path)
	{
		File selectedFile = new File(path);
		long length = selectedFile.length();
		byte[] content = new byte[(int) length];
		try
		{
			FileInputStream fis = new FileInputStream(selectedFile);
			BufferedInputStream bos = new BufferedInputStream(fis);
			bos.read(content, 0, (int)length);
			out.writeObject(content);
			out.flush();
		}catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * method that receives a name and extension to save the file as
	 * then receives that file over the socket and saves it. This is for the
	 * server side, saves to a pre-specified place
	 */
	public String recieveFile (String name, String ex)
	{
		String STORAGEPATH = "C:\\" + File.separator + "Users\\" + File.separator + "louis\\" + File.separator + "Desktop\\" + File.separator + "serverComputer\\" + File.separator;
		String NAME = name;
		String EXTENSION = ex;
		String path = STORAGEPATH + NAME + EXTENSION;
		try
		{
			byte[] content = (byte[]) in.readObject();
			File newFile = new File(path);
			if(! newFile.exists())
			{
				newFile.createNewFile();
			}
			FileOutputStream writer = new FileOutputStream (newFile);
			BufferedOutputStream bos = new BufferedOutputStream(writer);
			bos.write(content);
			bos.close();
			return path;
		}catch(IOException e)
		{
			e.printStackTrace();
			return path;
		}		
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
			return path;
		}
	}
}
