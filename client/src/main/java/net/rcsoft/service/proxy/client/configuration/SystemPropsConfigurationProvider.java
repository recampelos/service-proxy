package net.rcsoft.service.proxy.client.configuration;

import net.rcsoft.service.proxy.data.configuration.ConfigurationKey;
import net.rcsoft.service.proxy.data.configuration.ConfigurationProvider;

/**
 * Configuration provider using system properties.
 *
 * @author recampelos
 */
public class SystemPropsConfigurationProvider implements ConfigurationProvider{

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfigurationValue(final ConfigurationKey key) {
        return System.getProperty(key.getKey());
    }
}
