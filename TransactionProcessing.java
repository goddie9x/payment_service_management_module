import java.util.*;
import java.io.*;

public class TransactionProcessing {
    private ArrayList<Payment> paymentObjects;
    private IDCardManagement idcm;

    public TransactionProcessing(String idCardPath, String paymentPath) {
        idcm = new IDCardManagement(idCardPath);
        paymentObjects = new ArrayList<>();
        readPaymentObject(paymentPath);
    }

    public ArrayList<Payment> getPaymentObject() {
        return this.paymentObjects;
    }

    // Requirement 3
    public boolean readPaymentObject(String path) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String line = "";
            Payment payment;
            while ((line = bufferedReader.readLine()) != null) {
                // one line is an object
                String[] lineStr = line.split(",");
                if (lineStr.length == 1) {
                    if (lineStr[0].length() == 7) {
                        // E-wallet;
                        payment = new EWallet(Integer.parseInt(lineStr[0]));
                        paymentObjects.add(payment);
                    } else if (lineStr[0].length() == 6) {
                        // convenientCard
                        for (IDCard idCard : idcm.getIDCards()) {
                            if (idCard.getId() == Integer.parseInt(lineStr[0])) {
                                payment = new ConvenientCard(idCard);
                                paymentObjects.add(payment);
                            }
                        }
                    }
                } else if (lineStr.length == 2) {
                    // bank account
                    payment = new BankAccount(Integer.parseInt(lineStr[0]), Double.parseDouble(lineStr[1]));
                    paymentObjects.add(payment);
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    // Requirement 4
    public ArrayList<ConvenientCard> getAdultConvenientCards() {
        // code here
        ArrayList<ConvenientCard> cards = new ArrayList<>();
        for (Payment payment : paymentObjects) {
            if (payment instanceof ConvenientCard) {
                if (((ConvenientCard) payment).getType().equals("Adult")) {
                    cards.add((ConvenientCard) payment);
                }
            }
        }
        return cards;
    }

    // Requirement 5
    public ArrayList<IDCard> getCustomersHaveBoth() {
        // code here
        ArrayList<IDCard> idCards = new ArrayList<>();
        for (IDCard idCard : idcm.getIDCards()) {
            boolean haveE = false;
            boolean haveBank = false;
            boolean haveCard = false;
            for (Payment payment : paymentObjects) {
                if (payment instanceof EWallet) {
                    EWallet e = (EWallet) payment;
                    if (e.getPhoneNumber() == idCard.getPhoneNumber()) {
                        haveE = true;
                    }
                }
                if (payment instanceof BankAccount) {
                    BankAccount bankAccount = (BankAccount) payment;
                    if (bankAccount.getStk() == idCard.getId()) {
                        haveBank = true;
                    }
                }
                if (payment instanceof ConvenientCard) {
                    ConvenientCard convenientCard = (ConvenientCard) payment;
                    if (convenientCard.getID() == idCard.getId()) {
                        haveCard = true;
                    }
                }
            }
            if (haveCard && haveBank && haveE) {
                idCards.add(idCard);
            }
        }
        return idCards;
    }

    public void upPayment(int id, int type, double coin) {
        for (Payment payment : paymentObjects) {
            if (type == 1) {
                if (payment instanceof EWallet) {
                    if (((EWallet) payment).getPhoneNumber() == id) {
                        ((EWallet) payment).topUp(coin);
                    }
                }
            } else if (type == 2) {
                if (payment instanceof BankAccount) {
                    if (((BankAccount) payment).getStk() == id) {
                        ((BankAccount) payment).topUp(coin);
                    }
                }

            } else if (type == 3) {
                if (payment instanceof ConvenientCard) {
                    if (((ConvenientCard) payment).getID() == id) {
                        ((ConvenientCard) payment).topUp(coin);
                    }
                }
            }
        }
    }

    // Requirement 6
    public void processTopUp(String path) {
        // code here
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineStr = line.split(",");
                if (lineStr.length == 3) {
                    int type = -1;
                    switch (lineStr[0]) {
                        case "CC":
                            type = 3;
                            break;
                        case "BA":
                            type = 2;
                            break;
                        case "EW":
                            type = 1;
                            break;
                    }
                    if (type == -1)
                        continue;
                    upPayment(Integer.parseInt(lineStr[1]), type, Double.parseDouble(lineStr[2]));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean findAndPaidBill(int id, int type, double amount) {
        for (Payment payment : paymentObjects) {
            if (type == 1) {
                // e-wallet
                if (payment instanceof EWallet) {
                    if (((EWallet) payment).getPhoneNumber() == id) {
                        return payment.pay(amount);
                    }
                }
            } else if (type == 2) {
                // bank account or card
                if (payment instanceof BankAccount) {
                    if (((BankAccount) payment).getStk() == id) {
                        return payment.pay(amount);
                    }
                }

            } else if (type == 3) {
                if (payment instanceof ConvenientCard) {
                    if (((ConvenientCard) payment).getID() == id) {
                        return payment.pay(amount);
                    }
                }
            }
        }
        return false;
    }

    // Requirement 7
    public ArrayList<Bill> getUnsuccessfulTransactions(String path) {
        // code here
        ArrayList<Bill> unSuccessPaymentBill = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineStr = line.split(",");
                Bill bill = new Bill(Integer.parseInt(lineStr[0]), Double.parseDouble(lineStr[1]), lineStr[2]);
                switch (lineStr[3]) {
                    case "CC":
                        if (!findAndPaidBill(Integer.parseInt(lineStr[4]), 3, Double.parseDouble(lineStr[1]))) {
                            unSuccessPaymentBill.add(bill);
                        }
                        break;
                    case "BA":
                        if (!findAndPaidBill(Integer.parseInt(lineStr[4]), 2, Double.parseDouble(lineStr[1]))) {
                            unSuccessPaymentBill.add(bill);
                        }
                        break;
                    case "EW":
                        if (!findAndPaidBill(Integer.parseInt(lineStr[4]), 1, Double.parseDouble(lineStr[1]))) {
                            unSuccessPaymentBill.add(bill);
                        }
                        break;
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return unSuccessPaymentBill;
    }

    public BankAccount findSuccessPayBankAccount(int id, double amount) {
        BankAccount bankAccount = null;

        for (Payment payment : paymentObjects) {
            // bank account or card
            if (payment instanceof BankAccount) {
                if (((BankAccount) payment).getStk() == id) {
                    if (payment.pay(amount)) {
                        bankAccount = ((BankAccount) payment);
                    }
                }
            }
        }
        return bankAccount;
    }

    public void totalBankAccount(List<BankAccount> bankAccounts, double amount, BankAccount bankAccount,
            List<Map<String, Object>> ba_total) {
        boolean have = false;
        for (BankAccount bankAccount_ : bankAccounts) {
            if (bankAccount_.getStk() == bankAccount.getStk()) {
                calTotal(ba_total, bankAccount_.getStk(), amount);
                have = true;
            }
        }
        if (!have) {
            Map<String, Object> ba = new HashMap<>();
            ba.put("id", bankAccount.getStk());
            ba.put("total", 0);
            ba_total.add(ba);
            bankAccounts.add(bankAccount);
            calTotal(ba_total, bankAccount.getStk(), amount);
        }
    }

    public void calTotal(List<Map<String, Object>> ba_total, int id, double amount) {
        for (Map<String, Object> total : ba_total) {
            int id_ = Integer.parseInt(String.valueOf(total.get("id")));
            if (id_ == id) {
                total.put("total", (Double.parseDouble(String.valueOf(total.get("total"))) + amount));
            }
        }
    }

    public void removeLessThanMax(List<Map<String, Object>> ba_total, double max) {
        for (int i = 0; i < ba_total.size(); i++) {
            if (Double.parseDouble(String.valueOf(ba_total.get(i).get("total"))) < max) {
                ba_total.remove(ba_total.get(i));
                i = 0;
            }
        }
    }

    // Requirement 8
    public ArrayList<BankAccount> getLargestPaymentByBA(String path) {
        // code here
        ArrayList<BankAccount> bankAccounts = new ArrayList<>();
        List<Map<String, Object>> ba_total = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineStr = line.split(",");
                switch (lineStr[3]) {
                    case "BA":
                        BankAccount bankAccount = findSuccessPayBankAccount(Integer.parseInt(lineStr[4]),
                                Double.parseDouble(lineStr[1]));
                        if (bankAccount != null) {
                            totalBankAccount(bankAccounts, Double.parseDouble(lineStr[1]), bankAccount, ba_total);
                        }
                        break;
                }
            }
            double max = 0;
            for (int i = 0; i < ba_total.size(); i++) {
                double value = Double.parseDouble(String.valueOf(ba_total.get(i).get("total")));
                if (value > max) {
                    max = value;
                }
            }
            removeLessThanMax(ba_total, max);
            ArrayList<BankAccount> result = new ArrayList<>();
            for (BankAccount bankAccount : bankAccounts) {
                for (Map<String, Object> ba : ba_total) {
                    if (Integer.parseInt(String.valueOf(ba.get("id"))) == bankAccount.getStk()) {
                        result.add(bankAccount);
                    }
                }
            }
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    public IDCard findBySDT(int phoneNumber) {
        for (IDCard idCard : idcm.getIDCards()) {
            if (idCard.getPhoneNumber() == phoneNumber) {
                return idCard;
            }
        }
        return null;
    }

    public Payment findPaymentByIdentify(int identify, int type) {
        for (Payment payment : paymentObjects) {
            try {
                switch (type) {
                    case 1: {
                        if (((EWallet) payment).getPhoneNumber() == identify) {
                            return payment;
                        } else {
                            return null;
                        }
                    }
                    case 2: {
                        if (((BankAccount) payment).getStk() == identify) {
                            return payment;
                        } else {
                            return null;
                        }
                    }
                    case 3: {
                        if (((ConvenientCard) payment).getID() == identify) {
                            return payment;
                        } else {
                            return null;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public double calAmountForEWallet(IDCard idCard, double amount, String payFor) {
        if (amount > 500 && Objects.equals(payFor, "Clothing")) {
            int age = idCard.getAge();

            if ((Objects.equals(idCard.getGender(), "Male") && age < 20)
                    || (Objects.equals(idCard.getGender(), "Female") && age < 18)) {
                amount -= amount * 0.15;
            }
        }
        return amount;
    }

    public void payBill(double amount, int id, int type) {
        Payment payment = findPaymentByIdentify(id, type);
        if (payment != null) {
            payment.pay(amount);
            System.out.println(amount);
        }
    }

    // Requirement 9
    public void processTransactionWithDiscount(String path) {
        // code here
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineStr = line.split(",");
                double amount = Double.parseDouble(lineStr[1]);
                int identify = Integer.parseInt(lineStr[4]);
                switch (lineStr[3]) {
                    case "EW":
                        IDCard idCard = findBySDT(identify);
                        if (idCard != null) {
                            payBill(calAmountForEWallet(idCard, amount, lineStr[2]), identify, 1);
                        }
                        break;
                    case "BA":
                        payBill(amount, identify, 2);
                        break;
                    case "CC":
                        payBill(amount, identify, 3);
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
