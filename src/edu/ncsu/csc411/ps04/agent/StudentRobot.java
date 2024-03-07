package edu.ncsu.csc411.ps04.agent;

import edu.ncsu.csc411.ps04.environment.Environment;
import edu.ncsu.csc411.ps04.environment.Position;
import edu.ncsu.csc411.ps04.environment.Status;

public class StudentRobot extends Robot {

	public StudentRobot(Environment env) {
		super(env);
	}

	/**
	 * Problem Set 04 - For this Problem Set you will design an agent that can play Connect Four.
	 * The goal of Connect Four is to "connect" four (4) markers of the same color (role)
	 * horizontally, vertically, or diagonally. In this exercise your getAction method should
	 * return an integer between 0 and 6 (inclusive), representing the column you would like to
	 * "drop" your marker. Unlike previous Problem Sets, in this environment, you will be alternating
	 * turns with another agent.
	 *
	 * There are multiple example agents found in the edu.ncsu.csc411.ps04.examples package.
	 * Each example agent provides a brief explanation on its decision process, as well as demonstrations
	 * on how to use the various methods from Environment. In order to pass this Problem Set, you must
	 * successfully beat RandomRobot, VerticalRobot, and HorizontalRobot 70% of the time as both the
	 * YELLOW and RED player. This is distributed across the first six (6) test cases. In addition,
	 * you have the chance to earn EXTRA CREDIT by beating GreedyRobot (test cases 07 and 08) 70% of
	 * the time (10% possible, 5% per test case). Finally, if you successfully pass the test cases,
	 * you are welcome to test your implementation against your classmates.
	 *
	 * While Simple Reflex or Model-based agent may be able to succeed, consider exploring the Minimax
	 * search algorithm to maximize your chances of winning. While the first two will be easier, you may
	 * want to place priority on moves that prevent the adversary from winning.
	 */

	/**
	 * The getAction method determines the best move by iterating over all possible columns,
	 * and evaluating the new board state.
	 * The evaluateBoard method checks for winning conditions and
	 * returns a score based on the number of consecutive discs of the agent's role.
	 * The cloneBoard and drop methods are helper methods used to create a deep copy of the game board and simulate a disc drop, respectively.
	 * The best column, which leads to the best score, is returned by the getAction method, indicating the column where the agent will drop its disc.
	 */
	@Override
	public int getAction() {
		// Get a copy of the current game board
		Position[][] board = env.clonePositions();

		// Initialize the best score to negative infinity
		int bestScore = Integer.MIN_VALUE;
		int bestColumn = -1;

		// Iterate over all possible columns
		for (int column = 0; column < env.getCols(); column++) {
			// If the column is not valid, skip it
			if (!env.getValidActions().contains(column)) continue;

			// Clone the board and drop the disc in the column
			Position[][] newBoard = cloneBoard(board);
			drop(newBoard, column, getRole());

			// Evaluate the new board state
			int score = evaluateBoard(newBoard);

			// If the score is better than the current best score, update the best score and column
			if (score > bestScore) {
				bestScore = score;
				bestColumn = column;
			}
		}

		return bestColumn;
	}

	private Position[][] cloneBoard(Position[][] board) {
		// Create a blank version of the board
		Position[][] clone = new Position[board.length][board[0].length];
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				// Update the square to either blank, red, or yellow
				if (board[row][col].getStatus() == Status.BLANK)
					clone[row][col] = new Position(row, col, Status.BLANK);
				else if (board[row][col].getStatus() == Status.RED)
					clone[row][col] = new Position(row, col, Status.RED);
				else if (board[row][col].getStatus() == Status.YELLOW)
					clone[row][col] = new Position(row, col, Status.YELLOW);
			}
		}
		return clone;
	}

	private void drop(Position[][] board, int column, Status role) {
		Status status = Status.RED;
		if (role == Status.YELLOW) {
			status = Status.YELLOW;
		}
		Position[][] newBoard = new Position[board.length][board[0].length];
		for(int row = 0; row < board.length; row++) {
			for(int col = 0; col < board[0].length; col++) {
				if(row == board.length - 1 && col == column && board[row][col].getStatus() == Status.BLANK) {
					newBoard[row][col] = new Position(row, col, status);
				} else {
					newBoard[row][col] = board[row][col];
				}
			}
		}
		board = newBoard;
	}

	private int evaluateBoard(Position[][] board) {
		int score = 0;

		// Check horizontal directions
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length - 3; col++) {
				score += countConsecutiveDiscs(board, row, col, 1, 0);
			}
		}

		// Check vertical directions
		for (int row = 0; row < board.length - 3; row++) {
			for (int col = 0; col < board[0].length; col++) {
				score += countConsecutiveDiscs(board, row, col, 0, 1);
			}
		}

		// Check diagonal directions
		for (int row = 0; row < board.length - 3; row++) {
			for (int col = 0; col < board[0].length - 3; col++) {
				score += countConsecutiveDiscs(board, row, col, 1, 1);
				score += countConsecutiveDiscs(board, row, col, 1, -1);
			}
		}

		return score;
	}

	private int countConsecutiveDiscs(Position[][] board, int row, int col, int dRow, int dCol) {
		int count = 0;
		Status role = board[row][col].getStatus();

		for (int i = 0; i < 4; i++) {
			int newRow = row + i * dRow;
			int newCol = col + i * dCol;

			if (newRow >= 0 && newRow < board.length && newCol >= 0 && newCol < board[0].length &&
					board[newRow][newCol].getStatus() == role) {
				count++;
			} else {
				break;
			}
		}

		return count;
	}



}
