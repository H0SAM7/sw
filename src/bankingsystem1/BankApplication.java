package bankingsystem1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


// ==========================================
// MAIN APPLICATION CLASS
// ==========================================
public class BankApplication {

    public static void main(String[] args) {
        // Show login dialog first
        if(showLoginDialog()) {
            SwingUtilities.invokeLater(() -> {
                new BankGUI().setVisible(true);
            });
        } else {
            System.exit(0);
        }
    }

    private static boolean showLoginDialog() {
        LoginDialog login = new LoginDialog(null);
        login.setVisible(true);
        return SessionManager.getInstance().getCurrentUserId() != null;
    }

    // ==========================================
    // SINGLETON PATTERN CLASSES
    // ==========================================
    // SessionManager - Singleton
    static class SessionManager {
        private static SessionManager instance;
        private String currentUserId;

        private SessionManager() { }

        public static synchronized SessionManager getInstance() {
            if(instance == null) {
                instance = new SessionManager();
            }
            return instance;
        }

        public boolean authenticate(String username, String password) {
            // Mock authentication
            if(username != null && !username.trim().isEmpty()) {
                currentUserId = username;
                return true;
            }
            return false;
        }

        public String getCurrentUserId() {
            return currentUserId;
        }

        public void logout() {
            currentUserId = null;
        }
    }

    // TransactionManager - Singleton
    static class TransactionManager {
        private static TransactionManager instance;
        private List<Transaction> transactions = new ArrayList<>();

        private TransactionManager() { }

        public static synchronized TransactionManager getInstance() {
            if(instance == null) {
                instance = new TransactionManager();
            }
            return instance;
        }

        public void addTransaction(Transaction t) {
            transactions.add(t);
        }

        public List<Transaction> getAllTransactions() {
            return transactions;
        }
    }

    // ==========================================
    // FACTORY PATTERN CLASSES
    // ==========================================
    // Abstract Account
    static abstract class Account {
        protected String accountNumber;
        protected double balance;

        public Account(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public abstract String getAccountType();
        public double getBalance() { return balance; }
        public void deposit(double amount) { balance += amount; }
        public void withdraw(double amount) { balance -= amount; }
    }

    static class SavingsAccount extends Account {
        public SavingsAccount(String accountNumber) {
            super(accountNumber);
        }

        @Override
        public String getAccountType() {
            return "Savings Account";
        }
    }

    static class CheckingAccount extends Account {
        public CheckingAccount(String accountNumber) {
            super(accountNumber);
        }

        @Override
        public String getAccountType() {
            return "Checking Account";
        }
    }

    static class LoanAccount extends Account {
        public LoanAccount(String accountNumber) {
            super(accountNumber);
        }

        @Override
        public String getAccountType() {
            return "Loan Account";
        }
    }

    // AccountFactory
    static class AccountFactory {
        public static Account createAccount(String type, String accountNumber) {
            switch(type.toLowerCase()) {
                case "savings": return new SavingsAccount(accountNumber);
                case "checking": return new CheckingAccount(accountNumber);
                case "loan": return new LoanAccount(accountNumber);
                default: throw new IllegalArgumentException("Unknown account type");
            }
        }
    }

    // Abstract Loan
    static abstract class Loan {
        protected double principal;
        protected double interestRate;
        protected int termMonths;

        public abstract String getLoanType();
        public abstract double calculateMonthlyPayment();
    }

    static class HomeLoan extends Loan {
        public HomeLoan(double principal, double interestRate, int termMonths) {
            this.principal = principal;
            this.interestRate = interestRate;
            this.termMonths = termMonths;
        }

        @Override
        public String getLoanType() {
            return "Home Loan";
        }

        @Override
        public double calculateMonthlyPayment() {
            double monthlyRate = interestRate / 12;
            return (principal * monthlyRate) / (1 - Math.pow(1+monthlyRate, -termMonths));
        }
    }

    static class CarLoan extends Loan {
        public CarLoan(double principal, double interestRate, int termMonths) {
            this.principal = principal;
            this.interestRate = interestRate;
            this.termMonths = termMonths;
        }

        @Override
        public String getLoanType() {
            return "Car Loan";
        }

        @Override
        public double calculateMonthlyPayment() {
            double monthlyRate = interestRate / 12;
            return (principal * monthlyRate) / (1 - Math.pow(1+monthlyRate, -termMonths));
        }
    }

    static class PersonalLoan extends Loan {
        public PersonalLoan(double principal, double interestRate, int termMonths) {
            this.principal = principal;
            this.interestRate = interestRate;
            this.termMonths = termMonths;
        }

        @Override
        public String getLoanType() {
            return "Personal Loan";
        }

        @Override
        public double calculateMonthlyPayment() {
            double monthlyRate = interestRate / 12;
            return (principal * monthlyRate) / (1 - Math.pow(1+monthlyRate, -termMonths));
        }
    }

    // LoanFactory
    static class LoanFactory {
        public static Loan createLoan(String type, double principal, double interestRate, int termMonths) {
            switch(type.toLowerCase()) {
                case "home": return new HomeLoan(principal, interestRate, termMonths);
                case "car": return new CarLoan(principal, interestRate, termMonths);
                case "personal": return new PersonalLoan(principal, interestRate, termMonths);
                default: throw new IllegalArgumentException("Unknown loan type");
            }
        }
    }

    // ==========================================
    // BUILDER PATTERN
    // ==========================================
    static class Customer {
        private String name;
        private String nationalId;
        private List<String> addresses;
        private List<String> phoneNumbers;
        private List<String> emails;

        private Customer(String name, String nationalId, List<String> addresses,
                         List<String> phoneNumbers, List<String> emails) {
            this.name = name;
            this.nationalId = nationalId;
            this.addresses = addresses;
            this.phoneNumbers = phoneNumbers;
            this.emails = emails;
        }

        public String getName() { return name; }
        public String getNationalId() { return nationalId; }
        public List<String> getAddresses() { return addresses; }
        public List<String> getPhoneNumbers() { return phoneNumbers; }
        public List<String> getEmails() { return emails; }

        public static class CustomerBuilder {
            private String name;
            private String nationalId;
            private List<String> addresses;
            private List<String> phoneNumbers;
            private List<String> emails;

            public CustomerBuilder setName(String name) {
                this.name = name;
                return this;
            }

            public CustomerBuilder setNationalId(String nationalId) {
                this.nationalId = nationalId;
                return this;
            }

            public CustomerBuilder setAddresses(List<String> addresses) {
                this.addresses = addresses;
                return this;
            }

            public CustomerBuilder setPhoneNumbers(List<String> phoneNumbers) {
                this.phoneNumbers = phoneNumbers;
                return this;
            }

            public CustomerBuilder setEmails(List<String> emails) {
                this.emails = emails;
                return this;
            }

            public Customer build() {
                return new Customer(name, nationalId, addresses, phoneNumbers, emails);
            }
        }
    }

    // ==========================================
    // PROTOTYPE PATTERN
    // ==========================================
    static abstract class Transaction implements Cloneable {
        protected String transactionId;
        protected String fromAccount;
        protected String toAccount;
        protected double amount;

        public Transaction(String transactionId, String fromAccount, String toAccount, double amount) {
            this.transactionId = transactionId;
            this.fromAccount = fromAccount;
            this.toAccount = toAccount;
            this.amount = amount;
        }

        public abstract String getTransactionType();

        @Override
        public Transaction clone() {
            try {
                return (Transaction)super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class RegularPaymentTransaction extends Transaction {
        public RegularPaymentTransaction(String transactionId, String fromAccount, String toAccount, double amount) {
            super(transactionId, fromAccount, toAccount, amount);
        }

        @Override
        public String getTransactionType() {
            return "Regular Payment";
        }
    }

    static class TransactionPrototypeRegistry {
        private static Map<String, Transaction> prototypes = new HashMap<>();

        static {
            prototypes.put("monthlyPayment", new RegularPaymentTransaction("T000", "A001", "A002", 100.0));
        }

        public static Transaction getPrototype(String key) {
            Transaction prototype = prototypes.get(key);
            if(prototype != null) {
                return prototype.clone();
            }
            return null;
        }
    }

    // ==========================================
    // ADAPTER PATTERN
    // ==========================================
    interface CurrencyConverter {
        double convertToUSD(double amount, String fromCurrency);
        double convertFromUSD(double amount, String toCurrency);
    }

    // Legacy code we can't modify
    static class LegacyCurrencyConverter {
        public double convert(String fromCurrency, String toCurrency, double amount) {
            // Mock conversion
            return amount * 0.85; 
        }
    }

    static class CurrencyConverterAdapter implements CurrencyConverter {
        private LegacyCurrencyConverter legacyConverter;

        public CurrencyConverterAdapter(LegacyCurrencyConverter legacyConverter) {
            this.legacyConverter = legacyConverter;
        }

        @Override
        public double convertToUSD(double amount, String fromCurrency) {
            return legacyConverter.convert(fromCurrency, "USD", amount);
        }

        @Override
        public double convertFromUSD(double amount, String toCurrency) {
            return legacyConverter.convert("USD", toCurrency, amount);
        }
    }

    // ==========================================
    // SIMPLE GUI (SWING)
    // ==========================================
    static class BankGUI extends JFrame {
        private AccountPanel accountPanel;
        private TransactionPanel transactionPanel;
        private LoanPanel loanPanel;

        public BankGUI() {
            super("Banking System");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            JTabbedPane tabbedPane = new JTabbedPane();

            accountPanel = new AccountPanel();
            transactionPanel = new TransactionPanel();
            loanPanel = new LoanPanel();

            tabbedPane.addTab("Accounts", accountPanel);
            tabbedPane.addTab("Transactions", transactionPanel);
            tabbedPane.addTab("Loans", loanPanel);

            add(tabbedPane, BorderLayout.CENTER);

            setSize(800, 600);
            setLocationRelativeTo(null);
        }
    }

    // Simple login dialog
    static class LoginDialog extends JDialog {
        private JTextField userField;
        private JPasswordField passField;
        private JButton loginButton;

        public LoginDialog(Frame owner) {
            super(owner, "Login", true);
            setLayout(new GridLayout(3,2));
            add(new JLabel("Username:"));
            userField = new JTextField();
            add(userField);

            add(new JLabel("Password:"));
            passField = new JPasswordField();
            add(passField);

            loginButton = new JButton("Login");
            add(new JLabel()); // filler
            add(loginButton);

            loginButton.addActionListener(e -> {
                String username = userField.getText();
                String password = new String(passField.getPassword());

                if(SessionManager.getInstance().authenticate(username, password)) {
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Login failed.");
                }
            });

            pack();
            setLocationRelativeTo(owner);
        }
    }

    // Panels for demonstration only
    static class AccountPanel extends JPanel {
        private JTextField accountNumberField;
        private JComboBox<String> accountTypeBox;
        private JButton createButton;
        private JTextArea displayArea;

        public AccountPanel() {
            setLayout(new BorderLayout());
            JPanel topPanel = new JPanel(new GridLayout(2,2));
            topPanel.add(new JLabel("Account Number:"));
            accountNumberField = new JTextField();
            topPanel.add(accountNumberField);

            topPanel.add(new JLabel("Account Type:"));
            accountTypeBox = new JComboBox<>(new String[]{"Savings", "Checking", "Loan"});
            topPanel.add(accountTypeBox);

            createButton = new JButton("Create Account");
            createButton.addActionListener(e -> createAccount());

            add(topPanel, BorderLayout.NORTH);
            add(createButton, BorderLayout.CENTER);
            displayArea = new JTextArea();
            add(new JScrollPane(displayArea), BorderLayout.SOUTH);
        }

        private void createAccount() {
            String accNum = accountNumberField.getText();
            String type = (String)accountTypeBox.getSelectedItem();
            Account acc = AccountFactory.createAccount(type.toLowerCase(), accNum);
            displayArea.append("Created: " + acc.getAccountType() + " with Number: " + accNum + "\n");
        }
    }

    static class TransactionPanel extends JPanel {
        private JButton createTxnButton;
        private JTextArea displayArea;
        private JButton cloneTxnButton;

        public TransactionPanel() {
            setLayout(new BorderLayout());
            createTxnButton = new JButton("Create Monthly Payment (Prototype)");
            createTxnButton.addActionListener(e -> createTransactionFromPrototype());

            cloneTxnButton = new JButton("Clone Monthly Payment Transaction");
            cloneTxnButton.addActionListener(e -> cloneTransaction());

            JPanel topPanel = new JPanel();
            topPanel.add(createTxnButton);
            topPanel.add(cloneTxnButton);

            add(topPanel, BorderLayout.NORTH);

            displayArea = new JTextArea();
            add(new JScrollPane(displayArea), BorderLayout.CENTER);
        }

        private void createTransactionFromPrototype() {
            Transaction txn = TransactionPrototypeRegistry.getPrototype("monthlyPayment");
            if(txn != null) {
                txn.transactionId = "T001"; 
                TransactionManager.getInstance().addTransaction(txn);
                displayArea.append("Created Transaction from Prototype: " + txn.getTransactionType() + " ID:" + txn.transactionId + "\n");
            } else {
                displayArea.append("No prototype found.\n");
            }
        }

        private void cloneTransaction() {
            Transaction txn = TransactionPrototypeRegistry.getPrototype("monthlyPayment");
            if(txn != null) {
                Transaction cloned = txn.clone();
                cloned.transactionId = "T002";
                TransactionManager.getInstance().addTransaction(cloned);
                displayArea.append("Cloned Transaction: " + cloned.getTransactionType() + " ID:" + cloned.transactionId + "\n");
            } else {
                displayArea.append("No prototype found.\n");
            }
        }
    }

    static class LoanPanel extends JPanel {
        private JTextField principalField;
        private JTextField interestField;
        private JTextField termField;
        private JComboBox<String> loanTypeBox;
        private JButton createLoanButton;
        private JTextArea displayArea;

        public LoanPanel() {
            setLayout(new BorderLayout());

            JPanel formPanel = new JPanel(new GridLayout(4,2));
            formPanel.add(new JLabel("Principal:"));
            principalField = new JTextField();
            formPanel.add(principalField);

            formPanel.add(new JLabel("Interest Rate (decimal):"));
            interestField = new JTextField("0.05");
            formPanel.add(interestField);

            formPanel.add(new JLabel("Term (months):"));
            termField = new JTextField("360");
            formPanel.add(termField);

            formPanel.add(new JLabel("Loan Type:"));
            loanTypeBox = new JComboBox<>(new String[]{"Home", "Car", "Personal"});
            formPanel.add(loanTypeBox);

            createLoanButton = new JButton("Create Loan");
            createLoanButton.addActionListener(e -> createLoan());

            displayArea = new JTextArea();

            add(formPanel, BorderLayout.NORTH);
            add(createLoanButton, BorderLayout.CENTER);
            add(new JScrollPane(displayArea), BorderLayout.SOUTH);
        }

        private void createLoan() {
            try {
                double principal = Double.parseDouble(principalField.getText());
                double interest = Double.parseDouble(interestField.getText());
                int term = Integer.parseInt(termField.getText());
                String type = (String)loanTypeBox.getSelectedItem();

                Loan loan = LoanFactory.createLoan(type.toLowerCase(), principal, interest, term);
                displayArea.append("Created " + loan.getLoanType() + " - Monthly Payment: " + loan.calculateMonthlyPayment() + "\n");
            } catch(NumberFormatException e) {
                displayArea.append("Error creating loan: " + e.getMessage() + "\n");
            }
        }
    }
}
