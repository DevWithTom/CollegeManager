import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SemesterSchedule {

    // list of scheduled courses
    private List<ScheduledCourse> scheduledCourses;

    // constructor
    public SemesterSchedule(List<ScheduledCourse> scheduledCourses) {
        setScheduledCourses(scheduledCourses);
    }

    // list of scheduled courses setter
    public void setScheduledCourses(List<ScheduledCourse> scheduledCourses) {
        if (scheduledCourses == null) {
            throw new NullPointerException("array of scheduled courses cannot be null!");
        }
        this.scheduledCourses = new ArrayList<>();
        for (ScheduledCourse scheduledCourse: scheduledCourses) {
            addScheduledCourse(scheduledCourse);
        }
    }

    // helper method to prevent duplicate scheduled courses
    public void addScheduledCourse(ScheduledCourse scheduledCourse) {
        if (scheduledCourse == null) {
            throw new NullPointerException("scheduled course cannot be null!");
        }
        if (scheduledCourseExists(scheduledCourse)) {
            // prevent duplicates
            return;
        }
        this.scheduledCourses.add(scheduledCourse);
    }

    // helper method to determine whether this scheduled course already exists in our semester schedule
    public boolean scheduledCourseExists(ScheduledCourse scheduledCourse) {
        if (scheduledCourse == null) {
            throw new NullPointerException("scheduled course cannot be null!");
        }
        return this.scheduledCourses.contains(scheduledCourse);
    }

    // scheduled courses list getter - returns an unmodifiable copy to prevent user affecting the list from outside
    public List<ScheduledCourse> getAllScheduledCourses() {
        return Collections.unmodifiableList(this.scheduledCourses);
    }
}
