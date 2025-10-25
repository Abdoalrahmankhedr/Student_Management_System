package pages.components;

import databases.StudentDatabase;
import models.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Reusable form panel for adding/updating students.
 *
 * Constructor: Form(StudentDatabase db, Student initial)
 *  - if initial == null -> Add mode (button "Add+")
 *  - if initial != null -> Update mode (button "Update", fields prefilled)
 */
public class Form extends JPanel {
    private final StudentDatabase db;

    private final JTextField idField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField departmentField = new JTextField();
    private final JTextField gpaField = new JTextField();
    private final JTextField ageField = new JTextField();
    private final JComboBox<String> genderCombo = new JComboBox<>(new String[] {"", "male", "female"});

    private final JButton actionButton = new JButton();

    /**
     * Construct the form.
     * @param db student database to operate on
     * @param initial if null -> add mode, else -> update mode and fields filled from this student
     */
    public Form(StudentDatabase db, Student initial) {
        this.db = db;
        setLayout(new BorderLayout());
        setOpaque(false);

        initUI();
        attachBehavior();

        // Initialize based on provided Student object
        if (initial == null) {
            actionButton.setText("Add+");
            clearFields();
            idField.setEditable(true);
        } else {
            actionButton.setText("Update");
            populateFromStudent(initial);
            // Prevent changing ID on update to keep uniqueness/simplicity
            idField.setEditable(false);
        }
    }

    private void initUI() {
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        content.setBackground(new Color(0,0,0,0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 6, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Student_ID label + field
        gbc.gridy = 0;
        content.add(new JLabel("Student_ID"), gbc);

        gbc.gridy = 1;
        idField.setPreferredSize(new Dimension(520, 36));
        content.add(idField, gbc);

        // Full Name
        gbc.gridy = 2;
        content.add(new JLabel("Full Name"), gbc);

        gbc.gridy = 3;
        nameField.setPreferredSize(new Dimension(520, 36));
        content.add(nameField, gbc);

        // Department
        gbc.gridy = 4;
        content.add(new JLabel("Department"), gbc);

        gbc.gridy = 5;
        departmentField.setPreferredSize(new Dimension(520, 36));
        content.add(departmentField, gbc);

        // Labels row (GPA / Age / Gender)
        gbc.gridy = 6;
        JPanel rowLabels = new JPanel(new GridLayout(1,3,10,0));
        rowLabels.setOpaque(false);
        rowLabels.add(centerLabel("GPA"));
        rowLabels.add(centerLabel("Age"));
        rowLabels.add(centerLabel("Gender"));
        content.add(rowLabels, gbc);

        // Fields row (GPA / Age / Gender)
        gbc.gridy = 7;
        JPanel rowFields = new JPanel(new GridLayout(1,3,10,0));
        rowFields.setOpaque(false);

        gpaField.setPreferredSize(new Dimension(160,36));
        ageField.setPreferredSize(new Dimension(160,36));
        genderCombo.setPreferredSize(new Dimension(160,36));

        JPanel gpaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); gpaPanel.setOpaque(false); gpaPanel.add(gpaField);
        JPanel agePanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); agePanel.setOpaque(false); agePanel.add(ageField);
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); genderPanel.setOpaque(false); genderPanel.add(genderCombo);

        rowFields.add(gpaPanel);
        rowFields.add(agePanel);
        rowFields.add(genderPanel);

        content.add(rowFields, gbc);

        // Action button (Add+ / Update)
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);
        actionButton.setPreferredSize(new Dimension(520, 44));
        actionButton.setFont(actionButton.getFont().deriveFont(Font.BOLD, 18f));
        bottom.add(actionButton);

        add(content, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private JLabel centerLabel(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setPreferredSize(new Dimension(160, 20));
        return l;
    }

    private void attachBehavior() {
        // When id field loses focus check DB for existence to toggle mode/populate fields
        idField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                checkIdAndToggle();
            }
        });

        // Action button handler: add or update depending on state
        actionButton.addActionListener(e -> {
            String idText = idField.getText().trim();
            if (idText.isEmpty()) {
                showError("Student ID is required.");
                return;
            }

            int id;
            try {
                id = Integer.parseInt(idText);
            } catch (NumberFormatException ex) {
                showError("Student ID must be a number.");
                return;
            }

            // read and validate other fields
            String name = nameField.getText().trim();
            String dept = departmentField.getText().trim();
            String gender = (String) genderCombo.getSelectedItem();
            String ageText = ageField.getText().trim();
            String gpaText = gpaField.getText().trim();

            if (name.isEmpty() || dept.isEmpty() || gender == null || gender.isEmpty()
                    || ageText.isEmpty() || gpaText.isEmpty()) {
                showError("Please complete all fields.");
                return;
            }

            int age;
            float gpa;
            try {
                age = Integer.parseInt(ageText);
            } catch (NumberFormatException ex) {
                showError("Age must be an integer.");
                return;
            }

            try {
                gpa = Float.parseFloat(gpaText);
            } catch (NumberFormatException ex) {
                showError("GPA must be a number (e.g. 3.5).");
                return;
            }

            // validate using Student helpers
            if (Student.validateAge(age) == null) {
                showError("Age must be between 16 and 30.");
                return;
            }

            if (Student.validateGender(gender) == null) {
                showError("Gender must be male or female.");
                return;
            }

            if (Student.validateGPA(gpa) == null) {
                showError("GPA must be between 0.0 and 4.0.");
                return;
            }

            boolean exists = db.contains(id);
            if (exists) {
                // Update flow
                db.updateStudent(id, name, age, gender.toLowerCase(), dept, gpa);
                db.saveToFile(); // persist changes to file (required by requirements)
                JOptionPane.showMessageDialog(this, "Student updated successfully.");
                actionButton.setText("Update");
                // keep ID uneditable after update
                idField.setEditable(false);
            } else {
                // Add flow
                // If user entered an ID manually, we use it; StudentDatabase.addStudent will avoid duplicates
                db.addStudent(id, name, age, gender.toLowerCase(), dept, gpa);
                db.saveToFile(); // persist
                JOptionPane.showMessageDialog(this, "Student added successfully.");
                actionButton.setText("Update"); // switch to update after add
                idField.setEditable(false); // lock ID now that record exists
            }
        });
    }

    private void checkIdAndToggle() {
        String idText = idField.getText().trim();
        if (idText.isEmpty()) {
            actionButton.setText("Add+");
            clearFieldsExceptId();
            idField.setEditable(true);
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            if (db.contains(id)) {
                Student s = db.getStudentById(id);
                if (s != null) {
                    populateFromStudent(s);
                    actionButton.setText("Update");
                    idField.setEditable(false);
                } else {
                    actionButton.setText("Add+");
                    idField.setEditable(true);
                }
            } else {
                clearFieldsExceptId();
                actionButton.setText("Add+");
                idField.setEditable(true);
            }
        } catch (NumberFormatException ex) {
            actionButton.setText("Add+");
            idField.setEditable(true);
        }
    }

    private void populateFromStudent(Student s) {
        if (s == null) return;
        idField.setText(String.valueOf(s.getId()));
        nameField.setText(s.getName() == null ? "" : s.getName());
        departmentField.setText(s.getDepartment() == null ? "" : s.getDepartment());
        ageField.setText(s.getAge() == null ? "" : s.getAge().toString());
        gpaField.setText(s.getGpa() == null ? "" : String.format("%.2f", s.getGpa()));
        genderCombo.setSelectedItem(s.getGender() == null ? "" : s.getGender());
    }

    private void clearFieldsExceptId() {
        nameField.setText("");
        departmentField.setText("");
        ageField.setText("");
        gpaField.setText("");
        genderCombo.setSelectedIndex(0);
    }

    private void clearFields() {
        idField.setText("");
        clearFieldsExceptId();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation error", JOptionPane.ERROR_MESSAGE);
    }
}
