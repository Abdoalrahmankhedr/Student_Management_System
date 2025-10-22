import pages.LoginPage;

public class Main {
    public static void main(String[] args) {
        /* Check usage of MainWindow */
        /* Adds login page and loads it as the first page */
        MainWindow.addPage("login", new LoginPage());
        MainWindow.goTo("login");
        MainWindow.start();
    }
}