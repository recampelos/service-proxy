package net.rcsoft.service.proxy.data.util;

import net.rcsoft.service.proxy.data.dto.DTODataType;
import net.rcsoft.service.proxy.data.dto.ProxyServiceMethodParamDTO;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

/**
 * Dto Util unit test.
 *
 * @author : recampelos
 */
public class DtoUtilTest {

    @Test
    public void testToProxyServiceMethodParamDTO() throws Exception {
        String data1 = "data1";

        ProxyServiceMethodParamDTO param = DtoUtil.toProxyServiceMethodParamDTO(data1, 1);

        Assert.assertNotNull(param);
        Assert.assertEquals(param.getIndex(), 1);
        Assert.assertEquals(param.getValueClass(), String.class.getName());
        Assert.assertEquals(param.getParamData(), "\"data1\"");
        Assert.assertEquals(param.getType(), DTODataType.OBJECT);

        List<Integer> data2 = new ArrayList<>();
        data2.add(2);
        data2.add(3);

        param = DtoUtil.toProxyServiceMethodParamDTO(data2, 2);

        Assert.assertNotNull(param);
        Assert.assertEquals(param.getIndex(), 2);
        Assert.assertEquals(param.getValueClass(), Integer.class.getName());
        Assert.assertEquals(param.getParamData(), "[2,3]");
        Assert.assertEquals(param.getType(), DTODataType.LIST);

        String[] strArray = new String[]{"2","3"};

        param = DtoUtil.toProxyServiceMethodParamDTO(strArray, 3);

        Assert.assertNotNull(param);
        Assert.assertEquals(param.getIndex(), 3);
        Assert.assertEquals(param.getValueClass(), String[].class.getName());
        Assert.assertEquals(param.getParamData(), "[\"2\",\"3\"]");
        Assert.assertEquals(param.getType(), DTODataType.ARRAY);

        Map<String, Integer> map = new HashMap<>();
        map.put("1",1);

        param = DtoUtil.toProxyServiceMethodParamDTO(map, 4);

        Assert.assertNotNull(param);
        Assert.assertEquals(param.getIndex(), 4);
        Assert.assertEquals(param.getKeyClass(), String.class.getName());
        Assert.assertEquals(param.getValueClass(), Integer.class.getName());
        Assert.assertEquals(param.getParamData(), "{\"1\":1}");
        Assert.assertEquals(param.getType(), DTODataType.MAP);
    }

    @Test
    public void testFromResponseData() throws Exception {
        Object obj = DtoUtil.fromResponseData(String.class, "\"data\"", false);
        List list = (List) DtoUtil.fromResponseData(String.class, "[\"data\",\"data2\"]", true);

        Assert.assertEquals(obj, "data");
        Assert.assertNotNull(list);
        Assert.assertEquals(list.size(), 2);
        Assert.assertEquals(list.get(0), "data");
        Assert.assertEquals(list.get(1), "data2");
    }

    @Test
    public void testToList() throws Exception {
        List<String> listStr = DtoUtil.toList(String.class, "[\"data\",\"data2\"]");

        Assert.assertNotNull(listStr);
        Assert.assertEquals(listStr.size(), 2);
        Assert.assertEquals(listStr.get(0), "data");
        Assert.assertEquals(listStr.get(1), "data2");
    }

    @Test
    public void testToObject() throws Exception {
        String valueStr = DtoUtil.toObject(String.class, "\"data\"");
        Integer valueInt = DtoUtil.toObject(Integer.class, "25");

        Assert.assertEquals(valueStr, "data");
        Assert.assertEquals(valueInt.intValue(), 25);
    }
}
