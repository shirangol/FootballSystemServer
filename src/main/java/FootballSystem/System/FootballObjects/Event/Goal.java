package FootballSystem.System.FootballObjects.Event;

public class Goal extends AEvent {

    //<editor-fold desc="Constructor">
    public Goal(int minuteInTheGame, String playerName ,String teamName) {
        super(minuteInTheGame,playerName,teamName);
    }

    @Override
     public String getType() {
        return "goal";
    }
    //</editor-fold>



}
