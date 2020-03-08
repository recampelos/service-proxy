package net.rcsoft.service.proxy.client.handler;

import net.rcsoft.service.proxy.data.configuration.ConfigurationProvider;

import java.lang.reflect.InvocationHandler;

/**
 * Interface for the service invocation handler.
 *
 * @author : recampelos
 */
public interface ServiceInvocationHandler extends InvocationHandler {

    /**
     * Sets configuration provider.
     *
     * @param configurationProvider configuration provider
     */
    void setConfigurationProvider(final ConfigurationProvider configurationProvider);

    /**
     * Gets configuration provider.
     *
     * @return handler configuration provider
     */
    ConfigurationProvider getConfigurationProvider();
}
