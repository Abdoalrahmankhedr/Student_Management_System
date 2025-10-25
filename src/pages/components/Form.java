package pages.components;

import databases.StudentDatabase;
import models.Student;
import windows.MainWindow;

import javax.swing.*;
import java.awt.*;

public class Form extends JPanel {
    private final StudentDatabase db;

    private final JTextField idField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField departmentField = new JTextField();
    private final JTextField gpaField = new JTextField();
    private final JTextField ageField = new JTextField();
    private final JComboBox<String> genderCombo =
            new JComboBox<>(new String[]{"", "male", "female"});

    private final JButton actionButton = new JButton("Add+");
    private boolean isUpdateMode = false;

    public Form(StudentDatabase db) {
        this.db = db;
        setLayout(new BorderLayout());
        setOpaque(false);

        initUI();
        attachBehavior();
    }

    private void initUI() {
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 6, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        gbc.gridy = 0;
        content.add(new JLabel("Student_ID"), gbc);
        gbc.gridy = 1;
        idField.setPreferredSize(new Dimension(520, 36));
        content.add(idField, gbc);

        gbc.gridy = 2;
        content.add(new JLabel("Full Name"), gbc);
        gbc.gridy = 3;
        nameField.setPreferredSize(new Dimension(520, 36));
        content.add(nameField, gbc);

        gbc.gridy = 4;
        content.add(new JLabel("Department"), gbc);
        gbc.gridy = 5;
        departmentField.setPreferredSize(new Dimension(520, 36));
        content.add(departmentField, gbc);

        gbc.gridy = 6;
        JPanel rowLabels = new JPanel(new GridLayout(1, 3, 10, 0));
        rowLabels.setOpaque(false);
        rowLabels.add(centerLabel("GPA"));
        rowLabels.add(centerLabel("Age"));
        rowLabels.add(centerLabel("Gender"));
        content.add(rowLabels, gbc);

        gbc.gridy = 7;
        JPanel rowFields = new JPanel(new GridLayout(1, 3, 10, 0));
        rowFields.setOpaque(false);
        rowFields.add(gpaField);
        rowFields.add(ageField);
        rowFields.add(genderCombo);
        content.add(rowFields, gbc);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);
        actionButton.setPreferredSize(new Dimension(520, 44));
        actionButton.setFont(actionButton.getFont().deriveFont(Font.BOLD, 18f));
        actionButton.setBackground(new Color(25, 118, 210));
        actionButton.setForeground(Color.WHITE);
        actionButton.setFocusPainted(false);
        actionButton.setBorderPainted(false);
        actionButton.setOpaque(true);
        bottom.add(actionButton);

        add(content, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private JLabel centerLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(160, 20));
        return label;
    }

    private void attachBehavior() {
        actionButton.addActionListener(e -> handleFormSubmit());
    }

    private void handleFormSubmit() {
        String idText = idField.getText().trim();
        String name = nameField.getText().trim();
        String dept = departmentField.getText().trim();
        String gender = (String) genderCombo.getSelectedItem();
        String ageText = ageField.getText().trim();
        String gpaText = gpaField.getText().trim();


        if (name.isEmpty() || ageText.isEmpty() || gender == null || gender.isEmpty()
                || dept.isEmpty() || gpaText.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }


        if (!name.matches("^[A-Za-z ]+$")) {
            showError("Name must contain only letters and spaces.");
            return;
        }


        if (!dept.matches("^[A-Za-z ]+$")) {
            showError("Department must contain only letters and spaces.");
            return;
        }

        int age;
        float gpa;

        try {
            age = Integer.parseInt(ageText);
            gpa = Float.parseFloat(gpaText);
        } catch (NumberFormatException ex) {
            showError("Age and GPA must be valid numbers.");
            return;
        }


        if (age < 15 || age > 100) {
            showError("Age must be between 15 and 100.");
            return;
        }


        if (gpa < 0.0 || gpa > 4.0) {
            showError("GPA must be between 0.0 and 4.0.");
            return;
        }


        name = capitalizeWords(name);
        dept = capitalizeWords(dept);


        if (isUpdateMode) {
            int id = Integer.parseInt(idText);
            db.updateStudent(id, name, age, gender.toLowerCase(), dept, gpa);
            db.saveToFile();
            JOptionPane.showMessageDialog(this, "Student updated successfully.");
            MainWindow.goTo("HomePage");
            return;
        }


        if (idText.isEmpty()) {
            db.addStudent(name, age, gender.toLowerCase(), dept, gpa);
        } else {
            try {
                int id = Integer.parseInt(idText);
                if (db.contains(id)) {
                    showError("A student with this ID already exists.");
                    return;
                }
                db.addStudent(id, name, age, gender.toLowerCase(), dept, gpa);
            } catch (NumberFormatException ex) {
                showError("Student ID must be a number.");
                return;
            }
        }

        db.saveToFile();
        JOptionPane.showMessageDialog(this, "Student added successfully.");

        clearForm();
        MainWindow.goTo("HomePage");
    }


    private String capitalizeWords(String text) {
        String[] words = text.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (w.isEmpty()) continue;
            sb.append(Character.toUpperCase(w.charAt(0)))
                    .append(w.substring(1))
                    .append(" ");
        }
        return sb.toString().trim();
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        departmentField.setText("");
        gpaField.setText("");
        ageField.setText("");
        genderCombo.setSelectedIndex(0);
        isUpdateMode = false;
        actionButton.setText("Add+");
        idField.setEditable(true);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void setUpdateMode(Student student) {
        isUpdateMode = true;
        actionButton.setText("Update");
        idField.setEditable(false);

        idField.setText(String.valueOf(student.getId()));
        nameField.setText(student.getName());
        ageField.setText(String.valueOf(student.getAge()));
        gpaField.setText(String.valueOf(student.getGpa()));
        departmentField.setText(student.getDepartment());
        genderCombo.setSelectedItem(student.getGender());
    }
}
