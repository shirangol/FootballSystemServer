package FootballSystem.System.PersonalPages;

import FootballSystem.System.Users.Fan;
import FootballSystem.System.SystemEventLog;

import java.util.LinkedList;
import java.util.List;

public class PersonalPage {

    //<editor-fold desc="Fields">
     private IPageAvailable pageAvailable;
     private List<String> posts;
     private List <Fan> followers;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public PersonalPage(IPageAvailable pageAvailable) {
        this.pageAvailable = pageAvailable;
        this.followers = new LinkedList<>();
        this.posts= new LinkedList<>();
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    public IPageAvailable getPageAvailable() {
        return pageAvailable;
    }

    public List<Fan> getFollowers() {
        return followers;
    }

    public List<String> getPosts() {
        return posts;
    }
    //</editor-fold>

    //<editor-fold desc="Setters">
    public void setPageAvailable(IPageAvailable pageAvailable) {
        this.pageAvailable = pageAvailable;
    }

    public void setFollowers(List<Fan> followers) {
        this.followers = followers;
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    /**
     * Add follower to personal page
     * @param fan
     */
    public void follow(Fan fan){
        followers.add(fan);
        fan.addFollowPage(this);
        SystemEventLog.getInstance().writeToLog(fan.getName() +" (id: "+ fan.getId()+") follows "+ this.pageAvailable.getName());
    } //UC-7

    /**
     * Remove follower to personal page
     * @param fan
     */
    public void unfollow(Fan fan) {
        followers.remove(fan);
        fan.removeFollowPage(this);
        SystemEventLog.getInstance().writeToLog(fan.getName() +" (id: "+ fan.getId()+") unfollow "+ this.pageAvailable.getName());

    } //UC-8

    /**
     * Add a new post to a personal page
     * @param post
     */
    public void upload(String post){
        this.posts.add(post);

    }

    public void removePost(String post){}
    //</editor-fold>

}
