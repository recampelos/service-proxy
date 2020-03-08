package net.rcsoft.service.proxy.client.handler;

import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.util.ArrayList;
import net.rcsoft.service.proxy.client.configuration.ConfigurationKeys;
import net.rcsoft.service.proxy.client.exception.HttpServiceCallException;
import net.rcsoft.service.proxy.client.util.HttpConnectionRestClient;
import net.rcsoft.service.proxy.data.configuration.ConfigurationProvider;
import net.rcsoft.service.proxy.data.dto.ProxyServiceMehodParamDTO;
import net.rcsoft.service.proxy.data.dto.ProxyServiceRequestDTO;
import net.rcsoft.service.proxy.data.dto.ProxyServiceResponseDTO;

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
        ProxyServiceRequestDTO request = new ProxyServiceRequestDTO();
        
        request.setServiceClass(serviceClass.getName());
        request.setMethod(method.getName());
        request.setParams(new ArrayList<>());
        
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                ProxyServiceMehodParamDTO param = new ProxyServiceMehodParamDTO();
                
                param.setIndex(i);
                param.setParamClass(args[i].getClass().getName());
                param.setParamData(new Gson().toJson(args[i]));
                
                request.getParams().add(param);
            }
        }
        
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
        
        Class<?> serviceResponseClass = Class.forName(response.getResponseClass());
        
        return new Gson().fromJson(response.getData(), serviceResponseClass);
    }
}