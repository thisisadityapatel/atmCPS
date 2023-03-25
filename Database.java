import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Database {
    // private database variable
    private int databaseID;

    // access databaseID
    public int getDatabaseID(int accessCode) {
        return databaseID;
    }

    // get atm balance
    public Double getLastAtmBalance() {
        Double returnAmount = 0.0;
        try {
            String filename = "atmBalanceDatabase.txt";
            File file = new File(filename);
            Scanner in = new Scanner(file);

            while (in.hasNextLine()) {
                returnAmount = Double.parseDouble(in.nextLine());
            }
            in.close();
        } catch (IOException io) {
            System.out.println(io.getMessage()); // print error, if any
        }
        return returnAmount;
    }

    // method to update ATM balance
    public boolean updateLastAtmBalance(Double amount) {
        try {
            FileWriter fw = new FileWriter("atmBalanceDatabase.txt", false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.print(amount);
            pw.flush();
            pw.close();
            return true;
        } catch (IOException err) { // print error, if any
            System.out.println(err.getMessage());
        }
        return false;
    }

    // verify user credentials from database
    public boolean verifyUser(Card card, int verifyPin) {
        try {
            File file = new File("bankDatabase.txt");
            Scanner in = new Scanner(file);

            while (in.hasNextLine()) {
                String cardNumber = in.nextLine().trim();
                int pin = Integer.parseInt(in.nextLine());

                // verification
                if (pin == verifyPin && card.getCardNumber().equals(cardNumber)) {
                    in.close(); // terminating file stream
                    return true;
                }

                // skipping to the next customer
                for (int i = 0; i < 7; i++) {
                    in.nextLine();
                }
            }
            in.close(); // closing the stream
        } catch (IOException io) {
            System.out.println(io.getMessage()); // printing error, if any
        }
        return false;
    }

    // retrieve all the accounts of the customer from the database
    public List<Integer> retrieveAccounts(int custID) {
        List<String> tempAcc = new ArrayList<String>();
        try {
            File file = new File("bankDatabase.txt");
            Scanner in = new Scanner(file);

            while (in.hasNextLine()) {
                for (int i = 0; i < 2; i++) {
                    in.nextLine();
                }

                int customerId = Integer.parseInt(in.nextLine());

                // id verification
                if (custID == customerId) {
                    tempAcc = Arrays.asList(in.nextLine().split(","));
                    List<Integer> outputAccounts = new ArrayList<Integer>();
                    for (int i = 0; i < tempAcc.size(); i++) {
                        outputAccounts.add(Integer.parseInt(Arrays.asList(tempAcc.get(i).split(":")).get(0)));
                    }
                    in.close();
                    return outputAccounts;
                }

                // skipping to the next customer
                for (int i = 0; i < 6; i++) {
                    in.nextLine();
                }
            }
            in.close();
        } catch (IOException io) { // print error, if any
            System.out.println(io.getMessage());
        }
        return new ArrayList<Integer>();
    }

    // method to update user account amount in database
    public Boolean updateAccountAmount(Account account, double newAmount) {

        /**
         * Here's how the method works:
         * 1) It first creates a new file named "tempDatabase.txt" and copies the
         * contents of the existing "bankDatabase.txt" file into it.
         * 2) It searches for the specific customer in the new file, updates their
         * information, and writes the changes to the new file.
         * 3) It then closes the streams for both files to ensure that all changes are
         * saved properly.
         * 4) It deletes the original "bankDatabase.txt" file.
         * 5) Finally, it renames the new file to "bankDatabase.txt", effectively
         * replacing the old file with the updated one.
         **/

        String oldfilename = "bankDatabase.txt";
        String tempname = "tempDatabase.txt";

        File oldFile = new File(oldfilename);
        File newFile = new File(tempname);

        Scanner s = null;

        try {
            FileWriter fw = new FileWriter(tempname, false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            s = new Scanner(new File(oldfilename));

            while (s.hasNextLine()) {
                for (int i = 0; i < 3; i++) {
                    pw.println(s.nextLine());
                }
                String allAccounts = s.nextLine();
                List<String> tempAcc = Arrays.asList(allAccounts.split(","));
                List<String> tempAcc2 = tempAcc;
                for (int i = 0; i < tempAcc.size(); i++) {
                    int thisAccount = Integer.parseInt(Arrays.asList(tempAcc.get(i).split(":")).get(0));
                    if (thisAccount == account.getAccountNumber()) {
                        tempAcc2.set(i, (thisAccount + ":" + newAmount));
                    }
                }
                String test = "";
                test += tempAcc2.get(0);
                for (int i = 1; i < tempAcc2.size(); i++) {
                    test += "," + tempAcc2.get(i);
                }
                pw.println(test);
                for (int i = 0; i < 5; i++) {
                    pw.println(s.nextLine());
                }
            }
            s.close();
            pw.flush();
            pw.close();
            oldFile.delete();
            File dump = new File(oldfilename);
            newFile.renameTo(dump);
        } catch (Exception err) {
            System.out.println(err.getMessage());
            return false;
        }
        return true;
    }

    // updating the user PIN in the database
    public boolean updatePin(Card card, int newPin) {

        /**
         * Here's how the method works:
         * 1) It first creates a new file named "tempDatabase.txt" and copies the
         * contents of the existing "bankDatabase.txt" file into it.
         * 2) It searches for the specific customer in the new file, updates their
         * information, and writes the changes to the new file.
         * 3) It then closes the streams for both files to ensure that all changes are
         * saved properly.
         * 4) It deletes the original "bankDatabase.txt" file.
         * 5) Finally, it renames the new file to "bankDatabase.txt", effectively
         * replacing the old file with the updated osne.
         **/

        String oldfilename = "bankDatabase.txt";
        String tempname = "tempDatabase.txt";

        File oldFile = new File(oldfilename);
        File newFile = new File(tempname);

        Scanner s = null;

        try {
            FileWriter fw = new FileWriter(tempname, false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            s = new Scanner(new File(oldfilename));

            while (s.hasNextLine()) {
                String cardNumber = s.nextLine();
                pw.println(cardNumber);
                String PIN = s.nextLine();
                if (cardNumber.equals(card.getCardNumber())) {
                    pw.println(newPin);
                } else {
                    pw.println(PIN);
                }
                for (int i = 0; i < 7; i++) {
                    pw.println(s.nextLine());
                }
            }
            s.close();
            pw.flush();
            pw.close();
            oldFile.delete();
            File dump = new File(oldfilename);
            newFile.renameTo(dump);
        } catch (Exception err) {
            System.out.println(err.getMessage());
            return false;
        }
        return true;
    }

    // appending account transaction to database
    public void addAccountTransaction(Transaction transaction) {
        try {
            FileWriter fw = new FileWriter("transactionDatabase.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println(transaction.getAccount().getAccountNumber());
            pw.println(transaction.getAmount());
            pw.println(transaction.getDate());
            pw.println(" ");
            pw.flush();
            pw.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // getting all account transactions
    public ArrayList<Transaction> getStatement(int verifyAccountNumber) {
        ArrayList<Transaction> output = new ArrayList<Transaction>();
        try {
            File file = new File("transactionDatabase.txt");
            Scanner in = new Scanner(file);

            while (in.hasNextLine()) {
                int accountNumber = Integer.parseInt(in.nextLine());

                // verifying the account
                if (accountNumber == verifyAccountNumber) {
                    double amount = Double.parseDouble(in.nextLine());
                    String date = in.nextLine();
                    Transaction temp = new Transaction(date, amount, null, null);
                    in.nextLine();
                    output.add(temp);
                } else {
                    for (int i = 0; i < 3; i++) {
                        in.nextLine();
                    }
                }
            }
            in.close(); // closing the stream
        } catch (IOException io) {
            System.out.println(io.getMessage()); // printing error, if any
        }
        return output;
    }

    // extracting all the data from the database
    public ArrayList<retrieveDatabaseData> extractDatabase() {
        ArrayList<retrieveDatabaseData> output = new ArrayList<retrieveDatabaseData>();
        try {
            File file = new File("bankDatabase.txt");
            Scanner in = new Scanner(file);

            while (in.hasNextLine()) {

                String cardNumber = in.nextLine();
                int pin = Integer.parseInt(in.nextLine());
                int customerID = Integer.parseInt(in.nextLine());

                // extracting all the account and their respective amounts
                ArrayList<Integer> accounts = new ArrayList<Integer>();
                ArrayList<Double> amounts = new ArrayList<Double>();
                ArrayList<String> accountTypes = new ArrayList<String>();
                List<String> accsAms = Arrays.asList(in.nextLine().split(","));
                for (int i = 0; i < accsAms.size(); i++) {
                    int accountNumber = Integer.parseInt(Arrays.asList(accsAms.get(i).split(":")).get(0));
                    double accountAmount = Double.parseDouble(Arrays.asList(accsAms.get(i).split(":")).get(1));
                    accounts.add(accountNumber);
                    amounts.add(accountAmount);

                    // adding the account type
                    if ((int) (accountNumber / 100000) == 72) {
                        accountTypes.add("saving");
                    } else {
                        accountTypes.add("checking");
                    }
                }

                String firstName = in.nextLine();
                String lastName = in.nextLine();
                String dateofBirthday = in.nextLine();
                String cardholderName = in.nextLine();
                in.nextLine();

                retrieveDatabaseData temp = new retrieveDatabaseData(cardNumber, pin, customerID, accounts, amounts,
                        accountTypes,
                        firstName, lastName, dateofBirthday, cardholderName);
                output.add(temp);
            }
            in.close(); // closing the stream
        } catch (IOException io) {
            System.out.println(io.getMessage()); // printing error, if any
        }
        return output;
    }

    public static void main(String[] args) {
        Database testing = new Database();

        // Card temp = new Card("4321432143214321", "Aditya", "", 1234);
        // System.out.println(testing.verifyUser(temp, 7891));

        // System.out.println(testing.retrieveAccounts(56789));

        // System.out.println(testing.updateAccountAmount(new Account(1234567, 0, 0.0,
        // null), 100560.25));

        // System.out.println(testing.updatePin(new Card("1235123512", "", "", 1234),
        // 12334));

        // testing.addAccountTransaction(new Transaction("21/12/2002", -120.5, null, new
        // Account(1234569, 0, 0, null)));

        // System.out.println(testing.extractDatabase());

        System.out.println(testing.getLastAtmBalance());

        // System.out.println(testing.updateLastAtmBalance(200002.35));

        // System.out.println(testing.getStatement(1234115).get(0).getDate());
    }
}
