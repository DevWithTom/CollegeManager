import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentFileLoader {

    // the variables below are block variables, meaning they hold information about a single student and
    // they get reset every time we successfully construct a new student
    // the premise of our loader is that a student block lives between 2 ID lines, we don't count on blank lines
    // when distinguishing between 2 separate students

    // the student id that was parsed
    private String parsedId;
    // the student name that was parsed
    private String parsedName;
    // the student join date that was parsed
    private LocalDate parsedLocalDate;

    // the variables below hold their data throughout the entire student loading process

    // list that holds the courses successfully loaded so far
    private List<Student> loadedStudents;

    // the variables below describe all the parser keys and delimiters

    // delimiter between any {key:value} pair
    public static final String KEY_VALUE_DELIMITER_PATTERN = ":";
    // student id key
    public static final String ID_KEY = "ID";
    // student name key
    public static final String NAME_KEY = "Name";
    // student join date key
    public static final String JOINED_KEY = "Joined";

    // constructor
    public StudentFileLoader() {
        clearLoadedStudents();
        prepareToReadNextStudentBlock();
    }

    // list of loaded students so far getter
    public List<Student> getLoadedStudents() {
        return Collections.unmodifiableList(this.loadedStudents);
    }

    // instead of a setter, we clear and initialize the list for every new loader
    public void clearLoadedStudents() {
        this.loadedStudents = new ArrayList<>();
    }

    // if we think we have enough parsed data to construct a new student, we do it here
    // if some data is missing, we throw an exception
    // we call this method before trying to parse another student id
    private void maybeAddParsedStudentOrThrow(String errorMessage) throws BadFormatException {
        if (this.parsedId != null) {
            // if we get here, it means we successfully parsed the student id
            if (!parsedEnoughDataToCreateAnotherStudent()) {
                // if we get here, it means some other student attribute is missing, so we throw
                throw new BadFormatException(errorMessage);
            } else {
                // if we get here, we have everything we need to construct a new student and add it to our loaded students list
                Student student = new Student(this.parsedId, this.parsedName, this.parsedLocalDate);
                addLoadedStudent(student);
                prepareToReadNextStudentBlock();
            }
        }
    }

    // helper method to prevent duplicate loaded students
    private void addLoadedStudent(Student student) {
        if (student == null) {
            throw new NullPointerException("student cannot be null!");
        }
        if (getLoadedStudents().contains(student)) {
            // prevent duplication
            return;
        }
        this.loadedStudents.add(student);
    }

    // reset our parsing variables before parsing new data
    private void prepareToReadNextStudentBlock() {
        this.parsedId = null;
        this.parsedName = null;
        this.parsedLocalDate = null;
    }

    // returns true if we parsed all the mandatory attributes to construct a student, false otherwise
    private boolean parsedEnoughDataToCreateAnotherStudent() {
        return this.parsedId != null
                && this.parsedName != null
                && this.parsedLocalDate != null;
    }

    // helper method for parsing any string value and trimming it
    private String parseStringValueOrThrow(String value, String errorMessage) throws BadFormatException {
        if (value == null || value.isBlank()) {
            throw new BadFormatException(errorMessage);
        }
        return value.trim();
    }

    // helper method for parsing the student join date
    private LocalDate parseLocalDateValueOrThrow(String value, String errorMessage) throws BadFormatException {
        if (value == null || value.isBlank()) {
            throw new BadFormatException(errorMessage);
        }
        try {
            // use the built-in LocalDate parses and re-throw if it fails
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException e) {
            throw new BadFormatException(errorMessage);
        }
    }

    public List<Student> load(String filePath) throws IOException, BadFormatException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.isBlank()) {
                // skip blank lines
                continue;
            }

            String[] parts = line.split(KEY_VALUE_DELIMITER_PATTERN);
            if (parts.length < 2) {
                // line was not delimited properly or a value is missing after the delimiter
                throw new BadFormatException("student file lines should be delimited with '" + KEY_VALUE_DELIMITER_PATTERN + "'!");
            }

            switch (parts[0]) {
                case ID_KEY:
                    // if we get here, we try adding a new student to our loaded students
                    // if it's our first time here, no student will be added
                    // if it's not our first time here, then it means we landed on an ID_KEY while holding onto another parsedId
                    // so it must mean we finished an entire student block
                    maybeAddParsedStudentOrThrow("some student parameters are missing after reading a full student block!");
                    // parse student id
                    this.parsedId = parseStringValueOrThrow(parts[1], "expected student id!");
                    break;
                case NAME_KEY:
                    // parse student name
                    this.parsedName = parseStringValueOrThrow(parts[1], "expected student name!");
                    break;
                case JOINED_KEY:
                    // parse student join date
                    this.parsedLocalDate = parseLocalDateValueOrThrow(parts[1], "expected join date!");
                    break;
                default:
                    // unknown key
                    throw new BadFormatException("non-empty student file lines must start with: " +
                            ID_KEY + ", " +
                            NAME_KEY + " or " +
                            JOINED_KEY + "!");
            }
        }
        bufferedReader.close();

        // if we get here, we probably reached the end of the file. now, since we must land on the ID_KEY
        // to add a new student, we check for it here because the file does not end with the ID_KEY
        maybeAddParsedStudentOrThrow("unexpected end of file, some student parameters are missing!");

        return loadedStudents;
    }
}
