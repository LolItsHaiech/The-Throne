package db;

import db.interfaces.CheckFunc;
import db.interfaces.DBSerializable;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

// todo optimize
// todo implement cache
public class Database<T extends DBSerializable> implements Iterable<T> {
    private final File file;
    private final Object lock = new Object();

    public Database(String fileName) {
        this.file = new File(fileName + ".db");
    }

    public boolean write(T obj) {
        File temp = new File(this.file.getAbsolutePath() + ".temp");
        try {
            FileOutputStream fos = new FileOutputStream(temp);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            for (T t : this) {
                oos.writeObject(t);
            }
            oos.writeObject(obj);
            oos.close();
            fos.close();

        } catch (IOException e) {
            return false;
        }

        synchronized (this.lock) {
            this.file.delete();
            if (!temp.renameTo(this.file)) {
                return false;
            }
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
        File tempFile = new File(file.getAbsolutePath() + ".temp");
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

            oos.close();
            fos.close();
        } catch (IOException e) {
            return false;
        }

        synchronized (this.lock) {
            this.file.delete();
            if (!tempFile.renameTo(this.file)) {
                return false;
            }
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
        private ObjectInputStream ois;
        private T nextObj;
        private boolean finished;

        public DatabaseItr() throws IOException {
            try {
                FileInputStream fis = new FileInputStream(file);
                this.ois = new ObjectInputStream(fis);
                this.finished = false;
            } catch (FileNotFoundException e) {
                this.finished = true;
                this.ois = null;
            }
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
                closeStream();
            } catch (IOException | ClassNotFoundException e) {
                finished = true;
                nextObj = null;
                closeStream();
                throw new RuntimeException("Error reading from database", e);
            }
        }

        private void closeStream() {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException ignored) {
                }
                ois = null;
            }
        }

        @Override
        public boolean hasNext() {
            return !finished;
        }

        @Override
        public T next() {
            if (finished) {
                throw new NoSuchElementException();
            }
            T current = nextObj;
            advance();
            return current;
        }
    }
}