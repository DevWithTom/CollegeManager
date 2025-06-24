import java.io.Serializable;
import java.time.LocalDateTime;

public class Enrollment implements Serializable {

    // enrollment student
    private Student student;
    // enrollment course
    private Course course;
    // enrollment timestamp
    private LocalDateTime timestamp;

    // constructor
    public Enrollment(Student student, Course course, LocalDateTime timestamp) {
        setStudent(student);
        setCourse(course);
        setTimestamp(timestamp);
    }

    // enrollment student getter
    public Student getStudent() {
        return this.student;
    }

    // enrollment course getter
    public Course getCourse() {
        return this.course;
    }

    // enrollment timestamp getter
    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    // enrollment student setter
    public void setStudent(Student student) {
        if (student == null) {
            throw new NullPointerException("student cannot be null!");
        }
        this.student = student;
    }

    // enrollment course setter
    public void setCourse(Course course) {
        if (course == null) {
            throw new NullPointerException("course cannot be null!");
        }
        this.course = course;
    }

    // enrollment timestamp setter
    public void setTimestamp(LocalDateTime timestamp) {
        if (timestamp == null) {
            throw new NullPointerException("timestamp cannot be null!");
        }
        this.timestamp = timestamp;
    }

    // an enrollment ties a student to a course,
    // the timestamp is not a factor of equality,
    // this is critical to properly deal with DuplicateEnrollmentException later
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (o instanceof Enrollment) {
            Enrollment other = (Enrollment) o;
            return getStudent().equals(other.getStudent()) && getCourse().equals(other.getCourse());
        }
        return false;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "student=" + getStudent() +
                ", course=" + getCourse() +
                ", timestamp=" + getTimestamp() +
                '}';
    }
}
