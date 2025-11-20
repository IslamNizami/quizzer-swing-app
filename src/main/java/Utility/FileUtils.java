package Utility;

import java.io.*;

public class FileUtils {
    private FileUtils() {
        throw new UnsupportedOperationException("Utility class should not be instantiated.");
    }

    public static void saveObjectToFile(Object obj, File file) throws IOException {
        if(obj == null) {
            throw new IllegalArgumentException("Object to save cannot be null.");
        }
        if(file == null) {
            throw new IllegalArgumentException("File to save cannot be null.");
        }


        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(obj);
        }
    }

    public static Object loadObjectFromFile(File file) throws IOException,ClassNotFoundException{
        if(file == null || !file.exists()) {
            throw new IllegalArgumentException("File not found: " + file);
        }

        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))){
            return in.readObject();
        }
    }
}
