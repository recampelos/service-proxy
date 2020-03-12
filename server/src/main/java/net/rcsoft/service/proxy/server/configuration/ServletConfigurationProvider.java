package net.rcsoft.service.proxy.server.configuration;

import net.rcsoft.service.proxy.data.configuration.ConfigurationKey;
import net.rcsoft.service.proxy.data.configuration.ConfigurationProvider;

import javax.servlet.ServletConfig;

/**
 * Default configuration provider
 *
 * @author : recampelos
 */
public class ServletConfigurationProvider implements ConfigurationProvider {

    private ServletConfig servletConfig;

    /**
     * Creates a new instance of {@link ServletConfigurationProvider}.
     *
     * @param servletConfig servlet configuration
     */
    public ServletConfigurationProvider(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfigurationValue(final ConfigurationKey key) {
        return this.servletConfig.getInitParameter(key.getKey());
    }
}
