package FootballSystem.System.FootballObjects.Event;

import java.util.Date;

public class Injury extends AEvent {

    //<editor-fold desc="Constructor">
    public Injury(int minuteInTheGame, String playerName ,String teamName) {
        super(minuteInTheGame,playerName,teamName);
    }
    public Injury(int id, Date date, int minuteInTheGame, String playerName , String teamName) {

        super( id,date,minuteInTheGame,playerName ,teamName);
    }

    //</editor-fold>
    @Override
    public String getType() {
        return "Injury";
    }

    @Override
    public String getTypeToAlert() {
        return "injury";
    }
}
