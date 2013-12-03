package com.maestro.schoologyapp.api.lunchcounts.entity;

import javax.persistence.*;

import com.sirra.appcore.accounts.*;

@Entity
@Table(name = "lunchcounts")
public class LunchCount extends AccountEnabled {

	protected String id;
	protected String date;
	protected String teacherName;
	protected int count;
	protected String notes;
	
	public LunchCount() {
		
	}
		
	@Id
	@GeneratedValue(generator = "uuid")
	@org.hibernate.annotations.GenericGenerator(name = "uuid", strategy = "uuid")
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
