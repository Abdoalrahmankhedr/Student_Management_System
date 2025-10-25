package pages;
import java.util.*;
import databases.StudentDatabase;
import models.Student;
import pages.components.Header;
import windows.MainWindow;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;

public class StudentsList extends JPanel {
    private static List<Student> ListOfAllStudents;
    private static List<Student> shownStudents;
    private static JComboBox<String> sortCombo;
    private static JComboBox<String> searchCombo;
    private static JTextField searchField;
    private static JPanel tablePanel = new JPanel();

    public StudentsList() {
        setBackground(new Color(240, 240, 240));
        setLayout(new BorderLayout());

        // Show A header with title "Students Lists" and back button
        Header header = new Header("Students Lists", true);
        header.setPreferredSize(new Dimension(1000, 50));
        header.backButton.addActionListener(e -> MainWindow.goTo("HomePage"));
        add(header, BorderLayout.NORTH);

        // Add panel for controls
        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBackground(new Color(240, 240, 240));

        controlPanel.setBorder(BorderFactory.createEmptyBorder(80, 40, 40, 40));

        // panel for searchfield
        searchField = new JTextField("Write Search Key...");
        GridBagConstraints gbc = new GridBagConstraints();

        //grid
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // ComboBox for search type
        searchCombo = new JComboBox<>(new String[]{
                "Search By Name",
                "Search By ID",
        });
        controlPanel.add(searchCombo, gbc);
        searchCombo.setPreferredSize(new Dimension(300, 35));
        searchCombo.setBackground(new Color(37, 99, 235));
        searchCombo.setForeground(Color.WHITE);
        searchCombo.setFont(new Font("Arial", Font.BOLD, 22));
        searchCombo.addActionListener(e -> {
            String selectedValue = (String) searchCombo.getSelectedItem();
            ChangeFilterOnSearch(selectedValue, searchField.getText());
        });

        gbc.gridx = 1;
        // ComboBox for Sort type
        sortCombo = new JComboBox<>(new String[]{
                "Sort by ID (Ascending)",
                "Sort by Name (Ascending)",
                "Sort by ID (Descending)",
                "Sort by Name (Descending)",
                "Sort by GPA (Descending)",
                "Sort by GPA (Ascending)",
        });
        controlPanel.add(sortCombo, gbc);
        sortCombo.setPreferredSize(new Dimension(300, 35));
        sortCombo.setBackground(new Color(37, 99, 235));
        sortCombo.setForeground(Color.WHITE);
        sortCombo.setFont(new Font("Arial", Font.BOLD, 22));
        sortCombo.addActionListener(e -> {
            String selectedValue = (String) sortCombo.getSelectedItem();
            ChangeFilterOnSort(selectedValue);
        });

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;


        gbc.insets = new Insets(5, 10, 5, 10);

        searchField.setPreferredSize(new Dimension(625, 35));
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setForeground(Color.GRAY);

        // if the user press enter it will call method ChangeFilterOnSearch
        searchField.addActionListener(e -> {
            String selectedValue = (String) searchCombo.getSelectedItem();
            ChangeFilterOnSearch(selectedValue, searchField.getText());
        });

        //This will make the text in the searchBox is PlaceHolder if user focus of the box it text will disappear
        //if the user focusOut the text will appear again it the box agian
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Write Search Key...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Write Search Key...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        controlPanel.add(searchField, gbc);
        add(controlPanel, BorderLayout.NORTH);


        tablePanel.setLayout(new BorderLayout());


        tablePanel.setBorder(BorderFactory.createEmptyBorder(80, 5, 5, 5));

        add(tablePanel, BorderLayout.CENTER);

        GetStudentsData();
    }

    public static void GetStudentsData() {
        System.out.println("The data of the Students is Loaded from the file to the show student list Page");
        StudentDatabase studentDatabase = new StudentDatabase("src/resources/students.txt");
        ListOfAllStudents = studentDatabase.getStudents();
        shownStudents = ListOfAllStudents;
        searchField.setText("Write Search Key...");
        searchField.setForeground(Color.GRAY);
        sortCombo.setSelectedItem("Sort by ID (Ascending)");
        searchCombo.setSelectedItem("Search By Name");
        ChangeFilterOnSort("Sort by ID (Ascending)");
        showDataTable(shownStudents);
    }

    // This function will be called if there is a change in searchCombo or in SearchField
    private static void ChangeFilterOnSearch(String searchBy, String searchKey) {
        System.out.println("You search with " + searchBy + " And the Search key is " + searchKey);
        searchKey = searchKey.trim();
        //This will check if user entered a search key or not
        if (!searchKey.equals("Write Search Key...") && !searchKey.isEmpty()) {
            shownStudents = new ArrayList<>();
            //Iterates over the listOfStudents and check if the searchKey is included in this record or not
            // if yes add it to shownStudents
            for (Student student : ListOfAllStudents) {

                if ((searchBy.equals("Search By Name") && student.getName().toLowerCase().contains(searchKey.toLowerCase()))
                        || (searchBy.equals("Search By ID") && Integer.toString(student.getId()).contains(searchKey))) {
                    shownStudents.add(student);
                }
            }
            showDataTable(shownStudents);
        } else {
            shownStudents = ListOfAllStudents;
            ChangeFilterOnSort((String) sortCombo.getSelectedItem());
            showDataTable(shownStudents);
        }
    }

    // This will be called if there change in selected type of sort
    private static void ChangeFilterOnSort(String sortKey) {
        System.out.println("You are sorting and the sorting type is :" + sortKey);
        //check the sortkey and sort it according to the type that user choose
        if (sortKey.equals("Sort by ID (Ascending)")) {
            shownStudents.sort(Comparator.comparing(Student::getId));
        } else if (sortKey.equals("Sort by ID (Descending)")) {
            shownStudents.sort(Comparator.comparing(Student::getId).reversed());
        } else if (sortKey.equals("Sort by Name (Ascending)")) {
            shownStudents.sort(Comparator.comparing(Student::getName));
        } else if (sortKey.equals("Sort by Name (Descending)")) {
            shownStudents.sort(Comparator.comparing(Student::getName).reversed());
        } else if (sortKey.equals("Sort by GPA (Ascending)")) {
            shownStudents.sort(Comparator.comparing(Student::getGpa));
        } else if (sortKey.equals("Sort by GPA (Descending)")) {
            shownStudents.sort(Comparator.comparing(Student::getGpa).reversed());
        }
        showDataTable(shownStudents);
    }

    private static void showDataTable(List<Student> showThisData) {
        tablePanel.removeAll();

        String[] columnNames = {"ID", "Name", "Age", "Gender", "Department", "GPA", "Actions"};
        Object[][] data = new Object[showThisData.size()][7];

        for (int i = 0; i < showThisData.size(); i++) {
            Student s = showThisData.get(i);
            data[i][0] = s.getId();
            data[i][1] = s.getName();
            data[i][2] = s.getAge();
            data[i][3] = s.getGender();
            data[i][4] = s.getDepartment();
            data[i][5] = s.getGpa();
            data[i][6] = "Actions";
        }

        JTable table = new JTable(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        table.setRowHeight(30);


        TableColumn actionsColumn = table.getColumn("Actions");
        actionsColumn.setPreferredWidth(180);
        actionsColumn.setMinWidth(180);


        table.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        table.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), table));

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.revalidate();
        tablePanel.repaint();
    }


    static class ButtonRenderer extends JPanel implements TableCellRenderer {
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            add(updateButton);
            add(deleteButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {

            if (isSelected) {
                this.setBackground(table.getSelectionBackground());
                this.updateButton.setForeground(table.getSelectionForeground());
                this.deleteButton.setForeground(table.getSelectionForeground());
            } else {
                this.setBackground(table.getBackground());
                this.updateButton.setForeground(Color.BLACK);
                this.deleteButton.setForeground(Color.BLACK);
            }
            return this;
        }
    }


    static class ButtonEditor extends DefaultCellEditor {
        protected JPanel panel = new JPanel();
        protected JButton updateButton = new JButton("Update");
        protected JButton deleteButton = new JButton("Delete");
        private JTable table;

        public ButtonEditor(JCheckBox checkBox, JTable table) {
            super(checkBox);
            this.table = table;
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.add(updateButton);
            panel.add(deleteButton);


            updateButton.addActionListener(e -> {
                int row = table.getEditingRow();
                Student s = shownStudents.get(row);
                MainWindow.goTo("UpdateStudent", s);
            });


            deleteButton.addActionListener(e -> {
                int row = table.getEditingRow();
                Student s = shownStudents.get(row);
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete " + s.getName() + "?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    StudentDatabase db = new StudentDatabase("src/resources/students.txt");
                    db.deleteStudent(s.getId());
                    db.saveToFile();
                    StudentsList.GetStudentsData();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                                                     int row, int column) {
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
}