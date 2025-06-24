import java.time.LocalDate;

// Student is already Serializable thanks to Person
public class Student extends Person {

    // student enrollment date
    private LocalDate enrollmentDate;

    // constructor
    public Student(String id, String name, LocalDate enrollmentDate) {
        super(id, name);
        setEnrollmentDate(enrollmentDate);
    }

    // enrollment date getter
    public LocalDate getEnrollmentDate() {
        return this.enrollmentDate;
    }

    // enrollment date setter
    public void setEnrollmentDate(LocalDate enrollmentDate) {
        if (enrollmentDate == null) {
            throw new NullPointerException("enrollment date cannot be null!");
        }
        this.enrollmentDate = enrollmentDate;
    }

    @Override
    public String toString() {
        return "Student{" +
                "enrollmentDate=" + getEnrollmentDate() +
                ", id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                '}';
    }
}
