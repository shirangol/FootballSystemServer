package FootballSystem.System.I_Observer;

public interface IObserverGame {

    void update(String alert);
    void registerAlert(ISubjectGame iSubjectGame);
    void removeAlert(ISubjectGame iSubjectGame);

}
