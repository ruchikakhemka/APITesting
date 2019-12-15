package util.restUtil;

import util.ConfigReader;

public class BaseURL {

    ConfigReader config = new ConfigReader();


    public String getBaseURL()
    {
        return config.getProperty("baseurl");
    }
}
