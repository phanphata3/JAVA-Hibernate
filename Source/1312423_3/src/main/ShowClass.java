package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.regex.PatternSyntaxException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import connection.*;
import POJO.*;

public class ShowClass extends JFrame{
	private DefaultListModel<String> saveListStudent;
	private DefaultListModel<Integer> checkList;
	private JList<String> saveStudent;
	private Vector<MyStudent> saveVectorStudent;
	private Vector<String> obj;
	private Vector<Vector<String>> listTable;
	private JTable jtable;
	
	
	public ShowClass(MyClass mcl, int tab, int id) {
		// TODO Auto-generated constructor stub
		
		setPreferredSize(new Dimension(1200, 400));
		Calendar cal = Calendar.getInstance();
		
		setTitle("Class ID : " + mcl.getID() + " - " + mcl.getNAME() + " --> Today is : " + cal.getTime().toString());
		
		saveVectorStudent = new Vector<>();
		saveListStudent = new DefaultListModel<>();
		saveStudent = new JList<>(saveListStudent);
		checkList = new DefaultListModel<>();
		listTable = new Vector<>();
		
		
		
		
		
			
		JPanel jp = new JPanel(new BorderLayout());
		obj = new Vector<>();
		obj.add("Student ID");
		obj.add("Student Name");
		for (int i = 1; i <= 15; i++)
		{
			
			obj.addElement("Week " + i);
		}
			
		jtable = new JTable(listTable, obj);
		jtable.setFillsViewportHeight(true);
		jtable.getColumnModel().getColumn(0).setPreferredWidth(200);
		jtable.getColumnModel().getColumn(1).setPreferredWidth(300);
		jtable.setAutoCreateRowSorter(true);
		
		JScrollPane js = new JScrollPane(jtable);
		
		TableModel tm = jtable.getModel();
		TableRowSorter<TableModel> sort = new TableRowSorter<TableModel>(tm); 
		jtable.setRowSorter(sort);
		
		JPanel jn = new JPanel(new GridLayout(3, 2));
		jn.add(new JLabel("Class Room : " + mcl.getNAME()));
		jn.add(new JLabel("Study in : " + mcl.getDAYMONTH()));
		String str = "";
		cal = Calendar.getInstance();
		cal.setTime(mcl.getDATEBEGIN());
		String proc1, proc2;
		proc1 = "" + (cal.get(Calendar.MONTH) + 1);
		if (proc1.length() == 1)
			proc1 = "0" + proc1;
		proc2 = "" + cal.get(Calendar.DAY_OF_MONTH);
		if (proc2.length() == 1)
			proc2 = "0" + proc2;
		
		str = cal.get(Calendar.YEAR) + "-" + proc1 + "-" + proc2;
		
		jn.add(new JLabel("Date Begin : " + str));	
		jn.add(new JLabel("Time Begin : " + mcl.getHOURBEGIN()));
		
		cal.setTime(mcl.getDATEEND());
		proc1 = "" + (cal.get(Calendar.MONTH) + 1);
		if (proc1.length() == 1)
			proc1 = "0" + proc1;
		proc2 = "" + cal.get(Calendar.DAY_OF_MONTH);
		if (proc2.length() == 1)
			proc2 = "0" + proc2;
		str = cal.get(Calendar.YEAR) + "-" + proc1 + "-" + proc2;
		
		jn.add(new JLabel("Date End : " + str));
		jn.add(new JLabel("Time End : " + mcl.getHOUREND()));
		
		
		jn.setBorder(BorderFactory.createLineBorder(Color.darkGray, 2));
		
		JPanel jpbut = new JPanel();
		JButton jbut = new JButton();
		
		try {
			getClassDataFromDatabase(mcl.getID(), listTable, checkList);
			getStudentDataFromDatabase(saveVectorStudent);
			
		} catch (ClassNotFoundException | SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		// Thuc hien kiem tra
		if (tab == 0)
		{	
			for (int i = 0; i < 15; i++)
			{
				jtable.setEditingColumn(i + 3);
			}
			
			jbut.setText("Add Students");
			jbut.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					try {
						addStudent(mcl);
						jtable.getColumnModel().getColumn(0).setPreferredWidth(200);
						jtable.getColumnModel().getColumn(1).setPreferredWidth(300);
						
					} catch (ClassNotFoundException | SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			
			JButton jbut2 = new JButton("Attend Students");
			jbut2.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					attendStudent(mcl.getID());

					jtable.getColumnModel().getColumn(0).setPreferredWidth(200);
					jtable.getColumnModel().getColumn(1).setPreferredWidth(300);
				}
			});
			
			jpbut.add(jbut);
			jpbut.add(jbut2);
		}
		else
		{
			jbut.setText("Attend me");
			jbut.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
					studentAutoAttended(mcl, id);
					jtable.getColumnModel().getColumn(0).setPreferredWidth(200);
					jtable.getColumnModel().getColumn(1).setPreferredWidth(300);
				}
			});
			
			jpbut.add(jbut);
		}
		
		JPanel jfilter = new JPanel(new BorderLayout());
		JLabel lb = new JLabel("Filter ID    ");
		JTextField jtf = new JTextField();
		lb.setLabelFor(jtf);
		jtf.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				RowFilter<TableModel, Object> fil = null;
				try
				{
					fil = RowFilter.regexFilter(jtf.getText(), 0);
					sort.setRowFilter(fil);
				}
				catch (PatternSyntaxException ex)
				{
					
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		jfilter.add(lb, BorderLayout.WEST);
		jfilter.add(jtf, BorderLayout.CENTER);
		
		JPanel jnorth = new JPanel(new BorderLayout()); 
		jnorth.add(jn, BorderLayout.NORTH);
		jnorth.add(js, BorderLayout.CENTER);
		jnorth.add(jfilter, BorderLayout.SOUTH);
		
		
		jp.add(jnorth, BorderLayout.CENTER);
		jp.add(jpbut, BorderLayout.SOUTH);
		
		add(jp);
		pack();
	}
	
	public void addStudent(MyClass mcl) throws ClassNotFoundException, SQLException
	{
		int classID = mcl.getID();
		
		
		JPanel jp = new JPanel(new GridLayout(0, 1));
		jp.add(new JLabel("Select Student"));
		
		ButtonGroup bgroup = new ButtonGroup();
		JRadioButton jrd1 = new JRadioButton("Input new Student");
		JRadioButton jrd2 = new JRadioButton("Choose from list");
		JRadioButton jrd3 = new JRadioButton("Import from file");
		
		bgroup.add(jrd1);
		bgroup.add(jrd2);
		bgroup.add(jrd3);
		
		jp.add(jrd1);
		jp.add(jrd2);
		jp.add(jrd3);
		
		
		int res = JOptionPane.showConfirmDialog(null, jp, "Choose one", JOptionPane.OK_CANCEL_OPTION);
		if (res == JOptionPane.OK_OPTION)
		{
			// Nhap moi
			if (jrd1.isSelected())
			{
				JTextField jid, jname;
				JPanel jinput = new JPanel(new GridLayout(0, 1));
				jinput.add(new JLabel("Student ID"));
				jid = new JTextField();
				jinput.add(jid);
				
				jinput.add(new JLabel("Student Name"));
				jname = new JTextField();
				jinput.add(jname);
				
				int flag1 = JOptionPane.showConfirmDialog(null, jinput, "Input info Student", JOptionPane.OK_CANCEL_OPTION);
				if (flag1 == JOptionPane.OK_OPTION)
				{
					if ((String)jid.getText() == null || jname.getText() == null)
					{
						JOptionPane.showMessageDialog(null, "Please input correct info");
					}
					else
					{
						int studentID = Integer.parseInt(jid.getText());
						String studentName = jname.getText();
						
						if (!checkList.contains(studentID))
						{
							checkList.addElement(studentID);
							
							Vector<String> newRow = new Vector<>();
							newRow.addElement("" + studentID);
							newRow.addElement(studentName);
							
							for (int i = 0; i < 15; i++)
							{
								newRow.addElement(" ");
							}
							
							int temp = 1;
							for (int i = 0; i < saveVectorStudent.size(); i++)
							{
								if (saveVectorStudent.get(i).getID() == studentID)
									temp = 0;
							}
							
							ConnectionUtils con = new ConnectionUtils();
							if (temp == 1)
							{
								MyStudent ms = new MyStudent();
								ms.setStudentData(studentID, studentName);
								ms.setPASS("" + studentID);
								con.addStudentIntoDatabase(classID, ms);
							}
							else
							{
								con.insertClassData(mcl.getID(), newRow);
							}
							
							
							
							listTable.addElement(newRow);
							jtable.setUpdateSelectionOnSort(true);
							jtable.tableChanged(null);
							jtable.getColumnModel().getColumn(0).setPreferredWidth(200);
							jtable.getColumnModel().getColumn(1).setPreferredWidth(300);
							
						}
						
						else
						{
							JOptionPane.showMessageDialog(null, "This student already existed in class");
						}
					}
					
				}
			}
			
			// Chon tu list
			else if (jrd2.isSelected())
			{
				JPanel jchoose = new JPanel(new GridLayout(2, 1));
				
				saveStudent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				JScrollPane jsc = new JScrollPane(saveStudent);
				jchoose.add(jsc);
				
				JPanel jbut2 = new JPanel();
				JButton jall = new JButton("All from list");
				jall.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub

						for (int i = 0; i < saveVectorStudent.size(); i++)
						{
							if (!checkList.contains(saveVectorStudent.get(i).getID()))
							{
								Vector<String> newRow = new Vector<>();
								newRow.addElement("" + saveVectorStudent.get(i).getID());
								newRow.addElement(saveVectorStudent.get(i).getNAME());
								for (int j = 1; j <= 15; j++)
								{
									newRow.addElement(" ");
								}
								
								ConnectionUtils con = new ConnectionUtils();
								int res;
								try {
									res = con.insertClassData(classID, newRow);
									if (res == 0)
										JOptionPane.showMessageDialog(null, "Error while adding Student " + (i + 1));
									else
									{
										listTable.addElement(newRow);
										checkList.addElement(saveVectorStudent.get(i).getID());
										
										jtable.setUpdateSelectionOnSort(true);
										jtable.tableChanged(null);
										jtable.getColumnModel().getColumn(0).setPreferredWidth(200);
										jtable.getColumnModel().getColumn(1).setPreferredWidth(300);
									}
								} catch (ClassNotFoundException | SQLException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
							else
							{
								JOptionPane.showMessageDialog(null, "Student " + saveVectorStudent.get(i).getID() + " - " + saveVectorStudent.get(i).getNAME() + " already exist in Class");
							}
							
						}
					}
				});
				jbut2.add(jall);
				jchoose.add(jbut2);
				
				int flag2 = JOptionPane.showConfirmDialog(null, jchoose, "Choose from list", JOptionPane.OK_CANCEL_OPTION);
				if (flag2 == JOptionPane.OK_OPTION)
				{
					int pos = saveStudent.getSelectedIndex();
					
					if (!checkList.contains(saveVectorStudent.get(pos).getID()))
					{
						Vector<String> newRow = new Vector<>();
						newRow.addElement("" + saveVectorStudent.get(pos).getID());
						newRow.addElement(saveVectorStudent.get(pos).getNAME());
						for (int i = 1; i <= 15; i++)
						{
							newRow.addElement(" ");
						}
						
						
						
						ConnectionUtils con = new ConnectionUtils();
						int result = con.insertClassData(classID, newRow);
						if (result == 0)
							JOptionPane.showMessageDialog(null, "Error while adding data");
						else
						{
							listTable.addElement(newRow);
							checkList.addElement(saveVectorStudent.get(pos).getID());
							
							jtable.setUpdateSelectionOnSort(true);
							jtable.tableChanged(null);
							jtable.getColumnModel().getColumn(0).setPreferredWidth(200);
							jtable.getColumnModel().getColumn(1).setPreferredWidth(300);
						}
						
					}
					else
					{
						JOptionPane.showMessageDialog(null, "This student already exist in Class");
					}
				}
			}
			
			// Nhap tu file
			else
			{
				JTextField jt = new JTextField();
				int flag3 = JOptionPane.showConfirmDialog(null, jt, "Please input path of file ", JOptionPane.OK_CANCEL_OPTION);
				if (flag3 == JOptionPane.OK_OPTION)
				{
					String str = jt.getText();
					
					try {
						try {
							readCSVFile(classID, str);
						} catch (ClassNotFoundException | SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
		}
		
		
	}

	public void getStudentDataFromDatabase(Vector<MyStudent> saveVectorStudent) throws ClassNotFoundException, SQLException
	{
		try
		{
			ConnectionUtils con = new ConnectionUtils();
			con.getListStudent(saveVectorStudent);
			
			for (int i = 0; i < saveVectorStudent.size(); i++)
			{
				saveListStudent.addElement(saveVectorStudent.get(i).getID() +  " - " + saveVectorStudent.get(i).getNAME());
				
			}
		}
		catch (Exception ex)
		{
			String str = ex.getMessage();
			JOptionPane.showMessageDialog(null, "Error : " + str);
		}
		
	}

	public void getClassDataFromDatabase(int classId, Vector<Vector<String>> listTable, DefaultListModel<Integer> checkList)
	{
		try
		{
			ConnectionUtils con = new ConnectionUtils();
			int res = con.getClassData(classId, listTable, checkList);
			if (res != 1)
				JOptionPane.showMessageDialog(null, "Error while getting list students in class from database");
			
		}
		catch (Exception ex)
		{
			String str = ex.getMessage();
			JOptionPane.showMessageDialog(null, "Error : " + str);
		}
	}

	public void readCSVFile(int classID, String fileName) throws IOException, ClassNotFoundException, SQLException
	{
		File file = new File(fileName);
		
		if (file.exists())
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			String temp = "";
			while ((temp = br.readLine()) != null)
			{
				String[] str = temp.split(",");
				
				MyStudent ms = new MyStudent();
				ms.setStudentData(Integer.parseInt(str[0]), str[1]);
				ms.setPASS(str[0]);
				if (!checkList.contains(ms.getID()))
				{
					ConnectionUtils con = new ConnectionUtils();
					int flag = con.addStudentIntoDatabase(classID, ms);
					
					if (flag == 1)
					{
						Vector<String> newRow = new Vector<>();
						newRow.addElement("" + ms.getID());
						newRow.addElement(ms.getNAME());
						
						for (int i = 0; i < 15; i++)
						{
							newRow.addElement(" ");
						}
						
						listTable.add(newRow);
						checkList.addElement(ms.getID());
						jtable.setUpdateSelectionOnSort(true);
						jtable.tableChanged(null);
						
						jtable.getColumnModel().getColumn(0).setPreferredWidth(200);
						jtable.getColumnModel().getColumn(1).setPreferredWidth(300);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Student ID : " + ms.getID() + " - " + ms.getNAME() + " already existed in Class");
				}
				
			}
			
			br.close();
			
			
		}
		else 
			JOptionPane.showMessageDialog(null, "Please input correct path of file");
		
		
		
	}

	public void attendStudent(int classID)
	{
		int sumRow = jtable.getRowCount();
		int sumCol = jtable.getColumnCount();
		
		Vector<Vector<String>> tableChange = new Vector<>();
		
		for (int i = 0; i < sumRow; i++)
		{
			String studentName, studentID;
			
			Vector<String> row = new Vector<>();
			studentID = (String)jtable.getValueAt(i, 0);
			
			row.addElement(studentID);
			
			studentName = (String)jtable.getValueAt(i, 1);
			row.add(studentName);
			
				
			for (int j = 2; j < sumCol; j++)
			{
				String str = (String) jtable.getValueAt(i, j);
				
				if (str.contains("x"))
					row.addElement("1");
				else 
					row.addElement("0");

			}
			tableChange.addElement(row);
		}
		
		ConnectionUtils con = new ConnectionUtils();
		con.attendStudentInClass(classID, tableChange);
		
		JOptionPane.showMessageDialog(null, "Attend done");
	}
	
	public void studentAutoAttended(MyClass mcl, int studentID)
	{
		Calendar cal = Calendar.getInstance();
		int hour = cal.getTime().getHours();
		int minute = cal.getTime().getMinutes();
		int dayWeek = cal.getFirstDayOfWeek();
		
		String begin =  mcl.getDATEBEGIN() + " " + mcl.getHOURBEGIN();
		String end = mcl.getDATEEND() + " " + mcl.getHOUREND();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			Date timeBegin = format.parse(begin);
			Calendar classBegin = Calendar.getInstance();
			classBegin.setTime(timeBegin);
			
			
			Date timeEnd = format.parse(end);
			Calendar classEnd = Calendar.getInstance();
			classEnd.setTime(timeEnd);
			
			String str = mcl.getDAYMONTH();
			int dayinWeek = 0;
			
			if (str.equalsIgnoreCase("Sunday"))
				dayinWeek = 0;
			if (str.equalsIgnoreCase("Monday"))
				dayinWeek = 1;
			if (str.equalsIgnoreCase("Tuesday"))
				dayinWeek = 2;
			if (str.equalsIgnoreCase("Wednesday"))
				dayinWeek = 3;
			if (str.equalsIgnoreCase("Thursday"))
				dayinWeek = 4;
			if (str.equalsIgnoreCase("Friday"))
				dayinWeek = 5;
			if (str.equalsIgnoreCase("Saturday"))
				dayinWeek = 6;
			
			
			
			if ((cal.before(classEnd) || cal.equals(classEnd)) && (cal.after(classBegin) || cal.equals(classBegin)))
			{
				if (dayinWeek == cal.getTime().getDay())
				{
				//	int res = cal.get(Calendar.HOUR_OF_DAY)
					
					if ((cal.get(Calendar.HOUR_OF_DAY) >= classBegin.get(Calendar.HOUR_OF_DAY)) && (cal.get(Calendar.HOUR_OF_DAY) <= classEnd.get(Calendar.HOUR_OF_DAY)))
					{
						if ((cal.get(Calendar.MINUTE) >= classBegin.get(Calendar.MINUTE)) && (cal.get(Calendar.HOUR) != classEnd.get(Calendar.HOUR)))
						{
							
							int countWeek = cal.get(Calendar.DAY_OF_YEAR) - classBegin.get(Calendar.DAY_OF_YEAR);
							countWeek = countWeek / 7;
							
							int row = jtable.getRowCount();
							int col = jtable.getColumnCount();
							
							for (int i = 0; i < row; i++)
							{
								if (Integer.parseInt((String)jtable.getValueAt(i, 0)) == studentID)
								{
									listTable.get(i).set(countWeek + 2, "x");
									attendStudent(mcl.getID());
									jtable.tableChanged(null);
									
									
									jtable.getColumnModel().getColumn(0).setPreferredWidth(200);
									jtable.getColumnModel().getColumn(1).setPreferredWidth(300);
								}
							}
							
						}
						else
							JOptionPane.showMessageDialog(null, "Class is not teaching this time");
					}
						
					else
						JOptionPane.showMessageDialog(null, "Class is not teaching this time");
					
				}
				else
					JOptionPane.showMessageDialog(null, "Class is not teaching today");
			}
			else
				JOptionPane.showMessageDialog(null, "Sorry this class can not be accessed");
			
			
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
