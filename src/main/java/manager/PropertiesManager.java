package manager;

import java.util.Properties;

/**
 *
 */

public class PropertiesManager {
    private static PropertiesManager instance = new PropertiesManager();

    private Properties properties;

    /**
     * @return the Singleton instance of PropertiesManager.
     */

    public static PropertiesManager getInstance() {
        return instance;
    }

    private PropertiesManager() {
    }

    /**
     * Retrieves the {@link Properties} object.
     * @return the Properties object.
     */

    public Properties getProperties() {
        return properties;
    }

    /**
     * Sets the {@link Properties} file for the Singleton.
     * @param properties the Properties file object.
     */

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
