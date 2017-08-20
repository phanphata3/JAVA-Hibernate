package main;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataListener;

import org.hibernate.Query;
import org.hibernate.Session;

import connection.ConnectionUtils;
import POJO.*;
import Utilities.HibernateUtil;

public class Bai3 extends JFrame{

	protected static final int VK_TAB = 0;
	private DefaultListModel<String> saveListLecture, saveListStudent;
	private JList<String> jlist1, jlist2;
	private JButton show, add;
	private JButton logout, login;
	Vector<MyClass> saveMyClassLecture = new Vector<>();
	Vector<MyClass> saveMyClassStudent = new Vector<>();
	int count = 1;
	
	public Bai3()
	{
		super("Management Student");
		setPreferredSize(new Dimension(600, 400));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		saveListLecture = new DefaultListModel<>();
		saveListStudent = new DefaultListModel<>();
		
		JTabbedPane jpane = new JTabbedPane();
	
		// Start of program
		JPanel jp1 = notLogin(jpane, 0);	
		jpane.addTab("Lectures", null, jp1);
		
		JPanel jp2 = notLogin(jpane, 1);
		jpane.addTab("Students", null, jp2);
		
		add(jpane, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Bai3 b3 = new Bai3();
		
	}

	public JPanel notLogin(JTabbedPane jpane, int tab)
	{
		JPanel jp = new JPanel(new BorderLayout());
		
		if (tab == 0)
		{
			saveListLecture.removeAllElements();
			jlist1 = new JList<>(saveListLecture);
			
			jp.add(jlist1, BorderLayout.CENTER);
		}
		else
		{
			saveListStudent.removeAllElements();
			jlist2 = new JList<>(saveListStudent);
			
			jp.add(jlist2, BorderLayout.CENTER);
		}
		
		
		
		login = new JButton("Login");
		login.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				int id = 0;
				String password = "";
				JTextField jt1, jt2;
				
				
				JPanel jdiag = new JPanel(new GridLayout(4, 1));
				jdiag.add(new JLabel("ID : "));
				jt1 = new JTextField();
				jdiag.add(jt1);
				
				jdiag.add(new JLabel("Pass : "));
				jt2 = new JPasswordField();
				jdiag.add(jt2);
				
				int res = JOptionPane.showConfirmDialog(null, jdiag, "Login", JOptionPane.OK_CANCEL_OPTION);
				if (res == JOptionPane.OK_OPTION)
				{
					id = Integer.parseInt(jt1.getText());
					password = jt2.getText();
					
					if (jpane.getSelectedIndex() == 0)
					{
						jlist1 = new JList<>(saveListLecture);
						ConnectionUtils con = new ConnectionUtils();
						
						try {
							
							con.setLoginData("MyLecture", id, password);
							if (con.getLoginResult() == 1)
							{								
								jpane.setTitleAt(0, "Lecture : " +  con.getLoginName());
								jpane.setComponentAt(0, lectureAlreadyLogin(jpane, id));
							}
							
						} catch (ClassNotFoundException | SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else
					{
						jlist2 = new JList<>(saveListStudent);
						ConnectionUtils con = new ConnectionUtils();
						
						try {
							con.setLoginData("MyStudent", id, password);
							if (con.getLoginResult() == 1)
							{
								jpane.setTitleAt(1,"Student : " +  con.getLoginName());
								jpane.setComponentAt(1, studentAlreadyLogin(jpane, id));
							}
								
						} catch (ClassNotFoundException | SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				
			}
		});
		
		jp.add(login, BorderLayout.SOUTH);
		
		return jp;
	}

	public JPanel lectureAlreadyLogin(JTabbedPane jpane, int lectureId) throws ClassNotFoundException, SQLException
	{
		JPanel jp = new JPanel(new BorderLayout());
		saveListLecture.removeAllElements();
		jlist1 = new JList<>(saveListLecture);
		jlist1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane jscr = new JScrollPane(jlist1);
		
		ConnectionUtils con = new ConnectionUtils();
		con.getListClassForLecture(lectureId, saveMyClassLecture);
		
		for (int i = 0; i < saveMyClassLecture.size(); i++)
		{
			String add = "Class ID : " + saveMyClassLecture.get(i).getID() + " - " + saveMyClassLecture.get(i).getNAME();
			saveListLecture.addElement(add);
		}
		
		
		// Panel button 1
		JPanel jbut1 = new JPanel();
		JButton del = new JButton("Remove class");
		del.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				deleteClass(lectureId, saveMyClassLecture.get(jlist1.getSelectedIndex()).getID());
	//			saveListLecture.remove(jlist1.getSelectedIndex());
	//			saveMyClassLecture.removeElementAt(jlist1.getSelectedIndex());
				
				saveListLecture.removeAllElements();
				saveMyClassLecture.removeAllElements();
				
				ConnectionUtils con = new ConnectionUtils();
				try
				{
					con.getListClassForLecture(lectureId, saveMyClassLecture);
					for (int i = 0; i < saveMyClassLecture.size(); i++)
					{
						String add = "Class ID : " + saveMyClassLecture.get(i).getID() + " - " + saveMyClassLecture.get(i).getNAME();
						saveListLecture.addElement(add);
					}	
				}
				catch (Exception ex)
				{
					
				}
			}
		});
		
		
		show = new JButton("Show Class");
		show.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				int pos = jlist1.getSelectedIndex();
				ShowClass showCl = new ShowClass(saveMyClassLecture.get(pos), jpane.getSelectedIndex(), lectureId);
				showCl.setVisible(true);
			}
		});
		
		add = new JButton("Add Class");
		add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				try {
					buttonAddClass(lectureId);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		JButton changePasss = new JButton("Change password");
		changePasss.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ConnectionUtils con = new ConnectionUtils();
				con.changeLecturePassword(lectureId);
					
			}
		});
		
		logout = new JButton("Logout");
		logout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				saveMyClassLecture.removeAllElements();
				saveListLecture.removeAllElements();
				jpane.setTitleAt(0, "Lectures");
				jpane.setComponentAt(0, notLogin(jpane, 0));
			}
		});
		
		jbut1.add(del);
		jbut1.add(show);
		jbut1.add(add);
		jbut1.add(changePasss);
		jbut1.add(logout);
			
		jp.add(jscr, BorderLayout.CENTER);
		jp.add(jbut1, BorderLayout.SOUTH);
		
		return jp;
	}

	public JPanel studentAlreadyLogin(JTabbedPane jpane, int studentID) throws ClassNotFoundException, SQLException
	{
		JPanel jp = new JPanel(new BorderLayout());
		
		saveListStudent.removeAllElements();
		jlist2 = new JList<>(saveListStudent);
		jlist2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane jscr = new JScrollPane(jlist2);
		
		ConnectionUtils con = new ConnectionUtils();
		con.getListClassForStudent(studentID, saveMyClassStudent);
		
		for (int i = 0; i < saveMyClassStudent.size(); i++)
		{

			String add = "Class ID : " + saveMyClassStudent.get(i).getID() + " - " + saveMyClassStudent.get(i).getNAME();
			saveListStudent.addElement(add);
		}
		
		JPanel jbut = new JPanel();
		show = new JButton("Show Class");
		show.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				int pos = jlist2.getSelectedIndex();
				
				ShowClass showCl = new ShowClass(saveMyClassStudent.get(pos), jpane.getSelectedIndex(), studentID);
				showCl.setVisible(true);
			}
		});
		
		JButton changePasss = new JButton("Change password");
		changePasss.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ConnectionUtils con = new ConnectionUtils();
				con.changeStudentPassword(studentID);
			}
		});
		
		logout = new JButton("Logout");
		logout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				saveListStudent.removeAllElements();
				saveMyClassStudent.removeAllElements();
				jpane.setTitleAt(1, "Students");
				jpane.setComponentAt(1, notLogin(jpane, 1));
			}
		});
		
		jbut.add(show);
		jbut.add(changePasss);
		jbut.add(logout);
		
		jp.add(jscr, BorderLayout.CENTER);
		jp.add(jbut, BorderLayout.SOUTH);
		
		return jp;
	}

	public JPanel createClass(JTextField ID, JTextField Name, JTextField Room
			, JComboBox<Integer> dayBegin, JComboBox<Integer> monthBegin, JComboBox<Integer> yearBegin
			, JComboBox<Integer> dayEnd, JComboBox<Integer> monthEnd, JComboBox<Integer> yearEnd
			, JComboBox<Integer> hourBegin, JComboBox<Integer> minuteBegin
			, JComboBox<Integer> hourEnd, JComboBox<Integer> minuteEnd
			, JComboBox<String> dayMonth)
	{
		JPanel jp = new JPanel(new GridLayout(0, 1));
		jp.add(new JLabel("Class ID"));
		jp.add(ID);
		
		jp.add(new JLabel("Class Name"));
		jp.add(Name);
		
		jp.add(new JLabel("Class Room"));
		jp.add(Room);
		
		jp.add(new JLabel("Date Begin (Year - Month - Day)"));
		JPanel dateBegin = new JPanel();
		dateBegin.add(yearBegin);
		dateBegin.add(monthBegin);
		dateBegin.add(dayBegin);
		jp.add(dateBegin);
		
	//	jp.add(new JLabel("Date End (Year - Month - Day)"));
	//	JPanel dateEnd = new JPanel();
	//	dateEnd.add(yearEnd);
	//	dateEnd.add(monthEnd);
	//	dateEnd.add(dayEnd);
	//	jp.add(dateEnd);
		
		jp.add(new JLabel("Time Begin (Hour : Minute)"));
		JPanel timeBegin = new JPanel();
		timeBegin.add(hourBegin);
		timeBegin.add(minuteBegin);
		jp.add(timeBegin);
		
		jp.add(new JLabel("Time End (Hour : Minute)"));
		JPanel timeEnd = new JPanel();
		timeEnd.add(hourEnd);
		timeEnd.add(minuteEnd);
		jp.add(timeEnd);
		
		jp.add(new JLabel("Day in Month"));
		jp.add(dayMonth);
		
		return jp;
	}
	
	public void buttonAddClass(int lectureId) throws ParseException
	{
		JTextField id = new JTextField();
		JTextField Name = new JTextField();
		JTextField Room = new JTextField();
		
		Vector<Integer> day = new Vector<>();
		Vector<Integer> month = new Vector<>();
		Vector<Integer> year = new Vector<>();
		Vector<Integer> hour = new Vector<>();
		Vector<Integer> minute = new Vector<>();
		
		Calendar cal = Calendar.getInstance();
		for (int i = 1; i <= 31; i++)
		{
			if (i <= 12)
			{
				month.addElement(i);
			}
			
			if (i <= 31)
			{
				day.addElement(i);
			}
			
			year.addElement(cal.get(Calendar.YEAR) + i - 1);
		}
		
		for (int i = 0; i < 60; i++)
		{
			if (i <= 23)
				hour.addElement(i);
			minute.addElement(i);
			
		}
		
		JComboBox<Integer> dayBegin = new JComboBox<>(day);
		JComboBox<Integer> monthBegin = new JComboBox<>(month);
		JComboBox<Integer> yearBegin = new JComboBox<>(year);

		JComboBox<Integer> dayEnd = new JComboBox<>(day);
		JComboBox<Integer> monthEnd = new JComboBox<>(month);
		JComboBox<Integer> yearEnd = new JComboBox<>(year);
		
		JComboBox<Integer> hourBegin = new JComboBox<>(hour);
		JComboBox<Integer> minuteBegin = new JComboBox<>(minute);
		JComboBox<Integer> hourEnd = new JComboBox<>(hour);
		JComboBox<Integer> minuteEnd = new JComboBox<>(minute);
		
	
		Vector<String> dayMonth = new Vector<String>();
		dayMonth.addElement("Monday");
		dayMonth.addElement("Tuesday");
		dayMonth.addElement("Wednesday");
		dayMonth.addElement("Thursday");
		dayMonth.addElement("Friday");
		dayMonth.addElement("Saturday");
		dayMonth.addElement("Sunday");
		
		JComboBox<String> dayinMonth = new JComboBox<String>(dayMonth);
		
		JPanel result = createClass(id, Name, Room, dayBegin, monthBegin, yearBegin, dayEnd, monthEnd, yearEnd, hourBegin, minuteBegin, hourEnd, minuteEnd, dayinMonth);
		int flag = JOptionPane.showConfirmDialog(null, result, "Create Class", JOptionPane.OK_CANCEL_OPTION);
		
		if (flag == JOptionPane.OK_OPTION)
		{
			MyClass ml = new MyClass();

			String strId, strName, strRoom, strDateBegin, strDateEnd, strTimeBegin, strTimeEnd, strDayMonth;
			int strID = 0;
			
			if ((!id.getText().equals("")) && (!Name.getText().equals("")) && (!Room.getText().equals("")))
			{
				strID = Integer.parseInt(id.getText());
				strName = Name.getText();
				strRoom = Room.getText();
				
				String proc1 = "" + monthBegin.getSelectedItem();
				if (proc1.length() == 1)
					proc1 = "0" + proc1;
				String proc2 = "" + dayBegin.getSelectedItem();
				if (proc2.length() == 1)
					proc2 = "0" + proc2;
				strDateBegin = yearBegin.getSelectedItem() + "-" + proc1 + "-" + proc2;
				
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date dBegin = format.parse(strDateBegin);
				
				Date dEnd = format.parse(strDateBegin);
				Calendar calEnd = Calendar.getInstance();
				calEnd.setTime(dEnd);	
				calEnd.add(Calendar.DAY_OF_YEAR, 105);
				dEnd = calEnd.getTime();
						
				proc1 = "" + hourBegin.getSelectedItem();
				proc2 = "" + minuteBegin.getSelectedItem();
				Time tBegin = new Time(Integer.parseInt(proc1), Integer.parseInt(proc2), 0);
				if (proc1.length() == 1)
				{
					proc1 = "0" + proc1;
				}
				if (proc2.length() == 1)
				{
					proc2 = "0" + proc2;
				}
				
				
				strTimeBegin = proc1 + ":" + proc2 + ":00";
				
				proc1 = "" + hourEnd.getSelectedItem();
				proc2 = "" + minuteEnd.getSelectedItem();
				Time tEnd = new Time(Integer.parseInt(proc1), Integer.parseInt(proc2), 0);
				if (proc1.length() == 1)
				{
					proc1 = "0" + proc1;
				}
				if (proc2.length() == 1)
				{
					proc2 = "0" + proc2;
				}
				
				strTimeEnd = proc1 + ":" + proc2 + ":00";
				
				if (strTimeBegin.compareTo(strTimeEnd) >= 0)
				{
					JOptionPane.showMessageDialog(null, "Please input correct time end of class");
					return;
				}
	
				strDayMonth = (String)dayinMonth.getSelectedItem();
				
				try {
					ConnectionUtils con = new ConnectionUtils();
					int res = con.createClass(lectureId, strID, strName, strRoom, dBegin, dEnd, tBegin, tEnd, strDayMonth);
					
					if (res == 1)
					{
						JOptionPane.showMessageDialog(null, "Create class done");
						saveListLecture.addElement("Class ID : " + strID + " - " + strName);
						ml.setClass(strID, strName, strRoom, dBegin, dEnd, tBegin, tEnd, strDayMonth);
						
						saveMyClassLecture.add(ml);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Error while creating class");
					}
					
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Please input correct information");
			}
			
		}
	}
	
	public void deleteClass(int lectureId, int classID)
	{
	//	DefaultListModel<Integer> removeList = new DefaultListModel<>();
	//	for (int i = 0; i < saveMyClassLecture.size(); i++)
	//	{
	//		removeList.addElement(saveMyClassLecture.get(i).getClassNumber());
	//	}
		
		ConnectionUtils con = new ConnectionUtils();
		int flag = con.deleteClass(lectureId, classID);
		if (flag == 1)
		{
			JOptionPane.showMessageDialog(null, "Delete class done");
			int pos = 0;
			for (int i = 0; i < saveListLecture.size(); i++)
			{
				if (saveListLecture.get(i).equals("" + classID))
					pos = i;
					
			}
		}
		
	}

	public int hashPass(String password)
	{
		return password.hashCode();
	}
	
}
