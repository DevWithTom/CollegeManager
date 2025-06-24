import java.io.*;
import java.util.List;

public class EnrollmentSerializer {

    public static void save(List<Enrollment> enrollments, String filePath) throws IOException {
        if (enrollments == null) {
            throw new NullPointerException("enrollments list cannot be null!");
        }
        if (filePath == null) {
            throw new NullPointerException("filePath string cannot be null!");
        }

        File serializedEnrollmentsFile = new File(filePath);
        File serializedEnrollmentsFileParentDirectory = serializedEnrollmentsFile.getParentFile();
        // if our file does not exist, all parent directories leading to our file must exist, or the creation of the file itself will fail
        if (!serializedEnrollmentsFile.exists()) {
            if (serializedEnrollmentsFileParentDirectory != null && !serializedEnrollmentsFileParentDirectory.exists()) {
                throw new IOException("all parent directories leading to our file must exist before creating the file!");
            }
        }
        // if our file does exist, it should point to a regular file
        if (serializedEnrollmentsFile.exists() && !serializedEnrollmentsFile.isFile()) {
            throw new IOException("cannot save enrollments, the given filePath does not point to a regular file!");
        }

        // create the stream, write the list, and close the stream. all IOExceptions and their descendants are propagated back to Main
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath));
        objectOutputStream.writeObject(enrollments);
        objectOutputStream.close();
    }

    public static List<Enrollment> load(String filePath) throws IOException, ClassNotFoundException {
        if (filePath == null) {
            throw new NullPointerException("filePath string cannot be null!");
        }
        File serializedEnrollmentsFile = new File(filePath);
        if (!serializedEnrollmentsFile.exists() || !serializedEnrollmentsFile.isFile()) {
            throw new IOException("cannot load enrollments, filePath does not exist or does not point to a regular file!");
        }

        // create the stream, read the list, and close the stream. all IOExceptions and their descendants are propagated back to Main
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath));
        List<Enrollment> deserialized = (List<Enrollment>) objectInputStream.readObject();
        objectInputStream.close();
        return deserialized;
    }
}
