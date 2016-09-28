package ml.example;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

import java.sql.*;
import java.util.*;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.*;

import checkers.MoveHistory;
import checkers.board.*;
import checkers.player.Player;

public class ExampleAIPlayer extends Player {

	private PreparedStatement weightQuery;

	public ExampleAIPlayer(Side side) {
		super(side);
	}

	public ExampleAIPlayer(Side side, String name, List<String> pieceSkins) {
		super(side, name, pieceSkins);
	}

	@Autowired
	public void injectDb(@Qualifier("states") DataSource statesDs) throws SQLException {
		Connection connection = statesDs.getConnection();
		weightQuery = connection.prepareStatement(""
				+ "select move, weight/samples weight from states where board = ? and move in ( ?, ?, ?, ?, ?, ?, ?, ? );");
	}

	public Move getNextMove(Board board, Map<Position, List<Move>> availableMoves, MoveHistory history) {
		List<Move> allAvailableMoves = availableMoves.values().stream()
				.flatMap(List::stream)
				.collect(toList());
		Map<Move, Float> moveWeights = new HashMap<Move, Float>(
				allAvailableMoves.stream()
				.collect(toMap(identity(), e -> 0.5f)));

		try {
			weightQuery.setString(1, board.minString(side == Side.BLACK));
			for (int i=0; i<8; i++) {
				weightQuery.setString(i+2, allAvailableMoves.size() > i ? allAvailableMoves.get(i).toString() : "''");
			}

			ResultSet rs = weightQuery.executeQuery();

			while (rs.next()) {
				moveWeights.put(Move.parse(rs.getString("move")), rs.getFloat("weight"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		NavigableMap<Float, Move> randomSelector = new TreeMap<Float, Move>();
		double total = 0;
		for (Entry<Move, Float> e : moveWeights.entrySet()) {
			if (e.getValue() <= 0) {
				continue;
			}
			total += e.getValue();
			randomSelector.put((float) total, e.getKey());
		}

		Random rand = new Random();
		return randomSelector.ceilingEntry((float) (rand.nextDouble() * total)).getValue();
	}
}
