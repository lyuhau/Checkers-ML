package checkers;

import checkers.board.*;

public class BoardData {
	public Board board;
	public Side side;
	public Move move;

	public BoardData() {
		super();
	}

	public BoardData(Board board, Side side, Move move) {
		super();
		this.board = board.clone();
		this.side = side;
		this.move = move;
	}
}