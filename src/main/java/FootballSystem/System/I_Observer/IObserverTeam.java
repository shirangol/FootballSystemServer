package FootballSystem.System.I_Observer;

public interface IObserverTeam {

    void update(String s);
    void registerAlert(ISubjectTeam iSubjectTeam);
    void removeAlert(ISubjectTeam iSubjectTeam);

}
