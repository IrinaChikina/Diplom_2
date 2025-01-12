import com.github.javafaker.Faker;

public class GeneratorUser {

    static Faker faker = new Faker();

    public static CreatingUser getRandomUser() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(6, 10);
        String name = faker.name().username();
        return new CreatingUser(email, password, name);
    }

    public static String getRandomPassword() {
        return faker.internet().password(6, 10);
    }

    public static String getRandomEmail() {
        return faker.internet().emailAddress();
    }

    public static String getRandomName() {
        return faker.name().username();
    }
}
