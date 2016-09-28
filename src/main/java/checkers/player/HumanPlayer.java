package checkers.player;

import java.util.*;
import java.util.regex.*;

import checkers.MoveHistory;
import checkers.board.*;

public class HumanPlayer extends Player {

	private static final Pattern MOVE_INPUT_PATTERN = Pattern.compile("(?i)([a-h]).*(\\d).*([a-h]).*(\\d)");
	public static Scanner systemInScanner = new Scanner(System.in);

	public HumanPlayer(Side direction) {
		super(direction);
	}

	public HumanPlayer(Side direction, String name, List<String> pieceSkins) {
		super(direction, name, pieceSkins);
	}

	public Move getNextMove(Board board, Map<Position, List<Move>> availableMoves, MoveHistory history) {
		while (true) {
			String line = systemInScanner.nextLine();

			Matcher m = MOVE_INPUT_PATTERN.matcher(line);

			if (m.find()) {
				return new Move(
						new Position(m.group(1).toLowerCase().toCharArray()[0] - 'a', Integer.parseInt(m.group(2)) - 1),
						new Position(m.group(3).toLowerCase().toCharArray()[0] - 'a', Integer.parseInt(m.group(4)) - 1));

			} else {
				System.err.println("I didn't understand that");
			}
		}
	}
}
