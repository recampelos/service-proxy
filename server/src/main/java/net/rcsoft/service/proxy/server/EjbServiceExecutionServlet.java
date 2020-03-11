package net.rcsoft.service.proxy.server;

import com.google.gson.Gson;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rcsoft.service.proxy.data.dto.DTODataType;
import net.rcsoft.service.proxy.data.dto.ProxyServiceMethodParamDTO;
import net.rcsoft.service.proxy.data.dto.ProxyServiceRequestDTO;
import net.rcsoft.service.proxy.data.dto.ProxyServiceResponseDTO;
import net.rcsoft.service.proxy.data.util.DtoUtil;
import net.rcsoft.service.proxy.server.provider.InitialContextServiceProvider;
import net.rcsoft.service.proxy.server.provider.ServiceProvider;

/**
 * HttpServlet to execute local services registered in JNDI.
 *
 * @author recampelos
 */
public class EjbServiceExecutionServlet extends HttpServlet {
    
    private static final String JDNI_PREFIX = "jndi.prefix";
    
    private static final String SERVICE_PROVIDER_CLASS = "service.provider.class";
    
    private String jndiPrefix;
    
    private ServiceProvider serviceProvider;

    @Override
    public void init() throws ServletException {
        super.init();
        
        this.jndiPrefix = this.getServletConfig().getInitParameter(JDNI_PREFIX);
        this.serviceProvider = new InitialContextServiceProvider(this.jndiPrefix);
        
        final String providerFqn = this.getServletConfig().getInitParameter(SERVICE_PROVIDER_CLASS);
        
        if (providerFqn != null && !providerFqn.trim().isEmpty()) {
            try {
                Class<? extends ServiceProvider> serviceClass =  (Class<? extends ServiceProvider>) Class.forName(providerFqn);
                
                this.serviceProvider = serviceClass.newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(EjbServiceExecutionServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final ProxyServiceRequestDTO request = new Gson().fromJson(req.getReader(), ProxyServiceRequestDTO.class);
            final Class<?> serviceClass = Class.forName(request.getServiceClass());
            final Object serviceInstance = this.serviceProvider.getServiceInstance(serviceClass);
            final List<MethodParam> methodParams = MethodParam.fromProxyServiceMehodParamDTOList(request.getParams());
            Collections.sort(methodParams, (MethodParam o1, MethodParam o2) -> o1.getIndex().compareTo(o2.getIndex()));
            final List<Class<?>> paramTypes = new ArrayList<>();
            final List<Object> paramData = new ArrayList<>();
            methodParams.stream().forEach((param) -> {
                paramTypes.add(param.getClass());
                paramData.add(param.getParamData());
            });
            final Method method = this.getMethod(serviceInstance.getClass(), request.getMethod(), paramTypes.toArray(new Class<
                    ?>[paramTypes.size()]));

            final Object result = method.invoke(serviceInstance, paramData.toArray(new Object[paramTypes.size()]));
            final ProxyServiceResponseDTO response = DtoUtil.toProxyServiceRequeponsetDTO(result);
            
            final String respContent = new Gson().toJson(response, ProxyServiceResponseDTO.class);
            resp.setStatus(200);
            resp.setContentType("application/json");
            resp.getWriter().append(respContent);
            resp.getWriter().close();
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NamingException ex) {
            Logger.getLogger(EjbServiceExecutionServlet.class.getName()).log(Level.SEVERE, null, ex);
            
            resp.setStatus(500);
            resp.setContentType("text/plain");
            resp.getWriter().append(ex.getLocalizedMessage());
            resp.getWriter().close();
        } catch (Exception ex) {
            Logger.getLogger(EjbServiceExecutionServlet.class.getName()).log(Level.SEVERE, null, ex);

            resp.setStatus(500);
            resp.setContentType("text/plain");
            resp.getWriter().append(ex.getLocalizedMessage());
            resp.getWriter().close();
        }
    }
    
    private Method getMethod(final Class<?> serviceClass, final String methodName, final Class<?> ... paramTypes) throws NoSuchMethodException {
        return serviceClass.getDeclaredMethod(methodName, paramTypes);
    }
    
    private static class MethodParam {
        
        private Integer index;
        
        private Class<?> paramClass;
        
        private Object paramData;

        public Class<?> getParamClass() {
            return paramClass;
        }

        public Object getParamData() {
            return paramData;
        }

        public Integer getIndex() {
            return index;
        }
        
        public static MethodParam fromProxyServiceMehodParamDTO(final ProxyServiceMethodParamDTO dto) throws ClassNotFoundException {
            MethodParam methodParam = new MethodParam();
            Class<?> valueClass = Class.forName(dto.getValueClass());
            methodParam.index = dto.getIndex();

            if (DTODataType.MAP == dto.getType()) {
                Class<?> keyClass = Class.forName(dto.getKeyClass());

                methodParam.paramData = DtoUtil.toMap(keyClass, valueClass, dto.getParamData());
                methodParam.paramClass = methodParam.paramData.getClass();
            } else if (DTODataType.LIST == dto.getType()) {
                methodParam.paramData = DtoUtil.toList(valueClass, dto.getParamData());
                methodParam.paramClass = methodParam.paramData.getClass();
            } else {
                methodParam.paramData = DtoUtil.toObject(valueClass, dto.getParamData());
                methodParam.paramClass = valueClass;
            }
            
            return methodParam;
        }
        
        public static List<MethodParam> fromProxyServiceMehodParamDTOList(final List<ProxyServiceMethodParamDTO> dtos) throws ClassNotFoundException {
            List<MethodParam> methodParams = new ArrayList<>();
            
            if (dtos != null && dtos.size() > 0) {
                for (ProxyServiceMethodParamDTO dto : dtos) {
                    methodParams.add(fromProxyServiceMehodParamDTO(dto));
                }
            }
            
            return methodParams;
        }
    }
}
