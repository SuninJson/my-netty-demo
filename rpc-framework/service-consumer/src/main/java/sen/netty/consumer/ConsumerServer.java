package sen.netty.consumer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import rpc.bean.ServerRegisterInfo;
import rpc.config.CommonProperties;
import rpc.protocol.RpcProtocol;
import rpc.protocol.ServerRegisterProtocol;
import sen.netty.consumer.handler.RpcResultHandler;
import sen.rpc.provider.annotation.RpcService;
import sen.rpc.provider.config.ProjectProperties;
import sen.rpc.provider.handler.LocalInvokeHandler;
import sen.rpc.provider.handler.ServerRegisterHandler;
import util.NettyUtils;

import java.util.ArrayList;

/**
 * @author Huang Sen
 * @date 2019/6/26
 * @description
 */
public class ConsumerServer {

    /**
     * RPC
     */
    private void doRpc() {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        NettyUtils.initPipelineForRpc(ch)
                                .addLast("registerHandler", new RpcResultHandler());
                    }
                })
                .option(ChannelOption.TCP_NODELAY, true);

        try {
            ChannelFuture future = bootstrap.connect(CommonProperties.REGISTRY_IP, CommonProperties.REGISTRY_PORT).sync();

            RpcProtocol rpcProtocol = wrapRpcInfoAsProtocol();
            future.channel().writeAndFlush(rpcProtocol).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将注册信息包装为传输协议
     */
    private RpcProtocol wrapRpcInfoAsProtocol() {
        RpcProtocol rpcProtocol = new RpcProtocol();
        rpcProtocol.setServerName("");
        rpcProtocol.setServiceName("");
        rpcProtocol.setMethodName("");
        rpcProtocol.setParamTypes(new String[] ());
        rpcProtocol.setParamValue(new Object[] ());

        return rpcProtocol;
    }

    public static void main(String[] args) {
        new ProviderServer().start();
    }
}
