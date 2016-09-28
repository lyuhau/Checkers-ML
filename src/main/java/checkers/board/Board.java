package checkers.board;

import java.util.*;

import checkers.piece.*;

public class Board {
	public Piece[][] board;

	public Board() {
		this(new Piece[8][8]);
	}

	public Board(Piece[][] board) {
		super();
		this.board = board;
	}

	public static Board parse(String boardString) {
		Piece[][] board = new Piece[8][8];

		String[] lines = boardString.split("[\r\n]+");
		for (int i=0; i<8; i++) {
			char[] pieces = lines[i+1].substring(2).replaceAll("(.) ", "$1").toCharArray();
			for (int j=0; j<8; j++) {
				switch (pieces[j]) {
				case ' ': board[j][i] = Space.getInstance(); break;
				case 'o': board[j][i] = new Pawn(Side.WHITE); break;
				case 'O': board[j][i] = new King(Side.WHITE); break;
				case 'x': board[j][i] = new Pawn(Side.BLACK); break;
				case 'X': board[j][i] = new King(Side.BLACK); break;
				default: throw new IllegalArgumentException(String.format("Unknown piece %s at %s", pieces[j], new Position(i, j)));
				}
			}
		}

		return new Board(board);
	}

	public static Board parseMin(String boardString, boolean reverse) {
		if (reverse) {
			boardString = new StringBuilder(boardString).reverse().toString();
		}

		Piece[][] board = new Piece[8][8];

		for (int i=0; i<8; i++) {
			Arrays.fill(board[i], Space.getInstance());
		}

		char[] pieces = boardString.toCharArray();
		for (int i=0; i<32; i++) {
			int y = i/4;
			int x = i%4*2+((y+1)%2);
			switch (pieces[i]) {
			case ' ': board[x][y] = Space.getInstance(); break;
			case 'o': board[x][y] = new Pawn(Side.WHITE); break;
			case 'O': board[x][y] = new King(Side.WHITE); break;
			case 'x': board[x][y] = new Pawn(Side.BLACK); break;
			case 'X': board[x][y] = new King(Side.BLACK); break;
			default: throw new IllegalArgumentException(String.format("Unknown piece %s at %s", pieces[i], new Position(i, i)));
			}
		}

		return new Board(board);
	}

	public boolean outOfBounds(Position position) {
		return position.x < 0 || position.x > 7 ||
				position.y < 0 || position.y > 7;
	}

	public boolean outOfBounds(Move move) {
		return outOfBounds(move.from) || outOfBounds(move.to);
	}

	public Piece getPiece(Position position) {
		return board[position.x][position.y];
	}

	public void set(Position position, Piece piece) {
		board[position.x][position.y] = piece;
	}

	public List<Position> getPieces(Side side) {
		List<Position> positions = new ArrayList<Position>();

		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				if (board[i][j].side == side) {
					positions.add(new Position(i, j));
				}
			}
		}

		return positions;
	}

	public Board clone() {
		Board clone = new Board();
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				clone.board[i][j] = board[i][j];
			}
		}
		return clone;
	}

	public String minString(boolean reverse) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<8; i++) {
			for (int j=(i+1)%2; j<8; j+=2) {
				sb.append(getPiece(new Position(j, i)));
			}
		}

		if (reverse) {
			sb.reverse();
		}

		return sb.toString();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("  a b c d e f g h\n");
		for (int i=0; i<board.length; i++) {
			sb.append(i+1).append(" ");
			for (int j=0; j<board[i].length; j++) {
				sb.append(getPiece(new Position(j, i))).append(" ");
			}
			sb.append("\n");
		}

		return sb.toString();
	}
}
