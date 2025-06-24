import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CourseFileLoader {

    // the variables below are block variables, meaning they hold information about a single course and
    // they get reset every time we successfully construct a new course
    // the premise of our loader is that a course block lives between 2 CODE lines, we don't count on blank lines
    // when distinguishing between 2 separate courses

    // the course code that was parsed
    private String parsedCode;
    // the course title that was parsed
    private String parsedTitle;
    // the course credits that were parsed
    private int parsedCredits;
    // the course capacity that was parsed
    private int parsedCapacity;
    // the course prerequisite codes that were parsed
    private List<String> parsedPrerequisites;

    // the variables below hold their data throughout the entire course loading process

    // list that holds the courses successfully loaded so far
    private List<Course> loadedCourses;
    // list that contains lists of prerequisite codes for every course that was loaded so far
    // such that prerequisitesPerCourse.get(i) is the list of prerequisites for loadedCourses.get(i)
    private List<List<String>> prerequisitesPerCourse;

    // the variables below describe all the parser keys and delimiters

    // delimiter between any {key:value} pair
    public static final String KEY_VALUE_DELIMITER_PATTERN = ":";
    // the delimiter for prerequisite courses was not given in the assignment,
    // so a comma is used in our case
    public static final String PREREQUISITE_LIST_DELIMITER = ",";
    // course code key
    public static final String CODE_KEY = "CODE";
    // course title key
    public static final String TITLE_KEY = "TITLE";
    // course credits key
    public static final String CREDITS_KEY = "CREDITS";
    // course capacity key
    public static final String CAPACITY_KEY = "CAPACITY";
    // course prerequisites key
    public static final String PREREQUISITES_KEY = "PREREQUISITES";

    // constructor
    public CourseFileLoader() {
        clearLoadedCourses();
        prepareToReadNextCourseBlock();
    }

    // list of loaded courses so far getter
    public List<Course> getLoadedCourses() {
        return Collections.unmodifiableList(this.loadedCourses);
    }

    // instead of a setter, we clear and initialize these lists for every new loader
    public void clearLoadedCourses() {
        this.loadedCourses = new ArrayList<>();
        this.prerequisitesPerCourse = new ArrayList<>();
    }

    // list of lists of prerequisite codes per course getter
    public List<List<String>> getPrerequisitesPerCourse() {
        return Collections.unmodifiableList(this.prerequisitesPerCourse);
    }

    // if we think we have enough parsed data to construct a new course, we do it here
    // if some data is missing, we throw an exception
    // we call this method before trying to parse another course code
    private void maybeAddParsedCourseOrThrow(String errorMessage) throws BadFormatException {
        if (this.parsedCode != null) {
            // if we get here, it means we successfully parsed the course code
            if (!parsedEnoughDataToCreateAnotherCourse()) {
                // if we get here, it means some other course attribute is missing, so we throw
                throw new BadFormatException(errorMessage);
            } else {
                // if we get here, we have everything we need to construct a new course and add it to our loaded courses list
                Course course = null;
                try {
                    course = new Course(this.parsedCode, this.parsedTitle, this.parsedCredits, this.parsedCapacity, new ArrayList<>());
                } catch (CapacityTooSmallException e) {
                    // we didn't want to change the load() method signature, and it did not have 'throws CapacityTooSmallException', so we
                    // catch any CapacityTooSmallException and re-throw it as a BadFormatException back to Main
                    throw new BadFormatException(e);
                }
                addLoadedCourse(course);
                addLoadedPrerequisites(this.parsedPrerequisites);
                prepareToReadNextCourseBlock();
            }
        }
    }

    // helper method to prevent duplicate loaded courses
    private void addLoadedCourse(Course course) {
        if (course == null) {
            throw new NullPointerException("course cannot be null!");
        }
        if (getLoadedCourses().contains(course)) {
            // prevent duplication
            return;
        }
        this.loadedCourses.add(course);
    }

    // helper method to add loaded prerequisites for a specific course
    private void addLoadedPrerequisites(List<String> prerequisites) {
        if (prerequisites == null) {
            throw new NullPointerException("list of prerequisites cannot be null!");
        }
        this.prerequisitesPerCourse.add(prerequisites);
    }

    // reset our parsing variables before parsing new data
    private void prepareToReadNextCourseBlock() {
        this.parsedCode = null;
        this.parsedTitle = null;
        this.parsedCredits = 0;
        this.parsedCapacity = 0;
        this.parsedPrerequisites = new ArrayList<>();
    }

    // returns true if we parsed all the mandatory attributes to construct a course, false otherwise
    private boolean parsedEnoughDataToCreateAnotherCourse() {
        return this.parsedCode != null
                && this.parsedTitle != null
                && this.parsedCredits != 0
                && this.parsedCapacity != 0;
    }

    // helper method for parsing any string value and trimming it
    private String parseStringValueOrThrow(String value, String errorMessage) throws BadFormatException {
        if (value == null || value.isBlank()) {
            throw new BadFormatException(errorMessage);
        }
        return value.trim();
    }

    // helper method for parsing any int value
    private int parseIntValueOrThrow(String value, String errorMessage) throws BadFormatException {
        if (value == null || value.isBlank()) {
            throw new BadFormatException(errorMessage);
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new BadFormatException(errorMessage);
        }
    }

    // helper method for parsing any list of strings delimited by delimiterPattern
    private List<String> parseStringListValueOrThrow(String value, String delimiterPattern, String errorMessage) throws BadFormatException {
        if (value == null || value.isBlank()) {
            throw new BadFormatException(errorMessage);
        }
        List<String> prerequisites = new ArrayList<>();
        String[] prerequisiteCodes = value.split(delimiterPattern);
        for (String prerequisiteCode: prerequisiteCodes) {
            if (prerequisiteCode == null || prerequisiteCode.isBlank()) {
                throw new BadFormatException(errorMessage);
            }
            if (prerequisites.contains(prerequisiteCode.trim())) {
                continue;
            }
            prerequisites.add(prerequisiteCode.trim());
        }
        return prerequisites;
    }

    // if found among the loaded courses, returns a Course that corresponds to the given code, or null otherwise
    // this method is used when adding prerequisite courses to our already loaded and constructed courses
    private Course findCourseByCode(String courseCode) {
        for (Course course: getLoadedCourses()) {
            if (course.getCode().equals(courseCode)) {
                return course;
            }
        }
        return null;
    }

    public List<Course> load(String filePath) throws IOException, BadFormatException {
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
                throw new BadFormatException("course file lines should be delimited with '" + KEY_VALUE_DELIMITER_PATTERN + "'!");
            }

            switch (parts[0]) {
                case CODE_KEY:
                    // if we get here, we try adding a new course to our loaded courses
                    // if it's our first time here, no course will be added
                    // if it's not our first time here, then it means we landed on a CODE_KEY while holding onto another parsedCode
                    // so it must mean we finished an entire course block
                    maybeAddParsedCourseOrThrow("some course parameters are missing after reading a full course block!");
                    // parse course code
                    this.parsedCode = parseStringValueOrThrow(parts[1], "failed parsing course code!");
                    break;
                case TITLE_KEY:
                    // parse course title
                    this.parsedTitle = parseStringValueOrThrow(parts[1], "failed parsing course title!");
                    break;
                case CREDITS_KEY:
                    // parse course credits
                    this.parsedCredits = parseIntValueOrThrow(parts[1], "failed parsing course credit points!");
                    break;
                case CAPACITY_KEY:
                    // parse course capacity
                    this.parsedCapacity = parseIntValueOrThrow(parts[1], "failed parsing course capacity!");
                    break;
                case PREREQUISITES_KEY:
                    // parse course prerequisite but put them aside in out list of lists to be used later
                    this.parsedPrerequisites = parseStringListValueOrThrow(parts[1], PREREQUISITE_LIST_DELIMITER,"failed parsing list of prerequisites!");
                    break;
                default:
                    // unknown key
                    throw new BadFormatException("non-empty course file lines must start with: " +
                            CODE_KEY + ", " +
                            TITLE_KEY + ", " +
                            CREDITS_KEY + ", " +
                            CAPACITY_KEY + " or " +
                            PREREQUISITES_KEY + "!");
            }
        }
        bufferedReader.close();


        // if we get here, we probably reached the end of the file. now, since we must land on the CODE_KEY
        // to add a new course, we check for it here because the file does not end with the CODE_KEY
        maybeAddParsedCourseOrThrow("unexpected end of file, some course parameters are missing!");

        // here we take all the saved up prerequisites and add them to their respective courses
        if (getLoadedCourses().size() != prerequisitesPerCourse.size()) {
            throw new BadFormatException("failed parsing course file!");
        }
        for (int i = 0; i < getPrerequisitesPerCourse().size(); i++) {
            for (int j = 0; j < getPrerequisitesPerCourse().get(i).size(); j++) {
                Course prerequisite = findCourseByCode(prerequisitesPerCourse.get(i).get(j));
                if (prerequisite == null) {
                    throw new BadFormatException("failed finding the requested prerequisite among all the loaded courses!");
                }
                getLoadedCourses().get(i).addPrerequisite(prerequisite);
            }
        }
        return getLoadedCourses();
    }
}
