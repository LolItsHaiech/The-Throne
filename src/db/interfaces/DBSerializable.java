package db.interfaces;

import java.io.Serializable;

public interface DBSerializable extends Serializable {
    int getID();
    void save();
    default boolean equals(DBSerializable other){
        return this.getID() == other.getID();
    }
}
