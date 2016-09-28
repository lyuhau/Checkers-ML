package checkers.board;

import static java.util.Arrays.asList;

import java.util.List;

import checkers.piece.*;

public enum Side {
	WHITE (1,  "White", asList("o", "O")),
	BLACK (-1, "Black", asList("x", "X")),
	;

	static {
		WHITE.otherSide = BLACK;
		BLACK.otherSide = WHITE;
	}

	public int yMult;
	public Side otherSide;
	public String name;
	public List<String> pieceSkins;

	private Side(int yMult, String name, List<String> pieceSkins) {
		this.yMult = yMult;
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
}