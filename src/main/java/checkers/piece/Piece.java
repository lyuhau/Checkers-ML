package checkers.piece;

import java.util.List;

import checkers.MoveHistory;
import checkers.board.*;

public abstract class Piece {
	public Side side;

	public Piece(Side side) {
		super();
		this.side = side;
	}

	public boolean belongsToSide(Side side) {
		return this.side == side;
	}

	public String toString() {
		return side.printPiece(this);
	}

	public abstract List<Move> getAvailableMoves(Position position, Board board, MoveHistory moveHistory);
}
