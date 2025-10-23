import pages.*;
import windows.MainWindow;
public class Main {
    public static void main(String[] args) {
        /* Check usage of MainWindow */
        //Add the pages to the MainWindow
        MainWindow.addPage("StudentsList",new StudentsList());
        MainWindow.addPage("AddStudent",new AddStudent());
        MainWindow.addPage("HomePage",new HomePage());
        MainWindow.addPage("UpdateStudent",new UpdateStudent());
        /* Adds login page and loads it as the first page */
        MainWindow.addPage("login", new LoginPage());
        MainWindow.goTo("login");
        MainWindow.start();
    }
}