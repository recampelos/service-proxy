package net.rcsoft.service.proxy.data.dto;

/**
 * Proxy service generic Response. 
 *
 * @author recampelos
 */
public class ProxyServiceResponseDTO {
    
    private String responseClass;
    
    private String data;
    
    private Boolean isList;

    public String getResponseClass() {
        return responseClass;
    }

    public void setResponseClass(String responseClass) {
        this.responseClass = responseClass;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean getIsList() {
        return isList;
    }

    public void setIsList(Boolean isList) {
        this.isList = isList;
    }
}
