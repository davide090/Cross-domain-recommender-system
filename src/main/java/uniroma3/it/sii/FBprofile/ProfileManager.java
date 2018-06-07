package uniroma3.it.sii.FBprofile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* sulla base dei likes recuperati, crea un file parametrico rispetto id utente, contenente i vettori estratti dal modello */
public class ProfileManager {

	private String userID;
	private File model;

	public ProfileManager(String id) {
		this.userID = id;
		//this.model = new File("/home/davide/workspace/SII/src/main/resources/models/GoogleNews-vectors-negative300.bin");
	}


	
	
	
	

	//in input una lista di vettori (lista di stringhe, ogni stringa è in realta un double), restituisce un vettore che è la media degli input
	public static List<String> averagesVectors(List<List<String>> vectors) {

		List<String> res = new ArrayList<>();

		int pos = 0;
		res = vectors.get(0);


		while(pos<vectors.size()) {
			for(List<String> vec : vectors.subList(1,vectors.size())) {

				double tmp = Double.parseDouble(res.get(pos));
				tmp = tmp + Double.parseDouble(vec.get(pos));

				res.set(pos, String.valueOf(tmp));
			}
			pos++;

		}


		pos = 0;
		for(String s : res) {

			double b = Double.parseDouble(s);
			String nuovo = String.valueOf(b/vectors.size());
			res.set(pos, nuovo);

			pos++;

		}


		return res;
	}

	// input un file txt, sostituisce ogni spazio con underscore
	public static void cleanResult(File file) throws IOException {


		String linea;

		File temp = new File("/home/davide/workspace/SII/src/main/resources/likes/tmp.txt");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		FileWriter fw = new FileWriter(temp);
		BufferedWriter bw = new BufferedWriter(fw);


		while ((linea = br.readLine())!= null) {
			bw.write(linea.replaceAll(" ", "_").replaceAll("-", "_").replaceAll("'", ""));
			bw.newLine(); 
		}

		br.close();
		bw.close();
		file.delete();
		temp.renameTo(file);


	}







	//Getters & Setters
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public File getModel() {
		return model;
	}

	public void setModel(File model) {
		this.model = model;
	}
}
