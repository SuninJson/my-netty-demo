package sen.netty.consumer.factory;

import sen.netty.consumer.handler.RpcProxyHandler;

import java.lang.reflect.Proxy;

/**
 * @author Huang Sen
 * @date 2019/6/27
 * @description
 */
public class RpcProxyProvider {
    public static <T> T newProxyInstance(Class<T> apiClass) {
        return (T) Proxy.newProxyInstance(apiClass.getClassLoader(), new Class[]{apiClass}, new RpcProxyHandler());
    }
}