package pages;

import databases.StudentDatabase;
import models.Student;
import pages.components.Header;
import windows.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class UpdateStudent extends JPanel {

    private Student studentToUpdate;

    private JTextField nameField, ageField, departmentField, gpaField;
    private JComboBox<String> genderCombo;
    private JLabel studentIdLabel;

    public UpdateStudent(Student student) {
        this.studentToUpdate = student;

        setBackground(new Color(225, 225, 225));
        setLayout(new BorderLayout());


        Header header = new Header("Update Student", true);
        header.setPreferredSize(new Dimension(800, 50));
        header.backButton.addActionListener(e -> MainWindow.goTo("StudentsList"));
        add(header, BorderLayout.NORTH);


        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(225, 225, 225));


        setupFormComponents();
        loadStudentData(studentToUpdate);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;


        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        formPanel.add(studentIdLabel, gbc);


        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);
        row++;


        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        formPanel.add(ageField, gbc);
        row++;


        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        formPanel.add(genderCombo, gbc);
        row++;


        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        formPanel.add(departmentField, gbc);
        row++;


        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("GPA (0.0 - 4.0):"), gbc);
        gbc.gridx = 1;
        formPanel.add(gpaField, gbc);
        row++;


        JButton saveButton = new JButton("Save Changes");
        saveButton.setFont(new Font("Arial", Font.BOLD, 20));
        saveButton.setBackground(new Color(37, 99, 235));
        saveButton.setForeground(Color.WHITE);


        saveButton.addActionListener(e -> updateStudentAction());

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        formPanel.add(saveButton, gbc);

        add(formPanel, BorderLayout.CENTER);
    }


    private void setupFormComponents() {

        studentIdLabel = new JLabel("Updating Student ID: " + studentToUpdate.getId());
        studentIdLabel.setFont(new Font("Arial", Font.BOLD, 20));
        studentIdLabel.setForeground(new Color(239, 68, 68));
        studentIdLabel.setHorizontalAlignment(SwingConstants.CENTER);

        nameField = new JTextField();
        ageField = new JTextField();
        departmentField = new JTextField();
        gpaField = new JTextField();

        genderCombo = new JComboBox<>(new String[]{"male", "female"});

        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        Dimension fieldSize = new Dimension(200, 30);

        JTextField[] fields = {nameField, ageField, departmentField, gpaField};
        for (JTextField field : fields) {
            field.setFont(fieldFont);
            field.setPreferredSize(fieldSize);
        }
        genderCombo.setFont(fieldFont);
        genderCombo.setPreferredSize(fieldSize);
    }


    private void loadStudentData(Student student) {
        if (student != null) {
            nameField.setText(student.getName());
            ageField.setText(String.valueOf(student.getAge()));
            departmentField.setText(student.getDepartment());
            gpaField.setText(String.format("%.2f", student.getGpa()));
            genderCombo.setSelectedItem(student.getGender());
        } else {
            JOptionPane.showMessageDialog(this, "No student data provided for update.", "Error", JOptionPane.ERROR_MESSAGE);
            MainWindow.goTo("StudentsList");
        }
    }


    private static boolean isValidName(String name) {

        return name.matches("^[\\p{L}\\s]+$");
    }

    private void updateStudentAction() {

        int id = studentToUpdate.getId();
        String name = nameField.getText().trim();
        String ageStr = ageField.getText().trim();
        String gender = (String) genderCombo.getSelectedItem();
        String department = departmentField.getText().trim();
        String gpaStr = gpaField.getText().trim();


        if (name.isEmpty() || ageStr.isEmpty() || department.isEmpty() || gpaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields (Name, Age, Department, GPA).", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        if (!isValidName(name)) {
            JOptionPane.showMessageDialog(this, "Invalid name. The name must only contain letters and spaces.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        if (!isValidName(department)) {
            JOptionPane.showMessageDialog(this, "Invalid department name. The department name must only contain letters and spaces.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {

            int age = Integer.parseInt(ageStr);
            float gpa = Float.parseFloat(gpaStr);


            if (Student.validateAge(age) == null) {
                JOptionPane.showMessageDialog(this, "Invalid age. Age must be between 16 and 30.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            if (Student.validateGPA(gpa) == null) {
                JOptionPane.showMessageDialog(this, "Invalid GPA. GPA must be between 0.0 and 4.0.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            StudentDatabase db = new StudentDatabase("src/resources/students.txt");
            db.updateStudent(id, name, age, gender, department, gpa);
            db.saveToFile();

            JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            MainWindow.goTo("StudentsList");

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this, "Age and GPA must be valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    }
