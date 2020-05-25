package FootballSystem.System.FootballObjects.Event;

import java.util.Date;

public class Offside extends AEvent {

    //<editor-fold desc="Constructor">
    public Offside(int minuteInTheGame, String playerName ,String teamName) {
        super(minuteInTheGame,playerName,teamName);
    }
    public Offside(int id, Date date, int minuteInTheGame, String playerName , String teamName) {

        super( id,date,minuteInTheGame,playerName ,teamName);
    }

    //</editor-fold>
    @Override
    public String getType() {
        return "Offside";
    }

    @Override
    public String getTypeToAlert() {
        return "offside";
    }

}
