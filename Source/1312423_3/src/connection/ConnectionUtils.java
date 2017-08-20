package connection;

import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;

import POJO.*;
import Utilities.HibernateUtil;

public class ConnectionUtils extends JFrame{
	private String table;
	private int id;
	private String password; 
	private String Name;
	
    private Statement st = null;
	private ResultSet rs = null;
	
	public ConnectionUtils(){
	}


	 //---------------------------------------------------------- Hibernate ---------------------------------------------//
	
	 // ------------------------ Login -----------------------------//
	 
	 public int getLoginResult()
	 {
		int flag = 0; 

		Session ses = HibernateUtil.getSessionFactory().openSession();
		String HQL = "from " + table + " where ID = " + id;
		Query query = ses.createQuery(HQL);		
		ses.getTransaction().begin();
		if (query != null)
		{
			if (table.equals("MyLecture"))
			{
				MyLecture ms = (MyLecture)query.uniqueResult();
				flag = lectureLogin(ms, ses, flag);
			}
			else
			{
				MyStudent ms = (MyStudent) query.uniqueResult();
				flag = studentLogin(ms, ses, flag);
			}
			
			
			ses.getTransaction().commit();
			ses.close();
		}
		else
		{
			JOptionPane.showMessageDialog(this,	"ID not exist");
		}	
		
		return flag;
 	}
	 
	 public int studentLogin(MyStudent ms, Session ses, int flag)
	 {
		String str = ms.getPASS();
		int hashpass = password.hashCode() + 15;
		if (str.equals(password))
		{
			String temp = "";
			temp = temp + id;
			
			if (str.equals(temp))
			{
				JOptionPane.showMessageDialog(this, "You have to change this password");
				
				int changeFlag = 0;
				while (changeFlag == 0)
				{

					changeFlag = newStudentPassword(ms, ses, ms.getID(), changeFlag);
				}
				flag = 1;
				
			}
			
			else
			{
				JOptionPane.showMessageDialog(this,	"Wrong password");
			}
			
			
		}
		else if (str.equals("" + hashpass))
		{
			Query qr = ses.createQuery("from MyStudent b where b.ID =" + id);
			MyStudent result = (MyStudent)qr.uniqueResult();
			setLoginName(result.getNAME());
			
			flag = 1;
		}
		else
			JOptionPane.showMessageDialog(this,	"Wrong password");
		
		return flag;
	}

	 public int lectureLogin(MyLecture ms, Session ses, int flag)
	 {
		 String str = ms.getPASS();
		 int hasspass = password.hashCode() + 15;
			if (str.equals(password))
			{
				Name = ms.getNAME();
				String temp = "";
				temp = temp + id;
				if (str.equals(temp))
				{
					JOptionPane.showMessageDialog(this, "You have to change this password");
					
					int changeFlag = 0;
					while (changeFlag == 0)
					{

						changeFlag = newLecturePassword(ms, ses, ms.getID(), changeFlag);
					}
					flag = 1;
				}
				else
				{
					JOptionPane.showMessageDialog(this,	"Wrong password");
				}
				
			}
			else if (str.equals("" + hasspass))
			{
				Query qr = ses.createQuery("from MyLecture b where b.ID =" + id);
				MyLecture result = (MyLecture)qr.uniqueResult();
				setLoginName(result.getNAME());
				
				flag = 1;
			}
			else
				JOptionPane.showMessageDialog(this,	"Wrong password");
			
			return flag;
	 }

	 // ------------------------ Create Class ------------------------------//
	 
	 public int createClass(int lectureId, int classID, String className, String classRoom, Date dateBegin, Date dateEnd, 
			 Time hourBegin, Time hourEnd, String dayMonth) throws SQLException, ClassNotFoundException
	 {
		int flag = 0;
	
		Session ses = HibernateUtil.getSessionFactory().openSession();
		ses.getTransaction().begin();
		MyClass mcl = new MyClass();
		mcl.setClass(classID, className, classRoom, dateBegin, dateEnd, hourBegin, hourEnd, dayMonth);
		ses.save(mcl);
			
		String HQL = "from MyLecture where ID = " + lectureId;
		Query qr = ses.createQuery(HQL);
		
		if (qr != null)
		{
			MyLecture myLecture = (MyLecture) qr.uniqueResult();
			
			String str = null;
			if (myLecture.getCLASS() == null)
				str = "";
			else
				str = myLecture.getCLASS();
			
			str = str + classID + ",";
			
			myLecture.setCLASS(str);
			
			ses.update(myLecture);
			
			HQL = "CREATE TABLE CLASS" + classID + " ( STUDENTID INT NOT NULL, STUDENTNAME VARCHAR(100) NOT NULL, WEEK1 INT, WEEK2 INT, WEEK3 INT, WEEK4 INT, WEEK5 INT, "
					+ "WEEK6 INT, WEEK7 INT, WEEK8 INT, WEEK9 INT, WEEK10 INT, WEEK11 INT, WEEK12 INT, WEEK13 INT, WEEK14 INT, WEEK15 INT, PRIMARY KEY(STUDENTID) )";
			ses.createSQLQuery(HQL).executeUpdate();
			
			flag = 1;
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Error while query");
		}
		
		ses.getTransaction().commit();
		ses.close();
		
		return flag;
	 }

	 // ------------------------ set Data ---------------------------------//
	 
	 public void setLoginData(String table, int id, String password){
		  
		    this.table = table;
		    this.id = id;
		    this.password = password;
	 	 }
		 
	 public String getLoginName()
	 {
		 return Name;
	 }
	
	 public void setLoginName(String Name)
	 {
		 this.Name = Name;
	 }
	 
	 public JPanel changePassword(JTextField jt1, JTextField jt2, JTextField jt3)
	 {
		 JPanel result = new JPanel(new GridLayout(0, 1));
		 result.add(new JLabel("Old Password"));
		 result.add(jt1);
		 
		 result.add(new JLabel("New Password"));
		 result.add(jt2);
		 
		 result.add(new JLabel("Re-input New Password"));
		 result.add(jt3);
		 
		 return result;
	 }

	 public int newStudentPassword(MyStudent ms, Session ses, int studentID, int flag)
	 {
		 JTextField jt1, jt2, jt3;
		jt1 = new JPasswordField();
		jt2 = new JPasswordField();
		jt3 = new JPasswordField();
			
		JOptionPane.showConfirmDialog(this, changePassword(jt1, jt2, jt3), "Change Password", JOptionPane.OK_OPTION);
		
		String str1, str2, str3;
		str1 = jt1.getText();
		str2 = jt2.getText();
		str3 = jt3.getText();
		
		if (str1.equals(password))
		{
			if (str2.equals(str1))
			{
				JOptionPane.showMessageDialog(this, "New password must different with old password");
			}
			else if (str2.equals(str3))
			{
				int hasspass = str2.hashCode() + 15;
				
				ms.setPASS("" + hasspass);
				ses.update(ms);
				
				
				Query qr = ses.createQuery("from MyStudent b where b.ID = " + studentID);
				MyStudent result = (MyStudent)qr.uniqueResult();
				setLoginName(result.getNAME());
				
				JOptionPane.showMessageDialog(this, "Change password successful");
				flag = 1;
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Password not correct, try again");
			}
		}
		
		return flag;
	 }
	 
	 public int newLecturePassword(MyLecture ms, Session ses, int lectureID, int flag)
	 {
		 JTextField jt1, jt2, jt3;
		jt1 = new JPasswordField();
		jt2 = new JPasswordField();
		jt3 = new JPasswordField();
			
		JOptionPane.showConfirmDialog(this, changePassword(jt1, jt2, jt3), "Change Password", JOptionPane.OK_OPTION);
		
		String str1, str2, str3;
		str1 = jt1.getText();
		str2 = jt2.getText();
		str3 = jt3.getText();
		
		if (str1.equals(password))
		{
			if (str2.equals(str1))
			{
				JOptionPane.showMessageDialog(this, "New password must different with old password");
			}
			else if (str2.equals(str3))
			{
				
				int hasspass = str2.hashCode() + 15;
				ms.setPASS("" + hasspass);
				ses.update(ms);
				
				Query qr = ses.createQuery("from MyLecture b where b.ID = " + lectureID);
				MyLecture result = (MyLecture)qr.uniqueResult();
				setLoginName(result.getNAME());
				
				JOptionPane.showMessageDialog(this, "Change password successful");
				flag = 1;
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Password not correct, try again");
			}
		}
		
		return flag;
	 }
	
	 
	 // ------------------------ get List ---------------------------------//
	 
	 public void getListStudent(Vector<MyStudent> result)
	 {
 
		 Session ses = HibernateUtil.getSessionFactory().openSession();
		 ses.getTransaction().begin();
		 
		 String HQL = "SELECT b FROM MyStudent b";
		 Query qr = ses.createQuery(HQL);
		 ArrayList<MyStudent> list = (ArrayList<MyStudent>) qr.list();
		 
		 for (int i = 0; i < list.size(); i++)
		 {
			 result.addElement(list.get(i));
		 }
		 
		 ses.getTransaction().commit();
		 ses.close();
		 
	 }

	 public int getClassData(int classId, Vector<Vector<String>> listTable, DefaultListModel<Integer> checkList) throws SQLException, ClassNotFoundException
	 {
		 int flag = 0;
		 
		 Session ses = HibernateUtil.getSessionFactory().openSession();
		 ses.getTransaction().begin();
		 
		 try
		 {
			 String SQL = "SELECT * FROM CLASS" + classId;
			 rs = ses.doReturningWork(new ReturningWork<ResultSet>() {

				@Override
				public ResultSet execute(Connection connection) throws SQLException {
					// TODO Auto-generated method stub
					ResultSet rs2 = null;
					Statement st2 = connection.createStatement();
					rs2 = st2.executeQuery(SQL);
					
					return rs2;
				}
			 });
			 
			 while (rs.next())
			 {
				 String id, name;
				 id = "" + rs.getString(1);
				 
				 checkList.addElement(rs.getInt(1));
				 name = "" + rs.getString(2);
				 
				 Vector<String> newRow = new Vector<>();
				 newRow.addElement(id);
				 newRow.addElement(name);
				 
				 for (int i = 0; i < 15; i++)
				 {
					 String temp = "" + rs.getString(i + 3);
					 if (temp.equalsIgnoreCase("0"))
						 temp = " ";
					 else
						 temp = "x";
					 newRow.addElement(temp);
				 }
				 
				 listTable.addElement(newRow);
			 }
			 
			 flag = 1;
		 }
		 catch (Exception ex)
		 {
			 
		 }
			 
		 ses.getTransaction().commit();
		 ses.close();
		 return flag;
	 }

	 public int getListClassForStudent(int studentID, Vector<MyClass> result) throws SQLException, ClassNotFoundException
	 {
		 int flag = 0;

		 String HQL = "select c from MyClass c";
		 Session ses = HibernateUtil.getSessionFactory().openSession();
		 ses.getTransaction().begin();
		 Query qr = ses.createQuery(HQL);
		 ArrayList<MyClass> list = (ArrayList<MyClass>) qr.list();
		 
		 try
		 {
			 for (int i = 0; i < list.size(); i++)
			 {
				 int classID = list.get(i).getID();
				 		 
				 rs = ses.doReturningWork(new ReturningWork<ResultSet>() {

					@Override
					public ResultSet execute(Connection connection) throws SQLException {
						// TODO Auto-generated method stub
						Statement st2 = connection.createStatement();
						ResultSet rs2 = st2.executeQuery("SELECT * FROM CLASS" + classID);
						return rs2;
					}
				 });
				 
				 
				 while (rs.next())
				 {
		
					 if (studentID == rs.getInt(1))
					 {
					 	MyClass mcl = new MyClass();
						 
						 String className, classRoom, dayMonth;
						 Date  dateBegin, dateEnd;
						 Time timeBegin;
						 Time timeEnd;
						 
						 
						 className = list.get(i).getNAME();
						 classRoom = list.get(i).getROOM();
						 dateBegin = list.get(i).getDATEBEGIN();
						 dateEnd = list.get(i).getDATEEND();
						 timeBegin = list.get(i).getHOURBEGIN();
						 timeEnd = list.get(i).getHOUREND();
						 dayMonth = list.get(i).getDAYMONTH();
						 
						 mcl.setClass(classID, className, classRoom, dateBegin, dateEnd, timeBegin, timeEnd, dayMonth);
						 result.addElement(mcl);
					 }
				 }
				 
				 flag = 1;
				 rs.close();
			 }
		 }
		 catch (Exception e) {
			// TODO: handle exception
		}
		 
		 ses.getTransaction().commit();
		 ses.close();	 
		 return flag; 
	 }

	 public int getListClassForLecture(int lectureID, Vector<MyClass> result)
	 {
		int flag = 0;
		Session ses = HibernateUtil.getSessionFactory().openSession();
		ses.getTransaction().begin();
		
		String HQL = "select b from MyLecture b where b.ID = " + lectureID;
		Query qr = ses.createQuery(HQL);
		
		MyLecture ml = (MyLecture)qr.uniqueResult();
		if (ml.getCLASS() == null)
		{
			result = null;
		}
		else
		{
			String str = ml.getCLASS();
			String[] listClass = str.split(",");
			for (int i = 0; i < listClass.length; i++)
			{
				if (listClass[i].length() != 0)
				{
					HQL = "select b from MyClass b where b.ID = " + listClass[i];
					qr = ses.createQuery(HQL);
					MyClass mcl = (MyClass) qr.uniqueResult();
					
					result.addElement(mcl);
					
					flag = 1;
				}
			}
		}
		
		ses.getTransaction().commit();
		ses.close();
		return flag;
	 }

	 public int changeStudentPassword(int studentID)
	 {
		 int flag = 0;
		 
		 Session ses = HibernateUtil.getSessionFactory().openSession();
		 ses.beginTransaction();
		 
		 MyStudent ms = new MyStudent();
		 Query qr = ses.createQuery("from MyStudent b where b.ID = " + studentID);
		 ms = (MyStudent)qr.uniqueResult();
		 
		 JTextField jt1, jt2, jt3;
		jt1 = new JPasswordField();
		jt2 = new JPasswordField();
		jt3 = new JPasswordField();
			
		int res = JOptionPane.showConfirmDialog(null, changePassword(jt1, jt2, jt3), "Change Password", JOptionPane.OK_CANCEL_OPTION);
		
		if (res == JOptionPane.OK_OPTION)
		{
			String str1, str2, str3;
			str1 = jt1.getText();
			str2 = jt2.getText();
			str3 = jt3.getText();
			
			String str = "" + (str1.hashCode() + 15);
			if (str.equals(ms.getPASS()))
			{
				if (str2.equals(str1))
				{
					JOptionPane.showMessageDialog(this, "New password must different with old password");
				}
				else if (str2.equals(str3))
				{
					
					int hasspass = str2.hashCode() + 15;
					ms.setPASS("" + hasspass);
					ses.update(ms);
					
					qr = ses.createQuery("from MyStudent b where b.ID = " + studentID);
					MyStudent result = (MyStudent)qr.uniqueResult();
					setLoginName(result.getNAME());
					
					JOptionPane.showMessageDialog(this, "Change password successful");
					flag = 1;
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Password not correct, try again");
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Wrong old Password");
			}
		}
		
		
		 ses.getTransaction().commit();
		 ses.close();
		 return flag;
	 }
	 
	 public int changeLecturePassword(int lectureID)
	 {
		 int flag = 0;
		 
		 Session ses = HibernateUtil.getSessionFactory().openSession();
		 ses.beginTransaction();
		 
		 MyLecture ms = new MyLecture();
		 Query qr = ses.createQuery("from MyLecture b where b.ID = " + lectureID);
		 ms = (MyLecture)qr.uniqueResult();
		 
		 JTextField jt1, jt2, jt3;
		jt1 = new JPasswordField();
		jt2 = new JPasswordField();
		jt3 = new JPasswordField();
			
		int res = JOptionPane.showConfirmDialog(this, changePassword(jt1, jt2, jt3), "Change Password", JOptionPane.OK_CANCEL_OPTION);
		if (res == JOptionPane.OK_OPTION)
		{
			String str1, str2, str3;
			str1 = jt1.getText();
			str2 = jt2.getText();
			str3 = jt3.getText();
			
			String str = "" + (str1.hashCode() + 15);
			if (str.equals(ms.getPASS()))
			{
				if (str2.equals(str1))
				{
					JOptionPane.showMessageDialog(this, "New password must different with old password");
				}
				else if (str2.equals(str3))
				{
					
					int hasspass = str2.hashCode() + 15;
					ms.setPASS("" + hasspass);
					ses.update(ms);
					
					qr = ses.createQuery("from MyLecture b where b.ID = " + lectureID);
					MyLecture result = (MyLecture)qr.uniqueResult();
					setLoginName(result.getNAME());
					
					JOptionPane.showMessageDialog(this, "Change password successful");
					flag = 1;
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Password not correct, try again");
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Wrong old Password");
			}
		}
		
		 
		 ses.getTransaction().commit();
		 ses.close();
		 return flag;
	 }
	 // ------------------------ add ---------------------------------------//
	 
	 public int addStudentIntoDatabase(int classID, MyStudent myStudent) throws ClassNotFoundException, SQLException
	 {
		 int flag = 0;
	
		 Vector<MyStudent> getList = new Vector<>();
		 getListStudent(getList);
		 
		 DefaultListModel<Integer> def = new DefaultListModel<>();
		 for (int i = 0; i < getList.size(); i++)
		 {
			 if (!def.contains(getList.get(i).getID()))
			 {
				 def.addElement(getList.get(i).getID());
			 }
		 }
		 
		 Session ses = HibernateUtil.getSessionFactory().openSession();
		 ses.getTransaction().begin();
		 
		 if (!def.contains(myStudent.getID()))
		 {
			ses.save(myStudent);
		 }
	 
		 String HQL = "INSERT INTO CLASS" + classID + " VALUES (" + myStudent.getID() + ", N'" + myStudent.getNAME() + "', 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)";
		 ses.createSQLQuery(HQL).executeUpdate();
		 
		 flag = 1;
		 
		 ses.getTransaction().commit();
		 ses.close();
		 return flag;
	 }

	 public int attendStudentInClass(int classID, Vector<Vector<String>> changeTable)
	 {
		 int flag = 0;
	
		 Session ses = HibernateUtil.getSessionFactory().openSession();
		 ses.getTransaction().begin();
		 
		 for (int i = 0; i < changeTable.size(); i++)
		 {
			 String SQL = "UPDATE CLASS" + classID + " SET WEEK";
			 for (int j = 1; j <= 14; j++)
			 {
				 SQL = SQL + j + " = " + changeTable.get(i).get(j + 1) + ", WEEK";
			 }
			 SQL = SQL + "15 = " + changeTable.get(i).get(16) + " WHERE STUDENTID = " + changeTable.get(i).get(0);
			 
			 ses.createSQLQuery(SQL).executeUpdate();
		 }
		 
		 flag = 1;
		 
		 ses.getTransaction().commit();
		 ses.close();
		 return flag;
	 }

	 // ------------------------ delete -------------------------------------//
	 
	 public int deleteClass(int lectureId, int classID)
	 {
		 int flag = 0;
		 
		 Session ses = HibernateUtil.getSessionFactory().openSession();
		 ses.getTransaction().begin();
		 
		 String HQL = "SELECT b FROM MyLecture b WHERE b.ID = " + lectureId;
		 Query qr = ses.createQuery(HQL);
		 MyLecture ml = (MyLecture)qr.uniqueResult();
		 
		 String str = ml.getCLASS();
		 String[] split = str.split(",");
		 
		 String result = "";
		 for (int i = 0; i < split.length; i++)
		 {
			 if (!split[i].equals("" + classID))
				 result = result + split[i] + ",";
		 }
		 ml.setCLASS(result);
		 ses.update(ml);
 
		 HQL = "DROP TABLE CLASS" + classID;
		 ses.createSQLQuery(HQL).executeUpdate();
		 
		 HQL = "DELETE FROM CLASS WHERE ID = " + classID;
		 ses.createSQLQuery(HQL).executeUpdate();
		 
		 flag = 1;
		 
		 ses.getTransaction().commit();
		 ses.close();
		 
		 return flag;
	 }

	 // ----------------------- Insert data ----------------------------------//
	 
	 public int insertClassData(int classId, Vector<String> newRow) throws ClassNotFoundException, SQLException
	 {
		 int flag = 0;
		 
		 Session ses = HibernateUtil.getSessionFactory().openSession();
		 ses.getTransaction().begin();
		 
		 String SQL = "INSERT INTO CLASS" + classId + " VALUES (" + Integer.parseInt(newRow.get(0)) + ", N'" + newRow.get(1) + "',0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)";		 
		 ses.createSQLQuery(SQL).executeUpdate();
		 
		 ses.getTransaction().commit();
		 ses.close();
		 flag = 1;
		return flag;
	 }

}