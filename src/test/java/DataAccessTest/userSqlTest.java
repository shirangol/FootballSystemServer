package DataAccessTest;

import FootballSystem.DataAccess.UserSQL;
import FootballSystem.ServiceLayer.GuestController;
import FootballSystem.System.Enum.RefereeType;
import FootballSystem.System.Exeptions.NoSuchAUserNamedException;
import FootballSystem.System.Exeptions.WrongPasswordException;
import FootballSystem.System.Users.Fan;
import FootballSystem.System.Users.Referee;
import FootballSystem.System.Users.TeamOwner;
import org.junit.Test;


public class userSqlTest {

    @Test
	public void getUserFromDB(){
		String fanStr=UserSQL.getInstance().get("Max");
		String[] arr = fanStr.split(" ");
		assert(arr[0].equals("Fan"));
		assert (arr[1].equals("2"));
		assert(arr[2].equals("Max"));
		assert(arr[3].equals("123"));
		assert(arr[4].equals("Max"));
	}

	@Test
	public void addFanUser(){
		UserSQL userSQL=UserSQL.getInstance();
		Fan f=new Fan(7654,"danaFan4","123456","danaFan4");
		userSQL.save(f);
		String userStr=userSQL.get(7654);
		String[] arr = userStr.split(" ");
		assert(arr[0].equals("Fan"));
		assert (arr[1].equals("7654"));
		assert(arr[2].equals("danaFan4"));
		assert(arr[3].equals("123456"));
		assert(arr[4].equals("danaFan4"));
		userSQL.deleteByUserName("danaFan4");
	}

	@Test
	public void addRefreeUser(){
		UserSQL userSQL=UserSQL.getInstance();
		Referee harelush=new Referee("harelll", RefereeType.MAIN,345,"123456","harelushka");
		userSQL.save(harelush);
		String userStr=userSQL.get("harelushka");
		String[] arr = userStr.split(" ");
		assert(arr[0].equals("Referee"));
		assert (arr[1].equals("harelll"));
		assert(arr[2].equals(RefereeType.MAIN.toString()));
		assert(arr[3].equals("345"));
		assert(arr[4].equals("123456"));
		assert(arr[5].equals("harelushka"));
		userSQL.deleteByUserName("harelushka");
	}

	@Test
	public void addTeamOwnerToUserTable(){
		TeamOwner teamOwner=new TeamOwner(123,"owner8","123","owner8",0);
		UserSQL.getInstance().save(teamOwner);
		String userStr=UserSQL.getInstance().get("owner8");
		String[] arr = userStr.split(" ");
		assert(arr[0].equals("TeamOwner"));
		assert (arr[1].equals("123"));
		assert(arr[2].equals("owner8"));
		assert(arr[3].equals("123"));
		assert(arr[4].equals("owner8"));
		UserSQL.getInstance().deleteByUserName("owner8");
	}

	@Test
	public void userLoginTest() throws NoSuchAUserNamedException, WrongPasswordException {
		GuestController guestController=new GuestController();
		int userType=guestController.getUserType("Dana","123");
		assert(userType==2);
	}

}
