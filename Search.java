public class Search {

	public static Board compute(Board board) {
		if (board.isComplete()) {
			return board;
		}

		Cell cell = board.selectUnassignedCell();
		for (int value : board.orderDomainValues(cell)) {
			Board assignedBoard = board.assign(cell, value);
			if (assignedBoard.isConsistent()) {
				Board nextBoard = compute(assignedBoard);
				if (nextBoard != null) {
					return nextBoard;
				}
			}
		}
		return null;

	}

}
