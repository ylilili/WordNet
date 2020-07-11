import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.princeton.cs.algs4.Digraph;

public class SAP {

	private final Digraph digraph;
	private int shortestAncestor;
	private int shortestLenth;
	
	public SAP(Digraph G) {
		validateDigraph(G);
		digraph = new Digraph(G);
	}
	
	public int length(int v, int w) {
		validateVertex(v);
		validateVertex(w);
		helper(v, w);
		return shortestLenth;
	}
	
	public int ancestor(int v, int w) {
		validateVertex(v);
		validateVertex(w);
		helper(v, w);
		return shortestAncestor;
	}
	
	private void helper(int v, int w) {
		BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(digraph, v);	
		BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(digraph, w);
		
		List<Integer> listVertex = new ArrayList<Integer>();
		
		for(int i = 0; i < digraph.V(); i++) {
			if(bfsv.hasPathTo(i) && bfsw.hasPathTo(i))
				listVertex.add(i);	
		}
		
		if(listVertex.isEmpty()) {
			shortestAncestor = -1;
			shortestLenth = -1;
		}else {
//			shortestAncestor = listVertex.get(0);
//			shortestLenth = bfsv.distTo(shortestAncestor) + bfsw.distTo(shortestAncestor);
//			for(int i = 1; i < listVertex.size(); i++) {
//				int tempAncestor = listVertex.get(i);
//				int tempLenth = bfsv.distTo(tempAncestor) + bfsw.distTo(tempAncestor);
//				if(shortestLenth > tempLenth) {
//					shortestAncestor = tempAncestor;
//					shortestLenth = tempLenth;
//				}
//			}
			
			Map<Integer, Integer> distance = new HashMap<>();
			for(int i = 0; i < listVertex.size(); i++) {
				int vertexid = listVertex.get(i);
				int distanceSum = bfsv.distTo(vertexid) + bfsw.distTo(vertexid);
				distance.put(vertexid, distanceSum);
			}
			List<Map.Entry<Integer, Integer>> list = new ArrayList<>();
			list.addAll(distance.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>(){   
			    public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {      
			    	return (o1.getValue() - o2.getValue());
			    }
			}
			);
			shortestAncestor = list.get(0).getKey();
			shortestLenth = list.get(0).getValue();
		}
		
	}
	
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		validateVertecies(v);
		validateVertecies(w);
		helperSources(v, w);
		return shortestLenth;
	}
	
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		validateVertecies(v);
		validateVertecies(w);
		helperSources(v, w);
		return shortestAncestor;
	}
	
	private void helperSources(Iterable<Integer> v, Iterable<Integer> w) {
		BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(digraph, v);	
		BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(digraph, w);
		
		List<Integer> listVertex = new ArrayList<Integer>();
		
		for(int i = 0; i < digraph.V(); i++) {
			if(bfsv.hasPathTo(i) && bfsw.hasPathTo(i))
				listVertex.add(i);	
		}
		
		if(listVertex.isEmpty()) {
			shortestAncestor = -1;
			shortestLenth = -1;
		}else {
//			shortestAncestor = listVertex.get(0);
//			shortestLenth = bfsv.distTo(shortestAncestor) + bfsw.distTo(shortestAncestor);
//			for(int i = 1; i < listVertex.size(); i++) {
//				int tempAncestor = listVertex.get(i);
//				int tempLenth = bfsv.distTo(tempAncestor) + bfsw.distTo(tempAncestor);
//				if(shortestLenth > tempLenth) {
//					shortestAncestor = tempAncestor;
//					shortestLenth = tempLenth;
//				}
//			}
			Map<Integer, Integer> distance = new HashMap<>();
			for(int i = 0; i < listVertex.size(); i++) {
				int vertexid = listVertex.get(i);
				int distanceSum = bfsv.distTo(vertexid) + bfsw.distTo(vertexid);
				distance.put(vertexid, distanceSum);
			}
			List<Map.Entry<Integer, Integer>> list = new ArrayList<>();
			list.addAll(distance.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>(){   
			    public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {      
			    	return (o1.getValue() - o2.getValue());
			    }
			}
			);
			shortestAncestor = list.get(0).getKey();
			shortestLenth = list.get(0).getValue();
		}
	}
	
	private void validateDigraph(Digraph x) {
		if(x == null)
			throw new IllegalArgumentException();
	}
	
	private void validateVertex(int x) {
		if(x < 0 || x >= digraph.V())
			throw new IllegalArgumentException();
	}
	
	private void validateVertecies(Iterable<Integer> x) {
		if(x == null)
			throw new IllegalArgumentException();
		for (Integer temp : x) {
            if (temp == null) {
                throw new IllegalArgumentException();
            }
            validateVertex(temp);
        }
	}

	public static void main(String[] args) {
		Digraph test = new Digraph(13);
		test.addEdge(7, 3);
		test.addEdge(8, 3);
		test.addEdge(3, 1);
		test.addEdge(4, 1);
		test.addEdge(5, 1);
		test.addEdge(9, 5);
		test.addEdge(10, 5);
		test.addEdge(11, 10);
		test.addEdge(12, 10);
		test.addEdge(1, 0);
		test.addEdge(2, 0);
		SAP temp = new SAP(test);
		temp.length(3, 12);

	}

}
