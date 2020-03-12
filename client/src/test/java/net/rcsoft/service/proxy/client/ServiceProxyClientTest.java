package net.rcsoft.service.proxy.client;

import com.google.gson.Gson;
import net.rcsoft.service.proxy.client.handler.AbstractServiceInvocationHandler;
import net.rcsoft.service.proxy.client.service.TestService;
import net.rcsoft.service.proxy.data.dto.ProxyServiceRequestDTO;
import net.rcsoft.service.proxy.data.util.DtoUtil;
import net.rcsoft.service.proxy.data.util.ObjectUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit test class for service proxy.
 *
 * @author : recampelos
 */
public class ServiceProxyClientTest {

    @Test
    public void testProxyClientRequest() throws Exception {
        TestService service = ServiceProxyClient.getServiceProxy(TestService.class, new AbstractServiceInvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                ProxyServiceRequestDTO requestDTO = DtoUtil.toProxyServiceRequeponsetDTO(TestService.class, method, args);

                Assert.assertNotNull(requestDTO);
                Assert.assertEquals(requestDTO.getMethod(), method.getName());
                Assert.assertEquals(requestDTO.getServiceClass(), TestService.class.getName());

                if (args != null && args.length > 0) {
                    Assert.assertEquals(requestDTO.getParams().size(), args.length);

                    for (int i = 0; i < args.length; i++) {
                        Assert.assertEquals(requestDTO.getParams().get(i).getIndex(), i);
                        Assert.assertEquals(requestDTO.getParams().get(i).getParamData(), new Gson().toJson(args[i]));

                        if (ObjectUtil.isMap(args[i])) {
                            final Map map = (Map) args[i];
                            final String keyClass = ObjectUtil.getClassNameForObject(map.keySet());
                            final String valueClass = ObjectUtil.getClassNameForObject(map.values());

                            Assert.assertEquals(requestDTO.getParams().get(i).getKeyClass(), keyClass);
                            Assert.assertEquals(requestDTO.getParams().get(i).getValueClass(), valueClass);
                        } else {
                            final String valueClass = ObjectUtil.getClassNameForObject(args[i]);

                            Assert.assertNull(requestDTO.getParams().get(i).getKeyClass());
                            Assert.assertEquals(requestDTO.getParams().get(i).getValueClass(), valueClass);
                        }
                    }
                } else {
                    Assert.assertEquals(requestDTO.getParams().size(), 0);
                }

                return null;
            }
        });

        service.complextResult(new Data());
        service.multipleParams("asd", new Data());
        service.noParams();
        service.simpleResult("asd");
        service.voidResult();
        service.listParam(new ArrayList<>());

        List<Data> list = new ArrayList<>();
        list.add(new Data());
        service.listParam(list);

        Data[] array = new Data[]{};
        service.arrayParam(array);
        array = new Data[]{new Data()};
        service.arrayParam(array);

        Map<String, Data> map = new HashMap<>();
        map.put("key", new Data());
        service.mapParam(map);
    }
}
