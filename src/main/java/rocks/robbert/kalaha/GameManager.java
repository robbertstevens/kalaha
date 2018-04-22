package rocks.robbert.kalaha;

import rocks.robbert.kalaha.model.Board;
import rocks.robbert.kalaha.model.Pit;
import rocks.robbert.kalaha.model.Player;

public class GameManager {
    private Board board;
    private Player currentPlayersTurn;

    public GameManager(Player p1, Player p2) {
        board = new Board(p1, p2);
        currentPlayersTurn = p1;
    }

    public String move(int position) {
        Pit startPit = board.getPitOnPosition(position);

        if (!startPit.belongsToPlayer(currentPlayersTurn)) {
            return "Not your turn, bro";
        }

        if (startPit.isLarge()) {
            return "Can't take marbles from large pit";
        }

        if (startPit.isEmpty()) {
            return "Pit is empty, silly guy";
        }

        boolean playerTakesAnotherTurn = false;
        int marblesTakenFromPit = startPit.takeMarbles();

        while (marblesTakenFromPit > 0) {
            position = getNextPosition(position);
            Pit targetPit = getBoard().getPitOnPosition(position);
            Pit currentPlayerLargePit = getBoard().getLargePitForPlayer(currentPlayersTurn);

            if (skipOpponentLargePit(targetPit)) continue;

            marblesTakenFromPit -= 1;

            if (shouldTakeMarblesFromOppositePit(marblesTakenFromPit, targetPit)) {
                Pit oppositePit = getBoard().getOppositePit(targetPit);
                if (!oppositePit.isEmpty()) {
                    currentPlayerLargePit.addMarble(oppositePit.takeMarbles() + 1);
                }
                continue;
            } else {
                targetPit.addMarble(1);
            }

            if (targetPit.isLarge()) {
                playerTakesAnotherTurn = true;
            }
        }

        if (isGameFinished()) {
            finishGame();
            return "Game is finished";
        }

        if (!playerTakesAnotherTurn) {
            currentPlayersTurn = currentPlayersTurn == board.getPlayerOne() ?
                    board.getPlayerTwo() : board.getPlayerOne();
        }
        return "Next move please, " + currentPlayersTurn.getName();
    }

    public boolean shouldTakeMarblesFromOppositePit(int marblesTakenFromPit, Pit targetPit) {
        return marblesTakenFromPit == 0 && targetPit.isEmpty() && !targetPit.isLarge() && targetPit.belongsToPlayer(currentPlayersTurn);
    }

    private boolean skipOpponentLargePit(Pit targetPit) {
        return targetPit.isLarge() && !targetPit.belongsToPlayer(currentPlayersTurn);
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

    public int getNextPosition(int position) {
        position += 1;

        if (position > getBoard().getPits().length - 1) {
            position = 0;
        }
        return position;
    }

    private boolean isGameFinished() {
        return getBoard().allPitsAreEmptyFor(getBoard().getPlayerOne()) || getBoard().allPitsAreEmptyFor(getBoard().getPlayerTwo());
    }

    public Board getBoard() {
        return board;
    }
}
