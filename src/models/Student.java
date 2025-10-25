package models;

// Andrew :)

/*
    Object representation for student record
    Has validation methods to ensure attributes are correct
*/

public class Student implements Model {
    private int id;
    private String name;
    private Integer age;
    private String gender;
    private String department;
    private Float gpa;

    public Student(int id, String name, int age, String gender, String department, float gpa) {
        this.name = name;
        this.age = validateAge(age);
        this.gender = validateGender(gender);
        this.department = department;
        this.gpa = validateGPA(gpa);
        this.id = id;
    }

    @Override
    public String lineRepresentation() {
        // Student record representation in file "{id},{name},{age},{gender},{department},{gpa}"
        return String.format(
                "%d,%s,%d,%s,%s,%.2f",
                this.id, this.name, this.age,
                this.gender, this.department,this.gpa
        );
    }

    /* Validations */
    public static String validateGender(String gender) {
        /* Returns input if valid, otherwise returns null */
        if (gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("m")) {
            return "male";
        } else if (gender.equalsIgnoreCase("female") || gender.equalsIgnoreCase("f")) {
            return "female";
        }

        return null;
    }

    public static Float validateGPA(float gpa) {
        /* Returns input if valid, otherwise returns null */
        if (gpa >= 0.0f && gpa <= 4.0f) return gpa;

        return null;
    }

    public static Integer validateAge(int age) {
        /* Returns input if valid, otherwise returns null */
        /* Checks if student has appropriate age for study */
        if (age >= 15 && age <= 100) return age;

        return null;
    }

    /* Getters & Setters */
    public String getName() {return this.name;}
    public int getId() {return this.id;}
    public Integer getAge() {return this.age;}
    public String getGender() {return this.gender;}
    public String getDepartment() {return this.department;}
    public Float getGpa() {return this.gpa;}

    public void setName(String name) {this.name = name;}
    public void setAge(int age) {this.age = validateAge(age);}
    public void setGender(String gender) {this.gender = validateGender(gender);}
    public void setDepartment(String department) {this.department = department;}
    public void setGpa(float gpa) {this.gpa = validateGPA(gpa);}
}
