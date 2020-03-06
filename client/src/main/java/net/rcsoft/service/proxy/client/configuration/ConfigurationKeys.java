package net.rcsoft.service.proxy.client.configuration;

import net.rcsoft.service.proxy.data.configuration.ConfigurationKey;

/**
 * Configuration keys definition.
 *
 * @author recampelos
 */
public enum ConfigurationKeys implements ConfigurationKey {

    /** Full URL where the proxy service is deployed. */
    SERVICE_HOST_URL("proxy.service.host.url"),
    
    /** Service Host Username. */
    SERVICE_HOST_USERNAME("proxy.service.host.username"),
    
    /** Service Host Username. */
    SERVICE_HOST_PASSWORD("proxy.service.host.password");
        
    private final String key;

    private ConfigurationKeys(final String key) {
        this.key = key;
    }
    
    @Override
    public String getKey() {
        return this.key;
    }
}
