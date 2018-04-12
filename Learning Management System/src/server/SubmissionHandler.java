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
import java.util.ArrayList;

import dbManagers.AssignmentManager;
import dbManagers.CourseManager;
import dbManagers.SubmissionManager;

/**
 * handler responsible for submission/database related actions
 * @author louis rae
 * @version 1.0
 * @since April 11, 2018
 */
public class SubmissionHandler 
{
	/**
	 * objectinputstream to receive objects
	 */
	ObjectInputStream in = null;
	/**
	 * objectoutputstream to send objects
	 */
	ObjectOutputStream out = null;
		SubmissionHandler(ObjectInputStream in, ObjectOutputStream out)
		{
			this.in = in;
			this.out = out;
		}
	
	
		/**
		 * method that updates database with information receieved over the socket
		 * about the submission and also receives file over the socket to be saved on server machine
		 */
	public void uploadSub()
	{
		try
		{
			String assignName = (String)in.readObject();
			Course course = (Course)in.readObject();
			int studentID = (Integer)in.readObject();
			AssignmentManager a = new AssignmentManager();
			CourseManager c = new CourseManager();
			int courseID = c.findCourseID(course.name);
			int assignID = a.GETAssignID(assignName, courseID);
			SubmissionManager s = new SubmissionManager();
			String ex = (String)in.readObject();
			String title = (String)in.readObject();
			s.addItem(assignID, title, studentID);
			int subID = s.recentID();
			String path = recieveFile(Integer.toString(subID), ex);
			s.addPath(subID, path);
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * method allows prof to download the submission, receives the studentid and timestamp
	 * of the submission in order to retrieve path from database, we are under the assumption that
	 * this will work in identifying a unique submission since one student can only submit
	 * one thing at a time. Once the path is found the file is sent over the socket from the server machine
	 */
	public void downloadSub()
	{
		try
		{
			int studentID = Integer.parseInt((String)in.readObject());
			String date = (String)in.readObject();
			SubmissionManager s = new SubmissionManager();
			String path = s.getPath(studentID, date);
			sendFile(path);
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
	 * method that receives the course and assignment name then searches the database
	 * and sends an arraylist of all submissions associated with that assignment and course
	 * over the socket
	 */
	public void getSubs()
	{
		try
		{
			String assignName = (String)in.readObject();
			Course course = (Course)in.readObject();
			AssignmentManager a = new AssignmentManager();
			CourseManager c = new CourseManager();
			int courseID = c.findCourseID(course.name);
			int assignID = a.GETAssignID(assignName, courseID);
			SubmissionManager s = new SubmissionManager();
			ArrayList <Submissions> submissions = s.getSubmissions(assignID);
			out.writeObject(submissions);
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
			System.out.println("recieving file");
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
