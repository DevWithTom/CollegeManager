import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnrollmentManager {

    private static List<Enrollment> enrollments = new ArrayList<>();

    public void enroll(Student student, Course course) throws CourseFullException, PrerequisiteMissingException, DuplicateEnrollmentException {
        if (student == null) {
            throw new NullPointerException("student cannot be null!");
        }
        if (course == null) {
            throw new NullPointerException("course cannot be null!");
        }

        // handle CourseFullException
        // getEnrollmentsForCourse(course).size() is basically the amount of student that enrolled to this course
        if (getEnrollmentsForCourse(course).size() == course.getCapacity()) {
            throw new CourseFullException("course is full!");
        }

        // handle DuplicateEnrollmentException
        if (enrollmentExists(student, course)) {
            throw new DuplicateEnrollmentException("student is already enrolled to this course!");
        }

        // handle PrerequisiteMissingException
        // the TA said in her reply that an enrollment counts as if the student meets the prerequisite
        // so we iterate over all the prerequisites until we find one that the student did not enroll
        for (Course prerequisite: course.getPrerequisites()) {
            if (!enrollmentExists(student, prerequisite)) {
                throw new PrerequisiteMissingException("student does not meet all prerequisites!");
            }
        }

        enrollments.add(new Enrollment(student, course, LocalDateTime.now()));
    }

    // helper method to determine if an enrollment exists
    public boolean enrollmentExists(Student student, Course course) {
        if (student == null) {
            throw new NullPointerException("student cannot be null!");
        }
        if (course == null) {
            throw new NullPointerException("course cannot be null!");
        }

        List<Enrollment> enrollmentsForCourse = getEnrollmentsForCourse(course);
        // create a temporary enrollment just to check if it exists, this is exactly why the enrollment date should not
        // play a part when testing for equality
        Enrollment temporaryEnrollment = new Enrollment(student, course, LocalDateTime.now());
        return enrollmentsForCourse.contains(temporaryEnrollment);
    }

    // helper method to get all enrollments to a specific course
    public List<Enrollment> getEnrollmentsForCourse(Course course) {
        if (course == null) {
            throw new NullPointerException("course cannot be null!");
        }

        // create a new list
        List<Enrollment> enrollmentsForCourse = new ArrayList<>();
        // iterate over all enrollments and add the enrollments that specify the given course
        for (Enrollment enrollment : getAllEnrollments()) {
            if (enrollment.getCourse().equals(course)) {
                enrollmentsForCourse.add(enrollment);
            }
        }
        return enrollmentsForCourse;
    }

    // all enrollments list getter - returns an unmodifiable copy to prevent user affecting the list from outside
    public List<Enrollment> getAllEnrollments() {
        return Collections.unmodifiableList(enrollments);
    }
}
