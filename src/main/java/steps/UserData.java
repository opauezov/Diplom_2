package steps;

public class UserData {
        private final String email;
        private final String password;

        public UserData(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public static UserData from(User user) {
            return new UserData(user.getEmail(), user.getPassword());
        }

        @Override
        public String toString() {
            return "UserCredentials{" +
                    "email='" + email + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
}
