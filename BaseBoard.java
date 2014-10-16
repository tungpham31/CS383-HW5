/*
 * Copyright © 2014 Marc Liberatore.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of the University of Massachusetts.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A representation of a Kakuro board: both constraints (the board) and
 * assignments. For use with BacktrackingSearch.
 *
 * Created by liberato on 10/8/14.
 */
public class BaseBoard {
	private final Map<Cell, Integer> assignments;
	private final List<SumAndUniqueConstraint> sumAndUniqueConstraints;

	/**
	 * Construct a new Board.
	 * 
	 * @param assignments
	 *            an map of cells to values; use null to represent unfilled
	 *            cells
	 * @param sumAndUniqueConstraints
	 */
	public BaseBoard(Map<Cell, Integer> assignments,
			List<SumAndUniqueConstraint> sumAndUniqueConstraints) {
		this.assignments = assignments;
		this.sumAndUniqueConstraints = sumAndUniqueConstraints;
	}

	/**
	 *
	 * @return true if all cells are assigned values
	 */
	public boolean isComplete() {
		for (Integer i : assignments.values()) {
			if (i == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * TODO: Consider using MRV heuristic to choose next cell to assign
	 *
	 * @return an unassigned cell
	 */
	public Cell selectUnassignedCell() {
		for (Map.Entry<Cell, Integer> entry : assignments.entrySet()) {
			if (entry.getValue() == null) {
				return entry.getKey();
			}
		}
		throw new RuntimeException(
				"Programmer error: called selectUnassignedCell on board with no unassigned cells");
	}

	/**
	 * TODO: Consider using LSV heuristic to order the variables
	 * 
	 * @param cell
	 * @return an array of possible values for a given cell
	 */
	public int[] orderDomainValues(Cell cell) {
		return new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	}

	/**
	 * Create and return a new copy of the board, with the value assigned to the
	 * cell.
	 *
	 * TODO: If you change how mutable state (assignments) is stored in the
	 * board, you will need to make sure to copy that state here, or change the
	 * semantics of the constructor to be a a copy constructor.
	 *
	 * @param cell
	 * @param value
	 * @return a new copy of the board
	 */
	public Board assign(Cell cell, int value) {
		Map<Cell, Integer> newAssignments = new HashMap<Cell, Integer>(
				assignments);
		newAssignments.put(cell, value);
		return new Board(newAssignments, sumAndUniqueConstraints);
	}

	/**
	 *
	 * @return true if the current assignments are consistent with the
	 *         constraints
	 */
	public boolean isConsistent() {
		for (SumAndUniqueConstraint sumAndUniqueConstraint : sumAndUniqueConstraints) {
			List<Integer> assignedValues = new ArrayList<Integer>();
			for (Cell cell : sumAndUniqueConstraint.cells) {
				assignedValues.add(assignments.get(cell));
			}
			if (!uniqueExceptingNulls(assignedValues)) {
				return false;
			}

			if (incomplete(assignedValues)) {
				continue;
			}

			if (sum(assignedValues) != sumAndUniqueConstraint.sum) {
				return false;
			}
		}
		return true;
	}

	private static int sum(List<Integer> assignedValues) {
		Set<Integer> valueSet = new HashSet<Integer>(assignedValues);
		valueSet.remove(null);
		int sum = 0;
		for (int i : valueSet) {
			sum += i;
		}
		return sum;
	}

	private static boolean incomplete(List<Integer> assignedValues) {
		if (assignedValues.contains(null)) {
			return true;
		}
		return false;
	}

	private static boolean uniqueExceptingNulls(List<Integer> values) {
		List<Integer> valueList = new ArrayList<Integer>(values);

		// remove all nulls from the valueList
		// note we copied the values parameter into valueList! otherwise
		// our manipulations would leak outside this method thanks to
		// Java's pass-by-reference semantics.
		while (valueList.remove(null)) {
		}

		Set<Integer> valueSet = new HashSet<Integer>(valueList);

		if (valueList.size() == valueSet.size()) {
			return true;
		}
		return false;
	}

	public String assignmentsToString() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Cell, Integer> entry : assignments.entrySet()) {
			Cell cell = entry.getKey();
			Integer value = entry.getValue();
			sb.append(cell);
			sb.append(" ");
			sb.append(value);
			sb.append("\n");
		}
		return sb.toString();
	}
}
