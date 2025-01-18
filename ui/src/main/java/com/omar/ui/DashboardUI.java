package com.omar.ui;

import com.omar.api.ApiClient;
import com.omar.models.Employee;
import com.omar.utils.AuthTokenHolder;

import javax.swing.*;
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
        } else {
            System.err.println("Logo not found: /images/logo.png");
        }

        headerPanel.add(logoLabel, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        searchPanel.setBackground(new Color(0, 120, 160));
        nameField = new JTextField(10);
        idField = new JTextField(5);
        departmentField = new JTextField(10);
        jobTitleField = new JTextField(10);
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
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    private void addEmployee(ActionEvent e) {
        JTextField nameField = new JTextField();
        JTextField jobTitleField = new JTextField();
        JTextField departmentField = new JTextField();
        JTextField hireDateField = new JTextField();
        JTextField employmentStatusField = new JTextField();
        JTextField contactInfoField = new JTextField();
        JTextField addressField = new JTextField();

        Object[] fields = {
                "Name:", nameField,
                "Job Title:", jobTitleField,
                "Department:", departmentField,
                "Hire Date (yyyy-MM-dd):", hireDateField,
                "Employment Status:", employmentStatusField,
                "Contact Info:", contactInfoField,
                "Address:", addressField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Add Employee", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
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
                JOptionPane.showMessageDialog(this, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                if (ex.getMessage().contains("permission")) {
                    JOptionPane.showMessageDialog(this, "You don't have permission to add an employee.", "Permission Denied", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error adding employee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void editEmployee(ActionEvent e) {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an employee first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) employeeTable.getValueAt(selectedRow, 0);
        JTextField nameField = new JTextField((String) employeeTable.getValueAt(selectedRow, 1));
        JTextField jobTitleField = new JTextField((String) employeeTable.getValueAt(selectedRow, 2));
        JTextField departmentField = new JTextField((String) employeeTable.getValueAt(selectedRow, 3));
        JTextField hireDateField = new JTextField();
        JTextField employmentStatusField = new JTextField();
        JTextField contactInfoField = new JTextField();
        JTextField addressField = new JTextField();

        Object[] fields = {
                "Name:", nameField,
                "Job Title:", jobTitleField,
                "Department:", departmentField,
                "Hire Date (yyyy-MM-dd):", hireDateField,
                "Employment Status:", employmentStatusField,
                "Contact Info:", contactInfoField,
                "Address:", addressField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Edit Employee", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
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
                JOptionPane.showMessageDialog(this, "Employee updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating employee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteEmployee(ActionEvent e) {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an employee first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) employeeTable.getValueAt(selectedRow, 0);
        try {
            int deletedId = ApiClient.deleteEmployee(AuthTokenHolder.getToken(), id);
            refreshTableData();
            JOptionPane.showMessageDialog(this, "Employee with ID " + deletedId + " deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            if (ex.getMessage().contains("permission")) {
                JOptionPane.showMessageDialog(this, "You don't have permission to delete this employee.", "Permission Denied", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "You don't have permission to perform this action.", "Permission Denied", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
