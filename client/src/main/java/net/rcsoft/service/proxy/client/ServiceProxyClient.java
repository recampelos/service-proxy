package net.rcsoft.service.proxy.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import net.rcsoft.service.proxy.client.configuration.SystemPropsConfigurationProvider;
import net.rcsoft.service.proxy.client.handler.HttpConnectionHandler;
import net.rcsoft.service.proxy.data.configuration.ConfigurationProvider;

/**
 * Class to create a service proxy.
 *
 * @author recampelos
 */
public class ServiceProxyClient {
    
    /**
     * Get service proxy with default handler and configuration provider.
     * 
     * @param <T> service type class
     * @param type service type class
     * @return a instance of type
     */
    public static <T> T getServiceProxy(final Class<T> type) {
        final ConfigurationProvider provider = new SystemPropsConfigurationProvider();
        
        return getServiceProxy(type, provider);
    }
    
    /**
     * Get service proxy with custom invocation handler.
     * 
     * @param <T> service type class
     * @param type service type class
     * @param handler the proxy service invocation handler
     * @return a instance of type
     */
    public static <T> T getServiceProxy(final Class<T> type, final InvocationHandler handler) {
        return createServiceProxy(type, handler);
    }
    
    /**
     * Get service proxy with default handler and custom configuration provider.
     * 
     * @param <T> service type class
     * @param type service type class
     * @param configurationProvider configuration provider
     * @return a instance of type
     */
    public static <T> T getServiceProxy(final Class<T> type, final ConfigurationProvider configurationProvider) {
        final InvocationHandler handler = new HttpConnectionHandler(type, configurationProvider);
        
        return getServiceProxy(type, handler);
    }
    
    private static <T> T createServiceProxy(final Class<T> type, final InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler);
    }
}
