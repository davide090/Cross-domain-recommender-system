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

public class ProvaMain {




	public static void main(String[] args) throws IOException {


		String s = "davide_pluto_pippo[davide, pluto, pippo]";
		s = s.replaceAll(" ", "");
		List<String> words = Arrays.asList(s.substring(s.indexOf("[")+1, s.indexOf("]")).split(","));
		
		System.out.println(words);
		for(String a : words)
			System.out.println(a);
		
		System.out.println("sottostringa: " + s.substring(0, s.indexOf("[")) + " di ---> " + s);
	}


}


