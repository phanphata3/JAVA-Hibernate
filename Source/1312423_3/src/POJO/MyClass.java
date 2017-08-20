package POJO;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "CLASS")
public class MyClass {
	
	@Id
	@GeneratedValue
	@Column (name = "ID")
	private int ID;
	
	@Column (name = "NAME")
	private String NAME;
	
	@Column (name = "DATEBEGIN")
	private Date DATEBEGIN;
	
	@Column (name = "DATEEND")
	private Date DATEEND;
	
	@Column (name = "HOURBEGIN")
	private Time HOURBEGIN;
	
	@Column (name = "HOUREND")
	private Time HOUREND;
	
	@Column (name = "DAYMONTH")
	private String DAYMONTH;
	
	@Column (name = "ROOM")
	private String ROOM;
	
	public MyClass() {
		// TODO Auto-generated constructor stub
	}
	
	public void setClass(int classNumber, String className, String classRoom, Date dateBegin, Date dateEnd, 
			Time hourBegin, Time hourEnd, String dayMonth)
	{
		this.ID = classNumber;
		this.NAME = className;
		this.ROOM = classRoom;
		this.DATEBEGIN = dateBegin;
		this.DATEEND = dateEnd;
		this.HOURBEGIN = hourBegin;
		this.HOUREND = hourEnd;
		this.DAYMONTH = dayMonth;
		
	}
	
	public String getNAME()
	{
		return NAME;
	}
	
	public int getID()
	{
		return ID;
	}
	
	public String getROOM()
	{
		return ROOM;
	}
	
	public Date getDATEBEGIN()
	{
		return DATEBEGIN;
	}
	
	public Date getDATEEND()
	{
		return DATEEND;
	}
	
	public Time getHOURBEGIN()
	{
		return HOURBEGIN;
	}
	
	public Time getHOUREND()
	{
		return HOUREND;
	}
	
	public String getDAYMONTH()
	{
		return DAYMONTH;
	}
	
	
	public void setID(int classNumber)
	{
		this.ID = classNumber;
	}

	public void setNAME(String className)
	{
		this.NAME = className;
	}
	
	public void setROOM(String classRoom)
	{
		this.ROOM = classRoom;
	}
	
	public void setDATEBEGIN(Date dateBegin)
	{
		this.DATEBEGIN = dateBegin;
	}

	public void setDATEEND(Date dateEnd)
	{
		this.DATEEND = dateEnd;
	}
	
	public void setHOURBEGIN(Time timeBegin)
	{
		this.HOURBEGIN = timeBegin;
	}
	
	public void setHOUREND(Time timeEnd)
	{
		this.HOUREND = timeEnd;
	}
	
	public void setDAYMONTH(String dayMonth)
	{
		this.DAYMONTH = dayMonth;
	}

}
