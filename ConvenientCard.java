import java.security.AlgorithmParameterGenerator;

public class ConvenientCard implements Payment {
    private String type;

    private IDCard identificationCard;

    private double balance;

    public int getID() {
        return identificationCard.getId();
    }

    public ConvenientCard(IDCard idCard) throws CannotCreateCard {
        this.type = genType(idCard);
        this.identificationCard = idCard;
        this.balance = 100;
    }

    public String genType(IDCard idCard) throws CannotCreateCard {
        String type = "";
        int age = idCard.getAge();
        if (age < 12) {
            throw new CannotCreateCard("Not enough age");
        } else if (age <= 18) {
            type = "Student";
        } else {
            type = "Adult";
        }
        return type;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public boolean pay(double amount) {
        double cost = -1;
        switch (type) {
            case "Student": {
                cost = amount;
                break;
            }
            case "Adult": {
                double fee = amount * 0.01;
                cost = amount + fee;
                break;
            }
            default:
                return false;
        }
        // if cost still equal to -1 means type not in [Student,Adult] so we have
        // nothing to do with it
        if (cost == -1 || balance - cost < 0)
            return false;
        balance -= cost;
        return true;
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
        return identificationCard + "," + type + "," + balance;
    }
}
