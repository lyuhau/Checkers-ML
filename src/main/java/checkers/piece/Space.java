package checkers.piece;

import java.util.*;

import checkers.MoveHistory;
import checkers.board.*;

public class Space extends Piece {
	private static final Space SINGLETON = new Space(null);

	private Space(Side side) {
		super(side);
	}

	public static Space getInstance() {
		return SINGLETON;
	}

	public List<Move> getAvailableMoves(Position position, Board board, MoveHistory history) {
		return new ArrayList<Move>();
	}

	public String toString() {
		return " ";
	}
}
