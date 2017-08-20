package POJO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table (name = "LECTURE")
public class MyLecture {
	@Id
	@GeneratedValue
	@Column (name = "ID")
	private int ID;
	
	@Column (name = "NAME")
	private String NAME;
	
	@Column (name = "PASS")
	private String PASS;
	
	@Column (name = "CLASS")
	private String CLASS;
	
	
	public MyLecture()
	{
		ID = 0;
		NAME = "";
		PASS = "";
		
	}
	
	public void setLectureData(int lectureID, String lectureName)
	{
		this.ID = lectureID;
		this.NAME = lectureName;
	}
	
	public void setID(int lectureID)
	{
		this.ID = lectureID;
	}
	
	public void setNAME(String lectureName)
	{
		this.NAME = lectureName;
	}
	
	public void setPASS(String lecturePassword)
	{
		this.PASS = lecturePassword;
	}
	
	public void setCLASS(String classLecture)
	{
		this.CLASS = classLecture;
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
	
	public String getCLASS()
	{
		return CLASS;
	}
}
