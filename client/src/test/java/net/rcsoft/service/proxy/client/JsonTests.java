package net.rcsoft.service.proxy.client;

import com.google.gson.Gson;
import net.rcsoft.service.proxy.data.util.ObjectUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for json marshal an unmarshal.
 *
 * @author recampelos
 */
public class JsonTests {

    @Test
    public void marshalUnmarshalComplexObject() throws Exception {
        Data data = new Data();
        data.setName("Name");
        data.setAge(25);

        String json = new Gson().toJson(data);

        Assert.assertEquals(json, "{\"name\":\"Name\",\"age\":25}");

        Data data2 = new Gson().fromJson(json, Data.class);

        Assert.assertEquals(data, data2);
    }

    @Test
    public void marshalComplexObjectList() throws Exception {
        List<Data> data = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            Data obj = new Data();
            obj.setName("Name " + i);
            obj.setAge(i);
            data.add(obj);
        }

        String json = new Gson().toJson(data);

        Assert.assertEquals(json, "[{\"name\":\"Name 0\",\"age\":0},{\"name\":\"Name 1\",\"age\":1}]");

        Class<?> dataClass = Class.forName("net.rcsoft.service.proxy.client.Data");
        Object array = Array.newInstance(dataClass, 1);

        Object[] dataArray = (Object[]) new Gson().fromJson(json, array.getClass());
        List data2 = ObjectUtil.toList(dataClass, dataArray);

        Assert.assertEquals(data.size(), data2.size());
        Assert.assertEquals(data, data2);
    }

    @Test
    public void marshalString() throws Exception {
        String data = "data";

        String json = new Gson().toJson(data);

        Assert.assertEquals(json, "\"data\"");

        String data2 = new Gson().fromJson(json, String.class);

        Assert.assertEquals(data, data2);
    }

    @Test
    public void marshalInteger() throws Exception {
        Integer data = 25;

        String json = new Gson().toJson(data);

        Assert.assertEquals(json, "25");

        Integer data2 = new Gson().fromJson(json, Integer.class);

        Assert.assertEquals(data, data2);
    }

    @Test
    public void marshalBoolean() throws Exception {
        Boolean data = true;
        String json = new Gson().toJson(data);


        Boolean data2 = new Gson().fromJson(json, Boolean.class);

        Assert.assertTrue(data2);
    }
}