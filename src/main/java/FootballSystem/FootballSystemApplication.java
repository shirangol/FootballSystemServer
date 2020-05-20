package FootballSystem;

import FootballSystem.ServiceLayer.*;
import FootballSystem.System.Controller;
import FootballSystem.System.Enum.RefereeType;
import FootballSystem.System.FootballObjects.Field;
import FootballSystem.System.FootballObjects.League;
import FootballSystem.System.FootballObjects.LeagueInformation;
import FootballSystem.System.FootballObjects.Team.Team;
import FootballSystem.System.Users.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@SpringBootApplication
public class FootballSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(FootballSystemApplication.class, args);
		try {
			intiallieSystem();
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	public static void intiallieSystem() throws Exception{
		Controller.getInstance().initSystem();
//		GuestController guestController = new GuestController();
//		guestController.signUp(1,"Pudge","1234","Pudge");
		User systemManager = Controller.getInstance().login("Admin","2&^4BcE#@6");
		User teamOwner = SystemManagerController.getInstance().createNewTeamOwner((SystemManager)systemManager,4,"Puck","1234","Puck",0,0);

		Field field1 = TeamOwnerController.getInstance().createField(0,"rus");
		Field field2 = TeamOwnerController.getInstance().createField(1,"ukr");


		User footballAs = SystemManagerController.getInstance().createNewFootballAssociation((SystemManager)systemManager,4,"PA","1234","PA");
		User referee = SystemManagerController.getInstance().createNewReferee((SystemManager)systemManager,2,"Invoker","1234","ref1", RefereeType.MAIN);
		User refereeSide1 = SystemManagerController.getInstance().createNewReferee((SystemManager)systemManager,2,"Invoker2","1234","ref2", RefereeType.ASSISTANT);
		User refereeSide2 = SystemManagerController.getInstance().createNewReferee((SystemManager)systemManager,2,"Invoker3","1234","ref3", RefereeType.ASSISTANT);
		User fan= SystemManagerController.getInstance().createNewFan((SystemManager)systemManager,2,"fan","1234","fan");


		Team team1 = FootballAssosiationController.getInstance().createTeam("Navi",(TeamOwner)teamOwner);
		Team team2 = FootballAssosiationController.getInstance().createTeam("VP",(TeamOwner)teamOwner);

		TeamOwnerController.getInstance().addAssetToTeam((TeamOwner)teamOwner,team1,field1);
		TeamOwnerController.getInstance().addAssetToTeam((TeamOwner)teamOwner,team2,field2);

		List<Team> teamList = new LinkedList<>();
		teamList.add(team1);
		teamList.add(team2);

		League league = FootballAssosiationController.getInstance().initEmptyLeague("Major",teamList);
		LeagueInformation leagueInformation = FootballAssosiationController.getInstance().initLeague((FootballAssociation)footballAs,league,"2020");
		FootballAssosiationController.getInstance().schedulingGames((FootballAssociation)footballAs ,leagueInformation );

		List<Referee> referees = new LinkedList<>();
		referees.add((Referee)referee);
		referees.add((Referee)refereeSide1);
		referees.add((Referee)refereeSide2);

		leagueInformation.getGames().get(0).setDate(new Date());
		leagueInformation.getGames().get(1).setResult(0,9);
		FanController.getInstance().followGame((Fan)fan,leagueInformation.getGames().get(0));
		FanController.getInstance().followGame((Fan)fan,leagueInformation.getGames().get(1));

		FootballAssosiationController.getInstance().schedulingReferee((FootballAssociation)footballAs ,leagueInformation ,referees);
	}

}
