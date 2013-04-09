package com.FrancescoDeSa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
//import java.util.Set;
import java.util.StringTokenizer;

import org.bukkit.entity.Player;

public class mcapSession {
	
	public mcapSession(File file){
		this.file = file;
		sdf = new SimpleDateFormat("dd/MM/yyyy");
		if(file.exists()){
			McMMOCap.logger.info("File exists! I'm going to read it");
			try {
				read();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			McMMOCap.logger.info("File doesn't exist!");
			try {
				file.createNewFile();
				lastReset = today();
				save();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void save(){
		McMMOCap.logger.info("Saving session to file...");
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(sdf.format(lastReset.getTime()));
			out.newLine();
			if(!data.isEmpty()){
				String[] chiavi = data.keySet().toArray(new String[0]);
				for(String chiave : chiavi){
					String line = chiave+"|"+data.get(chiave);
					out.write(line);
					McMMOCap.logger.fine("I wrote the line: "+line);
					out.newLine();
				}
			}
			out.close();
			McMMOCap.logger.info("Saving complete!");
			
		} catch (IOException e) {
			McMMOCap.logger.warning("We had a fatal error while saving...");
			e.printStackTrace();
		}
	}
	public void read() throws IOException, ParseException{
		try {
			McMMOCap.logger.info("Loading session from file...");
			BufferedReader in = new BufferedReader(new FileReader(file));
			String dateline;
			dateline = in.readLine();
			lastReset = new GregorianCalendar();
			Date giusta = sdf.parse(dateline);
			lastReset.setTime(giusta);
			String line;
			line = in.readLine();
			while(line != null){
				StringTokenizer st = new StringTokenizer(line,"|");
				data.put(st.nextToken(), Integer.parseInt(st.nextToken()));
				McMMOCap.logger.fine("I read the line: "+line);
				line = in.readLine();
			}
			in.close();
			McMMOCap.logger.info("Load complete!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public boolean Check(Player player, int cap, int amount){
		if(today().after(lastReset)){
			data = null;
			lastReset = today();
			return true;
		}
		else{

			int exp = data.get(player.getName());
			int newexp = exp+amount;
			if(newexp > cap)return false;
			else return true;
		}
	}
	public void register(Player player, int amount){
		String name = player.getName();
		int exp = 0;
		if(data.containsKey(name))
			exp = data.get(name);
		data.put(name, exp+amount);
	}
	private GregorianCalendar today(){
		GregorianCalendar now  = new GregorianCalendar();
		return new GregorianCalendar(now.get(GregorianCalendar.YEAR),now.get(GregorianCalendar.MONTH),now.get(GregorianCalendar.DAY_OF_MONTH));
	}
	
	private SimpleDateFormat sdf;
	private GregorianCalendar lastReset;
	private Map<String, Integer> data = new HashMap<String,Integer>();
	private File file;
}
