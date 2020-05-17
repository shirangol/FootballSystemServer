package FootballSystem.System.FootballObjects.Event;

public class Offense extends AEvent {

    //<editor-fold desc="Constructor">
    public Offense(int minuteInTheGame, String playerName ,String teamName) {
        super(minuteInTheGame,playerName,teamName);
    }


    //</editor-fold>
    @Override
    public String getType() {
        return "offense";
    }
}
