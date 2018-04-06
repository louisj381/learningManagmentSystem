package dbManagers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import server.Assignments;
import server.Student;

public class AssignmentManager extends Manager
{
	private final String tableName = "assignmenttable";
	public AssignmentManager()
	{
		super();
	}
	
	public void addPath(int assignID, String line)
	{
		try 
		{ 		
			System.out.println(line);
			String sql = ("UPDATE " + tableName
					+ " SET path = '" + line + "' WHERE ID = " + assignID + ";"); 		
			statement = connection.prepareStatement(sql);
			statement.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void addItem (int course, String title, boolean active)
	{
		String sql = "INSERT IGNORE INTO " + tableName + "(course_id, title, active)" +
				 " VALUES ('" + course + "', '" + 
			 		title + "', '" +  
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
	
	public int recentID()
	{
		
		String sql = "SELECT MAX(id) AS largest FROM " + tableName;
			ResultSet assignment;
			int s = -1;
			try
			{
				statement = connection.prepareStatement(sql);
				assignment = statement.executeQuery();
				if(assignment.next())
				{
					s = assignment.getInt("largest");
				}
			} catch (SQLException e) { e.printStackTrace(); }
			return s;
	}
	public ArrayList<Assignments> getAssignments(int Course_ID)
	{
		String sql = "SELECT * FROM " + tableName + " WHERE course_id like" + "'" + Course_ID + "%'";
		ResultSet assignmentData;
		ArrayList<Assignments> assignments = new ArrayList<Assignments>();
		try {
			statement = connection.prepareStatement(sql);
			assignmentData = statement.executeQuery();
			while(assignmentData.next())
			{
				assignments.add(new Assignments(
						null, 
						assignmentData.getString("path")));
			}
		} catch (SQLException e) {  }
		catch (NullPointerException e)
		{
			return null;
		}
		return assignments;
	}
}

