package POJO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "STUDENT")
public class MyStudent {
	
	@Id
	@Column (name = "ID")
	private int ID;
	
	@Column (name = "NAME")
	private String NAME;
	
	@Column (name = "PASS")
	private String PASS;

	
	public MyStudent() {
		// TODO Auto-generated constructor stub
		ID = 0;
		NAME = "";
		PASS = "";
	}
	
	public void setStudentData(int studentID, String studentName)
	{
		this.ID = studentID;
		this.NAME = studentName;
	}
	
	public void setID(int studentID)
	{
		this.ID = studentID;
	}
	
	public void setNAME(String studentName)
	{
		this.NAME = studentName;
	}
	
	public void setPASS(String password)
	{
		this.PASS = password;
	}
	
	
	public int getID()
	{
		return ID;
	}
	
	public String getNAME()
	{
		return NAME;
	}
	
	public String getPASS()
	{
		return PASS;
	}
}
