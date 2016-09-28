package checkers;

import static java.lang.Math.floorMod;
import static org.junit.Assume.assumeTrue;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.*;

import checkers.board.*;

/**
 * Half of these don't pass. I'm so sorry :(
 */
@RunWith(Parameterized.class)
public class MainMoveTest {

	static enum Type {
		MOVE_PASS, MOVE_FAIL
	}

	@Parameter(0) public Type type;
	@Parameter(1) public Move move;

	@Parameters(name = "{index}: {1}")
	public static Collection<Object[]> data() {
		List<Object[]> data = new ArrayList<Object[]>();

		for (int i=0; i<8; i++) {
			for (int j=floorMod(-i+1, 2); j<8; j+=2) {
				for (int[] offset : new int[][] {{-2, -2}, {2, -2}, {-2, 2}, {2, 2}}) {
					int fromX = i;
					int fromY = j;
					int toX = i + offset[0];
					int toY = j + offset[1];
					
					if (toX < 0 || toX > 7 ||
							toY < 0 || toY > 7) {
						continue;
					}

					data.add(new Object[] { Type.MOVE_PASS, new Move(new Position(fromX, fromY), new Position(toX, toY)) });
				}

				for (int[] offset : new int[][] {
					{-2, -1}, {2, -1}, {-2, 1}, {2, 1},
					{-1, -2}, {1, -2}, {-1, 2}, {1, 2}}) {
					int fromX = i;
					int fromY = j;
					int toX = i + offset[0];
					int toY = j + offset[1];

					data.add(new Object[] { Type.MOVE_FAIL, new Move(new Position(fromX, fromY), new Position(toX, toY)) });
				}
			}
		}

		return data;
	}

	@Test
	public void moveTest() {
		assumeTrue(type == Type.MOVE_PASS);
		Game game = new Game();
		game.move(move);
	}

	@Test(expected = IllegalArgumentException.class)
	public void invalidMoveCoordinatesTest() {
		assumeTrue(type == Type.MOVE_FAIL);
		Game game = new Game();
		game.move(move);
	}
}
