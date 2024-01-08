package fifteenpuzzle;

import java.util.*;

public class Puzzle {
	int board[][];
	//store move by using a queue
    Queue<Integer> move;
    int size = 0;
    int distance = 0;
  
    HashMap<Integer,Queue<Integer>> position = new HashMap<Integer,Queue<Integer>>();
    public final static int UP = 0;
    public final static int DOWN = 1;
    public final static int LEFT = 2;
    public final static int RIGHT = 3;
    
    //constructor 
    public Puzzle(int dimension){
        board = new int[dimension][dimension];
        int temp = 1;
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                board[i][j] = temp++;
            }
        }
        board[dimension-1][dimension-1] = 0;

        this.move = new LinkedList<>();
        this.size = dimension;
    }

    public Puzzle(int[][] boardTemp,int dimension){
        this.board = new int[dimension][dimension];
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                this.board[i][j] = boardTemp[i][j];
                position.put(boardTemp[i][j],new LinkedList<>());
                position.get(boardTemp[i][j]).add(i);
                position.get(boardTemp[i][j]).add(j);
            }
        }
        this.move = new LinkedList<>();
        this.size = dimension;
    }

    public Puzzle(int[][] boardTemp,int dimension, Queue<Integer> temp){
        this.board = new int[dimension][dimension];
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                this.board[i][j] = boardTemp[i][j];
                position.put(boardTemp[i][j],new LinkedList<>());
                position.get(boardTemp[i][j]).add(i);
                position.get(boardTemp[i][j]).add(j);
            }
        }
        this.move = temp;
        this.size = dimension;
    }

    public Puzzle moveNum(int direction){
        Puzzle puzzle = new Puzzle(this.board, this.size);
        int col = 0;
        int row = 0;
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(board[i][j] == 0){
                    row = i;
                    col = j;
                }
            }
        }
        switch (direction){
            case UP:{
            	puzzle.board[row][col] = puzzle.board[row-1][col];
            	puzzle.board[row-1][col] = 0;
                break;
            }
            case DOWN:{
            	puzzle.board[row][col] = puzzle.board[row+1][col];
            	puzzle.board[row+1][col] = 0;
                break;
            }
            case LEFT:{
            	puzzle.board[row][col] = puzzle.board[row][col-1];
            	puzzle.board[row][col-1] = 0;
                break;
            }
            case RIGHT:{
            	puzzle.board[row][col] = puzzle.board[row][col+1];
            	puzzle.board[row][col+1] = 0;
                break;
            }
        }
        Iterator<Integer> it = this.move.iterator();
        while(it.hasNext())  {
        	puzzle.move.add(it.next());
        }

        puzzle.move.add(puzzle.board[row][col]);
        switch(direction){
            case UP:{
            	puzzle.move.add(DOWN);
                break;
            }
            case DOWN:{
            	puzzle.move.add(UP);
                break;
            }
            case LEFT:{
                puzzle.move.add(RIGHT);
                break;
            }
            case RIGHT:{
            	puzzle.move.add(LEFT);
                break;
            }
        }
        return puzzle;
    }


    boolean checkMove(int direction){
        int col = 0;
        int row = 0;
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(board[i][j] == 0){
                    row = i;
                    col = j;
                }
            }
        }
        switch(direction){
            case UP:{
                if(row - 1 < 0)
                    return false;
                return true;
            }
            case DOWN:{
                if(row + 1 >= size)
                    return false;
                return true;
            }
            case LEFT:{
                if(col - 1 < 0)
                    return false;
                return true;
            }
            case RIGHT:{
                if(col + 1 >= size)
                    return false;
                return true;
            }
            default:
                return false;
        }

    }

    //get steps
    public String getMove() {
        StringBuilder sb = new StringBuilder();
        while (!this.move.isEmpty()) {
            sb.append(this.move.poll()).append(" ");
            switch(this.move.poll()){
                case UP:
                    sb.append("U");
                    break;
                case DOWN:
                    sb.append("D");
                    break;
                case LEFT:
                    sb.append("L");
                    break;
                case RIGHT:
                    sb.append("R");
                    break;
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    //heuristics for size by size puzzle
    void heuristics(){
        for(int i = 0; i < size ; i++){
            for(int j = 0; j < size; j++){
            	//value of the current element 
                int cur = i*size + j + 1;
                if(cur != size*size) {
                    int r = position.get(cur).poll();
                    position.get(cur).add(r);
                    int c = position.get(cur).poll();
                    position.get(cur).add(c);
                    //use manhattan distance, except the last element is accumulated in the distanceOfThree variable.
                    distance += Math.abs(r - i);
                    distance += Math.abs(c - j);
                }
            }
        }
    }
    

    //check if match
    boolean checkBoard(Puzzle puzzle){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(this.board[i][j] != puzzle.board[i][j])
                    return false;
            }
        }
        return true;
    }

    
    //use hascode to store board
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Puzzle) {
            Puzzle other = (Puzzle) o;
            return Arrays.deepEquals(this.board, other.board);
        }
        return false;
    }

   
}
