package net.rcsoft.service.proxy.server.provider;

import net.rcsoft.service.proxy.server.configuration.ConfigurationKeys;

import javax.naming.InitialContext;

/**
 * Default service provider.
 *
 * @author recampelos
 */
public class InitialContextServiceProvider extends AbstractServiceProvider {
    
    @Override
    public <T> T getServiceInstance(Class<T> type) throws Exception {
        InitialContext context = new InitialContext();
        
        return (T) context.lookup(this.getJndiName(type));
    }
    
    private String getJndiName(final Class<?> serviceClass) {
        final String jndiPrefix = this.getConfigurationProvider().getConfigurationValue(ConfigurationKeys.JDNI_PREFIX);

        return jndiPrefix.concat("/").concat(serviceClass.getSimpleName());
    }
}
