package databases;

import models.Student;

import java.util.List;

// Andrew :)

/*
    This is the database responsible for loading and saving student records to text file
    Any record changes are stored at runtime till saveToFile is called

    It allows retrieval, adding, removing and updating student records
    It can also automatically generate sequential IDs for student records,
        WARNING: manually adding an ID that is ahead of the current sequence would disrupt it
                 meaning, if the latest record has ID 10, manually adding a record of ID 50
                 would add the next automatically generated one at 51
*/

public class StudentDatabase extends Database<Student> {
    /* Members */
    private int indexId;

    public StudentDatabase(String filename) {
        super(filename);
        this.logName = "StudentDatabase";

        /* Getting latest ID */
        /* Loading previous records */
        readFile();
        this.indexId = 0;
        /* Get the largest ID that was saved */
        for (Student record : this.records) {
            this.indexId = Math.max(this.indexId, record.getId());
        }
        // Note: Generating IDs will count from that
    }

    @Override
    public Student createRecordFrom(String line) {
        // Note:
        // Student.lineRepresentation() returns:
        //      "{id},{name},{age},{gender},{department},{gpa}"
        //        [0]   [1]   [2]    [3]        [4]       [5]
        if (line == null) return null; // Makes sure no null was passed

        String[] data = line.split("\\s*,\\s*");

        // If the given string doesn't represent EmployeeUser as expected
        if (data.length < 6 || line.isEmpty()) {
            // POSSIBLE: Unnecessary prints
            System.out.println("[StudentDatabase]: Invalid line representation for Student was given");
            return null;
        }

        try {
            int id = Integer.parseInt(data[0]);
            String name = data[1];
            int age = Integer.parseInt(data[2]);
            String gender = data[3];
            String department = data[4];
            float gpa = Float.parseFloat(data[5]);


            return new Student(id, name, age, gender, department, gpa);
        } catch (NumberFormatException err) {
            System.out.println("[StudentDatabase]: Invalid line representation for Student was given");
            return null;
        }
    }

    public List<Student> getStudents() {
        /* Return all saved students */
        return this.records;
    }

    public boolean contains(int id) {
        return getStudentById(id) != null;
    }

    public Student getStudentById(int id) {
        /* Return a student by a given ID */
        for (Student record : this.records) {
            if (record.getId() == id) return record;
        }
        return null;
    }

    public void addStudent(String name, int age, String gender, String department, float gpa) {
        /* Add a new student with a generated ID */
        addStudent(this.indexId + 1, name, age, gender, department, gpa);
    }

    public void addStudent(int id, String name, int age, String gender, String department, float gpa) {
        /* Add a new student with all arguments */
        if (getStudentById(id) == null) {
            Student student = new Student(id, name, age, gender, department, gpa);
            if (student.getGpa() == null || student.getGender() == null || student.getAge() == null) {
                System.out.println("[StudentDatabase]: Validation failed, student not added");
                return;
            }
            insertRecord(student);
            this.indexId = Math.max(this.indexId, id);
        }
    }

    public void deleteStudent(int id) {
        /* Deletes a student record by a given ID if it exists */
        this.records.removeIf((record) -> record.getId() == id);
    }

    public void updateStudent(int id, String name, int age, String gender, String department, float gpa) {
        /* Update changed fields in a student by a given ID */
        // Note: if you only changed the age parameter, you can still pass all attributes
        //       and only the age parameter will change in the records

        Student record = getStudentById(id);
        if (record == null) {
            System.out.printf("[StudentDatabase]: Student attempting to update doesn't exist [ID] %d\n", id);
            return;
        }

        /* Change filters */
        if (!record.getName().equals(name)) record.setName(name);
        if (Student.validateAge(age) != null && record.getAge() != age) record.setAge(age);
        if (Student.validateGender(gender) != null && !record.getGender().equals(gender)) record.setGender(gender);
        if (!record.getDepartment().equals(department)) record.setDepartment(department);
        if (Student.validateGPA(gpa) != null && record.getGpa() != gpa) record.setGpa(gpa);

        /* Remove the old one */
        deleteStudent(id);
        /* Add the changed one */
        insertRecord(record);
    }
}
