package checkers.board;

import static java.lang.Math.abs;

import java.util.Objects;

public class Move {
	public Position from, to;

	public Move(Position from, Position to) {
		super();
		this.from = from;
		this.to = to;
	}

	public static Move parse(String moveString) {
		Position from = Position.parse(moveString.substring(0, 2));
		Position to = Position.parse(moveString.substring(2, 4));

		return new Move(from, to);
	}

	public boolean isKill() {
		return abs(from.x - to.x) != 1 || abs(from.y - to.y) != 1;
	}

	public Position getKillPosition() {
		return new Position((from.x+to.x)/2, (from.y+to.y)/2);
	}

	public boolean isPromotion(Side side) {
		return to.y == 3.5 + 3.5*side.yMult;
	}

	public int hashCode() {
		return Objects.hash(from, to);
	}

	public boolean equals(Object o) {
		if (o instanceof Move) {
			Move move = (Move) o;
			return move.from.equals(from) && move.to.equals(to);
		} else {
			return false;
		}
	}

	public String toString() {
		return String.format("%s%s", from, to);
	}
}
