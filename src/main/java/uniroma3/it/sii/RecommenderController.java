package uniroma3.it.sii;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import uniroma3.it.sii.conf.Configuration;
import uniroma3.it.sii.profile.FacebookManager;
import uniroma3.it.sii.similarity.RecommenderEngine;
import uniroma3.it.sii.wikidata.WikidataManager;


@Controller
public class RecommenderController {


	//	@RequestParam(name="name", required=false, defaultValue="World") String name, Model model

	@GetMapping("recommendation/{accessToken}/{city}/{country}/{attractions}/{likes}/{latitude}/{longitude}/{raggio}")
	public String greeting(@PathVariable String accessToken, @PathVariable String city, @PathVariable String country, @PathVariable String[] attractions, 
			@PathVariable String[] likes, @PathVariable String latitude, @PathVariable String longitude, 
			@PathVariable String raggio, Model model) throws IOException {


		String access2 = "EAACoQZAGUlWwBABzR9b1x29LoFr8WPzjekTdJwfO6sfIcLMZBKPDIyVqH0LpYEyaE3PeNAMVU6Wqh6"
				+ "TfaKV6E1E4Yl3MB6thNu9zZAu7nvaTlgY6KvUwPawJCmbZBJtSf0FBFxZCnpxe458Pk8T5WDAwmRhHDN7kZD";

		//lista di filepath con i profili per tipologia di like
		List<String> filesFB = fetchFB(accessToken, likes);

		char first1 = Character.toUpperCase(city.charAt(0));
		String citta = first1 + city.substring(1);


		char first2 = Character.toUpperCase(country.charAt(0));
		String paese = first2 + country.substring(1);


		System.out.println("citta: " + citta + ", paese: " + paese);
		//lista di filepath con i file wikidata (creati a partire dalla scelta dell'utente)
		List<String> filesData = fetchWikidata(attractions, citta, paese);


		//lista di filepath con i file wikidata ordinati in base alla distanza dalla posizione dell'utente
		List<String> filesOrderedData = fetchOrderedWikidata(attractions, city, latitude, longitude, raggio);


		List<String> res1 = recommendation1(filesFB, filesData);
		List<String> results1 = new ArrayList<>();


		List<String> res2 = recommendation2(filesFB, filesData);
		List<String> results2 = new ArrayList<>();


		for(String s : res1) {

			File file = new File(s);

			FileReader fr = new FileReader(file);
			BufferedReader reader = new BufferedReader(fr);

			String linea;

			while((linea = reader.readLine()) != null) {
				if(Double.parseDouble(linea.substring(linea.indexOf(":")+1)) >= 0.01)
					results1.add(linea);	
			}
			reader.close();
		}


		for(String s : res2) {

			File file = new File(s);

			FileReader fr = new FileReader(file);
			BufferedReader reader = new BufferedReader(fr);

			String linea;

			while((linea = reader.readLine()) != null) {
				if(Double.parseDouble(linea.substring(linea.indexOf(":")+1)) >= 0.01)
					results2.add(linea);	
			}
			reader.close();
		}

		List<String> r = new ArrayList<>();
		r = getTotalRecommendation(results1, results2);

		List<String> tot = new ArrayList<>();

		//elimina i doppioni ed estra la similarita massima fra i vari poi doppi (poi:similarita come risultato)
		tot = ripulisci(r);

		//crea un file temporaneo con i vari tipi di p.o.i. ordinati globalmente in base alla distanza
		String pathOrderedData = orderedDataToFile(filesOrderedData);


		System.out.println("list 1 vuota --- > " + results1.isEmpty());
		System.out.println("list 2 vuota --- > " + results2.isEmpty());


		model.addAttribute("list1", results1);
		model.addAttribute("list2", results2);
		model.addAttribute("tot", tot);


		List<String> totOrdered = new ArrayList<>();
		totOrdered = orderTotalRecommendation(tot, pathOrderedData);

		System.out.println("list ordered vuota --- > " + totOrdered.isEmpty());


		List<String> totOrderedDavide = new ArrayList<>();
		totOrderedDavide = sortByDistance(orderDavideTotalRecommendation(tot, pathOrderedData));

		model.addAttribute("totOrdered", totOrdered);
		model.addAttribute("totOrderedDavide", totOrderedDavide);


		return "results";
	}




	//RIPULISCI PROBLEMA STREAM
	private List<String> ripulisci(List<String> tot) {

		List<String> res = new ArrayList<>();


		for(String s : tot) {

			String[] fields = s.split("/");
			String place = fields[1].split(":")[0];

			List<String> result = tot.stream()               
					.filter(line -> (line.split("/")[1].split(":")[0]).equals(place))  
					.collect(Collectors.toList()); 


			System.out.println("res: " + result.toString());

			double m = 0.0;


			for(String p : result) {

				double t = Double.parseDouble(p.split(":")[1]);

				if(t > m) {
					m = t;
				}
			}

			String r = place + ":" + String.valueOf(m);
			if(!res.contains(r))
				res.add(r);
		}



		return res;
	}





	//formula: sim/pos. (simlarità / posizione dell'elemento nella lista ordinata in base alla distanza)
	private List<String> orderDavideTotalRecommendation(List<String> tot, String pathOrderedData) throws IOException {


		List<String> res = new ArrayList<>();

		File ordered = new File(pathOrderedData);
		FileReader fr = new FileReader(ordered);
		BufferedReader dataReader = new BufferedReader(fr);

		String linea;

		int position = 1;

		while((linea = dataReader.readLine()) != null) {

			String poiOrdered = linea.split(":")[0];

			for(String s : tot) {

				String poi = s.split(":")[0];
				String sim = s.split(":")[1];

				if(poi.equals(poiOrdered)) {
					System.out.println("poi: " + poi + ", orde: " + poiOrdered + ", position: " + position);
					String tmpRes = poi + ":" + String.valueOf(Double.parseDouble(sim) / position);
					res.add(tmpRes);
				}
			}

			position++;
		}

		dataReader.close();

		return res;
	}




	private String orderedDataToFile(List<String> filesOrderedData) throws IOException {
		
		Configuration conf = Configuration.getInstance();
		
		File tmp = new File(conf.getResourcePath() + "tmpOrdered.txt");

		FileWriter fw = new FileWriter(tmp);
		BufferedWriter bw = new BufferedWriter(fw);

		for(String path : filesOrderedData) {

			FileReader fr = new FileReader(path);
			BufferedReader dataReader = new BufferedReader(fr);
			String linea;

			while((linea = dataReader.readLine()) != null) {
				bw.write(linea);
				bw.newLine();
			}
			dataReader.close();
			fr.close();
		}
		bw.close();
		fw.close();

		//path del file ordinato in base alla distanza
		String orderedPath = sortByDistanceInFile(tmp.getAbsolutePath());


		return orderedPath;
	}


	private String sortByDistanceInFile(String path) throws IOException {

		File tmp = new File(path);
		BufferedReader reader = new BufferedReader(new FileReader(tmp));
		ArrayList<NameDistanceRow> rows = new ArrayList<NameDistanceRow>();

		String currentLine = reader.readLine();

		while (currentLine != null)
		{
			String[] rowData = currentLine.split(":");

			String name = rowData[0];

			double distance = Double.valueOf(rowData[1]);
			rows.add(new NameDistanceRow(name, distance));
			currentLine = reader.readLine();
		}
		
		Configuration conf = Configuration.getInstance();

		File sorted = new File(conf.getResourcePath() + "sortedtmp.txt");

		Collections.sort(rows, new DistanceCompare());
		BufferedWriter writer = new BufferedWriter(new FileWriter(sorted));

		for (NameDistanceRow row : rows) {

			writer.write(row.getName());
			writer.write(":"+row.getDistance());
			writer.newLine();
		}

		reader.close();
		writer.close();
		tmp.delete();

		return sorted.getAbsolutePath();
	}



	private List<String> sortByDistance(List<String> daordinare) throws IOException {


		ArrayList<NameDistanceRow> rows = new ArrayList<NameDistanceRow>();


		for(String s : daordinare) {

			String[] rowData = s.split(":");
			String name = rowData[0];

			double distance = Double.valueOf(rowData[1]);
			rows.add(new NameDistanceRow(name, distance));
		}

		List<String> res = new ArrayList<>();

		Collections.sort(rows, new DistanceCompare());
		Collections.reverse(rows);


		for (NameDistanceRow row : rows) {
			res.add(row.getName() + ":" + row.getDistance());
		}

		return res;
	}





	//ri-ordina i risultati in base alla distanza dei poi (da precedenza alla distanza rispetto la similarita)
	private List<String> orderTotalRecommendation(List<String> tot, String pathOrdered) throws IOException {

		List<String> res = new ArrayList<>();

		File ordered = new File(pathOrdered);
		FileReader fr = new FileReader(ordered);
		BufferedReader dataReader = new BufferedReader(fr);

		String linea;

		while((linea = dataReader.readLine()) != null) {

			for(String s : tot) {

				//				System.out.println("s " + s);
				if((s.split(":")[0]).equals(linea.split(":")[0])) {
					res.add(linea+":"+s.split(":")[1]);
				}
			}
		}

		dataReader.close();

		return res;
	}






	//ritorna la lista di filepath contenente i poi ordinati in base alla distanza 
	private List<String> fetchOrderedWikidata(String[] params, String city, String latitude, String longitude, String raggio) throws IOException {


		List<String> filePath = new ArrayList<String>();
		WikidataManager wiki = new WikidataManager();

		wiki.userInCity(city, latitude, longitude);

		for(int i =0; i<params.length; i++) {
			String path = getOrderedData(wiki, params[i], city, latitude, longitude, raggio);
			filePath.add(path);
		}

		return filePath;



	}


	//metodo ausiliario per avere poi ordinati in base alla distanza (richiama i metodi di WikidataManager)
	private String getOrderedData(WikidataManager wiki, String string, String city, String latitude, String longitude, String raggio) throws IOException {

		String filePath = null;

		if(string.equals("squares"))
			filePath = wiki.getOrderedSquaresCity(city, latitude, longitude, raggio);
		else
			if(string.equals("churchs"))
				filePath = wiki.getOrderedChurchsCity(city, latitude, longitude, raggio);
			else
				if(string.equals("monuments"))
					filePath = wiki.getOrderedMonumentsCity(city, latitude, longitude, raggio);
				else
					if(string.equals("tourist"))
						filePath = wiki.getOrderedTouristAttractionsCity(city, latitude, longitude, raggio);
					else
						if(string.equals("museums"))
							filePath = wiki.getOrderedMuseumsCity(city, latitude, longitude, raggio);

		if(filePath==null) System.out.println("no match params");

		return filePath;
	}



	// mette insieme i risultati forniti dai due modelli (se una corrispondenza uso quella, se due considero la piu alta similarita)
	private List<String> getTotalRecommendation(List<String> result1, List<String> result2){

		List<String> res = new ArrayList<>();

		for(String a : result1) {

			String key = a.substring(0, a.indexOf(":"));
			boolean trovato = false;

			for(String b : result2) {

				if(b.contains(key)) {

					res.add(key+":"+maxSimilarity(a,b));
					trovato = true;
				}
			}

			if(!trovato)
				res.add(a);
		}




		for(String a : result2) {

			String key = a.substring(0, a.indexOf(":"));
			boolean trovato = false;

			for(String b : result1) {

				if(b.contains(key)) {

					res.add(key+":"+maxSimilarity(a,b));
					trovato = true;
				}
			}

			if(!trovato)
				res.add(a);
		}



		res = cleanResults(res);

		return res;
	}




	private List<String> cleanResults(List<String> res) {

		List<String> fin = new ArrayList<>();

		for(String s : res) {

			if(!fin.contains(s))
				fin.add(s);
		}


		return fin;
	}



	private String maxSimilarity(String a, String b) {


		double aa = Double.parseDouble(a.substring(a.indexOf(":")+1));
		double bb = Double.parseDouble(b.substring(b.indexOf(":")+1));

		if(aa >= bb) 
			return String.valueOf(aa);

		else
			return String.valueOf(bb);
	}



	private List<String> recommendation1(List<String> filesFB, List<String> filesData) throws IOException {


		System.out.println("DENTRO MODEL 1");
		RecommenderEngine rec = new RecommenderEngine();

		List<String> results = rec.getRecommendation1(filesFB, filesData);


		return results;
	}



	private List<String> recommendation2(List<String> filesFB, List<String> filesData) throws IOException {


		System.out.println("DENTRO MODEL 2");

		RecommenderEngine rec = new RecommenderEngine();

		List<String> results = rec.getRecommendation2(filesFB, filesData);


		return results;
	}







	//ritorna la lista di filepath contenente i dati (un file per ogni tipologia di like)
	private List<String> fetchFB(String accessToken, String[] likes) throws IOException {

		List<String> filePath = new ArrayList<String>();
		FacebookManager fb = new FacebookManager(accessToken);

		for(int i =0; i<likes.length; i++) {
			String path = getLikes(fb, likes[i]);
			filePath.add(path);
		}

		return filePath;
	}




	private String getLikes(FacebookManager fb, String likes) throws IOException {

		String filePath = null;

		if(likes.equals("all"))
			filePath = fb.getAllLikes();
		else
			if(likes.equals("movies"))
				filePath = fb.getMoviesLikes();
			else
				if(likes.equals("music"))
					filePath = fb.getMusicLikes();


		if(filePath==null) System.out.println("no match like param");

		return filePath;
	}


	//ritorna la lista di filepath contenente i dati (un file per ogni tipo di attrazione)
	public List<String> fetchWikidata(String[] params, String city, String country) throws IOException {

		List<String> filePath = new ArrayList<String>();
		WikidataManager wiki = new WikidataManager();


		for(int i =0; i<params.length; i++) {
			String path = getData(wiki, params[i], city, country);
			filePath.add(path);
		}

		return filePath;
	}



	private String getData(WikidataManager wiki, String string, String city, String country) throws IOException {


		String filePath = null;

		if(string.equals("squares"))
			filePath = wiki.getSquaresCity(city, country);
		else
			if(string.equals("churchs"))
				filePath = wiki.getChurchsCity(city, country);
			else
				if(string.equals("monuments"))
					filePath = wiki.getMonumentsCity(city, country);
				else
					if(string.equals("tourist"))
						filePath = wiki.getTouristAttractionsCity(city, country);
					else
						if(string.equals("museums"))
							filePath = wiki.getMuseumsCity(city, country);

		if(filePath==null) System.out.println("no match params");

		return filePath;
	}












}
