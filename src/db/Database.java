package db;

import java.io.*;
import java.util.Iterator;

// todo make synchronized
public class Database<T extends DBSerializable> implements Iterable<T> {
    private File file;

    public Database(String fileName) {
        this.file = new File(fileName + ".db");
    }

    public boolean write(T obj) {
        boolean append = file.exists();
        try (FileOutputStream fos = new FileOutputStream(file, true);
             ObjectOutputStream oos = append
                     ? new AppendableObjectOutputStream(fos)
                     : new ObjectOutputStream(fos)) {
            oos.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean check(T obj) {
        for (T temp : this) {
            if (temp.equals(obj)) {
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

        this.file.delete();
        tempFile.renameTo(this.file);
        this.file = tempFile;


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

    /**
     * Custom OOS that omits the header when appending.
     */
    private static class AppendableObjectOutputStream extends ObjectOutputStream {
        public AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            // Skip header
        }
    }

    /**
     * Iterator over the objects in the database file.
     */
    private class DatabaseItr implements Iterator<T> {
        private final ObjectInputStream ois;
        private T nextObj;
        private boolean finished = false;

        public DatabaseItr() throws IOException {
            FileInputStream fis = new FileInputStream(file);
            this.ois = new ObjectInputStream(fis);
            advance();
        }

        private void advance() {
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
