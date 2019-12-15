package testdata;


import org.testng.annotations.DataProvider;

public class LoginData {

    @DataProvider(name = "logincredentials")
    public static Object[][] loginCredentials()
    {
        return new String[][]{
                {"grant_type","password"},
                {"username","ruchikaahuja.asm@gmail.com"},
                {"password","abcd1234"}
        };
    }
}
