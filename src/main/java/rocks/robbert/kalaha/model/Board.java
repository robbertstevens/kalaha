package rocks.robbert.kalaha.model;

import java.util.Arrays;
import java.util.Optional;

public class Board {
    public static final int SMALL_PITS_PER_PLAYER = 6;
    public static final int LARGE_PITS_PER_PLAYER = 1;
    public static final int MARBLE_START_AMOUNT = 6;
    public static final int PLAYERS = 2;

    private Pit[] pits;
    private Player playerOne;
    private Player playerTwo;
    private Player currentPlayersTurn;

    public Board(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        boardSetup();
    }

    /**
     * Setup the board for a new game.
     *
     * @return Pit[]
     */
    private void boardSetup() {
        int totalPits = (SMALL_PITS_PER_PLAYER + LARGE_PITS_PER_PLAYER) * PLAYERS;
        pits = new Pit[totalPits];

        for (int i = 0; i < totalPits; i++) {
            Pit pit = new Pit();

            if (i > 0 && i <= 7) {
                pit.setOwner(playerOne);
            } else {
                pit.setOwner(playerTwo);
            }

            boolean isLarge = i == 7 || i == 0;

            pit.setLarge(isLarge);
            pit.setMarbles(isLarge ? 0 : MARBLE_START_AMOUNT);
            pit.setPosition(i);

            pits[i] = pit;
        }
        setCurrentPlayersTurn(playerOne);
    }

    public void move(int pitPosition) {
        Pit pit = pits[pitPosition];
        if (pit.getOwner() != currentPlayersTurn) {
            return;
        }

        if (allPitsAreEmptyFor(playerOne)) {
            Optional<Pit> large = getLargePitForPlayer(playerTwo);
            large.ifPresent(largePit -> largePit.setMarbles(sumAllPitsForPlayer(playerTwo)));
            return;
        }
        if (allPitsAreEmptyFor(playerTwo)) {
            Optional<Pit> large = getLargePitForPlayer(playerOne);
            large.ifPresent(largePit -> largePit.setMarbles(sumAllPitsForPlayer(playerOne)));
            return;
        }

        int marbles = pit.getMarbles();
        boolean addedMarbleToLargePit = false;

        pit.setMarbles(0);

        while (marbles > 0) {
            pitPosition += 1;
            if (pitPosition > pits.length - 1) {
                pitPosition = 0;
            }
            Pit nextPit = nextPit(pitPosition);

            if (nextPit.isLarge() && nextPit.getOwner() != currentPlayersTurn) {
                continue;
            }

            if (nextPit.isLarge()) {
                addedMarbleToLargePit = true;
            }

            nextPit.addMarble(1);
            marbles -= 1;
        }

        if (!addedMarbleToLargePit) {
            setCurrentPlayersTurn(currentPlayersTurn == playerOne ? playerTwo : playerOne);
        }
    }

    private Optional<Pit> getLargePitForPlayer(Player player) {
        return Arrays.stream(pits).findFirst().filter(pit -> pit.isLarge() && pit.getOwner() == player);
    }

    private int sumAllPitsForPlayer(Player player) {
        return Arrays.stream(pits).filter(pit -> {
            return !pit.isLarge() && pit.getOwner() == player;
        }).mapToInt(Pit::getMarbles).reduce(0, (a, b) -> a + b);
    }

    /**
     * @param player
     * @return
     */
    private boolean allPitsAreEmptyFor(Player player) {
        return Arrays.stream(pits).filter(pit ->
                !pit.isLarge()
                        && (pit.getOwner() == player)
                        && (pit.getMarbles() == 0)
        ).count() <= 0;
    }

    /**
     * @param i
     * @return
     */
    private Pit nextPit(int i) {
        if (i > pits.length - 1) {
            i = 0;
        }

        return pits[i];
    }

    public Pit[] getPits() {
        return pits;
    }

    public Player getCurrentPlayersTurn() {
        return currentPlayersTurn;
    }

    public void setCurrentPlayersTurn(Player currentPlayersTurn) {
        this.currentPlayersTurn = currentPlayersTurn;
    }
}
