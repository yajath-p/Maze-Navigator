package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


/**
 * <P>This class represents a general "directed graph", which could 
 * be used for any purpose.  The graph is viewed as a collection 
 * of vertices, which are sometimes connected by weighted, directed
 * edges.</P> 
 * 
 * <P>This graph will never store duplicate vertices.</P>
 * 
 * <P>The weights will always be non-negative integers.</P>
 * 
 * <P>The WeightedGraph will be capable of performing three algorithms:
 * Depth-First-Search, Breadth-First-Search, and Djikatra's.</P>
 * 
 * <P>The Weighted Graph will maintain a collection of 
 * "GraphAlgorithmObservers", which will be notified during the
 * performance of the graph algorithms to update the observers
 * on how the algorithms are progressing.</P>
 */
public class WeightedGraph<V> {

	private HashMap<V, Map<V, Integer>> weightedGraph;
	private Collection<GraphAlgorithmObserver<V>> observerList;


	public WeightedGraph() {
		weightedGraph = new HashMap<>();
		observerList = new HashSet<>();
	}

	/** Add a GraphAlgorithmObserver to the collection maintained
	 * by this graph (observerList).
	 * 
	 * @param observer
	 */
	public void addObserver(GraphAlgorithmObserver<V> observer) {
		observerList.add(observer);
	}

	/** Add a vertex to the graph.  If the vertex is already in the
	 * graph, throw an IllegalArgumentException.
	 * 
	 * @param vertex vertex to be added to the graph
	 * @throws IllegalArgumentException if the vertex is already in
	 * the graph
	 */
	public void addVertex(V vertex) {
		if(containsVertex(vertex)) {
			throw new IllegalArgumentException();
		} else {
			weightedGraph.put(vertex, new HashMap<>());
		}
	}

	/** Searches for a given vertex.
	 * 
	 * @param vertex the vertex we are looking for
	 * @return true if the vertex is in the graph, false otherwise.
	 */
	public boolean containsVertex(V vertex) {
		return weightedGraph.containsKey(vertex);
	}

	/** 
	 * <P>Add an edge from one vertex of the graph to another, with
	 * the weight specified.</P>
	 * 
	 * <P>The two vertices must already be present in the graph.</P>
	 * 
	 * <P>This method throws an IllegalArgumentExeption in three
	 * cases:</P>
	 * <P>1. The "from" vertex is not already in the graph.</P>
	 * <P>2. The "to" vertex is not already in the graph.</P>
	 * <P>3. The weight is less than 0.</P>
	 * 
	 * @param from the vertex the edge leads from
	 * @param to the vertex the edge leads to
	 * @param weight the (non-negative) weight of this edge
	 * @throws IllegalArgumentException when either vertex
	 * is not in the graph, or the weight is negative.
	 */
	public void addEdge(V from, V to, Integer weight) {
		if(containsVertex(from)==false || containsVertex(to)==false || weight < 0) {
			throw new IllegalArgumentException();
		} else {
			weightedGraph.get(from).put(to, weight);
		}
	}

	/** 
	 * <P>Returns weight of the edge connecting one vertex
	 * to another.  Returns null if the edge does not
	 * exist.</P>
	 * 
	 * <P>Throws an IllegalArgumentException if either
	 * of the vertices specified are not in the graph.</P>
	 * 
	 * @param from vertex where edge begins
	 * @param to vertex where edge terminates
	 * @return weight of the edge, or null if there is
	 * no edge connecting these vertices
	 * @throws IllegalArgumentException if either of
	 * the vertices specified are not in the graph.
	 */
	public Integer getWeight(V from, V to) {
		if(containsVertex(from)==false || containsVertex(to)==false) { 
			throw new IllegalArgumentException();
		}
		if(weightedGraph.get(from).containsKey(to)==false) {
			return null;
		} else {
			return weightedGraph.get(from).get(to);
		}

	}

	/** 
	 * <P>This method will perform a Breadth-First-Search on the graph.
	 * The search will begin at the "start" vertex and conclude once
	 * the "end" vertex has been reached.</P>
	 * 
	 * <P>Before the search begins, this method will go through the
	 * collection of Observers, calling notifyBFSHasBegun on each
	 * one.</P>
	 * 
	 * <P>Just after a particular vertex is visited, this method will
	 * go through the collection of observers calling notifyVisit
	 * on each one (passing in the vertex being visited as the
	 * argument.)</P>
	 * 
	 * <P>After the "end" vertex has been visited, this method will
	 * go through the collection of observers calling 
	 * notifySearchIsOver on each one, after which the method 
	 * should terminate immediately, without processing further 
	 * vertices.</P> 
	 * 
	 * @param start vertex where search begins
	 * @param end the algorithm terminates just after this vertex
	 * is visited
	 */
	public void DoBFS(V start, V end) {
		HashSet<V> visitedSet = new HashSet<>();
		ArrayList<V> queue = new ArrayList<>();

		for(GraphAlgorithmObserver<V> x : observerList) {
			x.notifyBFSHasBegun();
		}

		queue.add(start);

		while(!(queue.isEmpty())) {
			//Removes first item from queue
			V visiting = queue.remove(0);

			if(visitedSet.contains(visiting)==false) {
				for(GraphAlgorithmObserver<V> x : observerList) {
					x.notifyVisit(visiting);
				}

				//If last object is found the search ends
				if(visiting.equals(end)) {
					for(GraphAlgorithmObserver<V> x : observerList) {
						x.notifySearchIsOver();
					}
					return;
				}

				visitedSet.add(visiting);

				//Adds adjacency of the vertex currently visiting that isn't in finishedSet
				for(V adj: weightedGraph.get(visiting).keySet()) {
					if(visitedSet.contains(adj)==false) {
						//adds to end of queue
						queue.add(adj);	
					}
				}

			}

		}
	}

	/** 
	 * <P>This method will perform a Depth-First-Search on the graph.
	 * The search will begin at the "start" vertex and conclude once
	 * the "end" vertex has been reached.</P>
	 * 
	 * <P>Before the search begins, this method will go through the
	 * collection of Observers, calling notifyDFSHasBegun on each
	 * one.</P>
	 * 
	 * <P>Just after a particular vertex is visited, this method will
	 * go through the collection of observers calling notifyVisit
	 * on each one (passing in the vertex being visited as the
	 * argument.)</P>
	 * 
	 * <P>After the "end" vertex has been visited, this method will
	 * go through the collection of observers calling 
	 * notifySearchIsOver on each one, after which the method 
	 * should terminate immediately, without visiting further 
	 * vertices.</P> 
	 * 
	 * @param start vertex where search begins
	 * @param end the algorithm terminates just after this vertex
	 * is visited
	 */
	public void DoDFS(V start, V end) {
		HashSet<V> visitedSet = new HashSet<>();
		ArrayList<V> stack = new ArrayList<>();

		for(GraphAlgorithmObserver<V> x : observerList) {
			x.notifyDFSHasBegun();
		}

		stack.add(start);

		while(!(stack.isEmpty())) {
			//Pops from stack
			V visiting = stack.remove(stack.size()-1);

			if(visitedSet.contains(visiting)==false) {

				for(GraphAlgorithmObserver<V> x : observerList) {
					x.notifyVisit(visiting);
				}


				//If last object is found the search ends
				if(visiting.equals(end)) {
					for(GraphAlgorithmObserver<V> x : observerList) {
						x.notifySearchIsOver();
					}
					return;
				}

				visitedSet.add(visiting);

				//Adds adjacency of the vertex currently visiting that isn't in finishedSet
				for(V adj: weightedGraph.get(visiting).keySet()) {
					if(visitedSet.contains(adj)==false) {
						stack.add(adj);
					}
				}

			}
		}
	}

	/** 
	 * <P>Perform Dijkstra's algorithm, beginning at the "start"
	 * vertex.</P>
	 * 
	 * <P>The algorithm DOES NOT terminate when the "end" vertex
	 * is reached.  It will continue until EVERY vertex in the
	 * graph has been added to the finished set.</P>
	 * 
	 * <P>Before the algorithm begins, this method goes through 
	 * the collection of Observers, calling notifyDijkstraHasBegun 
	 * on each Observer.</P>
	 * 
	 * <P>Each time a vertex is added to the "finished set", this 
	 * method goes through the collection of Observers, calling 
	 * notifyDijkstraVertexFinished on each one (passing the vertex
	 * that was just added to the finished set as the first argument,
	 * and the optimal "cost" of the path leading to that vertex as
	 * the second argument.)</P>
	 * 
	 * <P>After all of the vertices have been added to the finished
	 * set, the algorithm will calculate the "least cost" path
	 * of vertices leading from the starting vertex to the ending
	 * vertex.  Next, it will go through the collection 
	 * of observers, calling notifyDijkstraIsOver on each one, 
	 * passing in as the argument the "lowest cost" sequence of 
	 * vertices that leads from start to end (I.e. the first vertex
	 * in the list will be the "start" vertex, and the last vertex
	 * in the list will be the "end" vertex.)</P>
	 * 
	 * @param start vertex where algorithm will start
	 * @param end special vertex used as the end of the path 
	 * reported to observers via the notifyDijkstraIsOver method.
	 */
	public void DoDijsktra(V start, V end) {
		HashSet<V> finishedSet = new HashSet<>();
		HashMap<V, V> pred = new HashMap<>(); //key is the vertex, value is the predecessor
		HashMap<V, Integer> cost = new HashMap<>(); //vertex, and total cost to get to the vertex
		ArrayList<V> predList = new ArrayList<>();
		ArrayList<V> predRevList = new ArrayList<>();

		for(V y: weightedGraph.keySet()) {
			pred.put(y, null);
			cost.put(y,1000);
		}

		//Starting conditions for the loop
		pred.replace(start, start);
		cost.replace(start, 0);
		V smallest = start;
		int smallestVal = 1000; //Arbitrary number so loop starts

		for(GraphAlgorithmObserver<V> x : observerList) {
			x.notifyDijkstraHasBegun();
		}

		while(finishedSet.size()!=weightedGraph.keySet().size()) {
			//finds smallest value & associated vertex
			for(V y: cost.keySet()) {
				if(cost.get(y)<smallestVal && !finishedSet.contains(y)) {
					smallestVal = cost.get(y);
					smallest = y;
				}
			}

			for(GraphAlgorithmObserver<V> x : observerList) {
				x.notifyDijkstraVertexFinished(smallest, smallestVal);
			}
			finishedSet.add(smallest);
			//Adjacency loop
			for(V adj: weightedGraph.get(smallest).keySet()) {
				//Cost evaluator 
				if(smallestVal + getWeight(smallest, adj) < cost.get(adj)) {
					cost.replace(adj,(smallestVal + getWeight(smallest,adj)));
					pred.replace(adj, smallest);
				} 
			}
			smallestVal=1000; //Resets smallestVal
		}
		//Gets most efficient path from end to start
		predRevList.add(end);
		while(predRevList.contains(start)==false) {
			for(V y:pred.keySet()) {
				if(predRevList.get(predRevList.size()-1).equals(y)) {
					predRevList.add(pred.get(y));
				}
			}	
		}
		//Reverses the list so its from start to end
		for(int i=predRevList.size()-1;i>=0;i--) {
			predList.add(predRevList.get(i));
		}

		for(GraphAlgorithmObserver<V> x : observerList) {
			x.notifyDijkstraIsOver(predList);
		}

	}



}
