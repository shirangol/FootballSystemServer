package FootballSystem.ServiceLayer;

import FootballSystem.System.Controller;
import FootballSystem.System.Exeptions.UserNameAlreadyExistException;
import FootballSystem.System.SystemErrorLog;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/notification")
@RestController
public class ObserverController {

    static HashMap<String,String> onlineUsers = new HashMap<>();



    @GetMapping(path = "/register/{myIP}/{myPort}/{userName}")
    public void addOnlineUser(@PathVariable("userName")String userName , @PathVariable("myPort")String portNumber , @PathVariable("myIP") String ipAddress, HttpServletRequest request){
//        String value = ipAddress+":"+portNumber;
        String value= request.getRemoteAddr() +":"+portNumber ;
        onlineUsers.put(userName,value);
}
    public static void removeOnlineUser(String userName) {
        try {

            onlineUsers.remove(userName);
        }catch (Exception e){
            SystemErrorLog.getInstance().writeToLog("Type: "+"Invalid username");
        }
    }

    @GetMapping(path = "/getMyAlerts/{user_name}")
    public ResponseEntity getMyAlerts(@PathVariable("user_name") String userName){
        List<String> myAlerts= Controller.getInstance().getMyAlerts(userName);
        if(myAlerts==null || myAlerts.size()==0){
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity(myAlerts,HttpStatus.ACCEPTED);
    }

    public static void notify(String userName , String event){
            String getter= onlineUsers.get(userName);
            if(getter==null){//save to DB for user that are not online
                Controller.getInstance().saveAlertToUser(userName,event);
                return;
            }
            String url = "http://"+getter+"/api/notification";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type","application/json");
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> e = new HttpEntity<>(event,headers);
            restTemplate.exchange(url, HttpMethod.POST, e , String.class);

    }
}
