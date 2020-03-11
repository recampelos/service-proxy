package net.rcsoft.service.proxy.client.handler;

import net.rcsoft.service.proxy.client.configuration.ConfigurationKeys;
import net.rcsoft.service.proxy.client.exception.HttpServiceCallException;
import net.rcsoft.service.proxy.client.util.HttpConnectionRestClient;
import net.rcsoft.service.proxy.data.dto.ProxyServiceRequestDTO;
import net.rcsoft.service.proxy.data.dto.ProxyServiceResponseDTO;
import net.rcsoft.service.proxy.data.util.DtoUtil;

import java.lang.reflect.Method;

/**
 * HTTP Connection Handler for proxy service.
 * 
 * @author recampelos
 */
public class HttpConnectionInvocationHandler extends AbstractServiceInvocationHandler {
    
    private final Class<?> serviceClass;

    public HttpConnectionInvocationHandler(final Class<?> serviceClass) {
        this.serviceClass = serviceClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ProxyServiceRequestDTO request = DtoUtil.toProxyServiceRequeponsetDTO(this.serviceClass, method, args);
        
        final String serverUrl = this.getConfigurationProvider().getConfigurationValue(ConfigurationKeys.SERVICE_HOST_URL);
        final String serverUsername = this.getConfigurationProvider().getConfigurationValue(ConfigurationKeys.SERVICE_HOST_USERNAME);
        final String serverPassword = this.getConfigurationProvider().getConfigurationValue(ConfigurationKeys.SERVICE_HOST_PASSWORD);
        final String serverConnectionTimeoutConf =
                this.getConfigurationProvider().getConfigurationValue(ConfigurationKeys.SERVICE_HOST_CONNECTION_TIMEOUT);
        final String serverReadTimeoutConf =
                this.getConfigurationProvider().getConfigurationValue(ConfigurationKeys.SERVICE_HOST_READ_TIMEOUT);

        final int serverReadTimeout = Integer.parseInt(serverReadTimeoutConf);
        final int serverConnectionTimeout = Integer.parseInt(serverConnectionTimeoutConf);
        
        HttpConnectionRestClient.HttpResponse httpResponse = HttpConnectionRestClient.getBuilder(serverUrl)
                .basicAuth(serverUsername, serverPassword)
                .body(request, "application/json")
                .connectionTimeOut(serverConnectionTimeout)
                .readTimeOut(serverReadTimeout)
                .post();
        
        if (!httpResponse.isSuccess()) {
            throw new HttpServiceCallException(httpResponse.getBody());
        }
        
        ProxyServiceResponseDTO response = httpResponse.getBody(ProxyServiceResponseDTO.class);
        
       return DtoUtil.fromProxyServiceResponseDTO(response);
    }
}
