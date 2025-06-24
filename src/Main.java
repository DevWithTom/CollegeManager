import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("=== Step 1: Loading students and courses from files ===\n");
            StudentFileLoader studentLoader = new StudentFileLoader();
            List<Student> students = studentLoader.load("data/students.txt");
            System.out.println("Loaded " + students.size() + " students");

            CourseFileLoader courseLoader = new CourseFileLoader();
            List<Course> courses = courseLoader.load("data/courses.txt");
            System.out.println("Loaded " + courses.size() + " courses\n");

            System.out.println("=== Step 2: Printing students and courses ===\n");
            for (Student s : students) {
                System.out.println(s);
            }
            System.out.println();
            for (Course c : courses) {
                System.out.println(c.getCode() + " - " + c.getTitle());
            }

            System.out.println("\n=== Step 3: Course enrollment attempts ===");
            EnrollmentManager manager = new EnrollmentManager();
            manager.enroll(students.get(0), courses.get(0));
            manager.enroll(students.get(1), courses.get(0));
            System.out.println("Two students enrolled successfully in the first course");

            try {
                manager.enroll(students.get(0), courses.get(0));
                System.out.println("Issue: duplicate enrollment not detected\n");
            } catch (DuplicateEnrollmentException e) {
                System.out.println("Duplicate enrollment detected: " + e.getMessage());
            }

            try {
                manager.enroll(students.get(0), courses.get(1));
                System.out.println("Issue: missing prerequisite not detected\n");
            } catch (PrerequisiteMissingException e) {
                System.out.println("Missing prerequisite detected: " + e.getMessage());
            }

            System.out.println("\n=== Step 4: Saving enrollments to binary file ===");
            EnrollmentSerializer.save(manager.getAllEnrollments(), "output/enrollments.bin");
            System.out.println("File enrollments.bin saved successfully\n");

            System.out.println("=== Step 5: Generating text report ===");
            ReportGenerator.writeEnrollmentReport("output/enrollment_report.txt", manager.getAllEnrollments());
            System.out.println("File enrollment_report.txt created successfully\n");

            System.out.println("=== Step 6: Testing CollectionUtils ===");
            List<String> names = List.of("Dana", "Amir", "Dana", "Yossi", "Amir");
            String max = CollectionUtils.max(names);
            System.out.println("Maximum value: " + max);

            List<String> duplicates = CollectionUtils.findDuplicates(names);
            System.out.println("Detected duplicates: " + duplicates);

            System.out.println("\nAll steps completed successfully");

        } catch (Exception e) {
            System.out.println("\nAn error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}