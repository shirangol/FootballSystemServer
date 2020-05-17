package FootballSystem.System.FootballObjects.Event;

public class RedCard extends AEvent {

    //<editor-fold desc="Constructor">
    public RedCard(int minuteInTheGame, String playerName ,String teamName) {
        super(minuteInTheGame,playerName,teamName);
    }

    //</editor-fold>

    @Override
    public String getType() {
        return "red card";
    }
}
