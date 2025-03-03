package com.project.guanzonems.controller;

import com.project.guanzonems.dao.EmployeeDAO;
import com.project.guanzonems.model.Employee;
import com.project.guanzonems.utilities.SonnerUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class ManagePayrollController implements Initializable {

    @FXML
    private AnchorPane managePayrollAnchorPane;
    @FXML
    private TextField employeeNameTextField;
    @FXML
    private ListView<String> employeeAutoCompleteListView;
    @FXML
    private TextField grossSalaryTextField;
    @FXML
    private TextField positionTextField;
    @FXML
    private ChoiceBox<String> payFrequencyChoiceBox;
    @FXML
    private Text employeeFullNameText;
    @FXML
    private Text positionText;
    @FXML
    private Text payFrequencyText;
    @FXML
    private Text grossSalaryText;
    @FXML
    private Text sssContributionText;
    @FXML
    private Text philhealthContributionText;
    @FXML
    private Text pagibigContributionText;
    @FXML
    private Text withholdingTaxText;
    @FXML
    private Text totalDeductionsText;
    @FXML
    private Text netSalaryText;
    @FXML
    private Text forThePeriodEndingText;
    @FXML
    private Text payslipEmployeeFullNameText;
    @FXML
    private Text payslipPositionText;
    @FXML
    private Text payslipPayFrequencyText;
    @FXML
    private Text payslipBasicSalaryText;
    @FXML
    private Text payslipSssContributionText;
    @FXML
    private Text payslipPhilhealthContributionText;
    @FXML
    private Text payslipPagibigContributionText;
    @FXML
    private Text payslipWithholdingTaxText;
    @FXML
    private Text payslipTotalDeductionsText;
    @FXML
    private Text payslipNetPayText;

    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private ObservableList<String> employeeNames = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadEmployeeNames();
        setupAutoComplete();
        setupPayFrequencyChoiceBox();
    }

    @FXML
    private void calculatePayrollButtonOnAction(ActionEvent event) {
        Stage ownerStage = (Stage) managePayrollAnchorPane.getScene().getWindow();
        try {
            String selectedEmployee = employeeNameTextField.getText();
            String payFrequency = payFrequencyChoiceBox.getValue();
            Employee employee = employeeDAO.getEmployeeByFullName(selectedEmployee);
            if (employee == null) {
                SonnerUtility.show(ownerStage, "Error", "No employee selected.");
                return;
            }
            double grossSalary = employee.getLdSalary();
            if ("Semi-monthly".equals(payFrequency)) {
                grossSalary /= 2;
            }
            double sss = calculateSSS(grossSalary);
            double philhealth = calculatePhilHealth(grossSalary);
            double pagibig = calculatePagIBIG(grossSalary);
            double withholdingTax = calculateWithholdingTax(grossSalary);
            double totalDeductions = sss + philhealth + pagibig + withholdingTax;
            double netSalary = grossSalary - totalDeductions;
            employeeFullNameText.setText(employee.getLsFullName());
            positionText.setText(employee.getLsPosition());
            payFrequencyText.setText(payFrequency);
            grossSalaryText.setText(String.format("₱%,.2f", grossSalary));
            sssContributionText.setText(String.format("₱%,.2f", sss));
            philhealthContributionText.setText(String.format("₱%,.2f", philhealth));
            pagibigContributionText.setText(String.format("₱%,.2f", pagibig));
            withholdingTaxText.setText(String.format("₱%,.2f", withholdingTax));
            totalDeductionsText.setText(String.format("₱%,.2f", totalDeductions));
            netSalaryText.setText(String.format("₱%,.2f", netSalary));
            updatePayslipDetails(employee, LocalDate.now());
            SonnerUtility.show(ownerStage, "Success", "Please switch to the summary tab.");
        } catch (SQLException e) {
            e.printStackTrace();
            SonnerUtility.show(ownerStage, "Error", "Failed to calculate payroll.");
        }
    }

    @FXML
    private void printPayslipButtonOnAction(ActionEvent event) {
        if (payslipEmployeeFullNameText.getText().isEmpty()) {
            SonnerUtility.show(
                    managePayrollAnchorPane.getScene().getWindow(),
                    "Warning",
                    "Please calculate the payroll first before printing."
            );
            return;
        }

        try {
            String jrxmlPath = "/com/project/guanzonems/jrxml/Payslip.jrxml";
            JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream(jrxmlPath));
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("EMPLOYEE_NAME", payslipEmployeeFullNameText.getText());
            parameters.put("POSITION", payslipPositionText.getText());
            parameters.put("PAY_FREQUENCY", payslipPayFrequencyText.getText());
            parameters.put("GROSS_SALARY", parseCurrency(payslipBasicSalaryText.getText()));
            parameters.put("SSS", parseCurrency(payslipSssContributionText.getText()));
            parameters.put("PHILHEALTH", parseCurrency(payslipPhilhealthContributionText.getText()));
            parameters.put("PAGIBIG", parseCurrency(payslipPagibigContributionText.getText()));
            parameters.put("WITHHOLDING_TAX", parseCurrency(payslipWithholdingTaxText.getText()));
            parameters.put("TOTAL_DEDUCTIONS", parseCurrency(payslipTotalDeductionsText.getText()));
            parameters.put("NET_PAY", parseCurrency(payslipNetPayText.getText()));
            parameters.put("PERIOD_END", forThePeriodEndingText.getText());
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters,
                    new JREmptyDataSource()
            );
            JasperViewer.viewReport(jasperPrint, false);
            SonnerUtility.show(
                    managePayrollAnchorPane.getScene().getWindow(),
                    "Success",
                    "Payslip generated successfully!"
            );
        } catch (JRException e) {
            e.printStackTrace();
            SonnerUtility.show(
                    managePayrollAnchorPane.getScene().getWindow(),
                    "Error",
                    "Failed to generate payslip: " + e.getMessage()
            );
        }
    }

    private double calculateSSS(double grossSalary) {
        if (grossSalary <= 2475) {
            return 135;
        } else if (grossSalary <= 2500) {
            return 150;
        }
        return 1125;
    }

    private double calculatePhilHealth(double grossSalary) {
        double contribution = grossSalary * 0.01375;
        return Math.min(contribution, 550);
    }

    private double calculatePagIBIG(double grossSalary) {
        return Math.min(grossSalary * 0.01, 100);
    }

    private double calculateWithholdingTax(double grossSalary) {
        if (grossSalary <= 20833) {
            return 0;
        } else if (grossSalary <= 33333) {
            return (grossSalary - 20833) * 0.15;
        } else if (grossSalary <= 66667) {
            return 2500 + (grossSalary - 33333) * 0.20;
        } else if (grossSalary <= 166667) {
            return 10833.33 + (grossSalary - 66667) * 0.25;
        } else if (grossSalary <= 666667) {
            return 40833.33 + (grossSalary - 166667) * 0.30;
        } else {
            return 195833.33 + (grossSalary - 666667) * 0.35;
        }
    }

    private void loadEmployeeNames() {
        try {
            List<Employee> employees = employeeDAO.readEmployees();
            for (Employee employee : employees) {
                employeeNames.add(employee.getLsFullName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupAutoComplete() {
        employeeAutoCompleteListView.setItems(employeeNames);
        employeeNameTextField.setOnKeyReleased(this::handleAutoComplete);
        employeeAutoCompleteListView.setOnMouseClicked(event -> {
            String selectedEmployee = employeeAutoCompleteListView.getSelectionModel().getSelectedItem();
            if (selectedEmployee != null) {
                employeeNameTextField.setText(selectedEmployee);
                employeeAutoCompleteListView.setVisible(false);
            }
            try {
                Employee employee = employeeDAO.getEmployeeByFullName(selectedEmployee);
                if (employee != null) {
                    double baseSalary = employee.getLdSalary();
                    positionTextField.setText(employee.getLsPosition());
                    updateGrossSalaryBasedOnFrequency();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void handleAutoComplete(KeyEvent event) {
        String userInput = employeeNameTextField.getText();
        if (userInput == null || userInput.trim().isEmpty()) {
            employeeAutoCompleteListView.setVisible(false);
            return;
        }
        ObservableList<String> filteredList = FXCollections.observableArrayList();
        for (String name : employeeNames) {
            if (name.toLowerCase().contains(userInput.toLowerCase())) {
                filteredList.add(name);
            }
        }
        employeeAutoCompleteListView.setItems(filteredList);
        employeeAutoCompleteListView.setVisible(!filteredList.isEmpty());
    }

    private void setupPayFrequencyChoiceBox() {
        ObservableList<String> payFrequencies = FXCollections.observableArrayList("Monthly", "Semi-monthly");
        payFrequencyChoiceBox.setItems(payFrequencies);
        payFrequencyChoiceBox.getSelectionModel().selectFirst();
        payFrequencyChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updateGrossSalaryBasedOnFrequency();
        });
    }

    private void updateGrossSalaryBasedOnFrequency() {
        String selectedEmployee = employeeNameTextField.getText();
        if (selectedEmployee == null || selectedEmployee.isEmpty()) {
            return;
        }
        try {
            Employee employee = employeeDAO.getEmployeeByFullName(selectedEmployee);
            if (employee != null) {
                double baseSalary = employee.getLdSalary();
                String frequency = payFrequencyChoiceBox.getValue();
                if ("Semi-monthly".equals(frequency)) {
                    grossSalaryTextField.setText(String.format("%.2f", baseSalary / 2));
                } else {
                    grossSalaryTextField.setText(String.valueOf(baseSalary));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePayslipDetails(Employee employee, LocalDate periodEndDate) {
        payslipEmployeeFullNameText.setText(employee.getLsFullName());
        payslipPositionText.setText(employee.getLsPosition());
        String payFrequency = payFrequencyChoiceBox.getValue();
        payslipPayFrequencyText.setText(payFrequency);
        double grossSalary = employee.getLdSalary();
        if ("Semi-monthly".equals(payFrequency)) {
            grossSalary /= 2;
        }
        payslipBasicSalaryText.setText(String.format("₱%,.2f", grossSalary));
        double sss = calculateSSS(grossSalary);
        double philhealth = calculatePhilHealth(grossSalary);
        double pagibig = calculatePagIBIG(grossSalary);
        double withholdingTax = calculateWithholdingTax(grossSalary);
        double totalDeductions = sss + philhealth + pagibig + withholdingTax;
        double netPay = grossSalary - totalDeductions;
        payslipSssContributionText.setText(String.format("₱%,.2f", sss));
        payslipPhilhealthContributionText.setText(String.format("₱%,.2f", philhealth));
        payslipPagibigContributionText.setText(String.format("₱%,.2f", pagibig));
        payslipWithholdingTaxText.setText(String.format("₱%,.2f", withholdingTax));
        payslipTotalDeductionsText.setText(String.format("₱%,.2f", totalDeductions));
        payslipNetPayText.setText(String.format("₱%,.2f", netPay));
        forThePeriodEndingText.setText(periodEndDate.toString());
    }

    private Double parseCurrency(String value) {
        return Double.valueOf(value.replaceAll("[^\\d.]", ""));
    }
}
