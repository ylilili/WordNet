import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import edu.princeton.cs.algs4.Topological;

public class WordNet{
	private final Map<String, Set<Integer>> dictionary = new HashMap<>();
	private final SAP sap;
	private final Map<Integer, StringBuilder> nounIndex = new HashMap<Integer, StringBuilder>();
	
	public WordNet(String synsets, String hypernyms) {
		Digraph hyperGraph;
		In synsetsFile = new In(synsets);
		String sysnsetsItem = synsetsFile.readLine();
		int vertexNo = 0;
		while(sysnsetsItem != null) {
			vertexNo++;
			String[] temp = sysnsetsItem.split(","); 
			String[] nounSet = temp[1].split(" ");
			int id = Integer.parseInt(temp[0]);
			StringBuilder nounCollection = new StringBuilder();
			nounIndex.put(id, nounCollection);
			for(String noun: nounSet) {
				nounCollection.append(noun);
				nounCollection.append(" ");
				if(!dictionary.containsKey(noun)) {
					Set<Integer> idset = new HashSet<>();
					dictionary.put(noun, idset);
				}
				dictionary.get(noun).add(id);
			}
			sysnsetsItem = synsetsFile.readLine();
		}
		
		hyperGraph = new Digraph(vertexNo);
		
		In hypernymsFile = new In(hypernyms);
		String hypernymItem = hypernymsFile.readLine();
		while(hypernymItem != null) {
			String[] temp = hypernymItem.split(",");
			int v = Integer.parseInt(temp[0]);
			for(int i = 1; i < temp.length; i++) {
				int w = Integer.parseInt(temp[i]);
				hyperGraph.addEdge(v, w);
			}
			hypernymItem = hypernymsFile.readLine();
		}	
		
		//check if we create a rooted DAG
		Topological testhelper = new Topological(hyperGraph);
		if(!testhelper.hasOrder())
			throw new IllegalArgumentException();
		
		int rootNo = 0;
		for(int i = 0; i < hyperGraph.V(); i++) {
			if(hyperGraph.outdegree(i) == 0) {
				rootNo++;
				if(rootNo > 1)
					throw new IllegalArgumentException();
			}
		}
		
		this.sap = new SAP(hyperGraph);
	}
	
	public Iterable<String> nouns(){
		return dictionary.keySet();
	}
	
	public boolean isNoun(String word) {
		validate(word);
		return dictionary.containsKey(word);
	}
	
	public int distance(String nounA, String nounB) {
		validate(nounA);
		validate(nounB);
		if(!isNoun(nounA) || !isNoun(nounB))
			throw new IllegalArgumentException();
		Set<Integer> A = dictionary.get(nounA);
		Set<Integer> B = dictionary.get(nounB);
		return sap.length(A, B);
	}
	
	public String sap(String nounA, String nounB) {
		validate(nounA);
		validate(nounB);
		if(!isNoun(nounA) || !isNoun(nounB))
			throw new IllegalArgumentException();
		Set<Integer> A = dictionary.get(nounA);
		Set<Integer> B = dictionary.get(nounB);	
		
		int ances = sap.ancestor(A, B);
//		StringBuilder sapSynsets = new StringBuilder();
//		for(String keyNoun: nouns()) {
//			if(dictionary.get(keyNoun).contains(ances)) {
//				sapSynsets.append(keyNoun);
//				sapSynsets.append(" ");
//			}
//		}
//		sapSynsets.toString();
//		return sapSynsets.toString();
		return nounIndex.get(ances).toString();
	}
	
	private void validate(String temp) {
		if(temp == null)
			throw new IllegalArgumentException();
	}
	
	public static void main(String[] args) {
		WordNet test = new WordNet("synsets.txt", "hypernyms.txt");
		System.out.println(test.distance("order_Conodontophorida", "viewer"));
		System.out.println(test.sap("order_Conodontophorida", "viewer"));
	}
}
