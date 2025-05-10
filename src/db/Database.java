package db;

import java.io.*;
import java.util.Iterator;

public class Database<T extends DBSerializable> implements Iterable<T> {
    private final File file;

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
                try { ois.close(); } catch (IOException ignored) {}
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
