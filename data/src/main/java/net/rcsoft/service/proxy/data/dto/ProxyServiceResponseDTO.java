package net.rcsoft.service.proxy.data.dto;

/**
 * Proxy service generic Response. 
 *
 * @author recampelos
 */
public class ProxyServiceResponseDTO {

    private String keyClass;

    private String valueClass;
    
    private String data;

    private DTODataType dataType;

    public String getKeyClass() {
        return keyClass;
    }

    public void setKeyClass(String keyClass) {
        this.keyClass = keyClass;
    }

    public String getValueClass() {
        return valueClass;
    }

    public void setValueClass(String valueClass) {
        this.valueClass = valueClass;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public DTODataType getDataType() {
        return dataType;
    }

    public void setDataType(DTODataType dataType) {
        this.dataType = dataType;
    }
}
