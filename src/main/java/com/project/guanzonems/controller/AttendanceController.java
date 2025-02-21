package com.project.guanzonems.controller;

import com.project.guanzonems.dao.DailyAttendanceDAO;
import com.project.guanzonems.dao.MonthlyAttendanceDAO;
import com.project.guanzonems.model.DailyAttendance;
import com.project.guanzonems.model.MonthlyAttendance;
import com.project.guanzonems.utilities.SonnerUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.converter.LocalTimeStringConverter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;

public class AttendanceController implements Initializable {

    @FXML
    private AnchorPane attendanceAnchorPane;
    @FXML
    private DatePicker selectDateDatePicker;
    @FXML
    private TableView<DailyAttendance> dailyAttendanceTableView;
    @FXML
    private TableColumn<DailyAttendance, Integer> employeeIdTableColumn;
    @FXML
    private TableColumn<DailyAttendance, String> fullNameTableColumn;
    @FXML
    private TableColumn<DailyAttendance, String> departmentTableColumn;
    @FXML
    private TableColumn<DailyAttendance, String> statusTableColumn;
    @FXML
    private TableColumn<DailyAttendance, LocalTime> timeInTableColumn;
    @FXML
    private TableColumn<DailyAttendance, LocalTime> timeOutTableColumn;
    @FXML
    private Text totalEmployeesText;
    @FXML
    private Text workingDaysText;
    @FXML
    private Text overallAttendancePercentageText;
    @FXML
    private ChoiceBox<String> selectMonthChoiceBox;
    @FXML
    private ChoiceBox<String> departmentsChoiceBox;
    @FXML
    private TextField searchEmployeeTextField;
    @FXML
    private TableView<MonthlyAttendance> monthlyAttendanceTableView;
    @FXML
    private TableColumn<MonthlyAttendance, Integer> mEmployeeIdTableColumn;
    @FXML
    private TableColumn<MonthlyAttendance, String> mFullNameTableColumn;
    @FXML
    private TableColumn<MonthlyAttendance, String> mDepartmentTableColumn;
    @FXML
    private TableColumn<MonthlyAttendance, Integer> mPresentDaysTableColumn;
    @FXML
    private TableColumn<MonthlyAttendance, Integer> mLateDaysTableColumn;
    @FXML
    private TableColumn<MonthlyAttendance, Integer> mAbsentDaysTableColumn;
    @FXML
    private TableColumn<MonthlyAttendance, Integer> mOnLeaveDaysTableColumn;
    @FXML
    private TableColumn<MonthlyAttendance, Double> mAttendancePercentageTableColumn;

    private final ObservableList<DailyAttendance> attendanceData = FXCollections.observableArrayList();
    private final ObservableList<MonthlyAttendance> monthlyAttendanceData = FXCollections.observableArrayList();
    private final DailyAttendanceDAO dailyAttendanceDAO = new DailyAttendanceDAO();
    private final MonthlyAttendanceDAO monthlyAttendanceDAO = new MonthlyAttendanceDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectDateDatePicker.setValue(LocalDate.now());
        dailyAttendanceTableView.setEditable(true);
        employeeIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("lnId"));
        fullNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("lsFullName"));
        departmentTableColumn.setCellValueFactory(new PropertyValueFactory<>("lsDepartment"));
        statusTableColumn.setCellValueFactory(new PropertyValueFactory<>("lsStatus"));
        statusTableColumn.setCellFactory(ComboBoxTableCell.forTableColumn("Present", "Absent", "Late", "On Leave"));
        statusTableColumn.setOnEditCommit(event -> {
            DailyAttendance attendance = event.getRowValue();
            attendance.setLsStatus(event.getNewValue());
            updateAttendance(attendance);
        });
        timeInTableColumn.setCellValueFactory(new PropertyValueFactory<>("ltTimeIn"));
        timeInTableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalTimeStringConverter(DateTimeFormatter.ofPattern("HH:mm"), null)));
        timeInTableColumn.setOnEditCommit(event -> {
            DailyAttendance attendance = event.getRowValue();
            attendance.setLtTimeIn(event.getNewValue());
            updateAttendance(attendance);
        });
        timeOutTableColumn.setCellValueFactory(new PropertyValueFactory<>("ltTimeOut"));
        timeOutTableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalTimeStringConverter(DateTimeFormatter.ofPattern("HH:mm"), null)));
        timeOutTableColumn.setOnEditCommit(event -> {
            DailyAttendance attendance = event.getRowValue();
            attendance.setLtTimeOut(event.getNewValue());
            updateAttendance(attendance);
        });
        initializeMonthChoiceBox();
        initializeDepartmentsChoiceBox();
        initializeSearchFunctionality();
        loadAttendanceData(LocalDate.now());
        loadMonthlyAttendanceData(YearMonth.now());
    }

    private void initializeMonthChoiceBox() {
        ObservableList<String> months = FXCollections.observableArrayList();
        int currentYear = LocalDate.now().getYear();
        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(currentYear, month);
            months.add(yearMonth.toString());
        }
        selectMonthChoiceBox.setItems(months);
        selectMonthChoiceBox.setValue(YearMonth.now().toString());
        selectMonthChoiceBox.setOnAction(this::selectMonthChoiceBoxOnAction);
    }

    private void initializeDepartmentsChoiceBox() {
        List<String> departments = monthlyAttendanceDAO.getDepartments();
        ObservableList<String> departmentOptions = FXCollections.observableArrayList(departments);
        departmentOptions.add(0, "All Departments");
        departmentsChoiceBox.setItems(departmentOptions);
        departmentsChoiceBox.setValue("All Departments");
        departmentsChoiceBox.setOnAction(this::departmentsChoiceBoxOnAction);
    }

    private void initializeSearchFunctionality() {
        FilteredList<MonthlyAttendance> filteredData = new FilteredList<>(monthlyAttendanceData, b -> true);
        searchEmployeeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(monthlyAttendance -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return monthlyAttendance.getLsFullName().toLowerCase().contains(lowerCaseFilter);
            });
        });
        SortedList<MonthlyAttendance> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(monthlyAttendanceTableView.comparatorProperty());
        monthlyAttendanceTableView.setItems(sortedData);
    }

    @FXML
    private void selectDateDatePickerOnAction(ActionEvent event) {
        LocalDate selectedDate = selectDateDatePicker.getValue();
        if (selectedDate != null) {
            loadAttendanceData(selectedDate);
            loadMonthlyAttendanceData(YearMonth.from(selectedDate));
        } else {
            SonnerUtility.show(attendanceAnchorPane.getScene().getWindow(), "Error", "Please select a date.");
        }
    }

    private void loadAttendanceData(LocalDate ldDate) {
        attendanceData.clear();
        attendanceData.addAll(dailyAttendanceDAO.getDailyAttendanceByDate(ldDate));
        dailyAttendanceTableView.setItems(attendanceData);
    }

    private void updateAttendance(DailyAttendance attendance) {
        LocalDate selectedDate = selectDateDatePicker.getValue();
        if (selectedDate != null) {
            attendance.setLdDate(selectedDate);
        } else {
            SonnerUtility.show(attendanceAnchorPane.getScene().getWindow(), "Error", "Please select a date.");
            return;
        }
        boolean isUpdated = dailyAttendanceDAO.saveDailyAttendance(attendance);
        if (isUpdated) {
            SonnerUtility.show(attendanceAnchorPane.getScene().getWindow(), "Success", "Attendance updated successfully.");
        } else {
            SonnerUtility.show(attendanceAnchorPane.getScene().getWindow(), "Error", "Failed to update attendance.");
        }
    }

    private void loadMonthlyAttendanceData(YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        List<MonthlyAttendance> monthlyAttendances = monthlyAttendanceDAO.getMonthlyAttendance(startDate, endDate);
        for (MonthlyAttendance monthlyAttendance : monthlyAttendances) {
            monthlyAttendanceDAO.saveMonthlyAttendance(monthlyAttendance, yearMonth);
        }
        monthlyAttendanceData.clear();
        monthlyAttendanceData.addAll(monthlyAttendanceDAO.getStoredMonthlyAttendance(yearMonth));
        mEmployeeIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("lnId"));
        mFullNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("lsFullName"));
        mDepartmentTableColumn.setCellValueFactory(new PropertyValueFactory<>("lsDepartment"));
        mPresentDaysTableColumn.setCellValueFactory(new PropertyValueFactory<>("lnPresentDays"));
        mLateDaysTableColumn.setCellValueFactory(new PropertyValueFactory<>("lnLateDays"));
        mAbsentDaysTableColumn.setCellValueFactory(new PropertyValueFactory<>("lnAbsentDays"));
        mOnLeaveDaysTableColumn.setCellValueFactory(new PropertyValueFactory<>("lnOnLeaveDays"));
        mAttendancePercentageTableColumn.setCellValueFactory(new PropertyValueFactory<>("ldAttendancePercentage"));
        applyDepartmentFilter(yearMonth, departmentsChoiceBox.getValue());
        int totalEmployees = monthlyAttendanceDAO.getTotalEmployeesWithAttendance(yearMonth);
        int workingDays = monthlyAttendanceDAO.getWorkingDays(yearMonth);
        double overallAttendancePercentage = calculateOverallAttendancePercentage(monthlyAttendanceData, workingDays);
        totalEmployeesText.setText(String.valueOf(totalEmployees));
        workingDaysText.setText(String.valueOf(workingDays));
        overallAttendancePercentageText.setText(String.format("%.2f%%", overallAttendancePercentage));
    }

    @FXML
    private void selectMonthChoiceBoxOnAction(ActionEvent event) {
        String selectedMonth = selectMonthChoiceBox.getValue();
        if (selectedMonth != null) {
            YearMonth yearMonth = YearMonth.parse(selectedMonth);
            loadMonthlyAttendanceData(yearMonth);
        }
    }

    @FXML
    private void departmentsChoiceBoxOnAction(ActionEvent event) {
        String selectedDepartment = departmentsChoiceBox.getValue();
        YearMonth selectedYearMonth = YearMonth.parse(selectMonthChoiceBox.getValue());
        applyDepartmentFilter(selectedYearMonth, selectedDepartment);
    }

    private void applyDepartmentFilter(YearMonth yearMonth, String department) {
        if ("All Departments".equals(department)) {
            monthlyAttendanceData.clear();
            monthlyAttendanceData.addAll(monthlyAttendanceDAO.getStoredMonthlyAttendance(yearMonth));
        } else {
            monthlyAttendanceData.clear();
            monthlyAttendanceData.addAll(monthlyAttendanceDAO.getStoredMonthlyAttendanceByDepartment(yearMonth, department));
        }
    }

    private double calculateOverallAttendancePercentage(ObservableList<MonthlyAttendance> monthlyAttendances, int workingDays) {
        if (workingDays == 0) {
            return 0.0;
        }
        int totalPresentDays = 0;
        for (MonthlyAttendance monthlyAttendance : monthlyAttendances) {
            totalPresentDays += monthlyAttendance.getLnPresentDays();
        }
        int totalEmployees = monthlyAttendances.size();
        if (totalEmployees == 0) {
            return 0.0;
        }
        return ((double) totalPresentDays / (totalEmployees * workingDays)) * 100;
    }

    @FXML
    private void exportToCsvButtonOnAction(ActionEvent event) {
        YearMonth selectedYearMonth = YearMonth.parse(selectMonthChoiceBox.getValue());
        String defaultFileName = String.format("monthly-attendance-%04d-%02d.csv", selectedYearMonth.getYear(), selectedYearMonth.getMonthValue());
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Monthly Attendance to CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName(defaultFileName);
        File selectedFile = fileChooser.showSaveDialog(attendanceAnchorPane.getScene().getWindow());
        if (selectedFile != null) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(selectedFile))) {
                bw.write("Employee ID,Full Name,Department,Present Days,Late Days,Absent Days,On Leave Days,Attendance Percentage\n");
                for (MonthlyAttendance monthlyAttendance : monthlyAttendanceData) {
                    bw.write(String.format("%d,%s,%s,%d,%d,%d,%d,%.2f%%\n",
                            monthlyAttendance.getLnId(),
                            monthlyAttendance.getLsFullName(),
                            monthlyAttendance.getLsDepartment(),
                            monthlyAttendance.getLnPresentDays(),
                            monthlyAttendance.getLnLateDays(),
                            monthlyAttendance.getLnAbsentDays(),
                            monthlyAttendance.getLnOnLeaveDays(),
                            monthlyAttendance.getLdAttendancePercentage()));
                }
                SonnerUtility.show(attendanceAnchorPane.getScene().getWindow(), "Success", "Monthly attendance exported successfully.");
            } catch (IOException e) {
                SonnerUtility.show(attendanceAnchorPane.getScene().getWindow(), "Error", "Failed to export monthly attendance: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void refreshButtonOnAction(ActionEvent event) {
        searchEmployeeTextField.clear();
        departmentsChoiceBox.setValue("All Departments");
        selectMonthChoiceBox.setValue(YearMonth.now().toString());
        LocalDate selectedDate = LocalDate.now();
        selectDateDatePicker.setValue(selectedDate);
        loadAttendanceData(selectedDate);
        YearMonth selectedYearMonth = YearMonth.now();
        loadMonthlyAttendanceData(selectedYearMonth);
    }
}
