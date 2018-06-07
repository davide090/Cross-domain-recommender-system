package uniroma3.it.sii;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class NDCG_String {

	// Prevent instantiation.
	private NDCG_String() {}

	public static double compute(List<String> ranked_items, Collection<String> correct_items, Collection<String> ignore_items) {

		if (ignore_items == null)
			ignore_items = new HashSet<String>();

		double dcg = 0;
		double idcg = computeIDCG(correct_items.size());
		int left_out = 0;

		for (int i = 0; i < ranked_items.size(); i++) {
			String item_id = ranked_items.get(i);
			if (ignore_items.contains(item_id)) {
				left_out++;
				continue;
			}

			if (!correct_items.contains(item_id))
				continue;

			// compute NDCG part
			int rank = i + 1 - left_out;
			dcg += Math.log(2) / Math.log(rank + 1);

		}

		return dcg/idcg;
	}
		private static double computeIDCG(int n){
			double idcg = 0;
			for (int i = 0; i < n; i++)
				idcg += Math.log(2) / Math.log(i + 2);
			return idcg;
		}
	}