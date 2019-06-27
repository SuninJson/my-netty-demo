package sen.netty.consumer.handler;

import rpc.config.CommonProperties;
import rpc.protocol.RpcProtocol;
import rpc.utils.RpcUtils;
import sen.netty.provider.config.ProviderApiProperties;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Huang Sen
 * @date 2019/6/27
 * @description
 */
public class RpcProxyHandler implements InvocationHandler {

    private Class<?> targetClass;

    public <T> RpcProxyHandler(Class<T> apiClass) {
        targetClass = apiClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Object response;
        if (Object.class.equals(method.getDeclaringClass())) {
            response = method.invoke(proxy, args);
        } else {
            response = doRpc(method, args);
        }
        return response;
    }

    private Object doRpc(Method method, Object[] args) {
        RpcProtocol rpcProtocol = new RpcProtocol();
        rpcProtocol.setServerName(ProviderApiProperties.SERVER_NAME);
        rpcProtocol.setServiceName(targetClass.getSimpleName());
        rpcProtocol.setMethodName(method.getName());
        rpcProtocol.setParamTypes(method.getParameterTypes());
        rpcProtocol.setParamValue(args);

        return RpcUtils.rpcServerService(CommonProperties.REGISTRY_IP, CommonProperties.REGISTRY_PORT, rpcProtocol);
    }


}
