import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportGenerator {

    public static void writeEnrollmentReport(String filePath, List<Enrollment> enrollments) throws ReportWriteException {
        if (filePath == null) {
            throw new NullPointerException("file path cannot be null!");
        }
        if (enrollments == null) {
            throw new NullPointerException("list of enrollments cannot be null!");
        }

        File enrollmentReportFile = new File(filePath);
        File enrollmentReportFileParentDirectory = enrollmentReportFile.getParentFile();
        // if our file does not exist, all parent directories leading to our file must exist, or the creation of the file itself will fail
        if (!enrollmentReportFile.exists()) {
            if (enrollmentReportFileParentDirectory != null && !enrollmentReportFileParentDirectory.exists()) {
                throw new ReportWriteException("all parent directories leading to our file must exist before creating the file!");
            }
        }
        // if our file does exist, it should point to a regular file
        if (enrollmentReportFile.exists() && !enrollmentReportFile.isFile()) {
            throw new ReportWriteException("cannot save enrollment report, the given filePath does not point to a regular file!");
        }

        // set the datetime formatting like it was specified in the assignment
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));
            // iterate over all the given enrollments and append their data to our report file
            for (Enrollment enrollment: enrollments) {
                bufferedWriter.append(String.format("%s | %s | %s | %s",
                        enrollment.getStudent().getId(),
                        enrollment.getStudent().getName(),
                        enrollment.getCourse().getTitle(),
                        enrollment.getTimestamp().format(dateTimeFormatter)));
                bufferedWriter.newLine();
            }
            // close the buffer
            bufferedWriter.close();
        } catch (IOException e) {
            if (e.getMessage() != null) {
                // we didn't want to change the method signature, and it did not have 'throws IOException', so we
                // catch any IOException and re-throw it as a ReportWriteException back to Main
                throw new ReportWriteException(e.getMessage());
            }
        }
    }
}
