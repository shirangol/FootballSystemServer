package FootballSystem.System.FootballObjects.Event;

public class YellowCard extends AEvent {

    //<editor-fold desc="Constructor">
    public YellowCard(int minuteInTheGame, String playerName ,String teamName) {
        super(minuteInTheGame,playerName,teamName);
    }


    //</editor-fold>
    @Override
    public String getType() {
        return "YellowCard";
    }
}
