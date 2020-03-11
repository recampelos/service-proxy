package net.rcsoft.service.proxy.data.util;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Object Util unit test.
 *
 * @author : recampelos
 */
public class ObjectUtilTest {

    @Test
    public void testIsList() throws Exception {
        Assert.assertTrue(ObjectUtil.isList(new ArrayList<>()));
        Assert.assertTrue(ObjectUtil.isList(new HashSet<>()));
    }

    @Test
    public void testIsMap() throws Exception {
        Assert.assertTrue(ObjectUtil.isMap(new HashMap<>()));
    }

    @Test
    public void testIsArray() throws Exception {
        Assert.assertTrue(ObjectUtil.isArray(new Object[]{}));
    }

    @Test
    public void testToList() throws Exception {
        Object[] data = new Object[]{"data1","data2"};
        List<String> data2 = ObjectUtil.toList(String.class, data);

        Assert.assertEquals(data.length, data2.size());
        Assert.assertEquals(data[0], data2.get(0));
        Assert.assertEquals(data[1], data2.get(1));
    }

    @Test
    public void testGetClassFromList() throws Exception {
        List<String> list = new ArrayList<>();
        list.add("data");

        Class<?> listClass = ObjectUtil.getClassFromCollection(list);

        Assert.assertEquals(listClass, String.class);
    }

    @Test
    public void testGetClassnameFromList() throws Exception {
        List<String> list = new ArrayList<>();
        list.add("data");

        String listClass = ObjectUtil.getClassNameFromCollection(list);

        Assert.assertEquals(listClass, String.class.getName());

        List<String> list2 = new ArrayList<>();

        String listClass2 = ObjectUtil.getClassNameFromCollection(list2);

        Assert.assertEquals(listClass2, Object.class.getName());
    }

    @Test
    public void testGetClassnameFromObject() throws Exception {
        List<String> list = new ArrayList<>();
        list.add("data");

        String listClass = ObjectUtil.getClassNameForObject(list);
        String objClass = ObjectUtil.getClassNameForObject("data");

        Assert.assertEquals(listClass, String.class.getName());
        Assert.assertEquals(objClass, String.class.getName());
    }
}
