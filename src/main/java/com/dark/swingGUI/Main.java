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
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class Main extends JFrame {

    // ═══════════════════════════════════════
    // DARK PREMIUM THEME
    // ═══════════════════════════════════════
    private static final Color BG = new Color(13, 13, 25);
    private static final Color BG_CARD = new Color(28, 28, 52);
    private static final Color BG_INPUT = new Color(18, 18, 36);
    private static final Color ACCENT = new Color(99, 102, 241);
    private static final Color ACCENT_GLO = new Color(129, 132, 255);
    private static final Color GREEN = new Color(34, 197, 94);
    private static final Color GREEN_H = new Color(22, 163, 74);
    private static final Color RED = new Color(239, 68, 68);
    private static final Color RED_H = new Color(220, 38, 38);
    private static final Color TXT = new Color(248, 250, 252);
    private static final Color TXT_G = new Color(148, 163, 184);
    private static final Color TXT_D = new Color(100, 116, 139);
    private static final Color BORDER = new Color(38, 38, 70);
    private static final Color HOVER = new Color(35, 35, 65);
    private static final Color ALT_ROW = new Color(24, 24, 46);

    private static final Font FH = new Font("Helvetica Neue", Font.BOLD, 32);
    private static final Font FH1 = new Font("Helvetica Neue", Font.BOLD, 24);
    private static final Font FH2 = new Font("Helvetica Neue", Font.BOLD, 18);
    private static final Font FH3 = new Font("Helvetica Neue", Font.BOLD, 15);
    private static final Font FB = new Font("Helvetica Neue", Font.PLAIN, 14);
    private static final Font FS = new Font("Helvetica Neue", Font.PLAIN, 12);
    private static final Font FBTN = new Font("Helvetica Neue", Font.BOLD, 13);

    // Services
    private final LoginService loginService = new LoginService();
    private final StudentService studentService = new StudentService();
    private final TeacherService teacherService = new TeacherService();

    // Layout
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    private final StudentPanel studentPanel;
    private final TeacherPanel teacherPanel;

    public Main() {
        setTitle("UniPortal — Course Registration System");
        setSize(1120, 800);
        setMinimumSize(new Dimension(900, 650));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        UIManager.put("OptionPane.background", BG_CARD);
        UIManager.put("Panel.background", BG_CARD);
        UIManager.put("OptionPane.messageForeground", TXT);
        UIManager.put("Label.foreground", TXT);

        mainPanel.setBackground(BG);
        LoginPanel loginPanel = new LoginPanel();
        studentPanel = new StudentPanel();
        teacherPanel = new TeacherPanel();
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(studentPanel, "STUDENT");
        mainPanel.add(teacherPanel, "TEACHER");
        add(mainPanel);
    }

    // ═══════════════════════════════════════
    // LOGIN PANEL
    // ═══════════════════════════════════════
    class LoginPanel extends JPanel {
        JTextField userField = darkField(20);
        JPasswordField passField = darkPass(20);
        JComboBox<String> roleCombo;

        LoginPanel() {
            setLayout(new GridBagLayout());
            roleCombo = new JComboBox<>(new String[] { "Student", "Teacher" });
            roleCombo.setFont(FB);
            roleCombo.setBackground(BG_INPUT);
            roleCombo.setForeground(TXT);
            roleCombo.setBorder(BorderFactory.createLineBorder(BORDER));

            JPanel card = new JPanel(new GridBagLayout()) {
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(22, 22, 42, 230));
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 24, 24));
                    g2.setColor(new Color(99, 102, 241, 50));
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 24, 24));
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            card.setOpaque(false);
            card.setPreferredSize(new Dimension(420, 530));
            GridBagConstraints g = new GridBagConstraints();
            g.insets = new Insets(6, 25, 6, 25);
            g.fill = GridBagConstraints.HORIZONTAL;
            g.gridx = 0;
            g.gridy = 0;
            g.gridwidth = 2;

            JLabel icon = lbl("🎓", new Font("Segoe UI Emoji", Font.PLAIN, 50), TXT);
            icon.setHorizontalAlignment(SwingConstants.CENTER);
            card.add(icon, g);

            g.gridy++;
            g.insets = new Insets(4, 25, 0, 25);
            JLabel t = lbl("Welcome Back", FH, TXT);
            t.setHorizontalAlignment(SwingConstants.CENTER);
            card.add(t, g);

            g.gridy++;
            g.insets = new Insets(2, 25, 20, 25);
            JLabel sub = lbl("Sign in to your university portal", FB, TXT_G);
            sub.setHorizontalAlignment(SwingConstants.CENTER);
            card.add(sub, g);

            g.gridy++;
            g.insets = new Insets(8, 25, 3, 25);
            card.add(lbl("User ID", FS, TXT_G), g);
            g.gridy++;
            g.insets = new Insets(0, 25, 8, 25);
            card.add(userField, g);

            g.gridy++;
            g.insets = new Insets(8, 25, 3, 25);
            card.add(lbl("Password", FS, TXT_G), g);
            g.gridy++;
            g.insets = new Insets(0, 25, 8, 25);
            card.add(passField, g);

            g.gridy++;
            g.insets = new Insets(8, 25, 3, 25);
            card.add(lbl("Sign in as", FS, TXT_G), g);
            g.gridy++;
            g.insets = new Insets(0, 25, 12, 25);
            card.add(roleCombo, g);

            g.gridy++;
            g.insets = new Insets(16, 25, 8, 25);
            GBtn loginBtn = new GBtn("Sign In  →", ACCENT, ACCENT_GLO);
            loginBtn.setPreferredSize(new Dimension(360, 48));
            loginBtn.addActionListener(e -> doLogin());
            card.add(loginBtn, g);

            g.gridy++;
            g.insets = new Insets(10, 25, 10, 25);
            JLabel ft = lbl("University Course Registration System", FS, TXT_D);
            ft.setHorizontalAlignment(SwingConstants.CENTER);
            card.add(ft, g);

            add(card);
        }

        void doLogin() {
            String id = userField.getText().trim(), pass = new String(passField.getPassword());
            if (id.isEmpty() || pass.isEmpty()) {
                err("Enter both User ID and Password.");
                return;
            }
            if ("Student".equals(roleCombo.getSelectedItem())) {
                Student s = loginService.loginStudent(id, pass);
                if (s != null) {
                    studentPanel.loadData(s);
                    cardLayout.show(mainPanel, "STUDENT");
                    userField.setText("");
                    passField.setText("");
                } else
                    err("Invalid Student Credentials");
            } else {
                Teacher t = loginService.loginTeacher(id, pass);
                if (t != null) {
                    teacherPanel.loadData(t);
                    cardLayout.show(mainPanel, "TEACHER");
                    userField.setText("");
                    passField.setText("");
                } else
                    err("Invalid Teacher Credentials");
            }
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(new GradientPaint(0, 0, BG, getWidth(), getHeight(), new Color(25, 20, 50)));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.04f));
            g2.setColor(ACCENT);
            g2.fillOval(-100, -100, 500, 500);
            g2.fillOval(getWidth() - 300, getHeight() - 300, 500, 500);
            g2.dispose();
        }
    }

    // ═══════════════════════════════════════
    // STUDENT PANEL
    // ═══════════════════════════════════════
    class StudentPanel extends JPanel {
        JLabel nameLbl = new JLabel(), rollLbl = new JLabel();
        JLabel stTotal = new JLabel("0"), stMy = new JLabel("0"), stAvail = new JLabel("0");
        DefaultListModel<Course> availModel = new DefaultListModel<>(), myModel = new DefaultListModel<>();
        JList<Course> availList = new JList<>(availModel), myList = new JList<>(myModel);
        Student cur;

        StudentPanel() {
            setLayout(new BorderLayout());
            setBackground(BG);

            // Header
            JPanel hdr = gradientHeader();
            nameLbl.setFont(FH1);
            nameLbl.setForeground(TXT);
            rollLbl.setFont(FB);
            rollLbl.setForeground(new Color(199, 210, 254));
            JPanel nb = new JPanel(new GridLayout(2, 1));
            nb.setOpaque(false);
            nb.add(nameLbl);
            nb.add(rollLbl);
            GBtn logout = new GBtn("Logout", RED, RED_H);
            logout.setPreferredSize(new Dimension(100, 36));
            logout.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));
            hdr.add(nb, BorderLayout.CENTER);
            hdr.add(logout, BorderLayout.EAST);
            add(hdr, BorderLayout.NORTH);

            // Body
            JPanel body = new JPanel(new BorderLayout(0, 16));
            body.setOpaque(false);
            body.setBorder(new EmptyBorder(16, 24, 24, 24));

            JPanel stats = new JPanel(new GridLayout(1, 3, 16, 0));
            stats.setOpaque(false);
            stats.add(statCard("📚", "Total Courses", stTotal));
            stats.add(statCard("✅", "Registered", stMy));
            stats.add(statCard("📋", "Available", stAvail));
            body.add(stats, BorderLayout.NORTH);

            JPanel cols = new JPanel(new GridLayout(1, 2, 20, 0));
            cols.setOpaque(false);

            JPanel left = darkCard("Available Courses");
            darkList(availList);
            availList.setCellRenderer(new CRend());
            GBtn regBtn = new GBtn("Register Selected", GREEN, GREEN_H);
            regBtn.addActionListener(e -> {
                Course c = availList.getSelectedValue();
                if (c == null) {
                    err("Select a course.");
                    return;
                }
                suc(studentService.registerStudentForCourse(cur.getRollNumber(), c.getCourseId()));
                refresh();
            });
            left.add(darkScroll(availList), BorderLayout.CENTER);
            left.add(regBtn, BorderLayout.SOUTH);

            JPanel right = darkCard("My Schedule");
            darkList(myList);
            myList.setCellRenderer(new CRend());
            GBtn dropBtn = new GBtn("Drop Selected", RED, RED_H);
            dropBtn.addActionListener(e -> {
                Course c = myList.getSelectedValue();
                if (c == null) {
                    err("Select a course.");
                    return;
                }
                suc(studentService.dropCourse(cur.getRollNumber(), c.getCourseId()));
                refresh();
            });
            right.add(darkScroll(myList), BorderLayout.CENTER);
            right.add(dropBtn, BorderLayout.SOUTH);

            cols.add(left);
            cols.add(right);
            body.add(cols, BorderLayout.CENTER);
            add(body, BorderLayout.CENTER);

            MouseAdapter ma = new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        JList<Course> l = (JList<Course>) e.getSource();
                        Course c = l.getSelectedValue();
                        if (c != null)
                            coursePopup(c);
                    }
                }
            };
            availList.addMouseListener(ma);
            myList.addMouseListener(ma);
        }

        void loadData(Student s) {
            cur = s;
            nameLbl.setText("Welcome, " + s.getName());
            rollLbl.setText("Roll No: " + s.getRollNumber());
            refresh();
        }

        void refresh() {
            List<Course> all = studentService.getAllCourses();
            cur = loginService.loginStudent(cur.getRollNumber(), cur.getPassword());
            List<Course> mine = cur.getRegisteredCourses();
            availModel.clear();
            all.forEach(availModel::addElement);
            myModel.clear();
            mine.forEach(myModel::addElement);
            stTotal.setText(String.valueOf(all.size()));
            stMy.setText(String.valueOf(mine.size()));
            stAvail.setText(String.valueOf(all.size() - mine.size()));
        }
    }

    // ═══════════════════════════════════════
    // TEACHER PANEL
    // ═══════════════════════════════════════
    class TeacherPanel extends JPanel {
        JLabel adminLbl = new JLabel(), stStu = new JLabel("0"), stCrs = new JLabel("0");
        DefaultListModel<Student> stuModel = new DefaultListModel<>();
        DefaultListModel<Course> crsModel = new DefaultListModel<>();
        DefaultListModel<String> detModel = new DefaultListModel<>();
        JList<Student> stuList = new JList<>(stuModel);
        JList<Course> crsList = new JList<>(crsModel);
        JList<String> detList = new JList<>(detModel);
        CardLayout leftCL = new CardLayout();
        JPanel leftContent = new JPanel(leftCL);
        GBtn tabS, tabC;

        TeacherPanel() {
            setLayout(new BorderLayout());
            setBackground(BG);

            // Header
            JPanel hdr = darkHeader();
            adminLbl.setFont(FH1);
            adminLbl.setForeground(TXT);
            GBtn logout = new GBtn("Logout", RED, RED_H);
            logout.setPreferredSize(new Dimension(100, 36));
            logout.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));
            hdr.add(adminLbl, BorderLayout.CENTER);
            hdr.add(logout, BorderLayout.EAST);
            add(hdr, BorderLayout.NORTH);

            JPanel body = new JPanel(new BorderLayout(0, 16));
            body.setOpaque(false);
            body.setBorder(new EmptyBorder(16, 24, 24, 24));

            JPanel stats = new JPanel(new GridLayout(1, 2, 16, 0));
            stats.setOpaque(false);
            stats.add(statCard("👥", "Students", stStu));
            stats.add(statCard("📚", "Courses", stCrs));
            body.add(stats, BorderLayout.NORTH);

            JPanel content = new JPanel(new GridLayout(1, 2, 20, 0));
            content.setOpaque(false);

            // Left tabbed panel
            JPanel leftPanel = darkCard("");
            leftPanel.setLayout(new BorderLayout(0, 0));
            JPanel tabs = new JPanel(new GridLayout(1, 2, 8, 0));
            tabs.setOpaque(false);
            tabs.setBorder(new EmptyBorder(0, 0, 12, 0));
            tabS = new GBtn("👥 Students", ACCENT, ACCENT_GLO);
            tabC = new GBtn("📚 Courses", BG_CARD, HOVER);
            tabS.addActionListener(e -> switchTab(true));
            tabC.addActionListener(e -> switchTab(false));
            tabs.add(tabS);
            tabs.add(tabC);
            leftPanel.add(tabs, BorderLayout.NORTH);

            // Student view
            JPanel sv = new JPanel(new BorderLayout(0, 8));
            sv.setOpaque(false);
            darkList(stuList);
            stuList.setCellRenderer(new SRend());
            JPanel sb = new JPanel(new GridLayout(1, 2, 8, 0));
            sb.setOpaque(false);
            GBtn addS = new GBtn("+ Add Student", ACCENT, ACCENT_GLO);
            GBtn refS = new GBtn("↻ Refresh", new Color(55, 55, 85), new Color(65, 65, 100));
            addS.addActionListener(e -> addStudentDlg());
            refS.addActionListener(e -> refreshAll());
            sb.add(addS);
            sb.add(refS);
            sv.add(darkScroll(stuList), BorderLayout.CENTER);
            sv.add(sb, BorderLayout.SOUTH);

            // Course view
            JPanel cv = new JPanel(new BorderLayout(0, 8));
            cv.setOpaque(false);
            darkList(crsList);
            crsList.setCellRenderer(new CRend());
            JPanel cb = new JPanel(new GridLayout(1, 2, 8, 0));
            cb.setOpaque(false);
            GBtn addC = new GBtn("+ Add Course", GREEN, GREEN_H);
            GBtn remC = new GBtn("✕ Remove", RED, RED_H);
            addC.addActionListener(e -> addCourseDlg());
            remC.addActionListener(e -> {
                Course c = crsList.getSelectedValue();
                if (c == null) {
                    err("Select a course.");
                    return;
                }
                if (JOptionPane.showConfirmDialog(this, "Remove " + c.getCourseName() + "?", "Confirm",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    teacherService.removeCourse(c.getCourseId());
                    refreshAll();
                    suc("Course removed.");
                }
            });
            cb.add(addC);
            cb.add(remC);
            cv.add(darkScroll(crsList), BorderLayout.CENTER);
            cv.add(cb, BorderLayout.SOUTH);

            leftContent.setOpaque(false);
            leftContent.add(sv, "S");
            leftContent.add(cv, "C");
            leftPanel.add(leftContent, BorderLayout.CENTER);

            // Right details
            JPanel rightPanel = darkCard("Details");
            darkList(detList);
            detList.setCellRenderer(new DRend());
            rightPanel.add(darkScroll(detList), BorderLayout.CENTER);

            content.add(leftPanel);
            content.add(rightPanel);
            body.add(content, BorderLayout.CENTER);
            add(body, BorderLayout.CENTER);

            stuList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    Student s = stuList.getSelectedValue();
                    if (s != null)
                        showStuDet(s);
                }
            });
            crsList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    Course c = crsList.getSelectedValue();
                    if (c != null)
                        showCrsDet(c);
                }
            });
            detList.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        String v = detList.getSelectedValue();
                        if (v != null && v.contains("📘")) {
                            int a = v.lastIndexOf('('), b = v.lastIndexOf(')');
                            if (a != -1 && b > a) {
                                String id = v.substring(a + 1, b);
                                teacherService.getAllCourses().stream().filter(c -> c.getCourseId().equals(id))
                                        .findFirst().ifPresent(c -> coursePopup(c));
                            }
                        }
                    }
                }
            });
        }

        void switchTab(boolean stu) {
            leftCL.show(leftContent, stu ? "S" : "C");
            tabS.setColors(stu ? ACCENT : BG_CARD, stu ? ACCENT_GLO : HOVER);
            tabC.setColors(!stu ? ACCENT : BG_CARD, !stu ? ACCENT_GLO : HOVER);
            detModel.clear();
        }

        void showStuDet(Student s) {
            detModel.clear();
            Student f = loginService.loginStudent(s.getRollNumber(), s.getPassword());
            if (f == null) {
                detModel.addElement("err:Could not load.");
                return;
            }
            detModel.addElement("h:" + f.getName());
            detModel.addElement("s:Roll No: " + f.getRollNumber());
            detModel.addElement("div");
            detModel.addElement("sec:Enrolled Courses");
            if (f.getRegisteredCourses().isEmpty())
                detModel.addElement("e:No courses registered.");
            else
                f.getRegisteredCourses()
                        .forEach(c -> detModel.addElement("📘 " + c.getCourseName() + " (" + c.getCourseId() + ")"));
        }

        void showCrsDet(Course c) {
            detModel.clear();
            List<Student> en = studentService.getStudentsEnrolledInCourse(c.getCourseId());
            detModel.addElement("h:" + c.getCourseName());
            detModel.addElement("s:ID: " + c.getCourseId());
            detModel.addElement("div");
            detModel.addElement("sec:Enrolled Students (" + en.size() + ")");
            if (en.isEmpty())
                detModel.addElement("e:No students enrolled.");
            else
                en.forEach(s -> detModel.addElement("👤 " + s.getName() + " (" + s.getRollNumber() + ")"));
        }

        void addStudentDlg() {
            JTextField r = darkField(18), n = darkField(18);
            JPasswordField p = darkPass(18);
            JPanel pan = new JPanel(new GridLayout(3, 2, 12, 12));
            pan.setOpaque(false);
            pan.add(lbl("Roll No:", FB, TXT_G));
            pan.add(r);
            pan.add(lbl("Name:", FB, TXT_G));
            pan.add(n);
            pan.add(lbl("Password:", FB, TXT_G));
            pan.add(p);
            if (JOptionPane.showConfirmDialog(this, pan, "Add Student", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION && !r.getText().isEmpty()) {
                teacherService.addStudent(r.getText().trim(), n.getText().trim(), new String(p.getPassword()));
                refreshAll();
                suc("Student added!");
            }
        }

        void addCourseDlg() {
            JTextField ci = darkField(18), cn = darkField(18);
            JPanel pan = new JPanel(new GridLayout(2, 2, 12, 12));
            pan.setOpaque(false);
            pan.add(lbl("Course ID:", FB, TXT_G));
            pan.add(ci);
            pan.add(lbl("Course Name:", FB, TXT_G));
            pan.add(cn);
            if (JOptionPane.showConfirmDialog(this, pan, "Add Course", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION && !ci.getText().isEmpty()) {
                teacherService.addCourse(ci.getText().trim(), cn.getText().trim());
                refreshAll();
                suc("Course added!");
            }
        }

        void loadData(Teacher t) {
            if (t != null)
                adminLbl.setText("Admin · " + t.getName());
            refreshAll();
            switchTab(true);
        }

        void refreshAll() {
            stuModel.clear();
            teacherService.getAllStudents().forEach(stuModel::addElement);
            crsModel.clear();
            teacherService.getAllCourses().forEach(crsModel::addElement);
            detModel.clear();
            stStu.setText(String.valueOf(stuModel.size()));
            stCrs.setText(String.valueOf(crsModel.size()));
        }
    }

    // ═══════════════════════════════════════
    // COURSE DETAILS POPUP
    // ═══════════════════════════════════════
    private void coursePopup(Course c) {
        List<Student> en = studentService.getStudentsEnrolledInCourse(c.getCourseId());
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(BG_CARD);
        p.setBorder(new EmptyBorder(24, 30, 24, 30));

        JLabel ic = lbl("📘", new Font("Segoe UI Emoji", Font.PLAIN, 44), TXT);
        ic.setAlignmentX(CENTER_ALIGNMENT);
        p.add(ic);
        p.add(Box.createVerticalStrut(8));
        JLabel tl = lbl(c.getCourseName(), FH1, TXT);
        tl.setAlignmentX(CENTER_ALIGNMENT);
        p.add(tl);
        JLabel il = lbl("ID: " + c.getCourseId(), FB, TXT_G);
        il.setAlignmentX(CENTER_ALIGNMENT);
        p.add(il);
        p.add(Box.createVerticalStrut(16));
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        p.add(sep);
        p.add(Box.createVerticalStrut(16));
        JLabel el = lbl("👥 Enrolled: " + en.size(), FH3, ACCENT);
        el.setAlignmentX(LEFT_ALIGNMENT);
        p.add(el);
        p.add(Box.createVerticalStrut(8));
        if (en.isEmpty()) {
            JLabel nl = lbl("  No students enrolled.", FB, TXT_D);
            nl.setAlignmentX(LEFT_ALIGNMENT);
            p.add(nl);
        } else
            en.forEach(s -> {
                JLabel sl = lbl("  👤 " + s.getName() + " (" + s.getRollNumber() + ")", FB, TXT);
                sl.setAlignmentX(LEFT_ALIGNMENT);
                p.add(sl);
                p.add(Box.createVerticalStrut(4));
            });

        JScrollPane sp = new JScrollPane(p);
        sp.setBorder(null);
        sp.getViewport().setBackground(BG_CARD);
        sp.setPreferredSize(new Dimension(400, Math.min(420, 220 + en.size() * 28)));
        JOptionPane.showMessageDialog(this, sp, "Course Details", JOptionPane.PLAIN_MESSAGE);
    }

    // ═══════════════════════════════════════
    // CUSTOM COMPONENTS
    // ═══════════════════════════════════════

    // Gradient Button
    static class GBtn extends JButton {
        Color c1, c2;
        boolean hov;

        GBtn(String t, Color a, Color b) {
            super(t);
            c1 = a;
            c2 = b;
            setFont(FBTN);
            setForeground(TXT);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(160, 42));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    hov = true;
                    repaint();
                }

                public void mouseExited(MouseEvent e) {
                    hov = false;
                    repaint();
                }
            });
        }

        void setColors(Color a, Color b) {
            c1 = a;
            c2 = b;
            repaint();
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, hov ? c2 : c1, getWidth(), getHeight(), hov ? c1 : c2));
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Cell Renderers
    class CRend extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
            JLabel lb = (JLabel) super.getListCellRendererComponent(l, v, i, s, f);
            if (v instanceof Course) {
                Course c = (Course) v;
                lb.setText("  📘 " + c.getCourseName() + "  ·  " + c.getCourseId());
            }
            lb.setFont(FB);
            lb.setBorder(new EmptyBorder(12, 16, 12, 16));
            lb.setOpaque(true);
            lb.setBackground(s ? new Color(99, 102, 241, 40) : i % 2 == 0 ? BG_CARD : ALT_ROW);
            lb.setForeground(s ? ACCENT_GLO : TXT);
            return lb;
        }
    }

    class SRend extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
            JLabel lb = (JLabel) super.getListCellRendererComponent(l, v, i, s, f);
            if (v instanceof Student) {
                Student st = (Student) v;
                lb.setText("  👤 " + st.getName() + "  ·  " + st.getRollNumber());
            }
            lb.setFont(FB);
            lb.setBorder(new EmptyBorder(12, 16, 12, 16));
            lb.setOpaque(true);
            lb.setBackground(s ? new Color(99, 102, 241, 40) : i % 2 == 0 ? BG_CARD : ALT_ROW);
            lb.setForeground(s ? ACCENT_GLO : TXT);
            return lb;
        }
    }

    class DRend extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
            JLabel lb = (JLabel) super.getListCellRendererComponent(l, v, i, s, f);
            String t = v.toString();
            lb.setOpaque(true);
            lb.setBackground(BG_CARD);
            lb.setBorder(new EmptyBorder(6, 16, 6, 16));
            if (t.startsWith("h:")) {
                lb.setText(t.substring(2));
                lb.setFont(FH2);
                lb.setForeground(TXT);
                lb.setBorder(new EmptyBorder(12, 16, 2, 16));
            } else if (t.startsWith("s:")) {
                lb.setText(t.substring(2));
                lb.setFont(FS);
                lb.setForeground(TXT_G);
            } else if (t.equals("div")) {
                lb.setText(" ");
                lb.setFont(FS.deriveFont(4f));
                lb.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
            } else if (t.startsWith("sec:")) {
                lb.setText(t.substring(4));
                lb.setFont(FH3);
                lb.setForeground(ACCENT);
                lb.setBorder(new EmptyBorder(10, 16, 4, 16));
            } else if (t.startsWith("e:")) {
                lb.setText(t.substring(2));
                lb.setFont(FB);
                lb.setForeground(TXT_D);
            } else {
                lb.setText("  " + t);
                lb.setFont(FB);
                lb.setForeground(TXT);
            }
            return lb;
        }
    }

    // ═══════════════════════════════════════
    // FACTORY HELPERS
    // ═══════════════════════════════════════
    private JPanel gradientHeader() {
        JPanel h = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, ACCENT, getWidth(), 0, new Color(79, 70, 229)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        h.setBorder(new EmptyBorder(20, 28, 20, 28));
        h.setPreferredSize(new Dimension(0, 80));
        return h;
    }

    private JPanel darkHeader() {
        JPanel h = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(22, 22, 42));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(BORDER);
                g2.fillRect(0, getHeight() - 1, getWidth(), 1);
                g2.dispose();
            }
        };
        h.setBorder(new EmptyBorder(20, 28, 20, 28));
        h.setPreferredSize(new Dimension(0, 80));
        return h;
    }

    private JPanel darkCard(String title) {
        JPanel p = new JPanel(new BorderLayout(0, 10)) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 16, 16));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        if (!title.isEmpty()) {
            JLabel l = lbl(title, FH3, TXT);
            p.add(l, BorderLayout.NORTH);
        }
        return p;
    }

    private JPanel statCard(String icon, String label, JLabel valLbl) {
        JPanel p = new JPanel(new BorderLayout(12, 0)) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 14, 14));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(16, 20, 16, 20));
        JLabel ic = lbl(icon, new Font("Segoe UI Emoji", Font.PLAIN, 28), TXT);
        p.add(ic, BorderLayout.WEST);
        JPanel txt = new JPanel(new GridLayout(2, 1));
        txt.setOpaque(false);
        valLbl.setFont(FH2);
        valLbl.setForeground(TXT);
        JLabel lb = lbl(label, FS, TXT_G);
        txt.add(valLbl);
        txt.add(lb);
        p.add(txt, BorderLayout.CENTER);
        return p;
    }

    static JLabel lbl(String t, Font f, Color c) {
        JLabel l = new JLabel(t);
        l.setFont(f);
        l.setForeground(c);
        return l;
    }

    static JTextField darkField(int cols) {
        JTextField f = new JTextField(cols);
        f.setFont(FB);
        f.setBackground(BG_INPUT);
        f.setForeground(TXT);
        f.setCaretColor(TXT);
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(10, 12, 10, 12)));
        return f;
    }

    static JPasswordField darkPass(int cols) {
        JPasswordField f = new JPasswordField(cols);
        f.setFont(FB);
        f.setBackground(BG_INPUT);
        f.setForeground(TXT);
        f.setCaretColor(TXT);
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(10, 12, 10, 12)));
        return f;
    }

    private <T> void darkList(JList<T> l) {
        l.setFont(FB);
        l.setBackground(BG_CARD);
        l.setForeground(TXT);
        l.setSelectionBackground(new Color(99, 102, 241, 40));
        l.setSelectionForeground(ACCENT_GLO);
        l.setFixedCellHeight(46);
    }

    private JScrollPane darkScroll(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(BorderFactory.createLineBorder(BORDER));
        sp.getViewport().setBackground(BG_CARD);
        sp.getVerticalScrollBar().setBackground(BG_CARD);
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        return sp;
    }

    private void err(String m) {
        JOptionPane.showMessageDialog(this, m, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void suc(String m) {
        JOptionPane.showMessageDialog(this, m, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::run);
    }
}