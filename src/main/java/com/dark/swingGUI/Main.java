package com.dark.swingGUI;

import com.dark.App;
import com.dark.entity.Course;
import com.dark.entity.Student;
import com.dark.entity.Teacher;
import com.dark.service.LoginService;
import com.dark.service.StudentService;
import com.dark.service.TeacherService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

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

    // --- MODERN THEME COLORS ---
    private static final Color BG_COLOR = new Color(243, 244, 246);       // Tailwind Gray 100
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color PRIMARY_COLOR = new Color(79, 70, 229);    // Indigo 600
    private static final Color PRIMARY_HOVER = new Color(67, 56, 202);    // Indigo 700
    private static final Color SUCCESS_COLOR = new Color(16, 185, 129);   // Emerald 500
    private static final Color SUCCESS_HOVER = new Color(5, 150, 105);    // Emerald 600
    private static final Color DANGER_COLOR = new Color(239, 68, 68);     // Red 500
    private static final Color DANGER_HOVER = new Color(220, 38, 38);     // Red 600
    private static final Color TEXT_DARK = new Color(17, 24, 39);         // Gray 900
    private static final Color TEXT_MUTED = new Color(107, 114, 128);     // Gray 500

    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    // --- PANEL INSTANCES ---
    private final StudentPanel studentPanel;
    private final TeacherPanel teacherPanel;

    public Main() {
        // Setup modern Look and Feel base
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error loading Look and Feel");
        }

        setTitle("University Course Registration System");
        setSize(1050, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_COLOR);

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
    // 1. LOGIN PANEL
    // ==========================================
    class LoginPanel extends JPanel {
        JTextField userField = new ModernTextField(20);
        JPasswordField passField = new ModernPasswordField(20);
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Student", "Teacher"});
        ModernButton loginBtn = new ModernButton("Sign In", PRIMARY_COLOR, PRIMARY_HOVER);

        public LoginPanel() {
            setLayout(new GridBagLayout());
            setBackground(BG_COLOR);

            RoundedPanel container = new RoundedPanel(20, CARD_COLOR);
            container.setLayout(new GridBagLayout());
            container.setBorder(new EmptyBorder(40, 50, 40, 50));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 5, 10, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // --- HEADER ---
            JLabel iconLabel = new JLabel("🎓", SwingConstants.CENTER);
            iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));

            JLabel title = new JLabel("Welcome Back", SwingConstants.CENTER);
            title.setFont(HEADER_FONT);
            title.setForeground(TEXT_DARK);

            JLabel subTitle = new JLabel("Please enter your details to sign in.", SwingConstants.CENTER);
            subTitle.setFont(REGULAR_FONT);
            subTitle.setForeground(TEXT_MUTED);

            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            container.add(iconLabel, gbc);
            gbc.gridy++; gbc.insets = new Insets(0, 5, 5, 5);
            container.add(title, gbc);
            gbc.gridy++; gbc.insets = new Insets(0, 5, 25, 5);
            container.add(subTitle, gbc);

            // --- INPUTS ---
            gbc.gridy++; gbc.gridwidth = 1; gbc.insets = new Insets(10, 5, 5, 5);
            JLabel userLbl = new JLabel("User ID");
            userLbl.setFont(TITLE_FONT.deriveFont(14f));
            userLbl.setForeground(TEXT_DARK);
            container.add(userLbl, gbc);

            gbc.gridx = 1;
            container.add(userField, gbc);

            gbc.gridy++; gbc.gridx = 0;
            JLabel passLbl = new JLabel("Password");
            passLbl.setFont(TITLE_FONT.deriveFont(14f));
            passLbl.setForeground(TEXT_DARK);
            container.add(passLbl, gbc);

            gbc.gridx = 1;
            container.add(passField, gbc);

            gbc.gridy++; gbc.gridx = 0;
            JLabel roleLbl = new JLabel("Role");
            roleLbl.setFont(TITLE_FONT.deriveFont(14f));
            roleLbl.setForeground(TEXT_DARK);
            container.add(roleLbl, gbc);

            gbc.gridx = 1;
            roleCombo.setFont(REGULAR_FONT);
            roleCombo.setBackground(Color.WHITE);
            container.add(roleCombo, gbc);

            // --- BUTTON ---
            gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2; gbc.insets = new Insets(25, 5, 5, 5);
            loginBtn.setPreferredSize(new Dimension(250, 45));
            container.add(loginBtn, gbc);

            add(container);

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
                    showError("Invalid Student Credentials");
                }
            } else {
                Teacher t = loginService.loginTeacher(id, pass);
                if (t != null) {
                    teacherPanel.loadData(t);
                    cardLayout.show(mainPanel, TEACHER_PANEL);
                } else {
                    showError("Invalid Teacher Credentials");
                }
            }
        }
    }

    // ==========================================
    // 2. STUDENT PANEL
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
            setLayout(new BorderLayout(0, 20));
            setBackground(BG_COLOR);
            setBorder(new EmptyBorder(20, 20, 20, 20));

            // --- HEADER ---
            RoundedPanel header = new RoundedPanel(15, PRIMARY_COLOR);
            header.setLayout(new BorderLayout());
            header.setBorder(new EmptyBorder(20, 30, 20, 30));

            nameLbl.setFont(HEADER_FONT.deriveFont(24f));
            nameLbl.setForeground(Color.WHITE);
            rollLbl.setFont(REGULAR_FONT);
            rollLbl.setForeground(new Color(224, 231, 255)); // Light indigo

            JPanel textPanel = new JPanel(new GridLayout(2, 1));
            textPanel.setOpaque(false);
            textPanel.add(nameLbl);
            textPanel.add(rollLbl);

            ModernButton logoutBtn = new ModernButton("Logout", new Color(255,255,255, 50), new Color(255,255,255, 80));
            logoutBtn.setForeground(Color.WHITE);
            logoutBtn.addActionListener(e -> cardLayout.show(mainPanel, LOGIN_PANEL));

            header.add(textPanel, BorderLayout.CENTER);
            header.add(logoutBtn, BorderLayout.EAST);
            add(header, BorderLayout.NORTH);

            // --- CONTENT ---
            JPanel content = new JPanel(new GridLayout(1, 2, 20, 0));
            content.setOpaque(false);

            // Left: Available
            RoundedPanel left = createModernCard("Available Courses");
            styleList(availableCoursesList);
            ModernButton registerBtn = new ModernButton("Register Selected", SUCCESS_COLOR, SUCCESS_HOVER);

            left.add(createScrollPane(availableCoursesList), BorderLayout.CENTER);
            left.add(registerBtn, BorderLayout.SOUTH);

            // Right: My Courses
            RoundedPanel right = createModernCard("My Schedule");
            styleList(myCoursesList);
            ModernButton dropBtn = new ModernButton("Drop Selected", DANGER_COLOR, DANGER_HOVER);

            right.add(createScrollPane(myCoursesList), BorderLayout.CENTER);
            right.add(dropBtn, BorderLayout.SOUTH);

            content.add(left);
            content.add(right);
            add(content, BorderLayout.CENTER);

            // Logic
            registerBtn.addActionListener(e -> registerAction());
            dropBtn.addActionListener(e -> dropAction());
        }

        private void registerAction() {
            Course selected = availableCoursesList.getSelectedValue();
            if (selected == null) return;
            String msg = studentService.registerStudentForCourse(currentStudent.getRollNumber(), selected.getCourseId());
            showSuccess(msg);
            refreshLists();
        }

        private void dropAction() {
            Course selected = myCoursesList.getSelectedValue();
            if (selected == null) return;
            String msg = studentService.dropCourse(currentStudent.getRollNumber(), selected.getCourseId());
            showSuccess(msg);
            refreshLists();
        }

        public void loadData(Student s) {
            this.currentStudent = s;
            nameLbl.setText("Welcome back, " + s.getName());
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
    // 3. TEACHER PANEL
    // ==========================================
    class TeacherPanel extends JPanel {
        private final JLabel adminLbl = new JLabel();
        private final DefaultListModel<Student> studentListModel = new DefaultListModel<>();
        private final JList<Student> studentList = new JList<>(studentListModel);
        private final DefaultListModel<String> detailsModel = new DefaultListModel<>();

        public TeacherPanel() {
            setLayout(new BorderLayout(0, 20));
            setBackground(BG_COLOR);
            setBorder(new EmptyBorder(20, 20, 20, 20));

            // --- HEADER ---
            RoundedPanel header = new RoundedPanel(15, TEXT_DARK);
            header.setLayout(new BorderLayout());
            header.setBorder(new EmptyBorder(20, 30, 20, 30));

            adminLbl.setFont(HEADER_FONT.deriveFont(24f));
            adminLbl.setForeground(Color.WHITE);

            ModernButton logoutBtn = new ModernButton("Logout", DANGER_COLOR, DANGER_HOVER);
            logoutBtn.addActionListener(e -> cardLayout.show(mainPanel, LOGIN_PANEL));

            header.add(adminLbl, BorderLayout.WEST);
            header.add(logoutBtn, BorderLayout.EAST);
            add(header, BorderLayout.NORTH);

            // --- CONTENT ---
            JPanel content = new JPanel(new GridLayout(1, 2, 20, 0));
            content.setOpaque(false);

            // LEFT: Student List
            RoundedPanel left = createModernCard("Student Directory");
            styleList(studentList);

            JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
            btnPanel.setOpaque(false);
            btnPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

            ModernButton addBtn = new ModernButton("Add Student", PRIMARY_COLOR, PRIMARY_HOVER);
            ModernButton refreshBtn = new ModernButton("Refresh", TEXT_MUTED, TEXT_DARK);

            btnPanel.add(addBtn);
            btnPanel.add(refreshBtn);

            studentList.setCellRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Student) {
                        Student s = (Student) value;
                        label.setText("  👤 " + s.getName() + " (" + s.getRollNumber() + ")");
                    }
                    label.setFont(REGULAR_FONT);
                    label.setBorder(new EmptyBorder(10, 10, 10, 10));
                    if (isSelected) {
                        label.setBackground(new Color(237, 242, 255)); // Light indigo
                        label.setForeground(PRIMARY_COLOR);
                    }
                    return label;
                }
            });

            left.add(createScrollPane(studentList), BorderLayout.CENTER);
            left.add(btnPanel, BorderLayout.SOUTH);

            // RIGHT: Details
            RoundedPanel right = createModernCard("Academic Details");
            JList<String> detailsList = new JList<>(detailsModel);
            styleList(detailsList);

            // Details Custom Renderer for clean spacing
            detailsList.setCellRenderer(new DefaultListCellRenderer(){
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    label.setFont(index == 0 ? TITLE_FONT : REGULAR_FONT);
                    label.setForeground(index == 0 ? PRIMARY_COLOR : TEXT_DARK);
                    label.setBorder(new EmptyBorder(8, 10, 8, 10));
                    return label;
                }
            });

            right.add(createScrollPane(detailsList), BorderLayout.CENTER);

            content.add(left);
            content.add(right);
            add(content, BorderLayout.CENTER);

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
            JTextField roll = new ModernTextField(15);
            JTextField name = new ModernTextField(15);
            JPasswordField pass = new ModernPasswordField(15);

            JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
            panel.add(new JLabel("Roll No:")); panel.add(roll);
            panel.add(new JLabel("Name:")); panel.add(name);
            panel.add(new JLabel("Password:")); panel.add(pass);

            int op = JOptionPane.showConfirmDialog(this, panel, "Add New Student", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (op == JOptionPane.OK_OPTION) {
                teacherService.addStudent(roll.getText(), name.getText(), new String(pass.getPassword()));
                loadData(null);
                showSuccess("Student added successfully!");
            }
        }

        private void showDetails(Student s) {
            detailsModel.clear();
            Student fresh = loginService.loginStudent(s.getRollNumber(), s.getPassword());
            detailsModel.addElement("Records for " + fresh.getName());
            detailsModel.addElement(" "); // spacer
            if(fresh.getRegisteredCourses().isEmpty()) {
                detailsModel.addElement("No courses currently registered.");
            } else {
                fresh.getRegisteredCourses().forEach(c -> detailsModel.addElement("📘 " + c.getCourseName() + " (" + c.getCourseId() + ")"));
            }
        }

        public void loadData(Teacher t) {
            if (t != null) adminLbl.setText("Admin Dashboard | " + t.getName());
            studentListModel.clear();
            teacherService.getAllStudents().forEach(studentListModel::addElement);
            detailsModel.clear();
        }
    }

    // ==========================================
    // CUSTOM UI COMPONENTS & UTILS
    // ==========================================

    private RoundedPanel createModernCard(String title) {
        RoundedPanel p = new RoundedPanel(15, CARD_COLOR);
        p.setLayout(new BorderLayout(0, 10));
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel l = new JLabel(title);
        l.setFont(TITLE_FONT);
        l.setForeground(TEXT_DARK);
        p.add(l, BorderLayout.NORTH);
        return p;
    }

    private <T> void styleList(JList<T> list) {
        list.setFont(REGULAR_FONT);
        list.setForeground(TEXT_DARK);
        list.setSelectionBackground(new Color(237, 242, 255));
        list.setSelectionForeground(PRIMARY_COLOR);
        list.setFixedCellHeight(40);
    }

    private JScrollPane createScrollPane(Component comp) {
        JScrollPane scroll = new JScrollPane(comp);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1)); // Very light gray border
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // --- Custom Rounded Panel ---
    static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bgColor;

        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // --- Custom Modern Button ---
    static class ModernButton extends JButton {
        private Color normalColor;
        private Color hoverColor;
        private boolean isHovered = false;

        public ModernButton(String text, Color normal, Color hover) {
            super(text);
            this.normalColor = normal;
            this.hoverColor = hover;

            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(150, 40));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { isHovered = true; repaint(); }
                public void mouseExited(MouseEvent e) { isHovered = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isHovered ? hoverColor : normalColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // --- Custom Text Fields ---
    static class ModernTextField extends JTextField {
        public ModernTextField(int columns) {
            super(columns);
            setFont(REGULAR_FONT);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
                    new EmptyBorder(10, 10, 10, 10)
            ));
        }
    }

    static class ModernPasswordField extends JPasswordField {
        public ModernPasswordField(int columns) {
            super(columns);
            setFont(REGULAR_FONT);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
                    new EmptyBorder(10, 10, 10, 10)
            ));
        }
    }

    // --- ENTRY POINT ---
    public static void main(String[] args) {
        // Ensure your App.run() triggers the visibility of this Main frame!
        SwingUtilities.invokeLater(App::run);
    }
}