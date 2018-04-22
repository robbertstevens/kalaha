package rocks.robbert.kalaha.model;

import java.util.Arrays;

public class Board {
    public static final int SMALL_PITS_PER_PLAYER = 6;
    public static final int LARGE_PITS_PER_PLAYER = 1;
    public static final int MARBLE_START_AMOUNT = 6;
    public static final int PLAYERS = 2;

    private Pit[] pits;
    private Player playerOne;
    private Player playerTwo;

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
                pit.setOwner(playerTwo);
            } else {
                pit.setOwner(playerOne);
            }

            boolean isLarge = i == 7 || i == 0;

            pit.setLarge(isLarge);
            pit.setMarbles(isLarge ? 0 : MARBLE_START_AMOUNT);
            pit.setPosition(i);

            pits[i] = pit;
        }
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public Pit getOppositePit(Pit pit) {
        return pits[pits.length - pit.getPosition()];
    }

    public Pit getLargePitForPlayer(Player player) {
        for (Pit pit : pits) {
            if (pit.isLarge() && pit.belongsToPlayer(player))
            {
                return pit;
            }
        }
        return null;
    }

    public int sumAllPitsForPlayer(Player player) {
        return Arrays.stream(pits).filter(pit -> !pit.isLarge() && pit.belongsToPlayer(player))
                .mapToInt(Pit::takeMarbles).reduce(0, (a, b) -> a + b);
    }

    /**
     * @param player
     * @return
     */
    public boolean allPitsAreEmptyFor(Player player) {
        Object[] objects = Arrays.stream(pits).filter(pit ->
                pit.isEmpty() &&
                        pit.belongsToPlayer(player) &&
                        !pit.isLarge()
        ).toArray();
        return objects.length == 6;
    }

    public Pit[] getPits() {
        return pits;
    }

    public Pit getPitOnPos(int pos) {
        return pits[pos];
    }
}
