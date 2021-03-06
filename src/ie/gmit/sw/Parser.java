package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

public class Parser implements Runnable {
	
	// private BlockingQueue<LanguageEntry> q = null;
	private Database db = null;
	private String file;
	private int k = 5;
	
	public Parser(String file, int k) {
		this.file = file;
		this.k = k;
	}
	
	public void setDb(Database db) {
		this.db = db;
	}
	
	@Override 
	public void run () {
		
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader
					(new FileInputStream(file)));
			
			String line = null;
			
			while ((line=br.readLine()) != null) {
				String[] record = line.trim().split("@");
				if(record.length != 2) continue;
				parse(record[0], record[1]);
			}
			
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void parse(String text, String lang, int... ks) {
		Language language = Language.valueOf(lang);
		
		for (int i = 0; i <= text.length() - k; i+=2) {
			CharSequence kmer = text.substring(i, i + k);
			db.add(kmer, language);
		}
	}
	
	public void analyseQuery(String f) throws IOException 
	{
		
		int frequency = 1;
		String kmer = "";
		int kmerH=0;

		Map<Integer, LanguageEntry> q = new TreeMap<>();
		
		BufferedReader br1;
		br1 = new BufferedReader(new InputStreamReader
					(new FileInputStream(f)));
		String line = null;
		
		while ((line=br1.readLine())!=null) {	
			
			for (int i = 0; i <= line.length() - k; i+=2) {	
				kmer = line.trim().substring(i, i+k);
				kmerH = kmer.hashCode();
			}
			
			if (q.containsKey(kmerH)) {
				frequency += q.get(kmerH).getFrequency();
			}
			LanguageEntry lang = new LanguageEntry(kmerH, frequency);
			q.put(frequency, lang);
		}
		
		Language language = db.getLanguage(q);
		System.out.println("The language appears to be " + language);
		br1.close();
		
	}
}
		

