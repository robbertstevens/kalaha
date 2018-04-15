package rocks.robbert.kalaha.model;

public class Pit {
    private int marbles = 0;
    private int position;
    private boolean isLarge = false;
    private Player owner;

    public void addMarble(int amount) {
        marbles += amount;
    }

    public void setLarge(boolean large) {
        isLarge = large;
    }

    public void setMarbles(int marbles) {
        this.marbles = marbles;
    }

    public boolean isLarge() {
        return isLarge;
    }

    public int getMarbles() {
        return marbles;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
