package FootballSystem.System.FootballObjects.Event;

import java.util.Date;

public class Goal extends AEvent {

    //<editor-fold desc="Constructor">
    public Goal(int minuteInTheGame, String playerName ,String teamName) {

        super(minuteInTheGame,playerName,teamName);
    }

    public Goal(int id, Date date, int minuteInTheGame, String playerName , String teamName) {

        super( id,date,minuteInTheGame,playerName ,teamName);
    }

    @Override
     public String getType() {
        return "Goal";
    }

    @Override
    public String getTypeToAlert() {
        return "goal";
    }
    //</editor-fold>
}
