import java.util.Scanner;
import java.sql.*;
import java.util.*;


interface UserActions {
    void dashboard();
}



 abstract class User {
    protected String username;

    public User(String username) {
        this.username = username;
    }

    public abstract void showMenu();
}








 class Student extends User implements UserActions {

    private List<String> chapters = new ArrayList<>(Arrays.asList(
        "Java Basics", "OOP Concepts", "Data Structures"
    ));

    public Student(String username) {
        super(username);
    }

    @Override
    public void dashboard() {
        System.out.println("\n===== Student Dashboard =====");
        showMenu();
    }

    @Override
    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        int choice = 0;

        while (choice != 4) {
            System.out.println("\n1. Chapters");
            System.out.println("2. View Syllabus");
            System.out.println("3. View PPT");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> chooseChapter();
                case 2 -> viewSyllabus();
                case 3 -> viewPPT();
                case 4 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid Choice!");
            }
        }
    }

    private void chooseChapter() {
        System.out.println("\nAvailable Chapters:");
        for (int i = 0; i < chapters.size(); i++) {
            System.out.println((i + 1) + ". " + chapters.get(i));
        }
        System.out.println("Choose a chapter to study!");
    }

    private void viewSyllabus() {
        System.out.println("\nSyllabus:");
        for (String c : chapters) {
            System.out.println(" - " + c);
        }
    }

    private void viewPPT() {
        System.out.println("\nPPTs Available for Chapters:");
        for (String c : chapters) {
            System.out.println(" - " + c + ".ppt");
        }
    }
}








class Faculty extends User implements UserActions {

    private List<String> chapters = new ArrayList<>();
    private List<String> assignments = new ArrayList<>();

    public Faculty(String username) {
        super(username);
    }

    @Override
    public void dashboard() {
        System.out.println("\n===== Faculty Dashboard =====");
        showMenu();
    }

    @Override
    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        int choice = 0;

        while (choice != 6) {
            System.out.println("\n1. Add Chapter");
            System.out.println("2. Remove Chapter");
            System.out.println("3. Update Chapter");
            System.out.println("4. Add Assignment");
            System.out.println("5. Remove Assignment");
            System.out.println("6. Logout");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addChapter(sc);
                case 2 -> removeChapter(sc);
                case 3 -> updateChapter(sc);
                case 4 -> addAssignment(sc);
                case 5 -> removeAssignment(sc);
                case 6 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid Choice!");
            }
        }
    }

    private void addChapter(Scanner sc) {
        System.out.print("Enter Chapter Name to Add: ");
        String ch = sc.nextLine();
        chapters.add(ch);
        System.out.println("Chapter added successfully!");
    }

    private void removeChapter(Scanner sc) {
        System.out.print("Enter Chapter Name to Remove: ");
        String ch = sc.nextLine();
        if (chapters.remove(ch)) {
            System.out.println("Chapter removed successfully!");
        } else {
            System.out.println("Chapter not found!");
        }
    }

    private void updateChapter(Scanner sc) {
        System.out.print("Enter Chapter Name to Update: ");
        String oldCh = sc.nextLine();
        if (chapters.contains(oldCh)) {
            System.out.print("Enter New Chapter Name: ");
            String newCh = sc.nextLine();
            int index = chapters.indexOf(oldCh);
            chapters.set(index, newCh);
            System.out.println("Chapter updated successfully!");
        } else {
            System.out.println("Chapter not found!");
        }
    }

    private void addAssignment(Scanner sc) {
        System.out.print("Enter Assignment Name to Add: ");
        String as = sc.nextLine();
        assignments.add(as);
        System.out.println("Assignment added successfully!");
    }

    private void removeAssignment(Scanner sc) {
        System.out.print("Enter Assignment Name to Remove: ");
        String as = sc.nextLine();
        if (assignments.remove(as)) {
            System.out.println("Assignment removed successfully!");
        } else {
            System.out.println("Assignment not found!");
        }
    }
}




//main class
public class LMSLogin {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Username: ");
        String username = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        String url = "jdbc:mysql://localhost:3306/lmsdb";  // Replace with your DB name
        String user = "root";                               // MySQL username
        String pass = "xxxxxxxxx";                       // MySQL password

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pass);

            String sql = "SELECT role FROM users WHERE username=? AND password=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");

                if (role.equalsIgnoreCase("student")) {
                    System.out.println("✔ Login Successful (Student)");
                    System.out.println("Welcome Student: " + username);
                    // Student Dashboard
                    Student s = new Student(username);
                    s.dashboard();

                } 
                else if (role.equalsIgnoreCase("faculty")) {
                    System.out.println("✔ Login Successful (Faculty)");
                    System.out.println("Welcome Faculty: " + username);
                    //Faculty Dashboard
                    Faculty f = new Faculty(username);
                    f.dashboard();


                }
            } 
            else {
                System.out.println("❌ Invalid Username or Password");
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
