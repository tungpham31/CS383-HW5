import java.util.List;
import java.util.Map;


public class Board extends BaseBoard{

	public Board(Map<Cell, Integer> assignments,
			List<SumAndUniqueConstraint> sumAndUniqueConstraints) {
		super(assignments, sumAndUniqueConstraints);
	}

}
