package org.jmel.mastermind;

public class Feedback {
    private int wellPlaced;
    private int misplaced;

    public Feedback(int wellPlaced, int misplaced) { // TODO could be not positive, the sum is greater than the code length
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
