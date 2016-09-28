package checkers;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;

import checkers.board.*;

public class MoveHistory extends ArrayList<BoardData> {
	private static final long serialVersionUID = 6603697538952051495L;

	public void addMove(Board board, Side side, Move move) {
		add(new BoardData(board, side, move));
	}

	public BoardData getLastData() {
		return get(size()-1);
	}

	public Board getStartingBoard() {
		return get(0).board;
	}

	public Board getBoard(int index) {
		return get(index).board;
	}

	public Board getLastBoard() {
		if (isEmpty()) {
			return null;
		}
		return getLastData().board;
	}

	public Side getSide(int index) {
		return get(index).side;
	}

	public Side getLastSide() {
		if (isEmpty()) {
			return null;
		}
		return getLastData().side;
	}

	public Move getMove(int index) {
		return get(index).move;
	}

	public Move getLastMove() {
		if (isEmpty()) {
			return null;
		}
		return getLastData().move;
	}

	@Override
	public String toString() {
		return getStartingBoard() + "\n" + stream().map(e -> e.move).collect(toList());
	}
}
