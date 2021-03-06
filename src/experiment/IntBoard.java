package experiment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class IntBoard {
	private Map<BoardCell, LinkedList<BoardCell>> adjacentMatrix;
	private Set<BoardCell> visited;
	private Set<BoardCell> targets;
	private BoardCell[][] grid;
	
	public static final int TOTAL_X = 4;
	public static final int TOTAL_Y = 4;
	
	public IntBoard() {
		super();
		adjacentMatrix = new HashMap<BoardCell, LinkedList<BoardCell>>();
		visited = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>(); //hash sets cause why not?
		grid = new BoardCell[TOTAL_X][TOTAL_Y];
		for(int i = 0; i < TOTAL_X; i++) {
			for(int j = 0; j < TOTAL_Y; j++) {
				grid[i][j] = new BoardCell(i, j);
			}
		}
	}
	
	public void calcAdjacencies(){
		for(int i = 0; i < TOTAL_X; i++) {
			for(int j = 0; j < TOTAL_Y; j++) {
				LinkedList<BoardCell> neighbors = new LinkedList<BoardCell>();
				if(i > 0) {
					neighbors.add(grid[i - 1][j]);
				}
				if(i < TOTAL_X - 1) {
					neighbors.add(grid[i + 1][j]);
				}
				if(j > 0) {
					neighbors.add(grid[i][j - 1]);
				}
				if(j < TOTAL_Y - 1) {
					neighbors.add(grid[i][j + 1]);
				}
				adjacentMatrix.put(grid[i][j], neighbors);
			}
		}
	}
	
	public void calcTargets(BoardCell startCell, int pathLength){
		visited.clear(); //clear the visited set
		targets.clear(); //clear the targets set
		visited.add(startCell);
		targets = findAllTargets(startCell, pathLength);
	}
	
	public Set<BoardCell> findAllTargets(BoardCell currentCell, int remainingSteps){
		visited.add(currentCell);
		LinkedList<BoardCell> adj = new LinkedList<BoardCell>(adjacentMatrix.get(currentCell));	//new linked list of cells that have not been visited
		for (BoardCell i:visited){
			adj.remove(i);
		}
		for (BoardCell i:adj){
			if(remainingSteps == 1){
				targets.add(i);
			}
			else {
				targets.addAll(findAllTargets(i, remainingSteps-1));
			}
			visited.remove(i);
		}
		return targets;
	}
	
	public LinkedList<BoardCell> getAdjList(BoardCell cell){
		return adjacentMatrix.get(cell);
	}

	public BoardCell getCell(int i, int j) {
		return grid[i][j];
	}

	public Set<BoardCell> getTargets() {
		return targets;
	}
	
}
