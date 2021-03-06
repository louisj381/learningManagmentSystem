package frontEnd;
import javax.swing.*;

import server.Course;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Font;

/**
 * Panel which is on the CourseView Panel and displays options for the user,
 * such as assignments, dropbox, and Student, depending on the user
 * @author Louis, Raemc
 * @version 1.1
 * @since April 9, 2018
 */
public class SideViewPanel extends JPanel
{
	/**
	 * the activate/deactivate button for that course (prof only)
	 */
	JButton activation;
	/**
	 * the student button
	 */
	JButton btnStudent;
	/**
	 * the grade button
	 */
	JButton btnGrades;
	/**
	 * the dropbox button
	 */
	JButton btnDropbox;
	/**
	 * the assignment button
	 */
	JButton btnAssignments;
	/**
	 * the back button
	 */
	JButton btnBack;
	/**
	 * the course that is contained within the courseViewPanel
	 */
	Course course;
	/**
	 * the parent panel of this panel
	 */
	CourseViewPanel panel;
	/**
	 * Object IO used to send objects across socket
	 */
	ObjectOutputStream out = null;
	/**
	 * Object IO used to send objects across socket
	 */
	ObjectInputStream in = null;
	/**
	 * the main frame
	 */
	DashboardFrame theFrame;
	
	public SideViewPanel (CourseViewPanel p, ObjectOutputStream out, ObjectInputStream in, Course course, DashboardFrame theFrame)
	{
		this.theFrame = theFrame;
		panel = p;
		this.course = course;
		this.out = out;
		this.in = in;
		setBackground(new Color(0, 153, 153));
		setForeground(Color.CYAN);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		btnBack = new JButton("back");
		add(btnBack);
		
		Component verticalStrut = Box.createVerticalStrut(15);
		add(verticalStrut);
		
		JLabel courseName = new JLabel(course.name);
		courseName.setFont(new Font("Arial Black", Font.BOLD, 20));
		courseName.setForeground(new Color(255, 255, 255));
		add(courseName);
		
		if (course.getActive())	//if the course is active
		{
			activation = new JButton("Deactivate");
		}
		else
		{
			activation = new JButton("Activate");			
		}
		if (theFrame.user.getType() == 'P')
		{
			add(activation);
		}
		activation.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							course.toggleActive();   //flip bit to true or to false;
							System.out.println("course toggle: " + course.getActive());
							if (activation.getText().equals("Deactivate"))
							{
								activation.setText("Activate");
							}
							else
							{
								activation.setText("Deactivate");
							}
							try {
								out.writeObject("updateCourse");
								out.writeObject(course);
								out.writeObject(course.getActive());
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					});
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		verticalStrut_1.setBackground(new Color(51, 153, 153));
		add(verticalStrut_1);
		
		btnAssignments = new JButton("assignments");//	AssignmentListener(CourseViewPanel p, ObjectOutputStream out, ObjectInputStream in, Course c)

		btnAssignments.addActionListener(new AssignmentListener(panel, out, in, course, theFrame));
		add(btnAssignments);
		
		Component verticalStrut_2 = Box.createVerticalStrut(20);
		add(verticalStrut_2);
		
		btnDropbox = new JButton("dropbox");
		btnDropbox.addActionListener(new DropboxListener(panel, out, in, course, theFrame));
		add(btnDropbox);
		
		Component verticalStrut_3 = Box.createVerticalStrut(20);
		add(verticalStrut_3);
		if (theFrame.user.getType() == 'S')	//if the user is a student, display the grades button
		{
			btnGrades = new JButton("grades");
			add(btnGrades);
		}
		else	//the user is a prof
		{
			btnStudent = new JButton("student");
			add(btnStudent);
		}
		
	
	}
}
