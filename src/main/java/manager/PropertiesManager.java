package manager;

import java.util.Properties;

public class PropertiesManager {
    private static PropertiesManager ourInstance = new PropertiesManager();

    private Properties properties;

    public static PropertiesManager getInstance() {
        return ourInstance;
    }

    private PropertiesManager() {
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
