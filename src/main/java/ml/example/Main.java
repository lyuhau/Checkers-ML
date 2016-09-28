package ml.example;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import checkers.*;
import checkers.board.Side;
import checkers.player.Player;

public class Main {

	public static void main(String[] args) {
		try (ClassPathXmlApplicationContext spring = new ClassPathXmlApplicationContext("spring.xml")) {
			AutowireCapableBeanFactory autowirer = spring.getAutowireCapableBeanFactory();

			int threads = 3;

			BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(threads);
			ThreadPoolExecutor tpe = new ThreadPoolExecutor(threads, threads, threads, TimeUnit.SECONDS, workQueue);

			for (int i=0; i<threads; i++) {
				List<Consumer<MoveHistory>> consumers = new ArrayList<Consumer<MoveHistory>>();
				ExampleTrainer learn1 = new ExampleTrainer();
				autowirer.autowireBean(learn1);
				consumers.add(learn1);

				Player white = new ExampleAIPlayer(Side.WHITE);
				Player black = new ExampleAIPlayer(Side.BLACK);

				autowirer.autowireBean(white);
				autowirer.autowireBean(black);

				tpe.execute(() -> {
					for (;;) {
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

						for (Consumer<MoveHistory> consumer : consumers) {
							consumer.accept(game.history);
						}
					}
				});
			}

			try {
				tpe.awaitTermination(1, TimeUnit.DAYS);
			} catch (InterruptedException e) {
				e.printStackTrace();
				tpe.shutdownNow();
			}
		}
	}
}
