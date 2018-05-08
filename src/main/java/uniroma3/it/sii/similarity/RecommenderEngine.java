package uniroma3.it.sii.similarity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.InternalServerErrorException;

import uniroma3.it.sii.conf.Configuration;

public class RecommenderEngine {



	public RecommenderEngine() {}


	//recommendation using model 1
	public List<String> getRecommendation1(List<String> profiles, List<String> data) throws IOException{

		List<String> results = new ArrayList<String>();


		for(String profilePath : profiles){

			File profile = new File(profilePath);

			for(String dataPath : data) {

				File dati = new File(dataPath);
				String resultPath = getRecommendationModel1(profile, dati);
				results.add(resultPath);

			}
		}


		return results;
	}

	//recommendation using model 2
	public List<String> getRecommendation2(List<String> profiles, List<String> data) throws IOException{

		List<String> results = new ArrayList<String>();

		for(String profilePath : profiles){

			File profile = new File(profilePath);

			for(String dataPath : data) {
				File dati = new File(dataPath);
				String resultPath = getRecommendationModel2(profile, dati);
				results.add(resultPath);
			}
		}

		return results;
	}



	//model 1
	public String getRecommendationModel1(File profile, File data) throws IOException {

		//carica file di configurazione
		Configuration conf = Configuration.getInstance();

		//legge profilo
		FileReader fr = new FileReader(profile);
		BufferedReader profileReader = new BufferedReader(fr);

		String keyword;

		//file che raccoglie i risultati
		String profileName = profile.getName().substring(0, profile.getName().indexOf("."));
		String dataName = data.getName().substring(0, data.getName().indexOf("."));

		File result = new File(conf.getSimilarityPath() + profileName + "@" + dataName + "_model1.txt");

		//scrive i risultati in un file temporaneo
		FileWriter fw = new FileWriter(result);
		BufferedWriter bw = new BufferedWriter(fw);



		//dati in un file temporaneo, cancello i dati che non sono sul modello (evito confronti inutili)
		File dataTmp = new File(conf.getSimilarityPath() + "tmpModel1.txt");
		dataTmp = cleanFile1(data, dataTmp);


		//inizia confronto fra profilo e data
		while ((keyword = profileReader.readLine()) != null) {


			if(getExistModel1(keyword)) {

				String linea;

				//legge dati
				FileReader frd = new FileReader(dataTmp);
				BufferedReader dataReader = new BufferedReader(frd);

				//scorre il file contenente i punti di interesse (ripulito)
				while((linea = dataReader.readLine()) != null) {

					System.out.println("similarita fra " + keyword +"/"+linea);
					List<String> words = Arrays.asList(linea.substring(linea.indexOf("[")+1, linea.indexOf("]")).split(","));
					List<Double> sims = new ArrayList<>();

					for(String word : words) {
						String r = getSimilarityModel1(keyword, word);
						if(r!=null)
							sims.add(Double.valueOf(r));
					}

					String res = String.valueOf(Collections.max(sims));

					if(res != null)
						bw.write(keyword + "/" + linea.substring(0, linea.indexOf("[")) + ":" + res);

					bw.newLine();
				}				
				dataReader.close();
			}
		}

		bw.close();
		profileReader.close();
		dataTmp.delete();



		return result.getAbsolutePath();
	}



	//model 2
	public String getRecommendationModel2(File profile, File data) throws IOException {

		Configuration conf = Configuration.getInstance();

		//legge profilo
		FileReader fr = new FileReader(profile);
		BufferedReader profileReader = new BufferedReader(fr);

		String keyword;

		//file che raccoglie i risultati
		String profileName = profile.getName().substring(0, profile.getName().indexOf("."));
		String dataName = data.getName().substring(0, data.getName().indexOf("."));

		File result = new File(conf.getSimilarityPath() + profileName + "@" + dataName + "_model2.txt");

		//scrive i risultati in un file temporaneo
		FileWriter fw = new FileWriter(result);
		BufferedWriter bw = new BufferedWriter(fw);



		//dati in un file temporaneo, cancello i dati che non sono sul modello (evito confronti inutili)
		File dataTmp = new File(conf.getSimilarityPath() + "tmpModel2.txt");
		dataTmp = cleanFile2(data, dataTmp);


		//inizia confronto fra profilo e data
		while ((keyword = profileReader.readLine()) != null) {


			if(getExistModel2(keyword)) {

				String linea;

				//legge dati
				FileReader frd = new FileReader(dataTmp);
				BufferedReader dataReader = new BufferedReader(frd);

				//scorre il file contenente i punti di interesse (ripulito)
				while((linea = dataReader.readLine()) != null) {

					System.out.println("similarita fra " + keyword +"/"+linea);
					List<String> words = Arrays.asList(linea.substring(linea.indexOf("[")+1, linea.indexOf("]")).split(","));
					List<Double> sims = new ArrayList<>();

					for(String word : words) {
						String r = getSimilarityModel2(keyword, word);
						if(r!=null)
							sims.add(Double.valueOf(r));
					}

					String res = String.valueOf(Collections.max(sims));

					if(res != null)
						bw.write(keyword + "/" + linea.substring(0, linea.indexOf("[")) + ":" + res);

					bw.newLine();
				}				
				dataReader.close();
			}
		}

		bw.close();
		profileReader.close();
		dataTmp.delete();



		return result.getAbsolutePath();
	}






	//similarity using model 1
	public String getSimilarityModel1(String word1, String word2) throws MalformedURLException{

		Configuration conf = Configuration.getInstance();

		URL url = new URL(conf.getModel1_similarity_url() + "w1="+word1 +"&w2="+word2);
		HttpURLConnection connection;

		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			connection.connect();

			InputStream res = connection.getInputStream();

			BufferedReader rd = new BufferedReader(new InputStreamReader(res, Charset.forName("UTF-8")));
			StringBuffer response = new StringBuffer(); 
			String line;

			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			res.close();
			connection.disconnect();

			return response.toString();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}

	}

	//inserisce nel file tmp solo righe che sono nel modello 1 (formato:   terme_di_caracalla[terme,di,caracalla])
	public File cleanFile1(File data, File tmp) throws IOException {


		BufferedReader reader = new BufferedReader(new FileReader(data));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tmp));

		String currentLine;

		while((currentLine = reader.readLine()) != null) {

			List<String> atoms = Arrays.asList(currentLine.split("_"));

			List<String> wordsOK = new ArrayList<>();
			boolean esiste = false;

			for(String word : atoms) {
				if(getExistModel1(word)) {
					wordsOK.add(word);
					esiste =true;
				}
			}

			if(esiste) {
				System.out.println(currentLine+wordsOK.toString());
				String stringa = (currentLine+wordsOK.toString()).replaceAll(" ", "");
				writer.write(stringa);
				writer.newLine();
			}

		}

		writer.close(); 
		reader.close(); 

		return tmp;

	}


	//inserisce nel file tmp solo righe che sono nel modello 2
	public File cleanFile2(File data, File tmp) throws IOException {


		BufferedReader reader = new BufferedReader(new FileReader(data));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tmp));

		String currentLine;

		while((currentLine = reader.readLine()) != null) {

			List<String> atoms = Arrays.asList(currentLine.split("_"));

			List<String> wordsOK = new ArrayList<>();
			boolean esiste = false;

			for(String word : atoms) {
				if(getExistModel2(word)) {
					wordsOK.add(word);
					esiste =true;
				}
			}

			if(esiste) {
				System.out.println(currentLine+wordsOK.toString());
				String stringa = (currentLine+wordsOK.toString()).replaceAll(" ", "");
				writer.write(stringa);
				writer.newLine();
			}

		}

		writer.close(); 
		reader.close(); 

		return tmp;

	}




	public boolean getExistModel1(String word) throws MalformedURLException {

		boolean exist = false;
		Configuration conf = Configuration.getInstance();

		URL url = new URL(conf.getModel1_wordvector_url() + "word="+word);
		
		try {
			HttpURLConnection connection;

			connection = (HttpURLConnection) url.openConnection();

			connection.setRequestProperty("Accept-Charset", "UTF-8");

			InputStream res = connection.getInputStream();

			connection.connect();

			BufferedReader rd = new BufferedReader(new InputStreamReader(res));
			StringBuffer response = new StringBuffer(); 
			String line;

			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			res.close();

			if(connection.getContentLength() == 4)
				return false;


			if(response.toString().equals("null")) {
				System.out.println(word + " riconosciuto come null");
				return false;
			}

			System.out.println("response "+ word +": " + response);

			connection.disconnect();
			exist = true;

			return exist;

		} catch (IOException e) {
			System.out.println("non esiste in model 1: " + word);
			// TODO Auto-generated catch block
			return false;
		}
	}



	//similarity using model 2
	public String getSimilarityModel2(String word1, String word2) throws MalformedURLException{

		Configuration conf = Configuration.getInstance();

		URL url = new URL(conf.getModel2_similarity_url() + "ws1="+word1 +"&ws2="+word2);
		HttpURLConnection connection;

		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			connection.connect();

			InputStream res = connection.getInputStream();

			BufferedReader rd = new BufferedReader(new InputStreamReader(res, Charset.forName("UTF-8")));
			StringBuffer response = new StringBuffer(); 
			String line;

			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			res.close();
			connection.disconnect();

			return response.toString();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}

	}






	public boolean getExistModel2(String word) {


		try {		
			Configuration conf = Configuration.getInstance();
			
			URL url = new URL(conf.getModel2_wordvector_url() + "positive="+word);

			HttpURLConnection connection;

			connection = (HttpURLConnection) url.openConnection();

			connection.setRequestProperty("Accept-Charset", "UTF-8");

			InputStream res = connection.getInputStream();

			connection.connect();

			BufferedReader rd = new BufferedReader(new InputStreamReader(res));
			StringBuffer response = new StringBuffer(); 
			String line;

			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			res.close();

			System.out.println("response "+ word +": " + response);

			connection.disconnect();
			return true;

		} catch (InternalServerErrorException | IOException e) {

			System.out.println("non esiste model 2: " + word);
			return false;
		}

	}









	//	//input il profilo utente, e i dati che si vogliono filtrare sulla base del profilo utente
	//	public void getRecommendation(File profile, File data) throws IOException {
	//
	//
	//		String keyword;
	//
	//		File temp = new File("/home/davide/workspace/SII/src/main/resources/similarity_tmp" + data.getName() + ".txt");
	//
	//		//legge profilo
	//		FileReader fr = new FileReader(profile);
	//		BufferedReader profileReader = new BufferedReader(fr);
	//
	//
	//		//scrive i risultati in un file temporaneo
	//		FileWriter fw = new FileWriter(temp);
	//		BufferedWriter bw = new BufferedWriter(fw);
	//
	//
	//		//scorre il file contenente i likes dal profilo utente
	//		while ((keyword = profileReader.readLine()) != null) {
	//
	//
	//			System.out.println(keyword + " \n");
	//			String word = keyword.substring(0, keyword.indexOf(":"));
	//			double[] vectorProfile = exctractVector(keyword);
	//			String linea;
	//
	//			//legge dati
	//			FileReader frd = new FileReader(data);
	//			BufferedReader dataReader = new BufferedReader(frd);
	//
	//			//scorre il file contenente i punti di interesse
	//			while((linea = dataReader.readLine()) != null) {
	//
	//				String wordData = linea.substring(0, linea.indexOf(":"));
	//				double[] vectorData = exctractVector(linea);
	//				double similarity = getSimilarity(vectorProfile, vectorData);
	//
	//				bw.write("similarity " + word + "/" + wordData + " : " + String.valueOf(similarity));
	//				bw.newLine();
	//
	//			}		
	//			dataReader.close();
	//
	//		}
	//
	//		profileReader.close();
	//		bw.close();
	//
	//	}
	//
	//
	//	//estrae vettore dalla riga
	//	private double[] exctractVector(String linea) {
	//
	//		List<Double> a = (List<Double>) new ArrayList<Double>();
	//
	//		String vec = linea.substring(linea.indexOf("[")+1, linea.indexOf("]"));
	//
	//		String[] split = vec.split(",");
	//
	//		for(int i=0; i<split.length; i++) {
	//			a.add(Double.parseDouble(split[i]));
	//		}
	//
	//		double[] res = new double[a.size()];
	//
	//		for(int i=0; i<res.length; i++) {
	//			res[i] = a.get(i);
	//		}
	//		return res;
	//	}
	//
	//
	//
	//
	//
	//
	//
	//
	//	// calcola coseno similarità
	//	private static double getSimilarity(double[] vectorA, double[] vectorB) {
	//
	//		double dotProduct = 0.0;
	//		double normA = 0.0;
	//		double normB = 0.0;
	//		for (int i = 0; i < vectorA.length; i++) {
	//			dotProduct += vectorA[i] * vectorB[i];
	//			normA += Math.pow(vectorA[i], 2);
	//			normB += Math.pow(vectorB[i], 2);
	//		} 
	//
	//		return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	//
	//	}



}
