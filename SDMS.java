
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class SDMS {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentDB";
    private static final String USER = "root";
    private static final String PASS = "";

    public static void main(String[] args) {
        // Set up GUI
        JFrame frame = new JFrame("Student Database Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        
        // Components
        JPanel panel = new JPanel(new GridLayout(5, 2));
        JLabel firstNameLabel = new JLabel("First Name:");
        JTextField firstNameField = new JTextField();
        JLabel lastNameLabel = new JLabel("Last Name:");
        JTextField lastNameField = new JTextField();
        JLabel ageLabel = new JLabel("Age:");
        JTextField ageField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        
        JButton addButton = new JButton("Add Student");
        JButton viewButton = new JButton("View Students");
        JButton searchButton = new JButton("Search by ID");
        
        JTextField searchField = new JTextField();
        JTextArea outputArea = new JTextArea();
        JTable table = new JTable(new DefaultTableModel(new Object[]{"ID", "First Name", "Last Name", "Age", "Email"}, 0));
        
        JScrollPane tableScrollPane = new JScrollPane(table);
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        
        // Add components to panel
        panel.add(firstNameLabel);
        panel.add(firstNameField);
        panel.add(lastNameLabel);
        panel.add(lastNameField);
        panel.add(ageLabel);
        panel.add(ageField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(addButton);
        panel.add(viewButton);
        panel.add(searchButton);
        panel.add(searchField);
        
        frame.add(panel, BorderLayout.NORTH);
        frame.add(tableScrollPane, BorderLayout.CENTER);
        frame.add(outputScrollPane, BorderLayout.SOUTH);
        
        // Button Listeners
        addButton.addActionListener(e -> addStudent(firstNameField.getText(), lastNameField.getText(), ageField.getText(), emailField.getText(), outputArea));
        viewButton.addActionListener(e -> viewStudents((DefaultTableModel) table.getModel(), outputArea));
        searchButton.addActionListener(e -> searchStudentById(searchField.getText(), outputArea));
        
        frame.setVisible(true);
    }

    private static void addStudent(String firstName, String lastName, String age, String email, JTextArea outputArea) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO students (first_name, last_name, age, email) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setInt(3, Integer.parseInt(age));
            stmt.setString(4, email);
            stmt.executeUpdate();
            outputArea.setText("Student added successfully!");
        } catch (SQLException | NumberFormatException ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }

    private static void viewStudents(DefaultTableModel model, JTextArea outputArea) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM students")) {
            model.setRowCount(0); // Clear the table
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("first_name"));
                row.add(rs.getString("last_name"));
                row.add(rs.getInt("age"));
                row.add(rs.getString("email"));
                model.addRow(row);
            }
        } catch (SQLException ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }

    private static void searchStudentById(String id, JTextArea outputArea) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM students WHERE id = ?")) {
            stmt.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    outputArea.setText("ID: " + rs.getInt("id") + "\n" +
                            "First Name: " + rs.getString("first_name") + "\n" +
                            "Last Name: " + rs.getString("last_name") + "\n" +
                            "Age: " + rs.getInt("age") + "\n" +
                            "Email: " + rs.getString("email"));
                } else {
                    outputArea.setText("No student found with ID " + id);
                }
            }
        } catch (SQLException | NumberFormatException ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }
}
