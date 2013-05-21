package com.ces.travelnotesmanager.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ces.travelnotesmanager.dao.Dao;
import com.ces.travelnotesmanager.model.Note;

public class Service {
	private static Service instance = null;
	private static SimpleDateFormat dateFormat;

	private Service() {
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try {
			createSomeObjects();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Service getInstance() {
		if (instance == null)
			instance = new Service();

		return instance;
	}

	public static Date convertDate(String d) throws ParseException {
		return dateFormat.parse(d);
	}

	public static String formatDate(Date d){
		return dateFormat.format(d);
	}
	public void createSomeObjects() throws ParseException {
		Dao d = Dao.getInstance();

		d.addNote(new Note(0, "Madrid", "Madrid, Spain", "Warm and sunny",
				convertDate("20/07/2011"), true));
		d.addNote(new Note(1, "Alicante", "Alicante, Spain",
				"Sea, sun, litter", convertDate("22/07/2011"), false));
		d.addNote(new Note(2, "Marseille", "Marseille, France",
				"Rocks, sea, warm", convertDate("15/10/2012"), true));
	}
}
