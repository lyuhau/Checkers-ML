Checkers Machine Learning
===

A checkers implementation with endpoints for machine learning.


Usage
---

To play checkers, run `checkers.Main` to start a game against a computer that makes random moves. Human input is taken from `System.in` in the format `A2B3` to represent moving the piece on `A2` to `B3`. To play against another human, change:

```
Player black = new RandomPlayer(Side.BLACK);
```

to:

```
Player black = new HumanPlayer(Side.BLACK);
```

Or alternatively, to watch two random computers play against each other, change the white player to a `RandomPlayer`.

Checkers rules are based on https://simple.wikipedia.org/wiki/Checkers.


Machine Learning
---

A (very simple) sample machine learning application is in package `ml.example`. The `ExampleTrainer` keeps a database of board states + moves along with a weight for each combination. After a game is completed, the results of the game are passed to the `ExampleTrainer`, which updates the database with the results of the game:

- all moves done by the winning player are updated with higher weights
- all moves done by the losing player are updated with lower weights

The `ExampleAIPlayer` queries the database based on the current state of the board and available moves, and chooses moves based on their weight (moves with higher weights have a higher chance of being chosen).


Notes
---

The database I am using is MySQL. The setup script is:

```
CREATE DATABASE checkers;
USE checkers;
CREATE TABLE states (board VARCHAR(32), move VARCHAR(4), weight FLOAT, samples INT);
ALTER TABLE states ADD UNIQUE (board, move);
```
