package uniroma3.it.sii.wikidata;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

import uniroma3.it.sii.conf.Configuration;



public class WikidataManager {

	private String sparqlService;
	private String prefixes;
	//	private File model;


	public WikidataManager() {

		this.sparqlService = "https://query.wikidata.org/sparql";

		this.prefixes = " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
				"PREFIX wd: <http://www.wikidata.org/entity/>\n" + 
				" PREFIX wdt: <http://www.wikidata.org/prop/direct/> \n" +
				"PREFIX wikibase: <http://wikiba.se/ontology#> \n" + 
				"PREFIX bd: <http://www.bigdata.com/rdf#> \n" + 
				"PREFIX schema: <http://schema.org/>\n"+
				"PREFIX geo: <http://www.opengis.net/ont/geosparql#>\n";

		//		this.model = new File("resources/models/GoogleNews-vectors-negative300.bin");
	}


	// metodi per ricavare info su alcune entita (chiese, piazze ecc)
	public String getChurchsCity(String city, String country) throws IOException {

		String id = getCityID(city, country).substring(28);

		System.out.println("------- id "+ city + " " + id + "------");

		String queryString = this.prefixes + "SELECT ?church ?label WHERE {\n" + 
				"  ?church wdt:P131 wd:" + id + ".\n" + 
				"  SERVICE wikibase:label { bd:serviceParam wikibase:language \"en,it\". }\n" + 
				"  ?church rdfs:label ?label.\n" + 
				"  ?church wdt:P31/wdt:P279* wd:Q16970.\n" + 
				"  FILTER(LANGMATCHES(LANG(?label), \"en\"))\n" + 
				"}";

		Query query = QueryFactory.create(queryString) ;

		QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(sparqlService, query);
		ResultSet results = qexec.execSelect() ;

		Configuration conf = Configuration.getInstance();

		File file = new File(conf.getWikidataPath() + "church@"+city+".txt");
		try {
			PrintWriter out = new PrintWriter(file);

			for(; results.hasNext(); ) {
				QuerySolution sol = results.nextSolution();
				RDFNode name = sol.get("label") ;
				out.println(cleanResults(name));
			}
			out.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block

			System.out.println("error at getChurchsCity");
			e.printStackTrace();
		}

		qexec.close();

		return file.getAbsolutePath();
	}




	//tourist attraction
	public String getTouristAttractionsCity(String city, String country) throws IOException {

		String id = getCityID(city, country).substring(28);

		System.out.println("------- id "+ city + " " + id + "------");
		String queryString =  this.prefixes + "SELECT ?church ?label WHERE {\n" + 
				"	 ?church wdt:P131 wd:" + id + "." + "\n" +
				"	 ?church rdfs:label ?label.\n" + 
				"	 ?church wdt:P31/wdt:P279* wd:Q570116. \n" + 
				"	FILTER(LANGMATCHES(LANG(?label),\"en\")) \n" + 
				"				}";

		Query query = QueryFactory.create(queryString) ;

		QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(sparqlService, query);
		ResultSet results = qexec.execSelect() ;

		Configuration conf = Configuration.getInstance();

		File file = new File(conf.getWikidataPath() + "touristAttraction@"+city+".txt");
		try {
			PrintWriter out = new PrintWriter(file);

			for(; results.hasNext(); ) {
				QuerySolution sol = results.nextSolution();
				RDFNode name = sol.get("label") ;
				out.println(cleanResults(name));
			}
			out.close();


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block

			System.out.println("error at getTouristAttractionsCity");
			e.printStackTrace();
		}


		qexec.close();

		return file.getAbsolutePath();

	}



	//square
	public String getSquaresCity(String city, String country) throws IOException {

		String id = getCityID(city, country).substring(28);

		String queryString =  this.prefixes + "SELECT ?church ?label WHERE {\n" + 
				"  ?church wdt:P131 wd:" + id + ".\n" +
				"  ?church rdfs:label ?label.\n" + 
				"  ?church wdt:P31/wdt:P279* wd:Q174782.\n" + 
				"  FILTER(LANGMATCHES(LANG(?label), \"en\"))\n" + 
				"}";

		Query query = QueryFactory.create(queryString) ;

		QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(sparqlService, query);
		ResultSet results = qexec.execSelect() ;

		Configuration conf = Configuration.getInstance();

		File file = new File(conf.getWikidataPath() + "square@"+city+".txt");
		try {
			PrintWriter out = new PrintWriter(file);

			for(; results.hasNext(); ) {
				QuerySolution sol = results.nextSolution();
				RDFNode name = sol.get("label") ;
				out.println(cleanResults(name));
			}
			out.close();


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block

			System.out.println("error at getSquaresCity");
			e.printStackTrace();
		}

		qexec.close();

		return file.getAbsolutePath();

	}


	//monuments
	public String getMonumentsCity(String city, String country) throws IOException {

		String id = getCityID(city, country).substring(28);

		String queryString =  this.prefixes + "SELECT ?church ?label WHERE {\n" + 
				"  ?church wdt:P131 wd:" + id + ".\n" +
				"  ?church rdfs:label ?label.\n" + 
				"  ?church wdt:P31/wdt:P279* wd:Q4989906.\n" + 
				"  FILTER(LANGMATCHES(LANG(?label), \"en\"))\n" + 
				"}";

		Query query = QueryFactory.create(queryString) ;

		QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(sparqlService, query);
		ResultSet results = qexec.execSelect() ;

		Configuration conf = Configuration.getInstance();


		File file = new File(conf.getWikidataPath() + "monument@"+city+".txt");
		try {
			PrintWriter out = new PrintWriter(file);

			for(; results.hasNext(); ) {
				QuerySolution sol = results.nextSolution();
				RDFNode name = sol.get("label") ;
				out.println(cleanResults(name));
			}
			out.close();


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block

			System.out.println("error at getMonuments");
			e.printStackTrace();
		}

		qexec.close();

		return file.getAbsolutePath();

	}


	//Museums
	public String getMuseumsCity(String city, String country) throws IOException {

		String id = getCityID(city, country).substring(28);

		String queryString =  this.prefixes + "SELECT ?church ?label WHERE {\n" + 
				"  ?church wdt:P131 wd:" + id + ".\n" +
				"  ?church rdfs:label ?label.\n" + 
				"  ?church wdt:P31/wdt:P279* wd:Q33506.\n" + 
				"  FILTER(LANGMATCHES(LANG(?label), \"en\"))\n" + 
				"}";

		Query query = QueryFactory.create(queryString) ;

		QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(sparqlService, query);
		ResultSet results = qexec.execSelect() ;

		Configuration conf = Configuration.getInstance();


		File file = new File(conf.getWikidataPath() + "museum@"+city+".txt");
		try {
			PrintWriter out = new PrintWriter(file);

			for(; results.hasNext(); ) {
				QuerySolution sol = results.nextSolution();
				RDFNode name = sol.get("label") ;
				out.println(cleanResults(name));
			}
			out.close();


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block

			System.out.println("error at getMuseumsCity");
			e.printStackTrace();
		}

		qexec.close();

		return file.getAbsolutePath();

	}


	// dato una stringa (es. "Berlin") restituisce l'id associata di wikidata
	private String getCityID(String city, String country){


		String countryID = getCountryID(country).substring(28);


		String queryString = this.prefixes + 
				"SELECT DISTINCT ?item ?itemDescription WHERE {\n" + 
				"  ?item ?label \"" + city + "\"@en.\n" + 
				"  ?item wdt:P31 wd:Q515.\n" + 
				"  ?item wdt:P17 wd:"+ countryID + ".\n" + 
				"  SERVICE wikibase:label { bd:serviceParam wikibase:language \"en,it\". }\n" + 
				"}";

		Query query = QueryFactory.create(queryString) ;

		QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(sparqlService, query);
		ResultSet results = qexec.execSelect() ;

		String res = "";

		if(results.hasNext()) {

			QuerySolution sol = results.nextSolution();
			RDFNode id = sol.get("item") ;
			res = id.toString().substring(3);
		}
		else if(getCapitalID(city) != "")
			res = getCapitalID(city);
		else if(getTownID(city) != "")
			res = getTownID(city);

		qexec.close();

		return res;
	}



	// dato una stringa (es. "Italy") restituisce l'id associata di wikidata
	private String getCountryID(String country){

		String queryString = this.prefixes + 
				"SELECT DISTINCT ?item ?itemDescription WHERE {\n" + 
				"  ?item ?label \"" + country + "\"@en.\n" + 
				"  ?item wdt:P31 wd:Q6256.\n" + 
				"  SERVICE wikibase:label { bd:serviceParam wikibase:language \"en,it\". }\n" + 
				"}";


		Query query = QueryFactory.create(queryString) ;

		QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(sparqlService, query);
		ResultSet results = qexec.execSelect() ;

		String res = "";

		if(results.hasNext()) {

			QuerySolution sol = results.nextSolution();
			RDFNode id = sol.get("item") ;
			res = id.toString().substring(3);
		}

		qexec.close();


		return res;
	}


	private String getCapitalID(String city) {

		String queryString = this.prefixes + 
				"SELECT DISTINCT ?item ?itemDescription WHERE {\n" + 
				"  ?item ?label \"" + city + "\"@en.\n" + 
				"  ?item wdt:P31 wd:Q5119.\n" + 
				"  SERVICE wikibase:label { bd:serviceParam wikibase:language \"en,it\". }\n" + 
				"}";

		Query query = QueryFactory.create(queryString) ;

		QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(sparqlService, query);
		ResultSet results = qexec.execSelect() ;

		String res = "";

		if(results.hasNext()) {

			QuerySolution sol = results.nextSolution();
			RDFNode id = sol.get("item") ;
			res = id.toString().substring(3);
		}

		qexec.close();



		return res;
	}




	private String getTownID(String city) {

		String queryString = this.prefixes + 
				"SELECT DISTINCT ?item ?itemDescription WHERE {\n" + 
				"  ?item ?label \"" + city + "\"@en.\n" + 
				"  ?item wdt:P31 wd:Q3957.\n" + 
				"  SERVICE wikibase:label { bd:serviceParam wikibase:language \"en,it\". }\n" + 
				"}";

		Query query = QueryFactory.create(queryString) ;

		QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(sparqlService, query);
		ResultSet results = qexec.execSelect() ;

		String res = "";

		if(results.hasNext()) {

			QuerySolution sol = results.nextSolution();
			RDFNode id = sol.get("item") ;
			res = id.toString().substring(3);
		}

		qexec.close();

		return res;
	}



	// rimuove @lang a fine stringa e sostituisce spazi vuoti con underscore
	public String cleanResults(RDFNode result) {


		String res = result.toString().replaceAll(" ", "_").replaceAll("-", "_").replaceAll("'", "");
		if(res.contains("@")) {
			res = res.substring(0, res.indexOf("@"));
		}

		return res;
	}



	// rimuove ^^http ecc a fine stringa
	public String cleanDistance(RDFNode result) {


		String res = result.toString().substring(0, result.toString().indexOf("^"));
		//		if(res.contains("@")) {
		//			res = res.substring(0, res.indexOf("@"));
		//		}

		return res;
	}



	//salva su file la lista ordinata in base alla distanza
	public String getOrderedChurchsCity(String city, String latitude, String longitude, String raggio) throws IOException {


		String queryString =  this.prefixes + "SELECT DISTINCT ?placeLabel ?distance WHERE {\n" + 
				"  ?place (wdt:P31/wdt:P279*) wd:Q16970.\n" + 
				"  SERVICE wikibase:around {\n" + 
				"    ?place wdt:P625 ?location.\n" + 
				"    bd:serviceParam wikibase:center \"Point(" + longitude + " " + latitude + ")\"^^geo:wktLiteral.\n" + 
				"    bd:serviceParam wikibase:radius \""+ raggio + "\".\n" + 
				"    bd:serviceParam wikibase:distance ?distance.\n" + 
				"  }\n" + 
				"  SERVICE wikibase:label { bd:serviceParam wikibase:language \"en,it,fr,de,es,no,pt\". }\n" + 
				"}\n" + 
				"ORDER BY ?distance";

		Query query = QueryFactory.create(queryString) ;

		QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(sparqlService, query);
		ResultSet results = qexec.execSelect() ;

		Configuration conf = Configuration.getInstance();

		File file = new File(conf.getWikidataPath() + "orderedChurch@"+city+".txt");
		try {
			PrintWriter out = new PrintWriter(file);

			for(; results.hasNext(); ) {
				QuerySolution sol = results.nextSolution();
				RDFNode name = sol.get("placeLabel");
				RDFNode distance = sol.get("distance");

				out.println(cleanResults(name) + ":" + cleanDistance(distance));
			}
			out.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block

			System.out.println("error at getOrderedChurchsCity");
			e.printStackTrace();
		}

		qexec.close();

		return file.getAbsolutePath();

	}


	//ordered tourist attraction
	public String getOrderedTouristAttractionsCity(String city, String latitude, String longitude, String raggio) throws IOException {

		String queryString =  this.prefixes + "SELECT DISTINCT ?placeLabel ?distance WHERE {\n" + 
				"  ?place (wdt:P31/wdt:P279*) wd:Q570116.\n" + 
				"  SERVICE wikibase:around {\n" + 
				"    ?place wdt:P625 ?location.\n" + 
				"    bd:serviceParam wikibase:center \"Point(" + longitude + " " + latitude + ")\"^^geo:wktLiteral.\n" + 
				"    bd:serviceParam wikibase:radius \""+ raggio + "\".\n" + 
				"    bd:serviceParam wikibase:distance ?distance.\n" + 
				"  }\n" + 
				"  SERVICE wikibase:label { bd:serviceParam wikibase:language \"en,it,fr,de,es,no,pt\". }\n" + 
				"}\n" + 
				"ORDER BY ?distance";

		Query query = QueryFactory.create(queryString) ;

		QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(sparqlService, query);
		ResultSet results = qexec.execSelect() ;

		Configuration conf = Configuration.getInstance();

		File file = new File(conf.getWikidataPath() + "orderedTouristAttraction@"+city+".txt");
		try {
			PrintWriter out = new PrintWriter(file);

			for(; results.hasNext(); ) {
				QuerySolution sol = results.nextSolution();
				RDFNode name = sol.get("placeLabel") ;
				RDFNode distance = sol.get("distance");
				out.println(cleanResults(name) + ":" + cleanDistance(distance));
			}
			out.close();


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block

			System.out.println("error at getOrderedTouristAttractionsCity");
			e.printStackTrace();
		}


		qexec.close();

		return file.getAbsolutePath();

	}





	//ordered Museums
	public String getOrderedMuseumsCity(String city, String latitude, String longitude, String raggio) throws IOException {

		String queryString =  this.prefixes + "SELECT DISTINCT ?placeLabel ?distance WHERE {\n" + 
				"  ?place (wdt:P31/wdt:P279*) wd:Q33506.\n" + 
				"  SERVICE wikibase:around {\n" + 
				"    ?place wdt:P625 ?location.\n" + 
				"    bd:serviceParam wikibase:center \"Point(" + longitude + " " + latitude + ")\"^^geo:wktLiteral.\n" + 
				"    bd:serviceParam wikibase:radius \""+ raggio + "\".\n" + 
				"    bd:serviceParam wikibase:distance ?distance.\n" + 
				"  }\n" + 
				"  SERVICE wikibase:label { bd:serviceParam wikibase:language \"en,it,fr,de,es,no,pt\". }\n" + 
				"}\n" + 
				"ORDER BY ?distance";

		Query query = QueryFactory.create(queryString) ;

		QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(sparqlService, query);
		ResultSet results = qexec.execSelect() ;

		Configuration conf = Configuration.getInstance();

		File file = new File(conf.getWikidataPath() + "orderedMuseum@"+city+".txt");
		try {
			PrintWriter out = new PrintWriter(file);

			for(; results.hasNext(); ) {
				QuerySolution sol = results.nextSolution();
				RDFNode name = sol.get("placeLabel") ;
				RDFNode distance = sol.get("distance");
				out.println(cleanResults(name) + ":" + cleanDistance(distance));
			}
			out.close();


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block

			System.out.println("error at getOrderedMuseumsCity");
			e.printStackTrace();
		}

		qexec.close();

		return file.getAbsolutePath();

	}






	//ordered squares
	public String getOrderedSquaresCity(String city, String latitude, String longitude, String raggio) throws IOException {

		String queryString =  this.prefixes + "SELECT DISTINCT ?placeLabel ?distance WHERE {\n" + 
				"  ?place (wdt:P31/wdt:P279*) wd:Q174782.\n" + 
				"  SERVICE wikibase:around {\n" + 
				"    ?place wdt:P625 ?location.\n" + 
				"    bd:serviceParam wikibase:center \"Point(" + longitude + " " + latitude + ")\"^^geo:wktLiteral.\n" + 
				"    bd:serviceParam wikibase:radius \""+ raggio + "\".\n" + 
				"    bd:serviceParam wikibase:distance ?distance.\n" + 
				"  }\n" + 
				"  SERVICE wikibase:label { bd:serviceParam wikibase:language \"en,it,fr,de,es,no,pt\". }\n" + 
				"}\n" + 
				"ORDER BY ?distance";

		Query query = QueryFactory.create(queryString) ;

		QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(sparqlService, query);
		ResultSet results = qexec.execSelect() ;

		Configuration conf = Configuration.getInstance();

		File file = new File(conf.getWikidataPath() + "orderedSquare@"+city+".txt");

		try {
			PrintWriter out = new PrintWriter(file);

			for(; results.hasNext(); ) {
				QuerySolution sol = results.nextSolution();
				RDFNode name = sol.get("placeLabel");
				RDFNode distance = sol.get("distance");
				out.println(cleanResults(name) + ":" + cleanDistance(distance));
			}
			out.close();


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block

			System.out.println("error at getOrderedSquaresCity");
			e.printStackTrace();
		}

		qexec.close();

		return file.getAbsolutePath();

	}





	//ordered monuments
	public String getOrderedMonumentsCity(String city, String latitude, String longitude, String raggio) throws IOException {

		String queryString =  this.prefixes + "SELECT DISTINCT ?placeLabel ?distance WHERE {\n" + 
				"  ?place (wdt:P31/wdt:P279*) wd:Q4989906.\n" + 
				"  SERVICE wikibase:around {\n" + 
				"    ?place wdt:P625 ?location.\n" + 
				"    bd:serviceParam wikibase:center \"Point(" + longitude + " " + latitude + ")\"^^geo:wktLiteral.\n" + 
				"    bd:serviceParam wikibase:radius \""+ raggio + "\".\n" + 
				"    bd:serviceParam wikibase:distance ?distance.\n" + 
				"  }\n" + 
				"  SERVICE wikibase:label { bd:serviceParam wikibase:language \"en,it,fr,de,es,no,pt\". }\n" + 
				"}\n" + 
				"ORDER BY ?distance";

		Query query = QueryFactory.create(queryString) ;

		QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(sparqlService, query);
		ResultSet results = qexec.execSelect() ;

		Configuration conf = Configuration.getInstance();

		File file = new File(conf.getWikidataPath() + "orderedMonument@"+city+".txt");
		try {
			PrintWriter out = new PrintWriter(file);

			for(; results.hasNext(); ) {
				QuerySolution sol = results.nextSolution();
				RDFNode name = sol.get("placeLabel") ;
				RDFNode distance = sol.get("distance");
				out.println(cleanResults(name) + ":" + cleanDistance(distance));
			}
			out.close();


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block

			System.out.println("error at getOrderedMonuments");
			e.printStackTrace();
		}

		qexec.close();

		return file.getAbsolutePath();

	}




	//check user position and returns if user is fisically in "city"
	public boolean userInCity(String city, String latitude, String longitude) {



		String queryString = this.prefixes + "SELECT DISTINCT * WHERE {\n" + 
				"  ?place (wdt:P31/wdt:P279*) wd:Q515.\n" + 
				"  ?place rdfs:label ?label.\n" + 
				"  SERVICE wikibase:around {\n" + 
				"    ?place wdt:P625 ?location.\n" + 
				"    bd:serviceParam wikibase:center \"Point(" + longitude + " " + latitude + ")\"^^geo:wktLiteral.\n" + 
				"    bd:serviceParam wikibase:radius \"10\".\n" + 
				"    bd:serviceParam wikibase:distance ?distance.\n" + 
				"  }\n" + 
				"  SERVICE wikibase:label { bd:serviceParam wikibase:language \"en\". }\n" + 
				"}\n" + 
				"ORDER BY ?distance";


		Query query = QueryFactory.create(queryString) ;

		QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(sparqlService, query);
		ResultSet results = qexec.execSelect() ;

		boolean inCity = false;

		for(; results.hasNext(); ) {
			QuerySolution sol = results.nextSolution();
			RDFNode name = sol.get("label") ;

			System.out.println("----------> label: " + cleanResults(name) + ", city: " + city + ", boolean: " + cleanResults(name).equals(city));

			if(cleanResults(name).equals(city))
				inCity = true;


		}

		qexec.close();
		System.out.println("in city ----> " + inCity);
		
		return inCity;

	}




	public String getLatitude(String citta, String paese) {

		String countryid = getCountryID(paese).substring(28);

		String queryString = this.prefixes + 
				"SELECT DISTINCT ?item ?itemDescription ?coord WHERE {\n" + 
				"  ?item ?label \"" + citta + "\"@en.\n" + 
				"   ?item (wdt:P31/wdt:P279*) wd:Q56061.\n" + 
				"  ?item wdt:P17 wd:"+ countryid + ".\n" +  
				"?item wdt:P625 ?coord.\n" +                
				"  SERVICE wikibase:label { bd:serviceParam wikibase:language \"en,it\". }\n" + 
				"}";

		Query query = QueryFactory.create(queryString) ;

		QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(sparqlService, query);
		ResultSet results = qexec.execSelect() ;

		String res = "";

		if(results.hasNext()) {

			QuerySolution sol = results.nextSolution();
			RDFNode id = sol.get("coord") ;
			res = extractLatitude(id.toString());
			return res;
		}

		return res;
	}

	
	

	public String getLongitude(String citta, String paese) {
		String countryid = getCountryID(paese).substring(28);

		String queryString = this.prefixes + 
				"SELECT DISTINCT ?item ?itemDescription ?coord WHERE {\n" + 
				"  ?item ?label \"" + citta + "\"@en.\n" + 
				"   ?item (wdt:P31/wdt:P279*) wd:Q56061.\n" + 
				"  ?item wdt:P17 wd:"+ countryid + ".\n" +  
				"?item wdt:P625 ?coord.\n" +                
				"  SERVICE wikibase:label { bd:serviceParam wikibase:language \"en,it\". }\n" + 
				"}";

		Query query = QueryFactory.create(queryString) ;

		QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(sparqlService, query);
		ResultSet results = qexec.execSelect() ;

		String res = "";

		if(results.hasNext()) {

			QuerySolution sol = results.nextSolution();
			RDFNode id = sol.get("coord") ;
			res = extractLongitude(id.toString());
			return res;
		}

		return res;
	}



	private String extractLatitude(String string) {

		return string.substring(string.indexOf("(") +1 , string.indexOf(")")).split(" ")[1];
	}

	private String extractLongitude(String string) {

		return string.substring(string.indexOf("(") +1 , string.indexOf(")")).split(" ")[0];
		
	}


	//Getters & Setters
	public String getPrefixes() {
		return prefixes;
	}

	public void setPrefixes(String prefixes) {
		this.prefixes = prefixes;
	}

	public String getSparqlService() {
		return sparqlService;
	}

	public void setSparqlService(String sparqlService) {
		this.sparqlService = sparqlService;
	}

}
