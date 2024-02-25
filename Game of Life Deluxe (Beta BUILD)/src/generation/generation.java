package generation;
import java.util.Random;
public class generation {
	private Boolean[][] board;
	private Boolean spontFormation = false;
	
	public generation(Boolean spont, Boolean[][] board) {
		this.spontFormation = spont;
		this.board = board;
	}
	
	/**
	 * Picks a random space, and if that space is dec`eased, there is a 1/(length * width) change that it will suddenly come to life!
	 * @param generation the board where in which the game is played.
	 */
	public void suddenLife(Boolean square) {
		Random rand = new Random(System.currentTimeMillis());
		int mu = 49;
		int workChance = rand.nextInt();
		if(workChance == 0) {
			workChance = rand.nextInt();
		}
		if(!square) {
			if(workChance % mu != 0) {
				square = true;
				return;
			}
			else {
				return;
			}
		}
		
	}
	
	public static int findNeighbors(Boolean[][] board, int x, int y) {
		int neighborCount = 0;
		boolean lookingSquare = false;
		int xPositions[] = {-1, 0, 1, 1, 1, 0, -1, -1};
		int yPositions[] = {1, 1, 1, 0, -1, -1, -1, 0};
		//String positionNames[] = {"UL", "UU", "UR", "CR", "LR", "LD", "LL", "CL"};
		for(int i = 0; i < xPositions.length; i++) {
			if((x + xPositions[i]) < board.length && (x + xPositions[i]) >= 0){
				if((y + yPositions[i]) < board[0].length && (y + yPositions[i]) >= 0) {
					lookingSquare = board[(x + xPositions[i])][(y + yPositions[i])];
					//System.out.println("Checking position " + positionNames[i] + ": " + lookingSquare);
					if(lookingSquare) {neighborCount++;}
				}
			}
		}
		return neighborCount;
	}
	
	public static Boolean consequences(boolean square, int neighborCount, boolean isLiving) {
		if(isLiving) {
			switch(neighborCount) {
				case 2:
				case 3: 
					return true;
				default:
					return false;
			}
		}
		else {
			switch(neighborCount) {
			case 2:
				return false;
			case 3:
				return true;
			default:
				return false;
			}
		}
	}

	public static Boolean[][] advanceGeneration(Boolean[][] currGeneration, int x, int y, Boolean spont){
		int neighborCount = 0, indexX = 0, indexY = 0; 
		generation generation = new generation(null, null);
		Boolean[][] newGen = currGeneration;
		
		// W.I.P For loop to call findNeighbors()
		for(indexX = 0; indexX < x; indexX++) {
			for(indexY = 0; indexY < y; indexY++) {
				neighborCount = findNeighbors(currGeneration, indexX, indexY);
				newGen[indexX][indexY] = consequences(currGeneration[indexX][indexY], neighborCount, currGeneration[indexX][indexY]);
				if(!currGeneration[indexX][indexY] && spont) { 
					generation.suddenLife(newGen[indexX][indexY]);
				}
			}
		}
		generation.printGeneration(newGen);
		return newGen;
	}
	
	public void printGeneration(Boolean[][] gen) {
		for(int x = 0; x < gen.length; x++) {
			for(int y = 0; y < gen[x].length; y++) {
				char state = (gen[x][y]) ? 'A' : 'D';
				System.out.print("[" + state + "] ");
			}
			System.out.println();
			System.out.println();
		}
		for(int i = 0; i < 20; i++) {
			System.out.print("- ");
		}
		System.out.println();
	}
	public Boolean[][] createGeneration(int length, int width, int seed) {
		Boolean[][] generation = new Boolean[length][width];
		Random rand = new Random(seed);
		Random modulo = new Random(seed * seed);
		for(int i = 0; i< length; i++) {
			for(int j = 0; j < width; j++) {
				int fallVal = rand.nextInt();
				int mod = modulo.nextInt();
				if(fallVal % mod < 13) {
					generation[i][j] = true;
				}
				else {
					generation[i][j] = false;
				}
			}
		}
		
		System.out.println("Generation created, sending 2D representation:");
		try {
		printGeneration(generation);
		}finally {
			System.out.println("Generation Success!");
		}
		return generation;
	}
	
	public void runSimulation(Boolean[][] board, int numOfRuns, Boolean spont) {
		for(int i = 0; i < numOfRuns; i++) {
			advanceGeneration(board, board.length, board[0].length, spont);
		}
	}
	
	public static void main(String[] args) {
		int length = 7, width = 7;
		Random rand = new Random(System.currentTimeMillis());
		int seed = rand.nextInt();
		seed = rand.nextInt();
		seed = rand.nextInt();
		seed = rand.nextInt();
		Boolean[][] board = new Boolean[length][width];
		Boolean spont = true;
		generation gen = new generation(spont, board );
		gen.board = gen.createGeneration(length, width, seed);
		
		gen.runSimulation(gen.board, 3, gen.spontFormation);
	}
}
