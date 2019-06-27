package sen.netty.consumer.factory;

import sen.netty.consumer.handler.RpcProxyHandler;

import java.lang.reflect.Proxy;

/**
 * @author Huang Sen
 * @date 2019/6/27
 * @description
 */
public class RpcProxyProvider {
    public static <T> T create(Class<T> apiClass) {
        Class<?>[] classes = apiClass.isInterface() ?
                new Class<?>[]{apiClass} : apiClass.getInterfaces();
        return (T) Proxy.newProxyInstance(apiClass.getClassLoader(), classes, new RpcProxyHandler(apiClass));
    }
}