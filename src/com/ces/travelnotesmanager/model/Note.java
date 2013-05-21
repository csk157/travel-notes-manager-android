package com.ces.travelnotesmanager.model;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String title, address, description;
	private Date date;
	private boolean visitAgain;
	
	public Note(){
		
	}

	public Note(int id, String title, String address, String description,
			Date date, boolean visitAgain) {
		super();
		this.id = id;
		this.title = title;
		this.address = address;
		this.description = description;
		this.date = date;
		this.visitAgain = visitAgain;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isVisitAgain() {
		return visitAgain;
	}

	public void setVisitAgain(boolean visitAgain) {
		this.visitAgain = visitAgain;
	}
	
	@Override
	public String toString(){
		return this.title;
	}
}
