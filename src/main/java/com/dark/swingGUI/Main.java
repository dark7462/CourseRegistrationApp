package com.dark.swingGUI;

import com.dark.entity.Course;
import com.dark.entity.Student;
import com.dark.entity.Teacher;
import com.dark.service.LoginService;
import com.dark.service.StudentService;
import com.dark.service.TeacherService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;


public class Main extends JFrame {

    // --- SERVICES ---
    private final LoginService loginService = new LoginService();
    private final StudentService studentService = new StudentService();
    private final TeacherService teacherService = new TeacherService();

    // --- LAYOUT & PANELS ---
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);

    // --- KEYS ---
    private static final String LOGIN_PANEL = "LOGIN";
    private static final String STUDENT_PANEL = "STUDENT";
    private static final String TEACHER_PANEL = "TEACHER";

    // --- THEME COLORS ---
    private static final Color PRIMARY_COLOR = new Color(44, 62, 80);    // Dark Navy
    private static final Color ACCENT_COLOR  = new Color(52, 152, 219);  // Bright Blue
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);  // Emerald Green
    private static final Color DANGER_COLOR  = new Color(231, 76, 60);   // Alizarin Red
    private static final Color TEXT_COLOR    = Color.WHITE;
    private static final Font HEADER_FONT    = new Font("Segue UI", Font.BOLD, 24);
    private static final Font LABEL_FONT     = new Font("Segue UI", Font.PLAIN, 14);

    // --- PANEL INSTANCES ---
    private final StudentPanel studentPanel;
    private final TeacherPanel teacherPanel;

    public Main() {
        setTitle("University Course Registration System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize Panels
        LoginPanel loginPanel = new LoginPanel();
        studentPanel = new StudentPanel();
        teacherPanel = new TeacherPanel();

        // Add to Card Stack
        mainPanel.add(loginPanel, LOGIN_PANEL);
        mainPanel.add(studentPanel, STUDENT_PANEL);
        mainPanel.add(teacherPanel, TEACHER_PANEL);

        add(mainPanel);
    }

    // ==========================================
    // 1. LOGIN PANEL (STYLED)
    // ==========================================
    class LoginPanel extends JPanel {
        JTextField userField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Student", "Teacher"});
        JButton loginBtn = new JButton("Login");

        public LoginPanel() {
            setLayout(new GridBagLayout());
            setBackground(PRIMARY_COLOR); // Dark Background

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // --- HEADER ---
            JLabel title = new JLabel("University Portal");
            title.setFont(HEADER_FONT);
            title.setForeground(TEXT_COLOR);
            title.setHorizontalAlignment(SwingConstants.CENTER);

            // Icon (Simple text fallback if no image)
            JLabel iconLabel = new JLabel("🎓", SwingConstants.CENTER);
            iconLabel.setFont(new Font("Segue UI Emoji", Font.PLAIN, 60));
            iconLabel.setForeground(Color.ORANGE);

            JPanel container = new JPanel(new GridBagLayout());
            container.setBackground(Color.WHITE);
            container.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(Color.GRAY, 1),
                    new EmptyBorder(30, 50, 30, 50)
            ));

            // Add Components to White Box
            GridBagConstraints boxGbc = new GridBagConstraints();
            boxGbc.insets = new Insets(5, 5, 5, 5);
            boxGbc.gridx = 0; boxGbc.gridy = 0; boxGbc.gridwidth = 2;
            container.add(iconLabel, boxGbc);

            boxGbc.gridy++;
            JLabel subTitle = new JLabel("Course Registration");
            subTitle.setFont(new Font("Segue UI", Font.BOLD, 18));
            subTitle.setForeground(PRIMARY_COLOR);
            container.add(subTitle, boxGbc);

            boxGbc.gridy++; boxGbc.gridwidth = 1;
            container.add(new JLabel("User ID:"), boxGbc);
            boxGbc.gridx = 1; container.add(userField, boxGbc);

            boxGbc.gridy++; boxGbc.gridx = 0;
            container.add(new JLabel("Password:"), boxGbc);
            boxGbc.gridx = 1; container.add(passField, boxGbc);

            boxGbc.gridy++; boxGbc.gridx = 0;
            container.add(new JLabel("Role:"), boxGbc);
            boxGbc.gridx = 1; container.add(roleCombo, boxGbc);

            boxGbc.gridy++; boxGbc.gridx = 0; boxGbc.gridwidth = 2;
            styleButton(loginBtn, ACCENT_COLOR);
            container.add(loginBtn, boxGbc);

            add(container); // Add white box to dark background

            // Logic
            loginBtn.addActionListener(e -> performLogin());
        }

        private void performLogin() {
            String id = userField.getText();
            String pass = new String(passField.getPassword());
            String role = (String) roleCombo.getSelectedItem();

            if ("Student".equals(role)) {
                Student s = loginService.loginStudent(id, pass);
                if (s != null) {
                    studentPanel.loadData(s);
                    cardLayout.show(mainPanel, STUDENT_PANEL);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Student Credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                Teacher t = loginService.loginTeacher(id, pass);
                if (t != null) {
                    teacherPanel.loadData(t);
                    cardLayout.show(mainPanel, TEACHER_PANEL);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Admin Credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // ==========================================
    // 2. STUDENT PANEL (STYLED)
    // ==========================================
    class StudentPanel extends JPanel {
        private final JLabel nameLbl = new JLabel();
        private final JLabel rollLbl = new JLabel();
        private final DefaultListModel<Course> availableModel = new DefaultListModel<>();
        private final DefaultListModel<Course> myModel = new DefaultListModel<>();
        private final JList<Course> availableCoursesList = new JList<>(availableModel);
        private final JList<Course> myCoursesList = new JList<>(myModel);

        private Student currentStudent;

        public StudentPanel() {
            setLayout(new BorderLayout());

            // --- HEADER ---
            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(PRIMARY_COLOR);
            header.setBorder(new EmptyBorder(15, 20, 15, 20));

            nameLbl.setFont(new Font("Segue UI", Font.BOLD, 20));
            nameLbl.setForeground(TEXT_COLOR);
            rollLbl.setFont(LABEL_FONT);
            rollLbl.setForeground(new Color(200, 200, 200));

            JPanel textPanel = new JPanel(new GridLayout(2, 1));
            textPanel.setOpaque(false);
            textPanel.add(nameLbl);
            textPanel.add(rollLbl);

            JLabel avatar = new JLabel("👤");
            avatar.setFont(new Font("Segue UI Emoji", Font.PLAIN, 40));
            avatar.setForeground(Color.WHITE);

            header.add(textPanel, BorderLayout.CENTER);
            header.add(avatar, BorderLayout.EAST);
            add(header, BorderLayout.NORTH);

            // --- CONTENT ---
            JPanel content = new JPanel(new GridLayout(1, 2, 20, 0));
            content.setBorder(new EmptyBorder(20, 20, 20, 20));
            content.setBackground(new Color(236, 240, 241)); // Light Gray

            // LEFT: Available
            JPanel left = createCard("Available Courses");
            JButton registerBtn = new JButton("Register Selected");
            styleButton(registerBtn, SUCCESS_COLOR);

            left.add(new JScrollPane(availableCoursesList), BorderLayout.CENTER);
            left.add(registerBtn, BorderLayout.SOUTH);

            // RIGHT: My Courses
            JPanel right = createCard("My Schedule");
            JButton dropBtn = new JButton("Drop Selected");
            styleButton(dropBtn, DANGER_COLOR);

            right.add(new JScrollPane(myCoursesList), BorderLayout.CENTER);
            right.add(dropBtn, BorderLayout.SOUTH);

            content.add(left);
            content.add(right);
            add(content, BorderLayout.CENTER);

            // --- FOOTER ---
            JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            footer.setBackground(Color.WHITE);
            JButton logoutBtn = new JButton("Logout");
            styleButton(logoutBtn, Color.GRAY);
            logoutBtn.addActionListener(e -> cardLayout.show(mainPanel, LOGIN_PANEL));
            footer.add(logoutBtn);
            add(footer, BorderLayout.SOUTH);

            // Logic
            registerBtn.addActionListener(e -> registerAction());
            dropBtn.addActionListener(e -> dropAction());
        }

        private void registerAction() {
            Course selected = availableCoursesList.getSelectedValue();
            if (selected == null) return;
            String msg = studentService.registerStudentForCourse(currentStudent.getRollNumber(), selected.getCourseId());
            JOptionPane.showMessageDialog(this, msg);
            refreshLists();
        }

        private void dropAction() {
            Course selected = myCoursesList.getSelectedValue();
            if (selected == null) return;
            String msg = studentService.dropCourse(currentStudent.getRollNumber(), selected.getCourseId());
            JOptionPane.showMessageDialog(this, msg);
            refreshLists();
        }

        public void loadData(Student s) {
            this.currentStudent = s;
            nameLbl.setText("Welcome, " + s.getName());
            rollLbl.setText("Roll No: " + s.getRollNumber());
            refreshLists();
        }

        private void refreshLists() {
            availableModel.clear();
            studentService.getAllCourses().forEach(availableModel::addElement);

            myModel.clear();
            currentStudent = loginService.loginStudent(currentStudent.getRollNumber(), currentStudent.getPassword());
            currentStudent.getRegisteredCourses().forEach(myModel::addElement);
        }
    }

    // ==========================================
    // 3. TEACHER PANEL (STYLED)
    // ==========================================
    class TeacherPanel extends JPanel {
        private final JLabel adminLbl = new JLabel();
        private final DefaultListModel<Student> studentListModel = new DefaultListModel<>();
        private final JList<Student> studentList = new JList<>(studentListModel);
        private final DefaultListModel<String> detailsModel = new DefaultListModel<>();

        public TeacherPanel() {
            setLayout(new BorderLayout());

            // --- HEADER ---
            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(PRIMARY_COLOR);
            header.setBorder(new EmptyBorder(15, 20, 15, 20));

            adminLbl.setFont(new Font("Segue UI", Font.BOLD, 20));
            adminLbl.setForeground(TEXT_COLOR);

            JButton logout = new JButton("Logout");
            styleButton(logout, DANGER_COLOR);
            logout.addActionListener(e -> cardLayout.show(mainPanel, LOGIN_PANEL));

            header.add(adminLbl, BorderLayout.WEST);
            header.add(logout, BorderLayout.EAST);
            add(header, BorderLayout.NORTH);

            // --- CONTENT ---
            JSplitPane splitPane = new JSplitPane();
            splitPane.setDividerLocation(300);
            splitPane.setBorder(null);

            // LEFT: Student List
            JPanel left = createCard("Student Directory");

            JPanel btnPanel = new JPanel(new GridLayout(1, 2, 5, 0));
            JButton addBtn = new JButton("Add Student");
            styleButton(addBtn, ACCENT_COLOR);
            JButton refreshBtn = new JButton("Refresh");
            styleButton(refreshBtn, Color.GRAY);

            btnPanel.add(addBtn);
            btnPanel.add(refreshBtn);

            // Custom Renderer
            studentList.setCellRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Student) {
                        Student s = (Student) value;
                        setText("👤 " + s.getName() + " (" + s.getRollNumber() + ")");
                        setBorder(new EmptyBorder(5, 5, 5, 5));
                    }
                    return this;
                }
            });

            left.add(new JScrollPane(studentList), BorderLayout.CENTER);
            left.add(btnPanel, BorderLayout.SOUTH);

            // RIGHT: Details
            JPanel right = createCard("Academic Details");
            JList<String> detailsList = new JList<>(detailsModel);
            detailsList.setBackground(new Color(250, 250, 250));
            right.add(new JScrollPane(detailsList), BorderLayout.CENTER);

            splitPane.setLeftComponent(left);
            splitPane.setRightComponent(right);
            add(splitPane, BorderLayout.CENTER);

            // Logic
            refreshBtn.addActionListener(e -> loadData(null));
            addBtn.addActionListener(e -> addStudentDialog());

            studentList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    Student s = studentList.getSelectedValue();
                    if (s != null) showDetails(s);
                }
            });
        }

        private void addStudentDialog() {
            JTextField roll = new JTextField();
            JTextField name = new JTextField();
            JPasswordField pass = new JPasswordField();
            Object[] msg = {"Roll No:", roll, "Name:", name, "Password:", pass};

            int op = JOptionPane.showConfirmDialog(this, msg, "New Student", JOptionPane.OK_CANCEL_OPTION);
            if (op == JOptionPane.OK_OPTION) {
                teacherService.addStudent(roll.getText(), name.getText(), new String(pass.getPassword()));
                loadData(null);
            }
        }

        private void showDetails(Student s) {
            detailsModel.clear();
            Student fresh = loginService.loginStudent(s.getRollNumber(), s.getPassword());
            detailsModel.addElement("DETAILS FOR: " + fresh.getName());
            detailsModel.addElement("--------------------------------");
            if(fresh.getRegisteredCourses().isEmpty()) detailsModel.addElement("No courses registered.");
            else fresh.getRegisteredCourses().forEach(c -> detailsModel.addElement("📘 " + c.getCourseName() + " (" + c.getCourseId() + ")"));
        }

        public void loadData(Teacher t) {
            if (t != null) adminLbl.setText("Admin Panel: " + t.getName());
            studentListModel.clear();
            teacherService.getAllStudents().forEach(studentListModel::addElement);
        }
    }

    // --- UTILITY: HELPER METHODS ---

    // Creates a standardized white card panel with a title
    private JPanel createCard(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        JLabel l = new JLabel(title);
        l.setFont(new Font("Segue UI", Font.BOLD, 14));
        l.setForeground(PRIMARY_COLOR);
        l.setBorder(new EmptyBorder(0, 0, 10, 0));
        p.add(l, BorderLayout.NORTH);
        return p;
    }

    // Styles buttons nicely
    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segue UI", Font.BOLD, 12));
        btn.setBorder(new EmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        try {
            // Apply Nimbus Look and Feel for modern components
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Problem in loading LookAndFeel : " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}