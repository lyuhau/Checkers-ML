package checkers.player;

import java.util.*;

import checkers.MoveHistory;
import checkers.board.*;

public class RandomPlayer extends Player {

	public RandomPlayer(Side direction) {
		super(direction);
	}

	public RandomPlayer(Side direction, String name, List<String> pieceSkins) {
		super(direction, name, pieceSkins);
	}

	public Move getNextMove(Board board, Map<Position, List<Move>> availableMoves, MoveHistory history) {
		Random rand = new Random();

		List<Position> pieces = board.getPieces(side);
		Move move = null;
		do {
			Position randomFrom = pieces.get(rand.nextInt(pieces.size()));

			List<Move> moves = availableMoves.get(randomFrom);
			if (!moves.isEmpty()) {
				move = moves.get(rand.nextInt(moves.size()));
			} else {
				pieces.remove(randomFrom);
				if (pieces.isEmpty()) {
					throw new IllegalStateException("No moves available");
				}
			}

		} while (move == null);

		return move;
	}

}
