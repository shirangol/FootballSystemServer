package FootballSystem;

import FootballSystem.DataAccess.DBConnector;
import FootballSystem.DataAccess.GameSQL;
import FootballSystem.DataAccess.LeagueInformationSQL;
import FootballSystem.DataAccess.TeamSQL;
import FootballSystem.ServiceLayer.*;
import FootballSystem.System.Controller;
import FootballSystem.System.Enum.RefereeType;
import FootballSystem.System.Enum.TeamStatus;
import FootballSystem.System.FootballObjects.*;
import FootballSystem.System.FootballObjects.Team.DefaultAllocate;
import FootballSystem.System.FootballObjects.Team.DefaultMethod;
import FootballSystem.System.FootballObjects.Team.Team;
import FootballSystem.System.Users.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@SpringBootApplication
public class FootballSystemApplication {

	public static void main(String[] args) {
		//delete event log table
		try {
			Connection con = DBConnector.getConnection();
			String query = "DELETE FROM event_log";
			PreparedStatement preparedStmt = con.prepareStatement(query);

			preparedStmt.execute();

			String query2 = "DELETE FROM event";
			PreparedStatement preparedStmt2 = con.prepareStatement(query2);

			preparedStmt2.execute();
			con.close();
		} catch (SQLException err) {
			throw new RuntimeException("Error connecting to the database", err);
		}



		SpringApplication.run(FootballSystemApplication.class, args);
		//Dc connect
		//creae WORLD
//		try {
			//DBConnector.getConnection();
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//			String dateString = format.format( new Date()   );
//			Date   date       = format.parse (  dateString);
//			boolean i=true;


			//intiallieSystem();
			//TeamSQL.getInstance().get(1);
			//TeamSQL.getInstance().getAll();
			Team t = new Team(5,"aa", TeamStatus.Active,null,null,0,0);
			Team t2 = new Team(10,"asd", TeamStatus.Active,null,null,0,0);
//			TeamSQL.getInstance().save(t);
			//TeamSQL.getInstance().getAll();
			//TeamSQL.getInstance().delete(t);
			//TeamSQL.getInstance().getAll();

//			GameSQL.getInstance().get(1);
//			GameSQL.getInstance().getAll();

//				Referee referee =new Referee("a",RefereeType.MAIN,100,"123","a");
//			Referee referee2 =new Referee("b",RefereeType.ASSISTANT,150,"123","b");
//			Referee referee3 =new Referee("c",RefereeType.ASSISTANT,200,"123","c");
//				Game game=new Game(100,new Date(),2000,"0:0",referee  , referee2, referee3, t, t2);
//				GameSQL.getInstance().save(game);
			//GameSQL.getInstance().getAll();
			//GameSQL.getInstance().delete(game);

//			LeagueInformationSQL.getInstance().get(1);
//			LeagueInformationSQL.getInstance().getAll();
//			List<Team> aa=new ArrayList<>();
//			League league=new League("aa",aa);
//			Season season=new Season(2000);
//			FootballAssociation footballAssociation=new FootballAssociation(10,"123","acc","123");
//			LeagueInformation leagueInformation=new LeagueInformation(5,league,season,footballAssociation,new DefaultAllocate(),new DefaultMethod());
//			LeagueInformationSQL.getInstance().save(leagueInformation);
//			LeagueInformationSQL.getInstance().getAll();
//			LeagueInformationSQL.getInstance().delete(leagueInformation);
//			LeagueInformationSQL.getInstance().getAll();


			//test to controller
			//Team tt=Controller.getInstance().getTeam(1);
			//List<Team> teams2=Controller.getInstance().getAllTeams();

//			List<Game> games= Controller.getInstance().getAllGames();
//			Fan fan= (Fan)Controller.getInstance().getUser("Max");
//			for (Game g:games){
//				FanController.getInstance().followGame(fan,g);
//			}

		//	Controller.getInstance().getUser("Inbar");
			//boolean i=true;
			//List<Game> games=Controller.getInstance().getAllGames();
			//List<String> lll=GameSQL.getInstance().getAllgamesForReferee("Hen");
			//List<Game> games=Controller.getInstance().getAllGamesForReferee("Hen");
			//List<League>ll=Controller.getInstance().getAllLeagues();
//			//boolean i=true;
			//String url = "http://132.72.200.39:3000/api/notification";
//			RestTemplate restTemplate = new RestTemplate();
//			HttpHeaders headers = new HttpHeaders();
//			headers.set("Content-Type","application/json");
//			headers.setContentType(MediaType.APPLICATION_JSON);
//			HttpEntity<String> e = new HttpEntity<>(headers);
//			restTemplate.exchange(url, HttpMethod.GET, e , String.class);
//
//		}catch (Exception e){
//			e.printStackTrace();
//		}

	}

	public static void intiallieSystem() throws Exception{
		Controller.getInstance().initSystem();
//		GuestController guestController = new GuestController();
//		guestController.signUp(1,"Pudge","1234","Pudge");
		User systemManager = Controller.getInstance().login("Admin","2&^4BcE#@6");
		User teamOwner = SystemManagerController.getInstance().createNewTeamOwner((SystemManager)systemManager,4,"Puck","1234","Puck",0,0);

		Field field1 = TeamOwnerController.getInstance().createField(0,"rus");
		Field field2 = TeamOwnerController.getInstance().createField(1,"ukr");


		User footballAs = SystemManagerController.getInstance().createNewFootballAssociation((SystemManager)systemManager,4,"PA1","1234","PA1");
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
