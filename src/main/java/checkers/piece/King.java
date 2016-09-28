package checkers.piece;

import java.util.*;
import java.util.stream.Collectors;

import checkers.MoveHistory;
import checkers.board.*;

public class King extends Piece {
	public King(Side side) {
		super(side);
	}

	public List<Move> getAvailableMoves(Position from, Board board, MoveHistory history) {
		List<Move> moves = new ArrayList<Move>();

		Move lastMove = history.getLastMove();
		if (!history.isEmpty() && history.getLastSide() == side && lastMove.isKill()) {
			if (from.equals(lastMove.to)) {
				Position diff = lastMove.to.subtract(lastMove.from);

				moves.add(new Move(from, from.add(diff)));
			}
		} else {
			for (int i=-1; i<=1; i+=2) {
				for (int j=-1; j<=1; j+=2) {
					for (int k=1; k<=2; k++) {
						Position to = new Position(from.x + i*k, from.y + j*k);

						moves.add(new Move(from, to));
					}
				}
			}
		}

		return moves.stream()
				.filter(move -> !board.outOfBounds(move))
				.filter(move -> board.getPiece(move.to) == Space.getInstance())
				.filter(move -> !move.isKill() || board.getPiece(move.getKillPosition()).belongsToSide(side.otherSide))
				.collect(Collectors.toList());
	}
}
