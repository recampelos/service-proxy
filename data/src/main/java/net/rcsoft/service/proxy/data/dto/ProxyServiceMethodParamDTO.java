package net.rcsoft.service.proxy.data.dto;

/**
 * DTO form service call params.
 *
 * @author recampelos
 */
public class ProxyServiceMethodParamDTO {
    
    private int index;
    
    private String paramData;

    private String keyClass;

    private String valueClass;

    private DTODataType type;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getParamData() {
        return paramData;
    }

    public void setParamData(String paramData) {
        this.paramData = paramData;
    }

    public DTODataType getType() {
        return type;
    }

    public void setType(DTODataType type) {
        this.type = type;
    }

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
}
