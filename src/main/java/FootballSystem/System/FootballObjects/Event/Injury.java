package FootballSystem.System.FootballObjects.Event;

public class Injury extends AEvent {

    //<editor-fold desc="Constructor">
    public Injury(int minuteInTheGame, String playerName ,String teamName) {
        super(minuteInTheGame,playerName,teamName);
    }

    //</editor-fold>
    @Override
    public String getType() {
        return "Injury";
    }
}
