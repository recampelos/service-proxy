package net.rcsoft.service.proxy.data.util;

import com.google.gson.Gson;
import net.rcsoft.service.proxy.data.dto.ProxyServiceMethodParamDTO;
import net.rcsoft.service.proxy.data.dto.ProxyServiceRequestDTO;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to marshal and unmarshal DTO.
 *
 * @author recampelos
 */
public class DtoUtil {

    /**
     * Transform object to
     *
     * @param serviceClass service class
     * @param method calling method
     * @param args method arguments
     * @return {@link ProxyServiceRequestDTO} instance
     */
    public static ProxyServiceRequestDTO toProxyServiceRequestDTO(final Class<?> serviceClass, final Method method,
            final Object[] args) {
        ProxyServiceRequestDTO requestDTO = new ProxyServiceRequestDTO();

        requestDTO.setServiceClass(serviceClass.getName());
        requestDTO.setMethod(method.getName());
        requestDTO.setParams(new ArrayList<>());

        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                requestDTO.getParams().add(toProxyServiceMethodParamDTO(args[i], i));
            }
        }

        return requestDTO;
    }

    /**
     * Transform object to
     *
     * @param param object to transform
     * @param index index of param
     * @return {@link ProxyServiceMethodParamDTO} instance
     */
    public static ProxyServiceMethodParamDTO toProxyServiceMethodParamDTO(final Object param, final int index) {
        ProxyServiceMethodParamDTO dto = new ProxyServiceMethodParamDTO();

        dto.setIndex(index);
        dto.setList(ObjectUtil.isList(param));
        dto.setParamClass(ObjectUtil.getClassNameForObject(param));
        dto.setParamData(new Gson().toJson(param));

        return dto;
    }

    /**
     * Get response object from data
     *
     * @param type response data type
     * @param data data
     * @param isList true if response is a list
     * @return the response object
     */
    public static <T> Object fromResponseData(final Class<T> type, final String data, final boolean isList) {
        if (isList) {
            return toList(type, data);
        } else {
            return toObject(type, data);
        }
    }

    /**
     * Get data has a list of type.
     *
     * @param type type
     * @param data data
     * @param <T> type
     * @return list of type
     */
    public static <T> List<T> toList(final Class<T> type, final String data) {
        Object array = Array.newInstance(type, 1);

        Object[] dataArray = (Object[]) new Gson().fromJson(data, array.getClass());

        return ObjectUtil.toList(type, dataArray);
    }

    /**
     * Get data has a object of type.
     *
     * @param type type
     * @param data data
     * @param <T> type
     * @return object of type
     */
    public static <T> T toObject(final Class<T> type, final String data) {
        return new Gson().fromJson(data, type);
    }
}
