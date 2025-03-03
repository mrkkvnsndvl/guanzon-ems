# Guanzon EMS (Employee Management System)

Guanzon EMS is a feature-rich desktop application designed to manage employee-related operations efficiently. It includes modules for employee management, attendance tracking, payroll processing, department handling, and reporting. Built using JavaFX, Maven, and MariaDB, this system ensures a seamless user experience and reliable backend functionality.

---

## Features

### 1. **Dashboard**
   - Central hub for navigating to different modules.
   - Displays real-time statistics such as total employees and departments.
   - Embedded web view for company updates.

### 2. **Employee Management**
   - Add, update, and delete employee records.
   - Validate employee details like email, phone number, and position.
   - Filter employees by position and department.

### 3. **Department Management**
   - Create, update, and delete departments.
   - Ensure unique department names.
   - Search and filter departments.

### 4. **Attendance Tracking**
   - Daily and monthly attendance tracking.
   - Editable attendance statuses (Present, Absent, Late, On Leave).
   - Export attendance data to CSV.

### 5. **Payroll Processing**
   - Calculate deductions (SSS, PhilHealth, Pag-IBIG, Withholding Tax).
   - Generate detailed payslips using JasperReports.
   - Support for monthly and semi-monthly pay frequencies.

### 6. **Reporting**
   - Generate reports on employee counts by department.
   - Visualize data using bar charts.
   - Export employee data to CSV.

### 7. **Authentication**
   - Secure admin login with email and password validation.
   - Sign-out confirmation dialog.

---

## Technologies Used

- **Frontend**: JavaFX, FXML, CSS
- **Backend**: Java, Maven
- **Database**: MariaDB
- **Reporting**: JasperReports
- **Utilities**: SLF4J for logging, Jackson for JSON processing
