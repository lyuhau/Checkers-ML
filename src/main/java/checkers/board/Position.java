package checkers.board;

import java.util.Objects;

public class Position {
	public int x, y;

	public Position(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public static Position parse(String posString) {
		char[] arr = posString.toCharArray();
		return new Position(arr[0]-'a', arr[1]-'1');
	}

	public Position add(Position p) {
		return new Position(p.x + x, p.y + y);
	}

	public Position subtract(Position p) {
		return new Position(x - p.x, y - p.y);
	}

	public boolean onBlackTile() {
		return (x + y) % 2 == 1;
	}

	public int hashCode() {
		return Objects.hash(x, y);
	}

	public boolean equals(Object o) {
		if (o instanceof Position) {
			Position p = (Position) o;
			return p.x == x && p.y == y;
		} else {
			return false;
		}
	}

	public String toString() {
		return String.format("%s%s", (char) ('a'+x), (char) ('1'+y));
	}
}
