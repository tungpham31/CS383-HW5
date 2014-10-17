public class Utilities {

	/*
	 * Calculate the lowerbound for domain of cells with constraint that they
	 * are unique and sum to $sum.
	 */
	public static int calDomainLowerbound(int nCells, int sum) {
		if (nCells <= 0) {
			throw new Error("Illegal arguments");
		}

		if (nCells == 1) {
			return sum;
		}

		return Math.max(1, sum - 9 * (nCells - 1) + (nCells - 1) * (nCells - 2)
				/ 2);
	}

	/*
	 * Calculate the upperbound for domain of cells with constraint that they
	 * are unique and sum to $sum.
	 */
	public static int calDomainUpperbound(int nCells, int sum) {
		if (nCells <= 0) {
			throw new Error("Illegal arguments");
		}

		if (nCells == 1) {
			return sum;
		}

		return Math
				.min(9, sum - (nCells - 1) - (nCells - 1) * (nCells - 2) / 2);
	}

}
