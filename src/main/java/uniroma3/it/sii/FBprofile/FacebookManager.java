package uniroma3.it.sii.FBprofile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Page;
import com.restfb.types.Place;
import com.restfb.types.User;

import uniroma3.it.sii.conf.Configuration;

@SuppressWarnings("static-access")
public class FacebookManager {

	private String accessToken;
	private static FacebookClient facebookClient;


	@SuppressWarnings("deprecation")
	public FacebookManager(String accessToken) {
		this.accessToken = accessToken;
		this.facebookClient = new DefaultFacebookClient(accessToken);

	}


	//riceve tutti i like di un utente
	public String getAllLikes() throws IOException {

		Connection<Page> fetchConnection = this.facebookClient.fetchConnection( "me/likes", Page.class, Parameter.with("fields", "name"));
		String id = getUserID();

		Configuration conf = Configuration.getInstance();

		File file = new File(conf.getLikesPath() + id + "@likes.txt");

		PrintWriter out = new PrintWriter(file);
		goNext(fetchConnection, out);
		out.close();
		cleanResult(file);


		return file.getAbsolutePath();

	}



	//riceve i like di un utente relativi a pagine di film
	public String getMoviesLikes() throws IOException {

		Connection<Page> fetchConnection = this.facebookClient.fetchConnection( "me/movies", Page.class, Parameter.with("fields", "name"));
		String id = getUserID();

		Configuration conf = Configuration.getInstance();

		File file = new File(conf.getLikesPath() + id + "@movies.txt");

		PrintWriter out = new PrintWriter(file);
		goNext(fetchConnection, out);
		out.close();
		cleanResult(file);


		return file.getAbsolutePath();
	}




	//riceve i like di un utente relativi a pagine musicali
	public String getMusicLikes() throws IOException {

		Connection<Page> fetchConnection = this.facebookClient.fetchConnection( "me/music", Page.class, Parameter.with("fields", "name"));
		String id = getUserID();
		Configuration conf = Configuration.getInstance();

		File file = new File(conf.getLikesPath() + id + "@music.txt");

		PrintWriter out = new PrintWriter(file);
		goNext(fetchConnection, out);
		out.close();
		cleanResult(file);

		return file.getAbsolutePath();
	}





	//id facebook dell'utente (usato per salvare il suo profilo)
	public String getUserID() {

		User user = this.facebookClient.fetchObject("me", User.class);
		return user.getId();
	}



	// chiamata ricorsiva per recuperare tutti i like di un utente (limite di facebook)
	private static void goNext(Connection<Page> fetchConnection,PrintWriter out) {

		for (Page p : fetchConnection.getData()){
			out.println(p.getName());
			//System.out.println( " ------------ name ----------- " + p.getName());
		}

		//next page of likes
		if(fetchConnection.hasNext()) {
			Connection<Page> tmp = facebookClient.fetchConnectionPage(fetchConnection.getNextPageUrl(), Page.class);			
			goNext(tmp, out);
		}
	}


	//riceve da fb i poi relativi alle categorie education e arts_entertainment ad un massimo di DISTANCE metri dalla posizione dell'utente
	public String getPOI_FB(String latitude, String longitude, String distance) throws FileNotFoundException {

		Configuration conf = Configuration.getInstance();
		String id = getUserID();
		File file = new File(conf.getPoiPath() + id + "@poi.txt");

		Connection<Place> publicSearch =  this.facebookClient.fetchConnection("search", Place.class, Parameter.with("type", "place"),
				Parameter.with("categories", "['EDUCATION','ARTS_ENTERTAINMENT']"), Parameter.with("center", latitude + "," + longitude),
				Parameter.with("distance", distance));

		PrintWriter out = new PrintWriter(file);
		int i = 1;

		while(i<=10) {
			for(Place p : publicSearch.getData()) {
				out.println(p.getName());
				i++;
			}
		}

		out.close();
		return file.getAbsolutePath();
	}



	// input un file txt, sostituisce ogni spazio con underscore
	public static void cleanResult(File file) throws IOException {


		String linea;
		Configuration conf = Configuration.getInstance();

		File temp = new File(conf.getLikesPath() + "tmp.txt");
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
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}		
}
