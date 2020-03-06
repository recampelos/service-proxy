package net.rcsoft.service.proxy.data.configuration;

/**
 * Proxy service configuration provider interface.
 *
 * @author recampelos
 */
public interface ConfigurationProvider {
    
    /**
     * Get configuration value.
     * 
     * @param key configuration key
     * @return configuration value for the given key
     */
    String getConfigurationValue(final ConfigurationKey key);
}
