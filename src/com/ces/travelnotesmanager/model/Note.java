package com.ces.travelnotesmanager.model;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable{
	private static final long serialVersionUID = 1L;
	private long id;
	private String title, address, description, image;
	private Date date;
	private boolean visitAgain;
	
	public Note(){
	}

	public Note(String title, String address, String description,
			Date date, boolean visitAgain) {
		this();
		this.title = title;
		this.address = address;
		this.description = description;
		this.date = date;
		this.visitAgain = visitAgain;
	}
	
	public Note(String title, String address, String description,
			Date date, boolean visitAgain, String image){
		this(title, address, description, date, visitAgain);
		this.image = image;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Note){
			Note n = (Note) o;
			return n.getId() == this.getId();
		}
		
		return super.equals(o);
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
