import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.junit.Test;

public class SearchTest {

	@Test
	public void sampleTest() throws IOException {
		String testPath = "/Users/tungpham31/Documents/workspace/CS383-HW5/Tests/sample.txt";
		String input = readInputFromFile(testPath);
		int[][][] initBoard = readBoardFromFile(input);
		String result = Search.compute(BoardBuilder.fromString(input))
				.assignmentsToString();
		assertTrue(isValidAssignments(initBoard, result));
	}

	@Test
	public void simple_3x3() throws IOException {
		String testPath = "/Users/tungpham31/Documents/workspace/CS383-HW5/Tests/3x3-simple.problem";
		String input = readInputFromFile(testPath);
		int[][][] initBoard = readBoardFromFile(input);
		String result = Search.compute(BoardBuilder.fromString(input))
				.assignmentsToString();
		assertTrue(isValidAssignments(initBoard, result));
	}
	
	@Test
	public void simple_4x4() throws IOException {
		String testPath = "/Users/tungpham31/Documents/workspace/CS383-HW5/Tests/4x4-simple.problem";
		String input = readInputFromFile(testPath);
		int[][][] initBoard = readBoardFromFile(input);
		String result = Search.compute(BoardBuilder.fromString(input))
				.assignmentsToString();
		assertTrue(isValidAssignments(initBoard, result));
	}
	
	@Test
	public void easy_6x6() throws IOException {
		String testPath = "/Users/tungpham31/Documents/workspace/CS383-HW5/Tests/6x6-easy.test";
		String input = readInputFromFile(testPath);
		int[][][] initBoard = readBoardFromFile(input);
		String result = Search.compute(BoardBuilder.fromString(input))
				.assignmentsToString();
		assertTrue(isValidAssignments(initBoard, result));
	}
	
	@Test
	public void hard_6x6() throws IOException {
		String testPath = "/Users/tungpham31/Documents/workspace/CS383-HW5/Tests/6x6-hard.test";
		String input = readInputFromFile(testPath);
		int[][][] initBoard = readBoardFromFile(input);
		String result = Search.compute(BoardBuilder.fromString(input))
				.assignmentsToString();
		assertTrue(isValidAssignments(initBoard, result));
	}
	
	@Test
	public void easy_9x9() throws IOException {
		String testPath = "/Users/tungpham31/Documents/workspace/CS383-HW5/Tests/9x9-easy.test";
		String input = readInputFromFile(testPath);
		int[][][] initBoard = readBoardFromFile(input);
		String result = Search.compute(BoardBuilder.fromString(input))
				.assignmentsToString();
		assertTrue(isValidAssignments(initBoard, result));
	}
	
	@Test
	public void hard_9x9() throws IOException {
		String testPath = "/Users/tungpham31/Documents/workspace/CS383-HW5/Tests/9x9-hard.test";
		String input = readInputFromFile(testPath);
		int[][][] initBoard = readBoardFromFile(input);
		String result = Search.compute(BoardBuilder.fromString(input))
				.assignmentsToString();
		assertTrue(isValidAssignments(initBoard, result));
	}

	@Test
	public void selfTest1() throws IOException {
		String testPath = "/Users/tungpham31/Documents/workspace/CS383-HW5/Tests/3x3-simple.problem";
		String input = readInputFromFile(testPath);
		int[][][] initBoard = readBoardFromFile(input);
		String result = "1 1 1\n2 2 4\n1 2 3\n2 1 3";

		assertFalse(isValidAssignments(initBoard, result));
	}

	@Test
	public void selfTest2() throws IOException {
		String testPath = "/Users/tungpham31/Documents/workspace/CS383-HW5/Tests/3x3-simple.problem";
		String input = readInputFromFile(testPath);
		int[][][] initBoard = readBoardFromFile(input);
		String result = "1 1 1\n2 2 4\n1 2 3";

		assertFalse(isValidAssignments(initBoard, result));
	}
	
	@Test
	public void selfTest3() throws IOException {
		String testPath = "/Users/tungpham31/Documents/workspace/CS383-HW5/Tests/3x3-simple.problem";
		String input = readInputFromFile(testPath);
		int[][][] initBoard = readBoardFromFile(input);
		String result = "1 1 2\n2 2 5\n1 2 2\n2 1 1";

		assertFalse(isValidAssignments(initBoard, result));
	}

	/**
	 * Helper methods.
	 */

	private int[][][] readBoardFromFile(String input) throws IOException {
		BufferedReader br = new BufferedReader(new StringReader(input));
		String line = br.readLine();
		StringTokenizer tokenizer = new StringTokenizer(line);
		int n = Integer.valueOf(tokenizer.nextToken());
		int m = Integer.valueOf(tokenizer.nextToken());
		final int[][][] board = new int[m][n][2];

		while ((line = br.readLine()) != null) {
			tokenizer = new StringTokenizer(line);
			int y = Integer.valueOf(tokenizer.nextToken());
			int x = Integer.valueOf(tokenizer.nextToken());
			char clue = tokenizer.nextToken().charAt(0);
			int value = Integer.valueOf(tokenizer.nextToken());

			if (clue == 'x') {
				board[x][y][0] = -1;
				board[x][y][1] = -1;
			}
			if (clue == 'h') {
				board[x][y][0] = value;
			}
			if (clue == 'v') {
				board[x][y][1] = value;
			}
		}

		br.close();

		return board;
	}

	private boolean isValidAssignments(int[][][] initBoard, String result) {
		final Map<Cell, Integer> assignments = parseResult(result);
		return isComplete(initBoard, assignments)
				&& satisfySumConstraint(initBoard, assignments)
				&& satisfyUniqueConstraint(initBoard, assignments);
	}

	private Map<Cell, Integer> parseResult(String result) {
		final Map<Cell, Integer> assignments = new HashMap<Cell, Integer>();
		String[] lines = result.split("\n");

		for (String line : lines) {
			String[] tokens = line.split(" ");
			int x = Integer.valueOf(tokens[0]);
			int y = Integer.valueOf(tokens[1]);
			int value = Integer.valueOf(tokens[2]);
			assignments.put(new Cell(x, y), value);
		}

		return assignments;
	}

	private boolean isComplete(int[][][] initBoard,
			Map<Cell, Integer> assignemnts) {
		int numEmptyCells = 0;
		for (int i = 0; i < initBoard.length; i++)
			for (int j = 0; j < initBoard[i].length; j++) {
				if (isEmptyCell(initBoard, i, j)) {
					numEmptyCells++;
				}
			}

		return numEmptyCells == assignemnts.size();
	}

	private boolean satisfySumConstraint(int[][][] initBoard,
			Map<Cell, Integer> assignemnts) {
		// Check rows.
		for (int i = 0; i < initBoard.length; i++) {
			int sum = 0;
			for (int j = initBoard[i].length - 1; j >= 0; j--) {
				if (isEmptyCell(initBoard, i, j)) {
					sum += assignemnts.get(new Cell(j, i));
				}
				if (isClueCell(initBoard, i, j)) {
					if (sum != initBoard[i][j][0]) {
						System.out.println("Sum Contraint Violated on " + i
								+ " " + j);
						return false;
					}
					sum = 0;
				}
			}
		}

		// Check columns.
		for (int j = 0; j < initBoard[0].length; j++) {
			int sum = 0;
			for (int i = initBoard.length - 1; i >= 0; i--) {
				if (isEmptyCell(initBoard, i, j)) {
					sum += assignemnts.get(new Cell(j, i));
				}
				if (isClueCell(initBoard, i, j)) {
					if (sum != initBoard[i][j][1]) {
						System.out.println("Sum Contraint Violated on " + i
								+ " " + j);
						return false;
					}
					sum = 0;
				}
			}
		}

		return true;
	}

	private boolean satisfyUniqueConstraint(int[][][] initBoard,
			Map<Cell, Integer> assignemnts) {
		// Check rows.
		for (int i = 0; i < initBoard.length; i++) {
			final Set<Integer> values = new HashSet<Integer>();
			for (int j = initBoard[i].length - 1; j >= 0; j--) {
				if (isEmptyCell(initBoard, i, j)) {
					int value = assignemnts.get(new Cell(j, i));
					if (values.contains(value)) {
						System.out.println("Unique Contraint Violated on " + i
								+ " " + j);
						return false;
					} else {
						values.add(value);
					}
				}

				if (isClueCell(initBoard, i, j)) {
					values.clear();
				}
			}
		}

		// Check columns.
		for (int j = 0; j < initBoard[0].length; j++) {
			final Set<Integer> values = new HashSet<Integer>();
			for (int i = initBoard.length - 1; i >= 0; i--) {
				if (isEmptyCell(initBoard, i, j)) {
					int value = assignemnts.get(new Cell(j, i));
					if (values.contains(value)) {
						System.out.println("Unique Contraint Violated on " + i
								+ " " + j);
						return false;
					} else {
						values.add(value);
					}
				}

				if (isClueCell(initBoard, i, j)) {
					values.clear();
				}
			}
		}

		return true;
	}

	private String readInputFromFile(String filePath) {
		File f = new File(filePath);
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line;
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	private boolean isEmptyCell(int[][][] board, int i, int j) {
		return board[i][j][0] == 0 && board[i][j][1] == 0;
	}

	private boolean isClueCell(int[][][] board, int i, int j) {
		return board[i][j][0] > 0 || board[i][j][1] > 0;
	}
}
