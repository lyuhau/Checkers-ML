package checkers;

import java.util.*;
import java.util.function.Consumer;

import checkers.board.Side;
import checkers.player.*;

public class Main {

	public static void main(String[] args) {
		Player white = new HumanPlayer(Side.WHITE);
		Player black = new RandomPlayer(Side.BLACK);

		for (;;) {
			// For setting up the board
			/*Board board = Board.parse("" +
							"  a b c d e f g h\n" + 
							"1   o           o \n" + 
							"2     o           \n" + 
							"3       o       x \n" + 
							"4     o           \n" + 
							"5   x             \n" + 
							"6 o       o       \n" + 
							"7           x   x \n" + 
							"8                 \n" + 
							"");*/

			/*Board board = Board.parseMin("oooooooo ooo o      xxxxxxxxxxxx");*/

			Game game = new Game(white, black);

			game.play();

			// Do stuff with the results
			List<Consumer<MoveHistory>> consumers = new ArrayList<Consumer<MoveHistory>>();

			for (Consumer<MoveHistory> consumer : consumers) {
				consumer.accept(game.history);
			}
		}
	}
}
