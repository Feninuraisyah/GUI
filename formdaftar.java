
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class formdaftar extends JFrame {
    // Database connection variables
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/pemlangui";
    private static final String USER = "root";
    private static final String PASSWORD = "123";

    // Form components
    private JTextField nameField;
    private JTextField addressField;
    private JButton submitButton;
    private JTextArea displayArea;

    public formdaftar() {
        // Setup form
        setTitle("Registration Form");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Add form components
        add(new JLabel("Nama:"));
        nameField = new JTextField(20);
        add(nameField);

        add(new JLabel("Alamat:"));
        addressField = new JTextField(20);
        add(addressField);

        submitButton = new JButton("Submit");
        add(submitButton);

        displayArea = new JTextArea(10, 30);
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea));

        // Button action
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String address = addressField.getText();
                if (name.isEmpty() || address.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    saveToDatabase(name, address);
                    displayData();
                }
            }
        });

        // Initial display of data
        displayData();
    }

    private void saveToDatabase(String name, String address) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "INSERT INTO pendaftaran (nama, alamat) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setString(2, address);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayData() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM pendaftaran";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                displayArea.setText("");
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("nama");
                    String address = rs.getString("alamat");
                    displayArea.append("ID: " + id + ", Nama: " + name + ", Alamat: " + address + "\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Load MySQL JDBC Driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // Create and show the form
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new formdaftar().setVisible(true);
            }
        });
    }
}
