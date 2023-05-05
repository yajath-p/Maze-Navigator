package graph;

import maze.Juncture;
import maze.Maze;

/** 
 * <P>The MazeGraph is an extension of WeightedGraph.  
 * The constructor converts a Maze into a graph.</P>
 */
public class MazeGraph extends WeightedGraph<Juncture> {

	/** 
	 * <P>Construct the MazeGraph using the "maze" contained
	 * in the parameter to specify the vertices (Junctures)
	 * and weighted edges.</P>
	 * 
	 * <P>The Maze is a rectangular grid of "junctures", each
	 * defined by its X and Y coordinates, using the usual
	 * convention of (0, 0) being the upper left corner.</P>
	 * 
	 * <P>Each juncture in the maze should be added as a
	 * vertex to this graph.</P>
	 * 
	 * <P>For every pair of adjacent junctures (A and B) which
	 * are not blocked by a wall, two edges should be added:  
	 * One from A to B, and another from B to A.  The weight
	 * to be used for these edges is provided by the Maze. 
	 * (The Maze methods getMazeWidth and getMazeHeight can
	 * be used to determine the number of Junctures in the
	 * maze. The Maze methods called "isWallAbove", "isWallToRight",
	 * etc. can be used to detect whether or not there
	 * is a wall between any two adjacent junctures.  The 
	 * Maze methods called "getWeightAbove", "getWeightToRight",
	 * etc. should be used to obtain the weights.)</P>
	 * 
	 * @param maze to be used as the source of information for
	 * adding vertices and edges to this MazeGraph.
	 */
	public MazeGraph(Maze maze) {
		//Creates vertices
		for(int x=0;x<maze.getMazeWidth();x++) {
			for(int y=0;y<maze.getMazeHeight();y++) {
				addVertex(new Juncture(x,y));

			}
		}
		//Creates edges
		for(int x=0;x<maze.getMazeWidth();x++) {
			for(int y=0;y<maze.getMazeHeight();y++) {
				Juncture curr = new Juncture(x,y);
				Juncture above = new Juncture(x,y-1);
				Juncture below = new Juncture(x,y+1);
				Juncture left = new Juncture(x-1,y);
				Juncture right = new Juncture(x+1,y);

				//Each if statement checks whether there is a wall to add edge
				if(!maze.isWallAbove(curr)) {
					this.addEdge(curr, above, maze.getWeightAbove(curr));
					this.addEdge(above, curr, maze.getWeightBelow(above));
				}
				if(!maze.isWallBelow(curr)) {
					this.addEdge(curr,below,maze.getWeightBelow(curr));
					this.addEdge(below,curr,maze.getWeightAbove(below));
				}
				if(!maze.isWallToRight(curr)) {
					this.addEdge(curr, right, maze.getWeightToRight(curr));
					this.addEdge(right, curr, maze.getWeightToLeft(right));
				}
				if(!maze.isWallToLeft(curr)) {
					this.addEdge(curr, left, maze.getWeightToLeft(curr));
					this.addEdge(left, curr, maze.getWeightToRight(left));
				}

			}
		}

	}
}
