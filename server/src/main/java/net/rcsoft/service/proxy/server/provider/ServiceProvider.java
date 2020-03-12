package net.rcsoft.service.proxy.server.provider;

import net.rcsoft.service.proxy.data.configuration.ConfigurationProvider;

/**
 * Provider to get service instances
 * 
 * @author recampelos
 */
public interface ServiceProvider {
    
    /**
     * Gets a new instance of given service type.
     * 
     * @param <T> service type
     * @param type service type
     * @return instance of type
     * @throws Exception in case of error
     */
    <T> T getServiceInstance(Class<T> type) throws Exception;

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
