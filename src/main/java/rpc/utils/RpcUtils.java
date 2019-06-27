package rpc.utils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import rpc.handler.RpcResponseHandler;
import rpc.protocol.RpcProtocol;
import util.NettyUtils;

/**
 * @author Huang Sen
 * @date 2019/6/27
 * @description
 */
public class RpcUtils {
    public static Object rpcServerService(String host, int port, RpcProtocol rpcProtocol) {
        RpcResponseHandler rpcResponseHandler = new RpcResponseHandler();

        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        NettyUtils.initPipelineForRpc(ch)
                                .addLast("responseHandler", rpcResponseHandler);
                    }
                });

        try {
            ChannelFuture future = bootstrap.connect(host, port).sync();

            System.out.println(String.format("远程调用<%s.%s.%s()>", rpcProtocol.getServerName(), rpcProtocol.getServiceName(), rpcProtocol.getMethodName()));
            future.channel().writeAndFlush(rpcProtocol).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
        return rpcResponseHandler.getResponse();
    }
}
