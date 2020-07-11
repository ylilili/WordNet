import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Outcast {
	private final WordNet wordnet;

	public Outcast(WordNet wordnet) {
		this.wordnet = wordnet;
	}
	
	public String outcast(String[] nouns) {
		//map nounPair to its distance
		Map<String, Integer> nounPairDistance = new HashMap<>();
		for(int i = 0; i < nouns.length; i++) {
			for(int j = i + 1; j < nouns.length; j++) {
				StringBuilder nounPair = new StringBuilder();
				nounPair.append(i);
				nounPair.append(j);
				int nounDistance = this.wordnet.distance(nouns[i], nouns[j]);
				nounPairDistance.put(nounPair.toString(), nounDistance);
			}
		}
		
		//query
		int[] nounSetDistance = new int[nouns.length];
		for(int i = 0; i < nounSetDistance.length; i++) {
			int tempDistance = 0;
			for(int j = 0; j < nounSetDistance.length; j++) {
				if(i == j) continue;
				if(i > j) {
					int reverse = i;
					i = j;
					j = reverse;
					StringBuilder nounPair = new StringBuilder();
					nounPair.append(i);
					nounPair.append(j);
					tempDistance += nounPairDistance.get(nounPair.toString());
					int recover = i;
					i = j;
					j = recover;
				}
				if(i < j) {
					StringBuilder nounPair = new StringBuilder();
					nounPair.append(i);
					nounPair.append(j);
					tempDistance += nounPairDistance.get(nounPair.toString());
				}
			}
			nounSetDistance[i] = tempDistance;
		}
		
		//find the max distance
		int[] copy = nounSetDistance.clone();
		Arrays.sort(copy);
		int idnoun = 0;
		for(int i = 0; i < nounSetDistance.length; i++) {
			if(nounSetDistance[i] == copy[copy.length - 1]) {
				idnoun = i;
				break;
			}
		}
		
		//return outcast noun 
		return nouns[idnoun];	
	}
	
	public static void main(String[] args) {
		WordNet test = new WordNet("synsets.txt", "hypernyms.txt");
		Outcast abbandonNoun = new Outcast(test);
		String[] nouns = {"probability", "statistics", "mathematics", "physics"};
		System.out.println(abbandonNoun.outcast(nouns));
	}

}
