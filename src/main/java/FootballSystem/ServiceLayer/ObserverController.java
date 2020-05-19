package FootballSystem.ServiceLayer;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@RequestMapping("/api/notification")
@RestController
public class ObserverController {

    static HashMap<String,String> onlineUsers = new HashMap<>();
    @GetMapping(path = "register/{myIP}/{myPort}/{userName}")
    public void addOnlineUser(@PathVariable("userName")String userName , @PathVariable("myPort")String portNumber ,@PathVariable("myIP") String ipAddress){
        String value = ipAddress+":"+portNumber;
        onlineUsers.put(userName,value);
        System.out.println("added");
}

    public static void notify(String userName , String event){
        String info = onlineUsers.get(userName);
        String notify = "http://"+info+"/api/notification/notify/"+event;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> e = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(notify, HttpMethod.GET, e , String.class);
        System.out.println("added");

    }
}
