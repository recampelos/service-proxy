package net.rcsoft.service.proxy.server.configuration;

import net.rcsoft.service.proxy.data.configuration.ConfigurationKey;

/**
 * Server configuration keys.
 *
 * @author : recampelos
 */
public enum ConfigurationKeys implements ConfigurationKey {

    /** System jndi prefix */
    JDNI_PREFIX("jndi.prefix");

    private final String key;

    ConfigurationKeys(String key) {
        this.key = key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey() {
        return null;
    }
}
