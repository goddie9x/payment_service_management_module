public class EWallet implements Payment, Transfer {

    private int phoneNumber;
    private double balance;

    public EWallet(int phoneNumber) {
        this.phoneNumber = phoneNumber;
        balance = 0;
    }

    @Override
    public boolean pay(double amount) {
        if (this.balance - amount >= 0) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    @Override
    public boolean transfer(double amount, Transfer to) {
        double amountToTransfer = amount + (amount * Transfer.transferFee);
        if (pay(amountToTransfer)) {
            if (to instanceof BankAccount) {
                ((BankAccount) to).topUp(amount);
            } else if (to instanceof EWallet) {
                ((EWallet) to).topUp(amount);
            }
            return true;
        }
        return false;
    }

    public void topUp(double coin) {
        this.balance += coin;
    }

    @Override
    public double checkBalance() {
        return this.balance;
    }

    @Override
    public String toString() {
        return phoneNumber + "," + balance;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
