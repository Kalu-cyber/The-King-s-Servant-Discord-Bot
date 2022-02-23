package com.kalu.Bot.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.kalu.Bot.Bot;

public class DataSaveManager {
	static DataSave data = new DataSave();
	public static void uploadData() {
		data.setGuilds();
		File f = new File("data.json");
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			PrintWriter  pw = new PrintWriter(f);
			pw.println(Bot.getGson().toJson(data));
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void downloadData() {
		File f = new File("data.json");
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			FileReader fr = new FileReader(f);
			data = Bot.getGson().fromJson(fr, DataSave.class);
			data.getGuilds();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
