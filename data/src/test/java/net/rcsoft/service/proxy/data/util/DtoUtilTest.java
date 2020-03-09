package net.rcsoft.service.proxy.data.util;

import net.rcsoft.service.proxy.data.dto.ProxyServiceMethodParamDTO;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

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
        List<Integer> data2 = new ArrayList<>();
        data2.add(2);
        data2.add(3);

        ProxyServiceMethodParamDTO param1 = DtoUtil.toProxyServiceMethodParamDTO(data1, 1);
        ProxyServiceMethodParamDTO param2 = DtoUtil.toProxyServiceMethodParamDTO(data2, 2);

        Assert.assertNotNull(param1);
        Assert.assertEquals(param1.getIndex(), 1);
        Assert.assertEquals(param1.getParamClass(), String.class.getName());
        Assert.assertEquals(param1.getParamData(), "\"data1\"");
        Assert.assertFalse(param1.isList());

        Assert.assertNotNull(param2);
        Assert.assertEquals(param2.getIndex(), 2);
        Assert.assertEquals(param2.getParamClass(), Integer.class.getName());
        Assert.assertEquals(param2.getParamData(), "[2,3]");
        Assert.assertTrue(param2.isList());
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
