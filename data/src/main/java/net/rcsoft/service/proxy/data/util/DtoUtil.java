package net.rcsoft.service.proxy.data.util;

import com.google.gson.Gson;
import net.rcsoft.service.proxy.data.dto.ProxyServiceMethodParamDTO;

import java.lang.reflect.Array;
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
