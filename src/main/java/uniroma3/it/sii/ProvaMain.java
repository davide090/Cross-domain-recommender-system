package uniroma3.it.sii;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import uniroma3.it.sii.wikidata.WikidataManager;

public class ProvaMain {




	public static void main(String[] args) throws IOException {

		
		WikidataManager w = new WikidataManager();
		
		String a = w.getLatitude("Rome", "Italy");
		String b = w.getLongitude("Rome", "Italy");
		
		System.out.println(a);
		System.out.println(b);

	}


}


