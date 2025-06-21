package db.interfaces;

import java.io.Serializable;

public interface DBSerializable extends Serializable {
    int getID();
    void save();
}
