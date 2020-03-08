package net.rcsoft.service.proxy.data.dto;

import java.util.List;

/**
 * Proxy ervice call request DTO;
 *
 * @author recampelos
 */
public class ProxyServiceRequestDTO {
    
    private String serviceClass;
    
    private String method;
    
    private List<ProxyServiceMethodParamDTO> params;

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<ProxyServiceMethodParamDTO> getParams() {
        return params;
    }

    public void setParams(List<ProxyServiceMethodParamDTO> params) {
        this.params = params;
    }
}
