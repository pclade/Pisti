package ch.sakru.pisti.settings;

public final class PistiSettings {
    private final String playerName;
    private final int pointsToReach;

    public PistiSettings(String playerName, int pointsToReach) {
        if (pointsToReach != 51 && pointsToReach != 101 && pointsToReach != 151) {
            throw new IllegalArgumentException("Unsupported target score: " + pointsToReach);
        }
        this.playerName = playerName;
        this.pointsToReach = pointsToReach;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPointsToReach() {
        return pointsToReach;
    }
}
