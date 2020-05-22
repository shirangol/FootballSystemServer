package FootballSystem.System.FootballObjects.Event;

public class Offside extends AEvent {

    //<editor-fold desc="Constructor">
    public Offside(int minuteInTheGame, String playerName ,String teamName) {
        super(minuteInTheGame,playerName,teamName);
    }


    //</editor-fold>
    @Override
    public String getType() {
        return "Offside";
    }

}
