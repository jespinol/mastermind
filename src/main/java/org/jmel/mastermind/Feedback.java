package org.jmel.mastermind;

public class Feedback {
    private int wellPlaced;
    private int misplaced;

    public Feedback(int wellPlaced, int misplaced) {
        this.wellPlaced = wellPlaced;
        this.misplaced = misplaced;
    }

    public int getWellPlaced() {
        return wellPlaced;
    }

    public int getMisplaced() {
        return misplaced;
    }

    @Override
    public String toString() {
        return "wellPlaced=" + wellPlaced + ", misplaced=" + misplaced;
    }
}
