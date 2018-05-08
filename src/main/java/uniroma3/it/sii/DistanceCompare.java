package uniroma3.it.sii;

import java.util.Comparator;

public class DistanceCompare implements Comparator<NameDistanceRow> {
	
	@Override
	public int compare(NameDistanceRow o1, NameDistanceRow o2) {
			
		
		if (o1.getDistance() < o2.getDistance()) return -1;
        if (o1.getDistance() > o2.getDistance()) return 1;
        return 0;
		
	}


}
