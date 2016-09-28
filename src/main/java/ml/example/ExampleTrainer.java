package ml.example;

import java.sql.*;
import java.util.function.Consumer;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.*;

import checkers.*;
import checkers.board.Side;

public class ExampleTrainer implements Consumer<MoveHistory> {

	private Connection connection;
	private PreparedStatement insertQuery;
	private PreparedStatement updateQuery;

	@Autowired
	public void injectDb(@Qualifier("states") DataSource statesDs) throws SQLException {
		connection = statesDs.getConnection();
		insertQuery = connection.prepareStatement("insert ignore into states ( board, move, weight, samples ) values ( ?, ?, 0, 0 );");
		updateQuery = connection.prepareStatement("update states set weight = weight+?, samples = samples+1 where board = ? and move = ?;");
	}

	public void accept(MoveHistory history) {
		Side winner = history.getLastSide();

		while (true) {
			try {
				connection.setAutoCommit(false);
				for (BoardData data : history) {
					String boardMin = data.board.minString(data.side == Side.BLACK);
					insertQuery.setString(1, boardMin);
					insertQuery.setString(2, data.move.toString());
					insertQuery.executeUpdate();
					updateQuery.setFloat(1, data.side == winner ? 1 : 0.5f);
					updateQuery.setString(2, boardMin);
					updateQuery.setString(3, data.move.toString());
					updateQuery.executeUpdate();
				}
				connection.commit();
				return;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
