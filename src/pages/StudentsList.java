package pages;
import java.util.*;
import databases.StudentDatabase;
import models.Student;
import pages.components.Header;
import windows.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

public class StudentsList extends JPanel {
    private static List<Student> ListOfAllStudents;
    private static List<Student> shownStudents;
    private static JComboBox<String> sortCombo;
    private static JComboBox<String> searchCombo;
    private static JTextField searchField;
    public StudentsList() {
        setBackground(new Color(240, 240, 240));
        setLayout(new BorderLayout());
        //Show A header with title "Students Lists" and back button
        Header header = new Header("Students Lists", true);
        header.setPreferredSize(new Dimension(1000, 50));
        header.backButton.addActionListener(e-> MainWindow.goTo("HomePage"));
        add(header, BorderLayout.NORTH);
        //Add panel for controls
        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBackground(new Color(240, 240, 240));
        //panel for searchfield
        searchField = new JTextField("Write Search Key...");
        //grid
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        //ComBox for search type
        searchCombo = new JComboBox<>(new String[]{
                "Search By Name",
                "Search By ID",
        });
        controlPanel.add(searchCombo, gbc);
        searchCombo.setPreferredSize(new Dimension(300, 35));
        searchCombo.setBackground(new Color(37,99,235));
        searchCombo.setForeground(Color.WHITE);
        searchCombo.setFont(new Font("Arial", Font.BOLD, 22));
        searchCombo.addActionListener(e -> {
            String selectedValue = (String) searchCombo.getSelectedItem();
            ChangeFilterOnSearch(selectedValue,searchField.getText());
        });
        gbc.gridx = 1;
        //ComBox for Sort type
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
        sortCombo.setBackground(new Color(37,99,235));
        sortCombo.setForeground(Color.WHITE);
        sortCombo.setFont(new Font("Arial", Font.BOLD, 22));
        sortCombo.addActionListener(e -> {
            String selectedValue = (String) sortCombo.getSelectedItem();
            ChangeFilterOnSort(selectedValue);
        });

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;

        searchField.setPreferredSize(new Dimension(625, 35));
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setForeground(Color.GRAY);
        //if the user press enter it will call method ChangeFilterOnSearch
        searchField.addActionListener(e -> {
            String selectedValue = (String) searchCombo.getSelectedItem();
            ChangeFilterOnSearch(selectedValue,searchField.getText());
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
        add(controlPanel, BorderLayout.CENTER);
        GetStudentsData();
    }
    public static void GetStudentsData(){
        System.out.println("The data of the Students is Loaded from the file to teh show student list Page");
        StudentDatabase studentDatabase=new StudentDatabase("src/resources/students.txt");
        ListOfAllStudents = studentDatabase.getStudents();
        shownStudents=ListOfAllStudents;
        searchField.setText("Write Search Key...");
        searchField.setForeground(Color.GRAY);
        sortCombo.setSelectedItem("Sort by ID (Ascending)");
        searchCombo.setSelectedItem("Search By Name");
        ChangeFilterOnSort("Sort by ID (Ascending)");
        showDataTable(shownStudents);
    }
    //This function will be called if there is a change in searchComBox or in SearchField
    private static void ChangeFilterOnSearch(String searchBy,String searchKey){
        System.out.println("You search with"+searchBy +"And the Searchkey is "+ searchKey);
        searchKey=searchKey.trim();
        //This will check if user entered a search key or not
        if (!searchKey.equals("Write Search Key...") && !searchKey.isEmpty()) {
            shownStudents = new ArrayList<>();
            //Iterates over the listOfStudents and check if the searchKey is included in this record or not
            // if yes add it to shownStudents
            for(Student student:ListOfAllStudents){
                if(searchBy.equals("Search By Name")&&student.getName().contains(searchKey)
                        ||searchBy.equals("Search By ID")&&Integer.toString(student.getId()).contains(searchKey)){
                    shownStudents.add(student);
                }
            }
            showDataTable(shownStudents);
        }
        else{
            shownStudents=ListOfAllStudents;
            ChangeFilterOnSort((String) sortCombo.getSelectedItem());
            showDataTable(shownStudents);
        }
    }
    //This will be called if there change in selected type of sort
    private static void ChangeFilterOnSort(String sortKey){
        System.out.println("You are sorting and the sorting type is :"+sortKey);
        //check the sortkey and sort it according to the type that user choose
        if(sortKey.equals("Sort by ID (Ascending)")){
            shownStudents.sort(Comparator.comparing(Student::getId));
        } else if(sortKey.equals("Sort by ID (Descending)")){
            shownStudents.sort(Comparator.comparing(Student::getId).reversed());
        } else if(sortKey.equals("Sort by Name (Ascending)")){
            shownStudents.sort(Comparator.comparing(Student::getName));
        } else if(sortKey.equals("Sort by Name (Descending)")){
            shownStudents.sort(Comparator.comparing(Student::getName).reversed());
        } else if(sortKey.equals("Sort by GPA (Ascending)")){
            shownStudents.sort(Comparator.comparing(Student::getGpa));
        } else if(sortKey.equals("Sort by GPA (Descending)")){
            shownStudents.sort(Comparator.comparing(Student::getGpa).reversed());
        }
        showDataTable(shownStudents);
    }
    private static void showDataTable(List<Student> showThisData){
        //construct the table and show this data it
    }
}