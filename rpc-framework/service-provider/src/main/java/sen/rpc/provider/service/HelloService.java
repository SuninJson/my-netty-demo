package sen.rpc.provider.service;

import sen.netty.provider.api.IHelloService;
import sen.rpc.provider.annotation.RpcService;

/**
 * @author Huang Sen
 * @date 2019/6/26
 * @description
 */
@RpcService
public class HelloService implements IHelloService {
    public String hello(String name) {
        return String.format("Hello %s,I'm provider", name);
    }
}
