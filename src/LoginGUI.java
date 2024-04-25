import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class LoginGUI extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signUpButton;

    public LoginGUI() {
        setTitle("Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        panel.add(usernameLabel);

        usernameField = new JTextField();
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("Login");
        panel.add(loginButton);

        signUpButton = new JButton("Sign Up");
        panel.add(signUpButton);

        add(panel);

        // Add action listeners
        loginButton.addActionListener(this);
        signUpButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            // Login button clicked
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                // Establish connection to the database
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/project", "root", "@Akc15064");

                // Prepare SQL statement for querying user credentials
                String sql = "SELECT * FROM Signup WHERE Username=? AND Password=?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, username);
                statement.setString(2, password);

                // Execute the statement
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    // User found, login successful
                    JOptionPane.showMessageDialog(LoginGUI.this, "Login Successful!");
                    dispose(); // Close the login window
                    // Open the booking window or any other functionality you want to provide after login
                    BookingGUI bookingGUI = new BookingGUI();
                    bookingGUI.setVisible(true);
                } else {
                    // User not found or invalid credentials
                    JOptionPane.showMessageDialog(LoginGUI.this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }

                // Close resources
                resultSet.close();
                statement.close();
                connection.close();
            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(LoginGUI.this, "Error connecting to the database", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == signUpButton) {
            // Sign Up button clicked
            dispose(); // Close the login window
            SignUpGUI signUpGUI = new SignUpGUI();
            signUpGUI.setVisible(true); // Open the sign-up window
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginGUI loginGUI = new LoginGUI();
            loginGUI.setVisible(true);
        });
    }
}

class SignUpGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signUpButton;

    public SignUpGUI() {
        setTitle("Sign Up");
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        panel.add(usernameLabel);

        usernameField = new JTextField();
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        panel.add(passwordField);

        signUpButton = new JButton("Sign Up");
        panel.add(signUpButton);

        add(panel);

        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle sign up logic here
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                try {
                    // Establish connection to the database
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/project", "root", "@Akc15064");

                    // Prepare SQL statement for inserting new user
                    String sql = "INSERT INTO Signup (Username, Password) VALUES (?, ?)";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, username);
                    statement.setString(2, password);

                    // Execute the statement to insert new user into the database
                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(SignUpGUI.this, "Sign Up Successful!");
                        dispose(); // Close the sign-up window
                        new LoginGUI().setVisible(true); // Open the login window
                    } else {
                         }

                    // Close resources
                    statement.close();
                    connection.close();
                } catch (ClassNotFoundException | SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(SignUpGUI.this, "Error connecting to the database", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}

class BookingGUI extends JFrame {
    private JComboBox<String> movieComboBox;
    private JComboBox<String> dayComboBox;
    private JComboBox<String> timeComboBox;
    private JComboBox<String> locationComboBox;
    private JTextField seatField;
    private JButton bookButton;
    private JButton cancelButton;

    public BookingGUI() {
        setTitle("Booking");
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        JLabel locationLabel = new JLabel("Location:");
        panel.add(locationLabel);

        String[] location = {"1", "2", "3"};
        locationComboBox = new JComboBox<>(location);
        panel.add(locationComboBox);

        JLabel seatsLabel = new JLabel("Number of Seats:");
        panel.add(seatsLabel);

        seatField = new JTextField();
        panel.add(seatField);

        JLabel movieLabel = new JLabel("Movie:");
        panel.add(movieLabel);

        String[] movies = {"Movie 1", "Movie 2", "Movie 3"};
        movieComboBox = new JComboBox<>(movies);
        panel.add(movieComboBox);

        JLabel dayLabel = new JLabel("Day:");
        panel.add(dayLabel);

        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        dayComboBox = new JComboBox<>(days);
        panel.add(dayComboBox);

        JLabel timeLabel = new JLabel("Time:");
        panel.add(timeLabel);

        String[] time = {"08.00 am", "11.00 am", "2.00 pm", "4.00 pm", "10.00 pm"};
        timeComboBox = new JComboBox<>(time);
        panel.add(timeComboBox);

        bookButton = new JButton("Book");
        panel.add(bookButton);

        add(panel);

        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookAction();
            }
        });
    }

    private void bookAction() {
        // Booking logic
        String location = (String) locationComboBox.getSelectedItem();
        int seat = Integer.parseInt(seatField.getText());
        String movie = (String) movieComboBox.getSelectedItem();
        String day = (String) dayComboBox.getSelectedItem();
        String time = (String) timeComboBox.getSelectedItem();
        double price;

        try {
            seat = Integer.parseInt(seatField.getText()); // Parse seats from the field
            if (seat <= 0) {
                JOptionPane.showMessageDialog(BookingGUI.this, "Number of seats must be greater than 0.", "Invalid Seats", JOptionPane.ERROR_MESSAGE);
                return; // Stop processing booking if number of seats is invalid
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(BookingGUI.this, "Please enter a valid number of seats.", "Invalid Seats", JOptionPane.ERROR_MESSAGE);
            return; // Stop processing booking if number of seats is not a valid number
        }
        // Switch statement to determine price based on number of seats
           switch (seat) {
            case 1:
                price = 10.0; // Adjust the price based on your requirements
                break;
            case 2:
                price = 20.0;
                break;
            case 3:
                price = 30.0;
                break;
            default:
                price = 10.0 * seat; // price for each additional seat
                break;
        }

        //booking details
        JOptionPane.showMessageDialog(BookingGUI.this, "Booking detail:\nLocation: " + location +
                "\nNumber of Seats: " + seat +
                "\nMovie: " + movie +
                "\nDay: " + day +
                "\nTime: " + time +
                "\nPrice: " + price);

        JOptionPane.showMessageDialog(BookingGUI.this, "Booking Successful", "Success", JOptionPane.INFORMATION_MESSAGE);

        //cancel button added after booking details displayed
        bookButton.setEnabled(false); // disable booking button
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JPanel panel = (JPanel) getContentPane().getComponent(0); // Get the panel
        panel.add(cancelButton); // Add cancel button
        panel.revalidate(); // revalidate panel to reflect the changes
    }
}

    



