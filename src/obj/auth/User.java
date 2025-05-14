package obj.auth;

import util.StringUtils;
//todo
public class User {
    private String displayName;
    private String username;
    private long hashedPassword;

    public User(String displayName, String username, String password) {
        this.displayName = displayName;
        this.username = username;
        this.hashedPassword = StringUtils.hash(password); // todo
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean verifyPassword(String password){
        return this.hashedPassword == StringUtils.hash(password);
    }

    public boolean changePassword(String oldPassword, String newPassword){
        if(verifyPassword(oldPassword)){
            this.hashedPassword = StringUtils.hash(newPassword);
            return true;
        }
        return false;
    }
    public String toString(){
        return "User { " + "displayName = " + displayName + "\n" + " username = " + username + "}";
    }
}
