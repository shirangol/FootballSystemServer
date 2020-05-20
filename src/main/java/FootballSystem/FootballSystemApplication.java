package FootballSystem;

import FootballSystem.DataAccess.DBConnector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FootballSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(FootballSystemApplication.class, args);
		DBConnector con=DBConnector.getInstance();
		DBConnector.getConnection();
		//	TeamSQL.getInstance().get(1);
//		Team team=new Team("aa",new TeamOwner(1,"cc","123","cc",100));
//		try{
//			//TeamSQL.getInstance().save(team);
//			TeamSQL.getInstance().delete(team);
//		}catch (Exception e){
//
//		}
	}

}
