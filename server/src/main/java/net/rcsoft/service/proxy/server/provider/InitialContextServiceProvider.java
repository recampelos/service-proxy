package net.rcsoft.service.proxy.server.provider;

import javax.naming.InitialContext;

/**
 *
 * @author recampelos
 */
public class InitialContextServiceProvider implements ServiceProvider {
    
    private final String jndiPrefix;

    public InitialContextServiceProvider(String jndiPrefix) {
        this.jndiPrefix = jndiPrefix;
    }

    @Override
    public <T> T getServiceInstance(Class<T> type) throws Exception {
        InitialContext context = new InitialContext();
        
        return (T) context.lookup(this.getJndiName(type));
    }
    
    private String getJndiName(final Class<?> serviceClass) {
        return this.jndiPrefix.concat("/").concat(serviceClass.getSimpleName());
    }
}
