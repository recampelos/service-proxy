package net.rcsoft.service.proxy.client.service;

import net.rcsoft.service.proxy.client.Data;

import java.util.List;

/**
 * Test service interface.
 *
 * @author : recampelos
 */
public interface TestService {

    String noParams();

    void voidResult();

    Data complextResult(Data param);

    String simpleResult(String param);

    void multipleParams(String param1, Data param2);

    void listParam(final List<Data> list);
}
