package FootballSystem;

import FootballSystem.DataAccess.DBConnector;
import FootballSystem.DataAccess.LeagueSQL;
import FootballSystem.DataAccess.UserSQL;
import FootballSystem.ServiceLayer.GuestController;
import FootballSystem.System.Enum.RefereeType;
import FootballSystem.System.Exeptions.NoSuchAUserNamedException;
import FootballSystem.System.Exeptions.WrongPasswordException;
import FootballSystem.System.FootballObjects.League;
import FootballSystem.System.FootballObjects.Team.Team;
import FootballSystem.System.Users.Fan;
import FootballSystem.System.Users.Player;
import FootballSystem.System.Users.Referee;
import FootballSystem.System.Users.TeamOwner;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
class FootballSystemApplicationTests {

	@Test
	void contextLoads() {

	}

//	@Test
//	void getFanById(){
//		UserSQL userSQL=UserSQL.getInstance();
//		Fan f=(Fan)(userSQL.get(123));
//		userSQL.getAll();
//		//Player p=new Player(654,"playerName","22d0","nickName",null,null,0,0);
//	}

//	@Test
//	void addFanUser(){
//		UserSQL userSQL=UserSQL.getInstance();
//		Fan f=new Fan(7654,"danaFan4","123456","danaFan4");
//		//userSQL.save(f);
//		userSQL.getAll();
//	}

//	@Test
//	void addRefreeUser(){
//		UserSQL userSQL=UserSQL.getInstance();
//		Referee harelush=new Referee("harelll", RefereeType.MAIN,345,"123456","harelushka");
//		userSQL.save(harelush);
//		//userSQL.get("harelush");
//		//userSQL.getAll();//get all before delete
//	}

//	@Test
//	void getRefreeUser(){
//		UserSQL userSQL=UserSQL.getInstance();
//		Referee harelush=(Referee)(userSQL.get(345));
//		userSQL.getAll();//get all before delete
//	}

//	@Test
//	void deleteRefreeUser1(){//here we send user object to the delete
//		UserSQL userSQL=UserSQL.getInstance();
//		Referee harelush=new Referee("harel", RefereeType.MAIN,345,"123456","harelush");
//		userSQL.delete(harelush);
//		userSQL.getAll();//get all after delete
//	}

//	@Test
//	void deleteUserByUserName(){//here we send string userName and by this parameter we execute the delete
//		UserSQL userSQL=UserSQL.getInstance();
//		userSQL.deleteByUserName("newNick4");
//		userSQL.getAll();//get all after delete
//	}

//	@Test
//	void userLoginTest() throws NoSuchAUserNamedException, WrongPasswordException {
//		GuestController guestController=new GuestController();
//		int userType=guestController.getUserType("Dana","123");
//		assert(userType==2);
//	}

//	@Test
//	void getLeagueTest(){
//		System.out.println("getLeagueTest :");
//		List<Team> teams=new LinkedList<>();
//		League l=new League("Premier League",teams);
//		String s= LeagueSQL.getInstance().get(l);
//		System.out.println(s);
//		System.out.println("");
//	}

//	@Test
//	void getAllLeagueTest(){
//		System.out.println("getAllLeagueTest :");
//		List<String> list=LeagueSQL.getInstance().getAll();
//		for (int i=0;i<list.size();i++){
//			System.out.println(list.get(i));
//		}
//		System.out.println("");
//	}

	@Test
	void saveLeagueTest() throws SQLException {
		List<Team> teams=new LinkedList<>();
		//League l=new League("The best League ever",teams);
		League l=new League(12345,"The best League ever777",teams);

		//League(int id,String name, List<Team> teams)
		LeagueSQL.getInstance().save(l);
	}

//	@Test
//	void deleteLeagueTest(){
//		List<Team> teams=new LinkedList<>();
//		League l=new League("The best League ever",teams);
//		LeagueSQL.getInstance().delete(l);
//	}

//	@Test
//	void deleteLeagueByNameTest(){
//		LeagueSQL.getInstance().delete("The best League ever");
//	}

//	@Test
//	void addTeamOwnerToUserTable(){
//		TeamOwner teamOwner=new TeamOwner(123,"owner8","123","owner8",0);
//		UserSQL.getInstance().save(teamOwner);
//	}


}
