package net.rcsoft.service.proxy.data.dto;

/**
 * DTO form service call params.
 *
 * @author recampelos
 */
public class ProxyServiceMehodParamDTO {
    
    private int index;
    
    private String paramClass;
    
    private String paramData;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getParamClass() {
        return paramClass;
    }

    public void setParamClass(String paramClass) {
        this.paramClass = paramClass;
    }

    public String getParamData() {
        return paramData;
    }

    public void setParamData(String paramData) {
        this.paramData = paramData;
    }
    
    
}
