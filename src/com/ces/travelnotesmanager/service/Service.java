package com.ces.travelnotesmanager.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.ces.travelnotesmanager.dao.Dao;
import com.ces.travelnotesmanager.model.Note;

public class Service {
	private static Service instance = null;

	private Service() {
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

	public void createSomeObjects() throws ParseException {
		Dao d = Dao.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		d.addNote(new Note(0, "Madrid", "Madrid, Spain", "Warm and sunny", df
				.parse("20/07/2011"), true));
		d.addNote(new Note(1, "Alicante", "Alicante, Spain",
				"Sea, sun, litter", df.parse("22/07/2011"), false));
		d.addNote(new Note(2, "Marseille", "Marseille, France",
				"Rocks, sea, warm", df.parse("15/10/2012"), true));
	}
}
