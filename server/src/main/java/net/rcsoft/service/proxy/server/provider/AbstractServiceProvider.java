package net.rcsoft.service.proxy.server.provider;

import net.rcsoft.service.proxy.data.configuration.ConfigurationProvider;

/**
 * Abstract class for service providers.
 *
 * @author : recampelos
 */
public abstract class AbstractServiceProvider implements ServiceProvider {

    private ConfigurationProvider configurationProvider;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConfigurationProvider(final ConfigurationProvider configurationProvider) {
        this.configurationProvider = configurationProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConfigurationProvider getConfigurationProvider() {
        return this.configurationProvider;
    }
}
