package obj.auth;

import db.DBSerializable;
import db.Database;
import exceptions.AuthenticationException;
import util.StringUtils;

//todo
public class User implements DBSerializable {
    private final static Database<User> DB = new Database<>("USER");

    private final int ID;
    private String displayName;
    private String username;
    private long hashedPassword;

    private User(String displayName, String username, String password) {
        synchronized (DB) {
            this.ID = DB.getNextID();
        }
        this.displayName = displayName;
        this.username = username;
        this.hashedPassword = StringUtils.hash(password);

    }

    public static User register(String displayName, String username, String password, String confirmPassword)
            throws AuthenticationException {
        // todo check for unique username
        if (password.equals(confirmPassword)) {
            User user = new User(displayName, username, password);
            synchronized (DB) {
                DB.write(user);
            }
            return user;
        }
        throw new AuthenticationException("password and confirm password do not match");
    }

    public static User login(String username, String password) throws AuthenticationException {
        synchronized (DB) {
            for (User user : DB) {
                if (user.username.equals(username) && user.hashedPassword == StringUtils.hash(password)) {
                    return user;
                }
            }
        }
        throw new AuthenticationException("username or password is incorrect");
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

    public boolean verifyPassword(String password) {
        return this.hashedPassword == StringUtils.hash(password);
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (verifyPassword(oldPassword)) {
            this.hashedPassword = StringUtils.hash(newPassword);
            return true;
        }
        return false;
    }

    public String toString() {
        return "User { " + "displayName = " + displayName + "\n" + " username = " + username + "}";
    }

    @Override
    public int getID() {
        return this.ID;
    }
}
