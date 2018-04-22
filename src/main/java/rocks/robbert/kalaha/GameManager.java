package rocks.robbert.kalaha;

import rocks.robbert.kalaha.model.Board;
import rocks.robbert.kalaha.model.Pit;
import rocks.robbert.kalaha.model.Player;

import java.util.Map;

public class GameManager {
    private Board board;
    private Player currentPlayersTurn;

    public GameManager(Player p1, Player p2) {
        board = new Board(p1, p2);
        currentPlayersTurn = p1;
    }

    public String move(int pos) {
        Pit startPit = board.getPitOnPos(pos);

        if (!startPit.belongsToPlayer(currentPlayersTurn)) {
            return "Not your turn, bro";
        }

        if (startPit.isLarge()) {
            return "Can't take marbles from large pit";
        }

        if (startPit.isEmpty()) {
            return "Pit is empty, silly guy";
        }

        boolean droppedMarbleInLargePit = false;
        int marblesTakenFromPit = startPit.takeMarbles();

        while (marblesTakenFromPit > 0) {
            marblesTakenFromPit -= 1;
            pos = getNextPos(pos);
            Pit pit = getBoard().getPitOnPos(pos);

            if (pit.isLarge() && !pit.belongsToPlayer(currentPlayersTurn)) {
                continue;
            }

            if (pit.isLarge()) {
                droppedMarbleInLargePit = true;
            }

            if (marblesTakenFromPit != 0) {
                pit.addMarble(1);
                continue;
            }

            Pit opposite = getBoard().getOppositePit(pit);
            Pit currentPlayerLargePit = getBoard().getLargePitForPlayer(currentPlayersTurn);

            if (opposite.isEmpty() || !pit.isEmpty()) {
                continue;
            }

            if (pit.belongsToPlayer(currentPlayersTurn) && !pit.isLarge()) {
                // +1 because of the marble of the this pit also has to be added
                currentPlayerLargePit.addMarble(opposite.takeMarbles() + 1);
            }
        }

        if (isGameFinished()) {
            finishGame();
            return "Game is finished";
        }

        if (!droppedMarbleInLargePit)
        {
            currentPlayersTurn = currentPlayersTurn == board.getPlayerOne() ?
                    board.getPlayerTwo() : board.getPlayerOne();
        }
        return "Next move please, " + currentPlayersTurn.getName();
    }

    /**
     * Finished the game, put all marbles in the large pits of players.
     */
    private void finishGame() {
        Pit playerOneLargePit = getBoard().getLargePitForPlayer(getBoard().getPlayerOne());
        Pit playerTwoLargePit = getBoard().getLargePitForPlayer(getBoard().getPlayerTwo());

        playerOneLargePit.addMarble(getBoard().sumAllPitsForPlayer(getBoard().getPlayerOne()));
        playerTwoLargePit.addMarble(getBoard().sumAllPitsForPlayer(getBoard().getPlayerTwo()));

    }

    public int getNextPos(int pos) {
        pos += 1;

        if (pos > getBoard().getPits().length - 1) {
            pos = 0;
        }
        return pos;
    }

    private boolean isGameFinished() {
        return getBoard().allPitsAreEmptyFor(getBoard().getPlayerOne()) || getBoard().allPitsAreEmptyFor(getBoard().getPlayerTwo());
    }

    public Board getBoard() {
        return board;
    }
}
