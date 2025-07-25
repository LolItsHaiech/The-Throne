package db;


import db.interfaces.CheckFunc;
import db.interfaces.DBSerializable;

import java.io.*;
import java.util.Iterator;


// todo optimize
// todo implement cache
public class Database<T extends DBSerializable> implements Iterable<T> {
    private File file;
    private final Object lock = new Object();


    public Database(String fileName) {
        this.file = new File(fileName + ".db");
    }

    public boolean write(T obj) {
        File temp = new File(this.file + ".temp.db");
        try {
            FileOutputStream fos = new FileOutputStream(temp);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            for (T t : this) {
                oos.writeObject(t);
            }
            oos.writeObject(obj);
            fos.close();
            oos.close();

        } catch (IOException e) {
            return false;
        }

        synchronized (this.lock){
            this.file.delete();
            temp.renameTo(file);
            this.file = temp;
        }


        return true;
    }

    public boolean exists(T obj) {
        for (T temp : this) {
            if (temp.getID() == obj.getID()) {
                return true;
            }
        }
        return false;
    }

    public boolean check(CheckFunc<T> check) {
        for (T t : this) {
            if (check.check(t)) {
                return true;
            }
        }
        return false;
    }

    public int getNextID() {
        int c = 0;
        for (T obj : this) {
            c = obj.getID();
        }
        return c + 1;
    }

    public boolean update(T object) {
        boolean saved = false;
        File tempFile = new File(file + ".temp.db");
        try {
            FileOutputStream fos = new FileOutputStream(tempFile, false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            for (T readObject : this) {
                if (object.getID() == readObject.getID()) {
                    oos.writeObject(object);
                    saved = true;
                } else {
                    oos.writeObject(readObject);
                }
            }

            fos.close();
            oos.close();
        } catch (IOException e) {
            return false;
        }

        synchronized (this.lock){
            this.file.delete();
            tempFile.renameTo(this.file);
            this.file = tempFile;
        }


        return saved;
    }


    @Override
    public Iterator<T> iterator() {
        try {
            return new DatabaseItr();
        } catch (IOException e) {
            throw new RuntimeException("Unable to open database file", e);
        }
    }

    private class DatabaseItr implements Iterator<T> {
        private final ObjectInputStream ois;
        private T nextObj;
        private boolean finished;

        public DatabaseItr() throws IOException {
            ObjectInputStream ois;
            try{
                ois = new ObjectInputStream(new FileInputStream(file));
                this.finished = false;
            } catch (FileNotFoundException e) {
                this.finished = true;
                ois = null;
            }
            this.ois = ois;
            advance();
        }

        private void advance() {
            if (this.ois == null) {
                return;
            }
            try {
                @SuppressWarnings("unchecked")
                T obj = (T) ois.readObject();
                nextObj = obj;
            } catch (EOFException eof) {
                finished = true;
                nextObj = null;
                try {
                    ois.close();
                } catch (IOException ignored) {
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("Error reading from database", e);
            }
        }

        @Override
        public boolean hasNext() {
            return !finished;
        }

        @Override
        public T next() {
            T current = nextObj;
            advance();
            return current;
        }
    }
}
