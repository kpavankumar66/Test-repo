import java.util.Scanner;

// Interface for bank accounts
interface Account {
    void displayAccountDetails();

    void deposit(double amount);

    void withdraw(double amount);
}

// Base class for all accounts
abstract class BankAccount implements Account {
    protected String accountNumber;
    protected double balance;

    public BankAccount(String accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = 0;
    }

    @Override
    public void displayAccountDetails() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Balance: " + balance);
    }
}


class Transaction {
    private String type;
    private double amount;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}
// Savings account class
class SavingsAccount extends BankAccount {
    private String accountHolder;
    private CurrentAccount currentAccount;
    private FixedAccount fixedAccount;
    private Transaction[] transactions;
    private int transactionCount;

    public SavingsAccount(String accountNumber, String accountHolder) {
        super(accountNumber);
        this.accountHolder = accountHolder;
        this.currentAccount = null;
        this.fixedAccount = null;
        this.transactions = new Transaction[10];
        this.transactionCount = 0;
    }

    @Override
    public void deposit(double amount) {
        balance += amount;
        addTransaction("Deposit", amount);
        System.out.println("Amount deposited to savings account: " + amount);
    }

    @Override
    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            addTransaction("Withdrawal", amount);
            System.out.println("Amount withdrawn from savings account: " + amount);
        } else {
            System.out.println("Insufficient funds in savings account.");
        }
    }

    public void linkCurrentAccount(CurrentAccount currentAccount) {
        this.currentAccount = currentAccount;
    }

    public void linkFixedAccount(FixedAccount fixedAccount) {
        this.fixedAccount = fixedAccount;
    }

    @Override
    public void displayAccountDetails() {
        System.out.println("\nSavings Account Details:");
        super.displayAccountDetails();

        System.out.println("Account Holder: " + accountHolder);

        if (currentAccount != null) {
            System.out.println("\nCurrent Account Details:");
            currentAccount.displayAccountDetails();
        }

        if (fixedAccount != null) {
            System.out.println("\nFixed Account Details:");
            fixedAccount.displayAccountDetails();
        }

        double totalBalance = balance;
        if (currentAccount != null) {
            totalBalance += currentAccount.balance;
        }
        if (fixedAccount != null) {
            totalBalance += fixedAccount.balance;
        }
        System.out.println("\nTotal Balance: " + totalBalance);
    }

    public void displayPassbook() {
        System.out.println("Savings Account Passbook");
        System.out.println("");
        System.out.println("Current Balance: " + balance);

        System.out.println("\nTransaction History:");
        int startIndex = Math.max(0, transactionCount - 10); // Get the starting index for displaying recent transactions
        for (int i = startIndex; i < transactionCount; i++) {
            System.out.println(transactions[i].getType() + ": " + transactions[i].getAmount());
        }
    }

    private void addTransaction(String type, double amount) {
        Transaction transaction = new Transaction(type, amount);
        if (transactionCount < 10) {
            transactions[transactionCount] = transaction;
            transactionCount++;
        } else {
            // Shift the transactions to make room for the new transaction
            for (int i = 0; i < 9; i++) {
                transactions[i] = transactions[i + 1];
            }
            transactions[9] = transaction;
        }
    }
}


// Fixed account class
class FixedAccount extends BankAccount {
    public FixedAccount(String accountNumber, double fixedAmount) {
        super(accountNumber);
        this.balance = fixedAmount;
    }

    @Override
    public void deposit(double amount) {
        System.out.println("Deposits not allowed in fixed account.");
    }

    @Override
    public void withdraw(double amount) {
        System.out.println("Withdrawals not allowed in fixed account.");
    }
}

// Current account class
class CurrentAccount extends BankAccount {
    public CurrentAccount(String accountNumber) {
        super(accountNumber);
    }

    @Override
    public void deposit(double amount) {
        balance += amount;
        System.out.println("Amount deposited to current account: " + amount);
    }

    @Override
    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println("Amount withdrawn from current account: " + amount);
        } else {
            System.out.println("Insufficient funds in current account.");
        }
    }
}

public class BankManagementSystem {
    private static SavingsAccount savingsAccount;
    private static CurrentAccount currentAccount;
    private static FixedAccount fixedAccount;
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        int choice;

        do {
            System.out.println("\nBank Management System");
            System.out.println("1. Create Account");
            System.out.println("2. Access Account");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    accessAccount();
                    break;
                case 3:
                    System.out.println("Thank you for using the Bank Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (choice != 3);
    }

    private static void createAccount() {
        System.out.print("Enter the name of the account holder: ");
        scanner.nextLine(); // Clear the input buffer
        String accountHolder = scanner.nextLine();

        System.out.print("Enter account number: ");
        String accountNumber = scanner.next();

        savingsAccount = new SavingsAccount(accountNumber, accountHolder);

        System.out.println("Savings account created successfully!");
    }

    private static void accessAccount() {
        if (savingsAccount == null) {
            System.out.println("No savings account available.");
            return;
        }

        int choice;

        do {
            System.out.println("\nAccount Options");
            System.out.println("1. Display Account Details");
            System.out.println("2. Deposit to Savings Account");
            System.out.println("3. Deposit to Current Account");
            System.out.println("4. Transfer from Savings to Current Account");
            System.out.println("5. Transfer from Current to Savings Account");
            System.out.println("6. Withdraw from Savings Account");
            System.out.println("7. Withdraw from Current Account");
            System.out.println("8. Display Passbook");
            System.out.println("9. Create Current Account");
            System.out.println("10. Create Fixed Account");
            System.out.println("11. Go Back to Previous Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    displayAccountDetails();
                    break;
                case 2:
                    depositToSavingsAccount();
                    break;
                case 3:
                    depositToCurrentAccount();
                    break;
                case 4:
                    transferFromSavingsToCurrentAccount();
                    break;
                case 5:
                    transferFromCurrentToSavingsAccount();
                    break;
                case 6:
                    withdrawFromSavingsAccount();
                    break;
                case 7:
                    withdrawFromCurrentAccount();
                    break;
                case 8:
                    displayPassbook();
                    break;
                case 9:
                    createCurrentAccount();
                    break;
                case 10:
                    createFixedAccount();
                    break;
                case 11:
                    System.out.println("Returning to previous menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (choice != 11);
    }

    private static void displayAccountDetails() {
        if (savingsAccount == null) {
            System.out.println("No savings account available.");
            return;
        }

        savingsAccount.displayAccountDetails();
    }

    private static void depositToSavingsAccount() {
        if (savingsAccount == null) {
            System.out.println("No savings account available.");
            return;
        }

        System.out.print("Enter the amount to deposit: ");
        double amount = scanner.nextDouble();

        savingsAccount.deposit(amount);
    }

    private static void depositToCurrentAccount() {
        if (currentAccount == null) {
            System.out.println("No current account available.");
            return;
        }

        System.out.print("Enter the amount to deposit: ");
        double amount = scanner.nextDouble();

        currentAccount.deposit(amount);
    }

    private static void transferFromSavingsToCurrentAccount() {
        if (currentAccount == null) {
            System.out.println("No current account available.");
            return;
        }

        System.out.print("Enter the amount to transfer: ");
        double amount = scanner.nextDouble();

        if (savingsAccount.balance >= amount) {
            savingsAccount.withdraw(amount);
            currentAccount.deposit(amount);
            System.out.println("Amount transferred from savings account to current account: " + amount);
        } else {
            System.out.println("Insufficient funds in savings account.");
        }
    }

    private static void transferFromCurrentToSavingsAccount() {
        if (currentAccount == null) {
            System.out.println("No current account available.");
            return;
        }

        System.out.print("Enter the amount to transfer: ");
        double amount = scanner.nextDouble();

        if (currentAccount.balance >= amount) {
            currentAccount.withdraw(amount);
            savingsAccount.deposit(amount);
            System.out.println("Amount transferred from current account to savings account: " + amount);
        } else {
            System.out.println("Insufficient funds in current account.");
        }
    }

    private static void withdrawFromSavingsAccount() {
        System.out.print("Enter the amount to withdraw: ");
        double amount = scanner.nextDouble();
        savingsAccount.withdraw(amount);
    }

    private static void withdrawFromCurrentAccount() {
        if (currentAccount == null) {
            System.out.println("No current account available.");
            return;
        }

        System.out.print("Enter the amount to withdraw: ");
        double amount = scanner.nextDouble();
        currentAccount.withdraw(amount);
    }

    private static void displayPassbook() {
        if (savingsAccount == null) {
            System.out.println("No savings account available.");
            return;
        }

        savingsAccount.displayPassbook();
    }

    private static void createCurrentAccount() {
        if (currentAccount != null) {
            System.out.println("Current account already created.");
            return;
        }

        System.out.print("Enter current account number: ");
        String accountNumber = scanner.next();

        currentAccount = new CurrentAccount(accountNumber);
        savingsAccount.linkCurrentAccount(currentAccount);

        System.out.println("Current account created successfully!");
    }

    private static void createFixedAccount() {
        if (fixedAccount != null) {
            System.out.println("Fixed account already created.");
            return;
        }

        System.out.print("Enter fixed account number: ");
        String accountNumber = scanner.next();

        System.out.print("Enter fixed account amount: ");
        double fixedAmount = scanner.nextDouble();

        if (savingsAccount.balance >= fixedAmount) {
            savingsAccount.withdraw(fixedAmount);
            fixedAccount = new FixedAccount(accountNumber, fixedAmount);
            savingsAccount.linkFixedAccount(fixedAccount);
            System.out.println("Fixed account created successfully!");
        } else {
            System.out.println("Insufficient funds in savings account to create fixed account.");
        }
    }
}