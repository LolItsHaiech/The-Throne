package obj.auth;

import db.interfaces.DBSerializable;
import db.Database;
import exceptions.AuthenticationException;
import util.StringUtils;

import java.util.Objects;

public class User implements DBSerializable {
    private final static Database<User> DB = new Database<>("USER");

    private final int ID;
    private String displayName;
    private String username;
    private long hashedPassword;

    public User(String displayName, String username, String password) {
        synchronized (DB) {
            this.ID = DB.getNextID();
        }
        this.displayName = displayName;
        this.username = username;
        this.hashedPassword = StringUtils.hash(password);

    }

    public static User register(String displayName, String username, String password, String confirmPassword)
            throws AuthenticationException {

        if (password.equals(confirmPassword)) {
            User user = new User(displayName, username, password);
            synchronized (DB) {
                if (DB.check(obj -> Objects.equals(obj.username, username))) {
                    throw new AuthenticationException("username already exists");
                }
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
        return this.displayName + " (@" + this.username + ")";
    }

    public static User[] search(String query) {
        int i = 0;
        User[] res = new User[5];
        synchronized (DB) {
            for (User user : DB) {
                if (user.username.toLowerCase().contains(query.toLowerCase())) {
                    res[i++] = user;
                    if (i==5) {
                        break;
                    }
                }
            }
        }
        return res;
    }

    @Override
    public int getID() {
        return this.ID;
    }

    @Override
    public void save() {
        synchronized (DB) {
            if (DB.exists(this)) {
                DB.update(this);
            } else {
                DB.write(this);
            }
        }

    }
}
