package dbManagers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import server.Course;
/**
 * CourseManager is a sub class which extends Manager,
 * it is responsible for adding, checking, and updating
 * the CourseTable
 * @author Louis, Raemc
 * @version 1.0
 * @since April 2, 2018
 */
public class CourseManager extends Manager
{
	/**
	 * the table name
	 */
	private final String tableName = "coursetable";
	public CourseManager()
	{
		super();
	}
	/**
	 * add the below parameters to the table and generate an id
	 * @param prof
	 * @param name
	 * @param active
	 */
	public void addItem (int prof, String name, boolean active)
	{
		String sql = "INSERT IGNORE INTO " + tableName + "(prof_id, name, active)" +
				 " VALUES ('" + prof + "', '" + 
			 		name + "', '" + 
			 		active + "');";
	try{
		statement = connection.prepareStatement(sql);
		statement.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * update the value of the active bit at the given id
	 * @param value - either 1 or 0
	 * @param AssignID - id of the table
	 */
	public void changeActive(int value, int CourseID)
	{
		try 
		{ 		
			String sql = "UPDATE " + tableName
					+ " SET ACTIVE = " + value + " WHERE id=" + CourseID; 		
			statement = connection.prepareStatement(sql);
			statement.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	
	}
	/**
	 * return the prof_id from the table where the table id is the below parameter
	 * @param id
	 * @return
	 */
	public int findCourseProf(int id)
	{
		String sql = "SELECT prof_id FROM " + tableName + " WHERE ID=" + id;
		ResultSet course;
		int s = -1;
		try
		{
			statement = connection.prepareStatement(sql);
			course = statement.executeQuery();
			if(course.next())
			{
				s = course.getInt("prof_id");
			}
		} catch (SQLException e) { e.printStackTrace(); }
		return s;
	}
	/**
	 * return the course_id where the coursename is like the below paramter
	 * @param n
	 * @return
	 */
	public int findCourseID(String n)
	{
		String sql = "SELECT * FROM " + tableName + " WHERE name like" + "'" + n + "%'";
		ResultSet course;
		int s = -1;
		try
		{
			statement = connection.prepareStatement(sql);
			course = statement.executeQuery();
			if(course.next())
			{
				s = course.getInt("id");
			}
		} catch (SQLException e) { e.printStackTrace(); }
		return s;
	}
	/**
	 * return the course name from the course_id
	 * @param id
	 * @return
	 */
	public String findCourseName(int id)
	{
		String sql = "SELECT name FROM " + tableName + " WHERE ID=" + id;
		ResultSet course;
		String s = null;
		try
		{
			statement = connection.prepareStatement(sql);
			course = statement.executeQuery();
			if(course.next())
			{
				s = course.getString("name");
			}
		} catch (SQLException e) { e.printStackTrace(); }
		return s;
	}
	/**
	 * valid if the possible name is a coursename, if so return that name,
	 * otherwise return null
	 * @param Possiblename
	 * @return
	 */
	public String findCourseName(String Possiblename)
	{
		String sql = "SELECT * FROM " + tableName + " WHERE name like" + "'" + Possiblename + "%'";
		ResultSet course;
		String s = null;
		try
		{
			statement = connection.prepareStatement(sql);
			course = statement.executeQuery();
			if(course.next())
			{
				s = course.getString("name");
			}
		} catch (SQLException e) { e.printStackTrace(); }
		catch (NullPointerException e)
		{
			return null;
		}
		return s;
	}
	/**
	 * return if the course is active, based on id
	 * @param id
	 * @return
	 */
	public boolean findCourseActive(int id)
	{
		String sql = "SELECT active FROM " + tableName + " WHERE ID=" + id;
		ResultSet course;
		boolean s = false;
		try
		{
			statement = connection.prepareStatement(sql);
			course = statement.executeQuery();
			if(course.next())
			{
				s = course.getBoolean("active");
			}
		} catch (SQLException e) { e.printStackTrace(); }
		return s;
	}
	
	/**
	 * gets the Courses in the database with the User_ID
	 * @param User_ID
	 * @return - returns a course that is related to that course ID
	 */
	public ArrayList<Course> getUserCourses(int User_ID)
	{
		String sql = "SELECT * FROM " + tableName + " WHERE prof_id=" + "'" + User_ID + "'";
		ResultSet course;
		ArrayList<Course> courses = new ArrayList<Course>();
		try {
			statement = connection.prepareStatement(sql);
			course = statement.executeQuery();
			while(course.next())
			{
				courses.add(new Course(	//String name, boolean active, int prof_id
						course.getString("name"), 
						course.getBoolean("active"), 
						course.getInt("prof_id")));
			}
		} catch (SQLException e) {  }
		catch (NullPointerException e)
		{
			return null;
		}
		return courses;
	}
	/**
	 * returns courses with the coresponding list of course_IDs
	 * @param Course_ID
	 * @return
	 */
	public ArrayList<Course> getUserCourses(ArrayList<Integer> Course_ID)
	{
		ArrayList<Course> courses = new ArrayList<Course>();
		Iterator it = Course_ID.iterator();
		if (Course_ID.size() == 0)
		{
			return courses;
		}
		while (it.hasNext())
		{
			String sql = "SELECT * FROM " + tableName + " WHERE id=" + "'" + it.next() + "'";
			ResultSet course;

			try {
				statement = connection.prepareStatement(sql);
				course = statement.executeQuery();
				if (course.next())
				{	
					courses.add(new Course(	//String name, boolean active, int prof_id
					course.getString("name"), 
					course.getBoolean("active"), 
					course.getInt("prof_id")));
				}
			} catch (SQLException e) { 
				System.err.println("courseID may not be in courseTable");
			}
			catch (NullPointerException e)
			{
				return null;
			}
		}
		
		return courses;
	}	
}
 
