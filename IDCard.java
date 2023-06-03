import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;

public class IDCard {
    private int id;
    private String fulName;
    private String gender;
    private String dateOfBirth;
    private String address;
    private int phoneNumber;

    public IDCard(int id, String fulName, String gender, String dateOfBirth, String address, int phoneNumber) {
        this.id = id;
        this.fulName = fulName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFulName() {
        return fulName;
    }

    public void setFullName(String fulName) {
        this.fulName = fulName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOFBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    // Assume that... à thôi :v giả sử ngày giờ chuẩn dd/MM/yyyy, chúng ta sẽ không
    // validate
    public int getAge() {
        int currentYear = Year.now().getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(dateOfBirth, formatter);
        Year year = Year.from(date);
        int extractedYear = year.getValue();

        return currentYear - extractedYear;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return id + "," + fulName + "," + gender + "," + dateOfBirth + "," + address + "," + phoneNumber;

    }
}
