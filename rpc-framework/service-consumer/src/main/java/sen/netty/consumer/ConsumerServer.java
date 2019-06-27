package sen.netty.consumer;

import sen.netty.consumer.factory.RpcProxyProvider;
import sen.netty.provider.api.ICalculateService;
import sen.netty.provider.api.IHelloService;

/**
 * @author Huang Sen
 * @date 2019/6/26
 * @description
 */
public class ConsumerServer {

    private static final ConsumerServer INSTANCE = new ConsumerServer();

    private ConsumerServer() {
    }

    public static ConsumerServer getInstance() {
        return INSTANCE;
    }

    public static void main(String[] args) {
        IHelloService helloService = RpcProxyProvider.create(IHelloService.class);
        System.out.println(helloService.hello("Consumer") + "\n");

        ICalculateService calculateService = RpcProxyProvider.create(ICalculateService.class);
        System.out.println(calculateService.add(6, 6) + "\n");
        System.out.println(calculateService.mul(6, 6) + "\n");

    }
}
