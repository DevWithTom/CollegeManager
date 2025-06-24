import java.time.LocalDateTime;

public class ScheduledCourse {

    // the scheduled course
    private Course course;
    // the scheduled classroom
    private Classroom classroom;
    // the scheduled datetime
    private LocalDateTime dateTime;

    // constructor
    public ScheduledCourse(Course course, Classroom classroom, LocalDateTime dateTime) {
        setClassroom(classroom);
        setCourse(course);
        setDateTime(dateTime);
    }

    // course getter
    public Course getCourse() {
        return this.course;
    }

    // classroom getter
    public Classroom getClassroom() {
        return this.classroom;
    }

    // datetime getter
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    // course setter
    public void setCourse(Course course) {
        if (course == null) {
            throw new NullPointerException("course cannot be null!");
        }
        this.course = course;
    }

    // classroom setter
    public void setClassroom(Classroom classroom) {
        if (classroom == null) {
            throw new NullPointerException("classroom cannot be null!");
        }
        this.classroom = classroom;
    }

    // datetime setter
    public void setDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new NullPointerException("datetime cannot be null!");
        }
        this.dateTime = dateTime;
    }

    // equality is determined by all 3 parameters
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (o instanceof ScheduledCourse) {
            ScheduledCourse other = (ScheduledCourse) o;
            return getCourse().equals(other.getCourse())
                    && getClassroom().equals(other.getClassroom())
                    && getDateTime().equals(other.getDateTime());
        }
        return false;
    }

    @Override
    public String toString() {
        return "ScheduledCourse{" +
                "course=" + getCourse() +
                ", classroom=" + getClassroom() +
                ", dateTime=" + getDateTime() +
                '}';
    }
}
