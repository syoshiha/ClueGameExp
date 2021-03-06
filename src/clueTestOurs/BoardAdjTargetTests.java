package clueTestOurs;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTests {

	//these test calcAdjacencies and calcTargets methods. 
	//Going in and out of doors, ensuring no movement inside a room,
	//ensuring shortcuts into doors (not using all moves)
	private static Board board;
	
	@BeforeClass
	public static void setUp() {
		board = new Board("Layout.csv", "Legend.txt");
		board.initialize();
	}
	
	//marked in GREEN on layout
	//should be no movement inside rooms
	@Test
	public void testAdjacenciesInsideRooms() { 
		LinkedList<BoardCell> testList = board.getAdjList(20, 24); //a corner of room and board
		assertEquals(0, testList.size());
		testList = board.getAdjList(8, 22); //walkway below
		assertEquals(0, testList.size());
		testList = board.getAdjList(7, 2); //walkway above
		assertEquals(0, testList.size());
		testList = board.getAdjList(3, 2); //middle of the room
		assertEquals(0, testList.size());
		testList = board.getAdjList(14, 11); //beside a door
		assertEquals(0, testList.size());
		testList = board.getAdjList(17, 9); //corner of a room but not board
		assertEquals(0, testList.size());
	}
	
	// Checks that from doors you can only go out the proper direction 
	// These tests are PURPLE on the planning spreadsheet
	@Test
	public void testAdjacencyRoomExit() 
	{
		LinkedList<BoardCell> testList = board.getAdjList(17, 4); //Right doorway
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(17, 5)));
		testList = board.getAdjList(0, 19); //left doorway
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(0, 18)));
		testList = board.getAdjList(5, 5); //doorway down
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(6, 5)));
		testList = board.getAdjList(8, 4); //doorway up
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(7, 4)));
	}
	
	// Test adjacency at entrance to rooms
	// These tests are YELLOW in planning spreadsheet
	@Test
	public void testAdjacencyDoorways() 
	{
		// Test beside a door direction RIGHT
		LinkedList<BoardCell> testList = board.getAdjList(17, 16);
		assertTrue(testList.contains(board.getCellAt(17, 15)));
		assertTrue(testList.contains(board.getCellAt(16, 16)));
		assertTrue(testList.contains(board.getCellAt(17, 17)));
		assertEquals(3, testList.size());
		// Test beside a door direction DOWN
		testList = board.getAdjList(6, 5);
		assertTrue(testList.contains(board.getCellAt(5, 5)));
		assertTrue(testList.contains(board.getCellAt(6, 6)));
		assertTrue(testList.contains(board.getCellAt(6, 4)));
		assertTrue(testList.contains(board.getCellAt(7, 5)));
		assertEquals(4, testList.size());
		// Test beside a door direction LEFT
		testList = board.getAdjList(0, 18);
		assertTrue(testList.contains(board.getCellAt(0, 19)));
		assertTrue(testList.contains(board.getCellAt(0, 17)));
		assertTrue(testList.contains(board.getCellAt(1, 18)));
		assertEquals(3, testList.size());
		// Test beside a door direction UP
		testList = board.getAdjList(10, 22);
		assertTrue(testList.contains(board.getCellAt(11, 22)));
		assertTrue(testList.contains(board.getCellAt(10, 21)));
		assertTrue(testList.contains(board.getCellAt(10, 23)));
		assertTrue(testList.contains(board.getCellAt(9, 22)));
		assertEquals(4, testList.size());
	}
	
	// Test a bunch of walkways i different ways
	// These tests are LIGHT RED on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways()
	{
		// Test on top edge of board
		LinkedList<BoardCell> testList = board.getAdjList(0, 4);
		assertTrue(testList.contains(board.getCellAt(1, 4)));
		assertTrue(testList.contains(board.getCellAt(0, 3)));
		assertTrue(testList.contains(board.getCellAt(0, 5)));
		assertEquals(3, testList.size());
		
		// Test on left edge of board, three possible pieces
		testList = board.getAdjList(11, 0);
		assertTrue(testList.contains(board.getCellAt(10, 0)));
		assertTrue(testList.contains(board.getCellAt(12, 0)));
		assertTrue(testList.contains(board.getCellAt(11, 1)));
		assertEquals(3, testList.size());

		// Test between two rooms, walkways right and left and down
		testList = board.getAdjList(9, 22);
		assertTrue(testList.contains(board.getCellAt(9, 21)));
		assertTrue(testList.contains(board.getCellAt(9, 23)));
		assertTrue(testList.contains(board.getCellAt(10, 22)));
		assertEquals(3, testList.size());

		// Test surrounded by 4 walkways
		testList = board.getAdjList(14,7);
		assertTrue(testList.contains(board.getCellAt(14, 8)));
		assertTrue(testList.contains(board.getCellAt(14, 6)));
		assertTrue(testList.contains(board.getCellAt(15, 7)));
		assertTrue(testList.contains(board.getCellAt(13, 7)));
		assertEquals(4, testList.size());
		
		// Test on bottom edge of board, next to 1 room piece
		testList = board.getAdjList(20, 20);
		assertTrue(testList.contains(board.getCellAt(20, 19)));
		assertTrue(testList.contains(board.getCellAt(19, 20)));
		assertEquals(2, testList.size());
		
		// Test on right edge of board, next to 1 room piece
		testList = board.getAdjList(5, 24);
		assertTrue(testList.contains(board.getCellAt(5, 23)));
		assertTrue(testList.contains(board.getCellAt(4, 24)));
		assertTrue(testList.contains(board.getCellAt(6, 24)));
		assertEquals(3, testList.size());

		// Test on walkway next to  door that is not in the needed
		// direction to enter
			testList = board.getAdjList(5, 4);
			assertTrue(testList.contains(board.getCellAt(4, 4)));
			assertTrue(testList.contains(board.getCellAt(6, 4)));
			assertEquals(2, testList.size());
	}
	
	// Tests of just walkways, 1 step, includes on edge of board and beside room
		// These are LIGHT BLUE on the planning spreadsheet
		@Test
		public void testTargetsOneStep() {
			board.calcTargets(12, 0, 1);  //left edge
			Set<BoardCell> targets= board.getTargets();
			assertEquals(3, targets.size());
			assertTrue(targets.contains(board.getCellAt(13, 0)));
			assertTrue(targets.contains(board.getCellAt(11, 0)));
			assertTrue(targets.contains(board.getCellAt(12, 1)));
			
			board.calcTargets(4, 24, 1); //right edge
			targets= board.getTargets();
			assertEquals(2, targets.size());
			assertTrue(targets.contains(board.getCellAt(4, 23)));
			assertTrue(targets.contains(board.getCellAt(5, 24)));				
		}
		
		// Tests of just walkways, 2 steps
		// These are LIGHT GREEN on the planning spreadsheet
		@Test
		public void testTargetsTwoSteps() {
			board.calcTargets(20, 19, 2);
			Set<BoardCell> targets= board.getTargets();
			assertEquals(2, targets.size());
			assertTrue(targets.contains(board.getCellAt(18, 19)));
			assertTrue(targets.contains(board.getCellAt(19, 20)));
			
			board.calcTargets(0, 9, 2);
			targets= board.getTargets();
			assertEquals(2, targets.size());
			assertTrue(targets.contains(board.getCellAt(2, 9)));
			assertTrue(targets.contains(board.getCellAt(1, 10)));				
		}
		
		// Tests of just walkways, 4 steps
		// These are LIGHT GREEN on the planning spreadsheet
		@Test
		public void testTargetsFourSteps() {
			board.calcTargets(20, 5, 4);
			Set<BoardCell> targets= board.getTargets();
			assertEquals(8, targets.size());
			assertTrue(targets.contains(board.getCellAt(16, 5)));
			assertTrue(targets.contains(board.getCellAt(19, 8)));
			assertTrue(targets.contains(board.getCellAt(18, 7)));
			assertTrue(targets.contains(board.getCellAt(17, 6)));
			assertTrue(targets.contains(board.getCellAt(19, 6)));
			assertTrue(targets.contains(board.getCellAt(18, 5)));
			assertTrue(targets.contains(board.getCellAt(20, 7)));
			assertTrue(targets.contains(board.getCellAt(17, 4)));
			
			// Includes a path that doesn't have enough length
			board.calcTargets(6, 0, 4);
			targets= board.getTargets();
			assertEquals(2, targets.size());
			assertTrue(targets.contains(board.getCellAt(6, 4)));
			assertTrue(targets.contains(board.getCellAt(5, 0)));
		}	
		
		// Tests of just walkways plus one door, 6 steps
		// also tests going into door without using all steps
		// These are LIGHT GREEN on the planning spreadsheet
		@Test
		public void testTargetsSixSteps() {
			board.calcTargets(4, 24, 6);
			Set<BoardCell> targets= board.getTargets();
			assertEquals(3, targets.size()); //doesnt let it go into door when it hasnt used all steps
			assertTrue(targets.contains(board.getCellAt(6, 24))); //into door without using all steps
			assertTrue(targets.contains(board.getCellAt(4, 20)));	
			assertTrue(targets.contains(board.getCellAt(4, 18)));
		}	
		
		// Test getting into a room with correct number of steps
		// These are LIGHT GREEN on the planning spreadsheet
		@Test 
		public void testTargetsIntoRoom()
		{
			// two rooms are exactly 1 away
			board.calcTargets(0, 4, 1);
			Set<BoardCell> targets= board.getTargets();
			assertEquals(3, targets.size());
			// one left, one right
			assertTrue(targets.contains(board.getCellAt(0, 5)));
			assertTrue(targets.contains(board.getCellAt(0, 3)));
			assertTrue(targets.contains(board.getCellAt(1, 4)));
		}
		
		// Test getting out of a room with 1 & 2 steps.
		// These are LIGHT GREEN on the planning spreadsheet
		@Test
		public void testRoomExit()
		{
			// Take one step, essentially just the adj list
			board.calcTargets(11, 22, 1);
			Set<BoardCell> targets= board.getTargets();
			// Ensure doesn't exit through the wall
			assertEquals(1, targets.size());
			assertTrue(targets.contains(board.getCellAt(10, 22)));
			// Take two steps
			board.calcTargets(11, 22, 2);
			targets= board.getTargets();
			assertEquals(3, targets.size());
			assertTrue(targets.contains(board.getCellAt(10, 21)));
			assertTrue(targets.contains(board.getCellAt(10, 23)));
			assertTrue(targets.contains(board.getCellAt(9, 22)));
		}
}