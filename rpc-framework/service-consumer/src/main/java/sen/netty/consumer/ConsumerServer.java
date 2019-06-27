package sen.netty.consumer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import rpc.config.CommonProperties;
import rpc.protocol.RpcProtocol;
import sen.netty.consumer.factory.RpcProxyProvider;
import sen.netty.consumer.handler.RpcProxyHandler;
import sen.netty.provider.api.IHelloService;
import util.NettyUtils;

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

    /**
     * RPC
     */
    public void doRpc(RpcProtocol rpcProtocol) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        NettyUtils.initPipelineForRpc(ch)
                                .addLast("resultHandler", new RpcProxyHandler().getResultHandlerInstance());
                    }
                })
                .option(ChannelOption.TCP_NODELAY, true);

        try {
            ChannelFuture future = bootstrap.connect(CommonProperties.REGISTRY_IP, CommonProperties.REGISTRY_PORT).sync();

            future.channel().writeAndFlush(rpcProtocol).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        IHelloService helloService = RpcProxyProvider.newProxyInstance(IHelloService.class);
        System.out.println(helloService.hello("Consumer"));

    }
}
