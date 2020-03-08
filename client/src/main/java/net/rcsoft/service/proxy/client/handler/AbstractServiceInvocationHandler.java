package net.rcsoft.service.proxy.client.handler;

import net.rcsoft.service.proxy.data.configuration.ConfigurationProvider;

/**
 * Abstract class for service handlers.
 *
 * @author : recampelos
 */
public abstract class AbstractServiceInvocationHandler implements ServiceInvocationHandler {

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
