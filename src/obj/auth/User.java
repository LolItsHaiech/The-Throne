package obj.auth;

import util.StringUtils;
//todo
public class User {
    private String displayName;
    private int username;
    private long hashedPassword;

    public User(String displayName, int username, String password) {
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

    public int getUsername() {
        return this.username;
    }

    public void setUsername(int username) {
        this.username = username;
    }
}
