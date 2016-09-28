package checkers.player;

import java.util.*;

import checkers.MoveHistory;
import checkers.board.*;
import checkers.piece.*;

public abstract class Player {
	public Side side;

	public String name;
	public List<String> pieceSkins;

	public Player(Side side) {
		this(side, side.name, side.pieceSkins);
	}

	public Player(Side side, String name, List<String> pieceSkins) {
		super();
		this.side = side;
		this.name = name;
		this.pieceSkins = pieceSkins;
	}

	public String printPiece(Piece piece) {
		if (piece instanceof Pawn) {
			return pieceSkins.get(0);
		} else if (piece instanceof King) {
			return pieceSkins.get(1);
		} else {
			return null;
		}
	}

	public String toString() {
		return name;
	}

	public abstract Move getNextMove(Board board, Map<Position, List<Move>> availableMoves, MoveHistory history);
}
