package uniroma3.it.sii;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Place;

import uniroma3.it.sii.conf.Configuration;

public class ProvaMain {




	public static void main(String[] args) throws IOException {


		//		String accessToken = "EAACoQZAGUlWwBANbz8mf2D6JbaIXO10Qi7wyxXbhIAGQpuD5KwzijLtVyI6OhP4EA2vZC4"
		//				+ "Cgx2m0MjEzEULPmsn7WUkeQKoS3AQPpJGly9McHuZBTRg6YQByFIthbwOdYo6Mz3SLZBQDtBKDBAkDnatJbymJkr4blPZACmfLwx"
		//				+ "zMAlZCABoPQ7JSKkcxPeZCA4fXzrKBG1hNhXF4mtEaGiE";
		//
		//		@SuppressWarnings("deprecation")
		//		FacebookClient facebookClient = new DefaultFacebookClient(accessToken);;
		//		String latitude = "41.855286799999995";
		//		String longitude = "12.469929299999999";
		//		String distance = "100000";
		//		Connection<Place> publicSearch =  facebookClient.fetchConnection("search", Place.class, Parameter.with("type", "place"),
		//				Parameter.with("categories", "['EDUCATION','ARTS_ENTERTAINMENT']"), Parameter.with("center", latitude + "," + longitude),
		//				Parameter.with("distance", distance));
		//
		//		for(Place p : publicSearch.getData()) {
		//
		//			System.out.println("place--> " + p.getName());
		//		}


		List<Integer> rankedList = new ArrayList<>();
		rankedList.add(2);
	    rankedList.add(5);
	    rankedList.add(61);
	    rankedList.add(81);
	    rankedList.add(30);

		//{2, 6, 5, 8, 3}
		List<Integer> correctList = new ArrayList<>();
		correctList.add(2);
		correctList.add(6);
		correctList.add(5);
		correctList.add(8);
		correctList.add(3);
		
		
		
		List<String> rankedList1 = new ArrayList<>();
		rankedList1.add(String.valueOf(2));
	    rankedList1.add(String.valueOf(5));
	    rankedList1.add(String.valueOf(6));
	    rankedList1.add(String.valueOf(8));
	    rankedList1.add(String.valueOf(11));

		//{2, 6, 5, 8, 3}
		List<String> correctList1 = new ArrayList<String>();
		correctList1.add(String.valueOf(2));
		correctList1.add(String.valueOf(6));
		correctList1.add(String.valueOf(5));
		correctList1.add(String.valueOf(8));
		correctList1.add(String.valueOf(11));
		
		
	
		double ndcgVal = NDCG.compute(rankedList, correctList, null);
		System.out.println("NDCG Value: " + ndcgVal);
	}


}


