import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// implements Serializable to make Enrollment Serializable
public class Course implements Serializable {

    // minimum capacity for a course to handle CapacityTooSmallException
    public static final int MINIMUM_CAPACITY_REQUIREMENT = 10;
    // course code
    private String code;
    // course title
    private String title;
    // course credit points
    private int creditPoints;
    // course capacity - the maximum amount of students that can enroll to this course
    private int capacity;
    // list of prerequisite courses for this course
    private List<Course> prerequisites;

    // constructor
    public Course(String code, String title, int creditPoints, int capacity, List<Course> prerequisites) throws CapacityTooSmallException {
        setCode(code);
        setTitle(title);
        setCreditPoints(creditPoints);
        setCapacity(capacity);
        setPrerequisites(prerequisites);
    }

    // course code getter
    public String getCode() {
        return this.code;
    }

    // course title getter
    public String getTitle() {
        return this.title;
    }

    // course credit points getter
    public int getCreditPoints() {
        return this.creditPoints;
    }

    // course capacity getter
    public int getCapacity() {
        return this.capacity;
    }

    // prerequisite list getter - returns an unmodifiable copy to prevent user affecting the list from outside
    public List<Course> getPrerequisites() {
        return Collections.unmodifiableList(this.prerequisites);
    }

    // course code setter
    public void setCode(String courseCode) {
        if (courseCode == null) {
            throw new NullPointerException("course code cannot be null!");
        }
        if (courseCode.isBlank()) {
            throw new IllegalArgumentException("course code cannot be empty!");
        }
        this.code = courseCode.trim();
    }

    // course title setter
    public void setTitle(String courseTitle) {
        if (courseTitle == null) {
            throw new NullPointerException("course title cannot be null!");
        }
        if (courseTitle.isBlank()) {
            throw new IllegalArgumentException("course title cannot be empty!");
        }
        this.title = courseTitle.trim();
    }

    // course credit points setter
    public void setCreditPoints(int courseCreditPoints) {
        if (courseCreditPoints <= 0) {
            throw new IllegalArgumentException("course credits amount must be a positive integer!");
        }
        this.creditPoints = courseCreditPoints;
    }

    // course capacity setter
    public void setCapacity(int courseCapacity) throws CapacityTooSmallException {
        if (courseCapacity <= 0) {
            throw new IllegalArgumentException("course capacity must be a positive integer!");
        }
        // the assignment did not specify how to handle the exceptions so we propagate them back to Main
        if (courseCapacity < MINIMUM_CAPACITY_REQUIREMENT) {
            throw new CapacityTooSmallException("course capacity must be at least '" + MINIMUM_CAPACITY_REQUIREMENT + "'!");
        }
        this.capacity = courseCapacity;
    }

    // course prerequisite list setter
    public void setPrerequisites(List<Course> coursePrerequisites) {
        if (coursePrerequisites == null) {
            throw new NullPointerException("array of prerequisites cannot be null!");
        }
        this.prerequisites = new ArrayList<>();
        for (Course prerequisite: coursePrerequisites) {
            addPrerequisite(prerequisite);
        }
    }

    // helper method to prevent duplicate prerequisite courses
    public void addPrerequisite(Course prerequisite) {
        if (prerequisite == null) {
            throw new NullPointerException("prerequisite course cannot be null!");
        }
        if (hasPrerequisite(prerequisite)) {
            // prevent duplicates
            return;
        }
        this.prerequisites.add(prerequisite);
    }

    // helper method to determine whether this course has a specific prerequisite
    public boolean hasPrerequisite(Course prerequisite) {
        if (prerequisite == null) {
            throw new NullPointerException("prerequisite course cannot be null!");
        }
        return this.prerequisites.contains(prerequisite);
    }

    // courses are uniquely defined by their code, only the code determines equality
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (o instanceof Course) {
            Course other = (Course) o;
            return getCode().equals(other.getCode());
        }
        return false;
    }

    @Override
    public String toString() {
        return "Course{" +
                "code='" + getCode() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", creditPoints=" + getCreditPoints() +
                ", capacity=" + getCapacity() +
                ", prerequisites=" + getPrerequisites() +
                '}';
    }
}
