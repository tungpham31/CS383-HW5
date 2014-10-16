import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class KakuroSolver {

	public static void main(String[] args) throws IOException {
		String input = readInputFromFile(args[0]);

		Board initBoard = BoardBuilder.fromString(input);

		Board assignedBoard = Search.compute(initBoard);

		System.out.print(assignedBoard.assignmentsToString());
	}

	private static String readInputFromFile(String filePath) throws IOException {
		final File f = new File(filePath);
		final StringBuilder sb = new StringBuilder();
		final BufferedReader br = new BufferedReader(new FileReader(f));

		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}
		br.close();

		return sb.toString();
	}
}
