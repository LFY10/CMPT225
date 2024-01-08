package fifteenpuzzle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Solver {
	//main method
	public static void main(String[] args) throws IOException {
		
		if (args.length < 1) {
	        System.err.println("Usage: java PuzzleSolver <input-file> <output-file>");
	        System.exit(1);
	    }
	    String inputFile = args[0];
	    File outputFile = new File(args[1]);
	    
	  
	    
	    
	    //read input and write output
	    BufferedReader reader = new BufferedReader(new FileReader(inputFile));
	   
	    PrintWriter writer = new PrintWriter(new FileWriter(outputFile));	
	    
	    //find the puzzle dimension
	    String firstLine = reader.readLine();
		int dimension = Integer.parseInt(firstLine);
		
		Puzzle correctBoard = new Puzzle(dimension);
		//Turn file to a matrix
		int[][] board = new int[dimension][dimension];
		int c1, c2, c3;
		for (int i = 0; i < dimension; i++) {
			String temp = reader.readLine();
			int num = 0;
			for (int j = 0; j < dimension; j++) {
				if(num >= temp.length())
					c1 = ' ';
				else
					c1 = temp.charAt(num++);
				if(num >= temp.length())
					c2 = ' ';
				else
					c2 = temp.charAt(num++);
				if(num >= temp.length())
					c3 = ' ';
				else
					c3 = temp.charAt(num++);
				if (c1 == ' ')
					c1 = '0';
				if (c2 == ' ')
					c2 = '0';
				board[i][j] = 10 * (c1 - '0') + (c2 - '0');
			}
		}
		
		if (dimension <= 5) {
		
	
			//load the matrix to the puzzle 
			Puzzle solveBoard = new Puzzle(board,dimension);
	
			//Check solution, then output the move, else create a queue and put it in.
			if(solveBoard.checkBoard(correctBoard)) {
				//solveBoard.printMove();
				writer.close();
				return;
			}
			//A* to solve
			Puzzle boardN = new Puzzle(solveBoard.board,solveBoard.size);
			//Create a priority queue
			PriorityQueue<Puzzle> openSet = new PriorityQueue<Puzzle>(new Comparator<Puzzle>() {
				@Override
				public int compare(Puzzle p1, Puzzle p2) {
					return Integer.compare(p1.distance,p2.distance);
				}
			});
			
			boardN.heuristics();
			openSet.add(boardN);
			HashSet<Puzzle> closeSet = new HashSet<Puzzle>();
			//A*
			while(!openSet.isEmpty()) {
				Puzzle tempBoard = openSet.poll();
				for (int i = 0; i < 4; i++) {
					if (tempBoard.checkMove(i)) {
						Puzzle temp = tempBoard.moveNum(i);
						Puzzle moveBoard = new Puzzle(temp.board,temp.size,temp.move);
						if(moveBoard.checkBoard(correctBoard)) {
							//afterMove.printMove();
							
							//write output to.txt
							writer.print(moveBoard.getMove());
		                    writer.close();
							return;
						}
						//keep searching if not match
						if(!closeSet.contains(moveBoard)) {
							moveBoard.heuristics();
							openSet.add(moveBoard);
							closeSet.add((moveBoard));
						}
					}
				}
			}
			
		
		}else if (dimension >5 && dimension<10){
			//generate answer
			int p[][] = generateBoard(dimension);
			//greedy bfs 
			List<String> moves = greedyBestFirstSearch(board, dimension, p);
			//Print result
			writeOutStepForMoreThanFive(outputFile, moves);
			
		}
		
	reader.close();	
	writer.close();
	}
	
	//generate board
	public static int[][] generateBoard(int size) {
		int[][] board = new int[size][size];
		int count = 1;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board[i][j] = count;
				count++;
			}
		}
		board[size - 1][size - 1] = 0; // set the blank tile to 0
		return board;
	}
	
	//write in .txt
	private static void writeOutStepForMoreThanFive(File file, List<String> steps) {
		try {
			List<String> moves = new ArrayList<>();
			for (String move : steps) {
				moves.add(move.substring(0, move.length() - 1) + " " + move.substring(move.length() - 1));
			}
			Files.write(file.toPath(), moves);
		} catch (IOException e) {
			System.err.println("Error occuring: " + e.getMessage());
		}
	}
	
	//solve puzzle dimension more than 5
	private static List<String> greedyBestFirstSearch(int[][] initialState, int size, int[][] targetState) {
		PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(Node::h));
		Set<String> closedSet = new HashSet<>();

		Node initialNode = new Node(initialState, null, "", 0, size);
		openSet.add(initialNode);

		while (!openSet.isEmpty()) {
			Node cur = openSet.poll();

			if (Arrays.deepEquals(cur.state, targetState)) {
				return cur.getMoves();
			}

			closedSet.add(Arrays.deepToString(cur.state));

			for (Node neighbor : cur.getNeighbors()) {
				if (closedSet.contains(Arrays.deepToString(neighbor.state))) {
					continue;
				}
				openSet.add(neighbor);
			}
		}

		return null;
	}
	
	
}
