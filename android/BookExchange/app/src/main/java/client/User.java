package client;

/**
 * //TODO make sure this class can be deleted
 * Created by jarthur on 4/16/15.
 * Code from http://stackoverflow.com/questions/22209046/
 * fix-android-studio-login-activity-template-generated-activity
 */
public class User {
    public long userId;
    public String username;
    public String password;

    public User(long userId, String username, String password){
        this.userId=userId;
        this.username=username;
        this.password=password;
    }

}
