package sen.rpc.provider.service;

import sen.rpc.provider.annotation.RpcService;
import api.IHelloService;

/**
 * @author Huang Sen
 * @date 2019/6/26
 * @description
 */
@RpcService
public class HelloServiceImpl implements IHelloService {
    public String hello(String name) {
        return String.format("Hello %s,I'm provider", name);
    }
}
