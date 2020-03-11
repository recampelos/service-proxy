package net.rcsoft.service.proxy.data.util;

import com.google.gson.Gson;
import net.rcsoft.service.proxy.data.dto.ProxyServiceMethodParamDTO;
import net.rcsoft.service.proxy.data.dto.DTODataType;
import net.rcsoft.service.proxy.data.dto.ProxyServiceRequestDTO;
import net.rcsoft.service.proxy.data.dto.ProxyServiceResponseDTO;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility class to marshal and unmarshal DTO.
 *
 * @author recampelos
 */
public class DtoUtil {

    /**
     * Transform object to {@link ProxyServiceRequestDTO}.
     *
     * @param serviceClass service class
     * @param method calling method
     * @param args method arguments
     * @return {@link ProxyServiceRequestDTO} instance
     */
    public static ProxyServiceRequestDTO toProxyServiceRequeponsetDTO(final Class<?> serviceClass, final Method method,
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
     * Transform object to {@link ProxyServiceMethodParamDTO}.
     *
     * @param param object to transform
     * @param index index of param
     * @return {@link ProxyServiceMethodParamDTO} instance
     */
    public static ProxyServiceMethodParamDTO toProxyServiceMethodParamDTO(final Object param, final int index) {
        ProxyServiceMethodParamDTO dto = new ProxyServiceMethodParamDTO();

        dto.setIndex(index);
        dto.setType(DtoUtil.getDataType(param));

        if (DTODataType.MAP == dto.getType()) {
            Map map = (Map) param;

            dto.setKeyClass(ObjectUtil.getClassNameFromCollection(map.keySet()));
            dto.setValueClass(ObjectUtil.getClassNameFromCollection(map.values()));
        } else {
            dto.setValueClass(ObjectUtil.getClassNameForObject(param));
        }

        dto.setParamData(new Gson().toJson(param));

        return dto;
    }

    /**
     * Transform object to {@link ProxyServiceResponseDTO}.
     *
     * @param obj object to transform
     * @return {@link ProxyServiceResponseDTO} instance
     */
    public static ProxyServiceResponseDTO toProxyServiceRequeponsetDTO(final Object obj) {
        ProxyServiceResponseDTO responseDTO = new ProxyServiceResponseDTO();

        DTODataType dataTYpe = DtoUtil.getDataType(obj);

        if (DTODataType.MAP == dataTYpe) {
            Map map = (Map) obj;

            responseDTO.setKeyClass(ObjectUtil.getClassNameFromCollection(map.keySet()));
            responseDTO.setValueClass(ObjectUtil.getClassNameFromCollection(map.values()));
        } else {
            responseDTO.setValueClass(ObjectUtil.getClassNameForObject(obj));
        }

        responseDTO.setData(new Gson().toJson(obj));

        return responseDTO;
    }

    /**
     * Transform {@link ProxyServiceResponseDTO} to object.
     *
     * @param response response to transform
     * @return response data has a object
     * @throws ClassNotFoundException if value or key class is not found
     */
    public static Object fromProxyServiceResponseDTO(final ProxyServiceResponseDTO response)
            throws ClassNotFoundException {
        Object result = null;
        Class<?> valueClass = Class.forName(response.getValueClass());

        if (DTODataType.MAP == response.getDataType()) {
            Class<?> keyClass = Class.forName(response.getKeyClass());

            result = DtoUtil.toMap(keyClass, valueClass, response.getData());
        } else if (DTODataType.LIST == response.getDataType()) {
            result = DtoUtil.toList(valueClass, response.getData());
        } else if (DTODataType.ARRAY == response.getDataType()) {
            result = DtoUtil.toArray(valueClass, response.getData());
        } else {
            result = new Gson().fromJson(response.getData(), valueClass);
        }

        return result;
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
     * Get data has a array of type.
     *
     * @param type type
     * @param data data
     * @param <T> type
     * @return array of type
     */
    public static <T> T[] toArray(final Class<T> type, final String data) {
        return (T[]) DtoUtil.toObject(type, data);
    }

    /**
     * Get data has a map.
     *
     * @param keyType key type
     * @param valueType value type
     * @param data data
     * @param <K> key type
     * @param <V> value type
     * @return list of type
     */
    public static <K,V> Map<K,V> toMap(Class<K> keyType, Class<V> valueType, final String data) {
        Map mapData = new Gson().fromJson(data, Map.class);

        return ObjectUtil.toMap(keyType, valueType, mapData);
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

    /**
     * Gets parameter type for given object
     *
     * @param o object to use
     * @return type os parameter
     */
    public static DTODataType getDataType(final Object o) {
        DTODataType type = DTODataType.OBJECT;

        if (ObjectUtil.isList(o)) {
            type = DTODataType.LIST;
        } else if (ObjectUtil.isMap(o)) {
            type = DTODataType.MAP;
        } else if (ObjectUtil.isArray(o)) {
            type = DTODataType.ARRAY;
        }

        return type;
    }
}
