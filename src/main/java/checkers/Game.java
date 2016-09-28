package checkers;

import static java.lang.Math.floorMod;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.Map.Entry;

import checkers.board.*;
import checkers.piece.*;
import checkers.player.*;

public class Game {
	public Map<Side, Player> players;

	public Board board;
	public Side side;
	public MoveHistory history;

	public Map<Position, List<Move>> availableMoves;

	public Game() {
		this(new Board(), Side.WHITE, new HumanPlayer(Side.WHITE), new HumanPlayer(Side.BLACK));
		resetBoard();
	}

	public Game(Player white, Player black) {
		this(new Board(), Side.WHITE, white, black);
		resetBoard();
	}

	public Game(Board board, Side startingSide) {
		this(board, startingSide, new HumanPlayer(Side.WHITE), new HumanPlayer(Side.BLACK));
	}

	public Game(Board board, Side startingSide, Player white, Player black) {
		super();
		this.board = board;
		this.side = startingSide;

		this.history = new MoveHistory();

		this.players = new HashMap<Side, Player>();
		this.players.put(Side.WHITE, white);
		this.players.put(Side.BLACK, black);
	}

	public void resetBoard() {
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				board.set(new Position(i, j), Space.getInstance());
			}
		}

		// White
		for (int i=0; i<3; i++) {
			for (int j=floorMod(-i+1, 2); j<8; j+=2) {
				board.set(new Position(j, 0+i), new Pawn(Side.WHITE));
			}
		}

		// Black
		for (int i=0; i<3; i++) {
			for (int j=floorMod(-i, 2); j<8; j+=2) {
				board.set(new Position(j, 5+i), new Pawn(Side.BLACK));
			}
		}
	}

	public Map<Position, List<Move>> getAvailableMoves() {
		Map<Position, List<Move>> availableMoves = board.getPieces(side).stream()
				.collect(toMap(
						identity(),
						(Position pos) -> board.getPiece(pos).getAvailableMoves(pos, board, history)));

		// Check for any kill moves
		Map<Position, List<Move>> availableKillMoves = availableMoves.entrySet().stream()
				.collect(toMap(
						Entry::getKey,
						entry -> entry.getValue().stream()
								.filter(Move::isKill)
								.collect(toList())));

		return availableKillMoves.values().stream().mapToInt(Collection::size).sum() != 0 ?
				availableKillMoves : availableMoves;
	}

	public String validateMove(Move move) {
		if (board.outOfBounds(move)) {
			return String.format("Move %s is out of bounds", move);
		}

		if (!board.getPiece(move.from).belongsToSide(side)) {
			return String.format("Piece at %s does not belong to %s", move.from, side);
		}

		if (!history.isEmpty() && history.getLastSide() == side && !history.getLastMove().to.equals(move.from)) {
			return String.format("Must move the last piece that killed");
		}

		if (availableMoves == null) {
			availableMoves = getAvailableMoves();
		}

		if (!availableMoves.get(move.from).contains(move)) {
			return String.format("Invalid coordinates for move: %s", move);
		}

		if (board.getPiece(move.to) != Space.getInstance()) {
			return String.format("Board is not empty at %s", move.to);
		}

		if (move.isKill() && !board.getPiece(move.getKillPosition()).belongsToSide(side.otherSide)) {
			return String.format("Piece at %s does not belong to the opponent", move.getKillPosition());
		}

		return null;
	}

	public void move(Move move) {
		String error = validateMove(move);
		if (error != null) {
			throw new IllegalArgumentException(error);
		}

		history.addMove(board, side, move);

		Piece resultingPiece = move.isPromotion(side) ? new King(side) : board.getPiece(move.from);

		board.set(move.to, resultingPiece);
		board.set(move.from, Space.getInstance());

		if (move.isKill()) {
			board.set(move.getKillPosition(), Space.getInstance());
		}
	}

	public boolean checkForWinner() {
		for (Position position : board.getPieces(side)) {
			if (!board.getPiece(position).getAvailableMoves(position, board, history).isEmpty()) {
				return false;
			}
		}

		return true;
	}

	private String printError(String error) {
		if (error != null) {
			System.err.println(error);
		}

		return error;
	}

	public void play() {
		System.out.println(board);

		while (!checkForWinner()) {
			System.out.format("%s's turn\n", side);

			availableMoves = getAvailableMoves();

			Move move;
			do {
				move = players.get(side).getNextMove(board, availableMoves, history);
			} while (printError(validateMove(move)) != null);

			move(move);

			System.out.println(board);

			if (!move.isKill() || board.getPiece(move.to).getAvailableMoves(move.to, board, history).isEmpty()) {
				side = side.otherSide;
			}
		}

		List<Move> moves = history.stream().map(e -> e.move).collect(toList());
		System.out.format("%s wins!\n%s\n%s\nNumber of moves: %s\n", side.otherSide, history.getStartingBoard(), moves, moves.size());
	}
}
