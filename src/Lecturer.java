import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lecturer extends Person {

    // lecturer department
    private String department;
    // list of courses that the lecturer teaches
    private List<Course> teaches;

    // constructor
    public Lecturer(String id, String name, String department, List<Course> teachingCourses) {
        super(id, name);
        setDepartment(department);
        setTeachingCourses(teachingCourses);
    }

    // department getter
    public String getDepartment() {
        return this.department;
    }

    // list of teaching courses getter - returns an unmodifiable copy to prevent user affecting the list from outside
    public List<Course> getCourses() {
        return Collections.unmodifiableList(this.teaches);
    }

    // helper method to determine whether this lecturer teaches a specific course
    public boolean isTeachingCourse(Course course) {
        if (course == null) {
            throw new NullPointerException("course cannot be null!");
        }
        return getCourses().contains(course);
    }

    // department setter
    public void setDepartment(String departmentName) {
        if (departmentName == null) {
            throw new NullPointerException("department name cannot be null!");
        }
        if (departmentName.isBlank()) {
            throw new IllegalArgumentException("department name cannot be empty!");
        }
        this.department = departmentName.trim();
    }

    // teaching courses list setter
    public void setTeachingCourses(List<Course> teachingCourses) {
        if (teachingCourses == null) {
            throw new NullPointerException("array of teaching courses cannot be null!");
        }
        this.teaches = new ArrayList<>();
        for (Course course: teachingCourses) {
            addCourse(course);
        }
    }

    // helper method to prevent duplicate teaching courses
    public void addCourse(Course course) {
        if (course == null) {
            throw new NullPointerException("teaching course cannot be null!");
        }
        if (isTeachingCourse(course)) {
            // prevent duplicates
            return;
        }
        this.teaches.add(course);
    }

    @Override
    public String toString() {
        return "Lecturer{" +
                "department='" + getDepartment() + '\'' +
                ", teaches=" + getCourses() +
                ", id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                '}';
    }
}
