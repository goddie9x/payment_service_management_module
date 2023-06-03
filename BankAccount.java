public class BankAccount implements Payment, Transfer {
    private int stk;
    private double interestRate;
    private double balance;

    public BankAccount(int stk, double interestRate) {
        this.stk = stk;
        this.interestRate = interestRate;
        this.balance = 50;
    }

    public int getStk() {
        return stk;
    }

    public void topUp(double coin) {
        balance += coin;
    }

    @Override
    public boolean pay(double amount) {
        if (balance - amount >= 50) {
            balance -= amount;
            return true;
        }
        return false;
    }

    @Override
    public boolean transfer(double amount, Transfer to) {
        double amountToTransfer = amount + (amount * Transfer.transferFee);
        if (pay(amountToTransfer)) {
            if (to instanceof EWallet) {
                ((EWallet) to).topUp(amount);
            } else if (to instanceof BankAccount) {
                ((BankAccount) to).topUp(amount);
            }
            return true;
        }
        return false;
    }

    @Override
    public double checkBalance() {
        return balance;
    }
    // code here

    @Override
    public String toString() {
        return stk + "," + interestRate + "," + balance;
    }
}
