public class Admin extends User {

    public Admin(String username, int age, String email) {
        super(username, age, email);
    }

    @Override
    public boolean isAdmin() {
        return true;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "username='" + getUsername() + '\'' +
                ", age=" + getAge() +
                ", email='" + getEmail() + '\'' +
                '}';
    }
}
