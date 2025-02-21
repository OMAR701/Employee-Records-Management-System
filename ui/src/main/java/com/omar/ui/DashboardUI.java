package com.omar.ui;

import com.omar.api.ApiClient;
import com.omar.models.Employee;
import com.omar.utils.AuthTokenHolder;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

public class DashboardUI extends JFrame {
    private JTable employeeTable;
    private JButton addButton, editButton, deleteButton, refreshButton, searchButton;
    private JTextField nameField, idField, departmentField, jobTitleField;

    public DashboardUI() {
        setTitle("Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        initComponents();
        setVisible(true);
        this.toFront();
        this.requestFocus();
        this.repaint();
    }



    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel sidebar = new JPanel(new GridBagLayout());
        sidebar.setPreferredSize(new Dimension(150, 0));
        sidebar.setBackground(new Color(0, 120, 160));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));
        buttonContainer.setOpaque(false);
        buttonContainer.add(Box.createVerticalStrut(20));

        addButton = createStyledButton("Add", "/icons/add.png");
        editButton = createStyledButton("Edit", "/icons/edit.png");
        deleteButton = createStyledButton("Delete", "/icons/delete.png");
        refreshButton = createStyledButton("Refresh", "/icons/refresh.png");

        buttonContainer.add(addButton);
        buttonContainer.add(Box.createVerticalStrut(10));
        buttonContainer.add(editButton);
        buttonContainer.add(Box.createVerticalStrut(10));
        buttonContainer.add(deleteButton);
        buttonContainer.add(Box.createVerticalStrut(10));
        buttonContainer.add(refreshButton);

        sidebar.add(buttonContainer, gbc);

        JPanel headerSearchPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 120, 160));

        JLabel logoLabel = new JLabel();
        URL logoResource = getClass().getResource("/images/logo.png");
        if (logoResource != null) {
            ImageIcon logoIcon = new ImageIcon(new ImageIcon(logoResource)
                    .getImage()
                    .getScaledInstance(150, 60, Image.SCALE_SMOOTH));
            logoLabel.setIcon(logoIcon);
            logoLabel.setBorder(BorderFactory.createEmptyBorder(35, 30, 0, 0));
        } else {
            System.err.println("Logo not found: /images/logo.png");
        }

        headerPanel.add(logoLabel, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        searchPanel.setBackground(new Color(0, 120, 160));
        nameField = createStyledTextField();
        idField = createStyledTextField();
        departmentField = createStyledTextField();
        jobTitleField = createStyledTextField();
        searchButton = createStyledButton("Search", "/icons/search.png");

        searchPanel.add(new JLabel("Name:"));
        searchPanel.add(nameField);
        searchPanel.add(new JLabel("ID:"));
        searchPanel.add(idField);
        searchPanel.add(new JLabel("Department:"));
        searchPanel.add(departmentField);
        searchPanel.add(new JLabel("Job Title:"));
        searchPanel.add(jobTitleField);
        searchPanel.add(searchButton);

        headerSearchPanel.add(headerPanel, BorderLayout.NORTH);
        headerSearchPanel.add(searchPanel, BorderLayout.CENTER);

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        employeeTable = new JTable();
        refreshTableData();
        JScrollPane tableScrollPane = new JScrollPane(employeeTable);

        tableContainer.add(tableScrollPane, BorderLayout.CENTER);

        mainPanel.add(headerSearchPanel, BorderLayout.NORTH);
        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(tableContainer, BorderLayout.CENTER);

        add(mainPanel);

        addButton.addActionListener(this::addEmployee);
        editButton.addActionListener(this::editEmployee);
        deleteButton.addActionListener(this::deleteEmployee);
        refreshButton.addActionListener(e -> refreshTableData());
        searchButton.addActionListener(this::searchEmployees);
    }


    private void initHeader(JPanel headerSearchPanel) {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setPreferredSize(new Dimension(150, 60));
        headerPanel.setBackground(new Color(0, 120, 160));

        JLabel logoLabel = new JLabel();
        URL logoResource = getClass().getResource("/images/logo.png");
        if (logoResource != null) {
            ImageIcon logoIcon = new ImageIcon(new ImageIcon(logoResource)
                    .getImage()
                    .getScaledInstance(150, 60, Image.SCALE_SMOOTH));
            logoLabel.setIcon(logoIcon);
        } else {
            System.err.println("Logo not found: /images/logo.png");
        }

        headerPanel.add(logoLabel);
        headerSearchPanel.add(headerPanel, BorderLayout.WEST);
    }


    private JButton createStyledButton(String text, String iconPath) {
        JButton button = new JButton(text);
        URL resource = getClass().getResource(iconPath);
        if (resource != null) {
            ImageIcon originalIcon = new ImageIcon(resource);
            Image scaledImage = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));
        } else {
            System.err.println("Icon not found: " + iconPath);
        }

        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 100, 140));
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 10));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(100, 30));
        button.setPreferredSize(new Dimension(100, 30));
        return button;
    }



    private void refreshTableData() {
        try {
            List<Employee> employees = ApiClient.getAllEmployees(AuthTokenHolder.getToken());

            DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Full Name", "Job Title", "Department"}, 0);
            for (Employee emp : employees) {
                model.addRow(new Object[]{emp.getId(), emp.getFullName(), emp.getJobTitle(), emp.getDepartment()});
            }

            employeeTable.setModel(model);
            employeeTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

        } catch (Exception ex) {
            handleException(ex);
        }
    }

    private void addEmployee(ActionEvent e) {
        JDialog dialog = new JDialog(this, "Add Employee", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField();
        JTextField jobTitleField = new JTextField();
        JTextField departmentField = new JTextField();
        JTextField hireDateField = new JTextField();
        JTextField employmentStatusField = new JTextField();
        JTextField contactInfoField = new JTextField();
        JTextField addressField = new JTextField();

        String[] labels = {"Name:", "Job Title:", "Department:", "Hire Date (yyyy-MM-dd):",
                "Employment Status:", "Contact Info:", "Address:"};
        JTextField[] fields = {nameField, jobTitleField, departmentField, hireDateField,
                employmentStatusField, contactInfoField, addressField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.3;
            dialog.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.7;
            dialog.add(fields[i], gbc);
        }

        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(0, 120, 160));
        addButton.setForeground(Color.WHITE);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(Color.GRAY);
        cancelButton.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        dialog.add(buttonPanel, gbc);

        addButton.addActionListener(event -> {
            try {
                Employee newEmployee = new Employee();
                newEmployee.setFullName(nameField.getText());
                newEmployee.setJobTitle(jobTitleField.getText());
                newEmployee.setDepartment(departmentField.getText());
                newEmployee.setHireDate(LocalDate.parse(hireDateField.getText()).toString());
                newEmployee.setEmploymentStatus(employmentStatusField.getText());
                newEmployee.setContactInfo(contactInfoField.getText());
                newEmployee.setAddress(addressField.getText());

                ApiClient.addEmployee(AuthTokenHolder.getToken(), newEmployee);
                refreshTableData();
                showNotification("Employee added successfully!", true, false);
                dialog.dispose();
            } catch (Exception ex) {
                if (ex.getMessage().contains("403")) {
                    showNotification("You don't have permission to add an employee.", false, true);
                } else {
                    showNotification("Error adding employee: " + ex.getMessage(), false, false);
                }
            }
        });


        cancelButton.addActionListener(event -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void editEmployee(ActionEvent e) {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            showNotification("Select an employee first!", false,false);
            return;
        }

        int id = (int) employeeTable.getValueAt(selectedRow, 0);

        try {
            Employee existingEmployee = ApiClient.getEmployeeById(AuthTokenHolder.getToken(), id);

            JDialog dialog = new JDialog(this, "Edit Employee", true);
            dialog.setLayout(new GridBagLayout());
            dialog.setSize(400, 500);
            dialog.setLocationRelativeTo(this);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField nameField = new JTextField(existingEmployee.getFullName());
            JTextField jobTitleField = new JTextField(existingEmployee.getJobTitle());
            JTextField departmentField = new JTextField(existingEmployee.getDepartment());
            JTextField hireDateField = new JTextField(existingEmployee.getHireDate());
            JTextField employmentStatusField = new JTextField(existingEmployee.getEmploymentStatus());
            JTextField contactInfoField = new JTextField(existingEmployee.getContactInfo());
            JTextField addressField = new JTextField(existingEmployee.getAddress());

            String[] labels = {
                    "Name:", "Job Title:", "Department:", "Hire Date (yyyy-MM-dd):",
                    "Employment Status:", "Contact Info:", "Address:"
            };
            JTextField[] fields = {
                    nameField, jobTitleField, departmentField, hireDateField,
                    employmentStatusField, contactInfoField, addressField
            };

            for (int i = 0; i < labels.length; i++) {
                gbc.gridx = 0;
                gbc.gridy = i;
                gbc.weightx = 0.3;
                dialog.add(new JLabel(labels[i]), gbc);

                gbc.gridx = 1;
                gbc.weightx = 0.7;
                dialog.add(fields[i], gbc);
            }

            JButton saveButton = new JButton("Save");
            saveButton.setBackground(new Color(0, 120, 160));
            saveButton.setForeground(Color.WHITE);
            JButton cancelButton = new JButton("Cancel");
            cancelButton.setBackground(Color.GRAY);
            cancelButton.setForeground(Color.WHITE);

            gbc.gridx = 0;
            gbc.gridy = labels.length;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
            dialog.add(buttonPanel, gbc);

            saveButton.addActionListener(event -> {
                try {
                    Employee updatedEmployee = new Employee();
                    updatedEmployee.setId(id);
                    updatedEmployee.setFullName(nameField.getText());
                    updatedEmployee.setJobTitle(jobTitleField.getText());
                    updatedEmployee.setDepartment(departmentField.getText());
                    updatedEmployee.setHireDate(LocalDate.parse(hireDateField.getText()).toString());
                    updatedEmployee.setEmploymentStatus(employmentStatusField.getText());
                    updatedEmployee.setContactInfo(contactInfoField.getText());
                    updatedEmployee.setAddress(addressField.getText());

                    ApiClient.updateEmployee(AuthTokenHolder.getToken(), id, updatedEmployee);
                    refreshTableData();
                    showNotification("Employee updated successfully!", true, false);
                    dialog.dispose();
                } catch (Exception ex) {
                    if (ex.getMessage().contains("403")) {
                        showNotification("You don't have permission to update this employee.", false, true);
                    } else {
                        showNotification("Error updating employee: " + ex.getMessage(), false, false);
                    }
                }
            });


            cancelButton.addActionListener(event -> dialog.dispose());

            dialog.setVisible(true);

        } catch (Exception ex) {
            showNotification("Error fetching employee details: " + ex.getMessage(), false, false);
        }
    }


    private void deleteEmployee(ActionEvent e) {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            showNotification("Select an employee first!", false, false);
            return;
        }

        int id = (int) employeeTable.getValueAt(selectedRow, 0);
        try {
            int deletedId = ApiClient.deleteEmployee(AuthTokenHolder.getToken(), id);
            refreshTableData();
            showNotification("Employee with ID " + deletedId + " deleted successfully!", true, false);
        } catch (Exception ex) {
            if (ex.getMessage().contains("403")) {
                showNotification("You don't have permission to delete this employee.", false, true);
            } else {
                showNotification(" " + ex.getMessage(), false, false);
            }
        }
    }


    private void searchEmployees(ActionEvent e) {
        String name = nameField.getText().trim();
        String idText = idField.getText().trim();
        String department = departmentField.getText().trim();
        String jobTitle = jobTitleField.getText().trim();

        Integer id = idText.isEmpty() ? null : Integer.parseInt(idText);

        try {
            List<Employee> employees = ApiClient.searchEmployees(
                    AuthTokenHolder.getToken(),
                    id,
                    name.isEmpty() ? null : name,
                    department.isEmpty() ? null : department,
                    jobTitle.isEmpty() ? null : jobTitle
            );

            DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Name", "Job Title", "Department"}, 0);
            for (Employee emp : employees) {
                model.addRow(new Object[]{emp.getId(), emp.getFullName(), emp.getJobTitle(), emp.getDepartment()});
            }
            employeeTable.setModel(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void handleException(Exception ex) {
        if (ex.getMessage().contains("403")) {
            showNotification("You don't have permission to perform this action.", false, true);
        } else {
            showNotification("An error occurred: " + ex.getMessage(), false, false);
        }
    }

    private static class CustomTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (isSelected) {
                component.setBackground(new Color(0, 100, 140));
                component.setForeground(Color.WHITE);
            } else if (row % 2 == 0) {
                component.setBackground(new Color(220, 240, 250));
                component.setForeground(Color.BLACK);
            } else {
                component.setBackground(new Color(200, 220, 230));
                component.setForeground(Color.BLACK);
            }

            component.setFont(new Font("Arial", Font.PLAIN, 12));

            return component;
        }
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(100, 30));
        textField.setFont(new Font("Arial", Font.PLAIN, 12));
        return textField;
    }

    private void showNotification(String message, boolean success, boolean permissionError) {
        JDialog notification = new JDialog(this, "Notification", true);
        notification.setUndecorated(true);
        notification.setSize(350, 200);
        notification.setLocationRelativeTo(this);
        notification.setLayout(new BorderLayout());

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(Color.WHITE);

        Color borderColor = success ? new Color(0, 200, 100)
                : permissionError ? new Color(255, 165, 0)
                : new Color(200, 50, 50);
        outerPanel.setBorder(BorderFactory.createLineBorder(borderColor, 4));

        JLabel iconLabel = new JLabel();
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        String iconPath = success
                ? "/icons/success.png"
                : permissionError
                ? "/icons/access-denied.png"
                : "/icons/error.png";
        URL iconResource = getClass().getResource(iconPath);
        if (iconResource != null) {
            iconLabel.setIcon(new ImageIcon(
                    new ImageIcon(iconResource)
                            .getImage()
                            .getScaledInstance(60, 60, Image.SCALE_SMOOTH)
            ));
        } else {
            System.err.println("Icon not found: " + iconPath);
            iconLabel.setText(success ? "✓" : "!");
            iconLabel.setFont(new Font("Arial", Font.BOLD, 48));
            iconLabel.setForeground(borderColor);
        }

        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        messageLabel.setForeground(borderColor);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton closeButton = new JButton("OK");
        closeButton.setBackground(borderColor);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setFocusPainted(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        closeButton.addActionListener(e -> notification.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(closeButton);

        outerPanel.add(iconLabel, BorderLayout.NORTH);
        outerPanel.add(messageLabel, BorderLayout.CENTER);
        outerPanel.add(buttonPanel, BorderLayout.SOUTH);

        notification.add(outerPanel);

        Timer fadeInTimer = new Timer(10, new AbstractAction() {
            float opacity = 0.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                if (opacity > 1.0f) {
                    opacity = 1.0f;
                    ((Timer) e.getSource()).stop();
                }
                notification.setOpacity(opacity);
            }
        });
        fadeInTimer.start();

        notification.setOpacity(0.0f);
        notification.setVisible(true);
    }



}
