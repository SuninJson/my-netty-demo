package sen.rpc.registry;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import rpc.config.CommonProperties;
import rpc.protocol.ServerRegisterProtocol;
import sen.rpc.registry.handler.RpcServiceHandler;
import sen.rpc.registry.handler.ServerRegisterHandler;
import util.NettyUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 服务注册中心
 * 1、初始化服务列表
 * 2、开启服务注册中心通信
 * 3、接收网络请求后调用服务端接口
 *
 * @author Huang Sen
 * @date 2019/6/26
 * @description
 */
public class RegistryServer {

    /**
     * Key：Server名称
     * Value：Server IP
     */
    private static List<ServerRegisterProtocol> serverMappings = Collections.synchronizedList(new ArrayList<>());

    /**
     * 开启服务注册中心
     */
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            NettyUtils.initPipelineForRpc(ch)
                                    .addLast(new ServerRegisterHandler())
                                    .addLast(new RpcServiceHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(CommonProperties.REGISTRY_PORT).sync();
            System.out.println("Registry Server have already started");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 添加服务至 serverMapping
     */
    public static void addService(ServerRegisterProtocol protocol) {
        ServerRegisterProtocol serverInfo = getServerInfo(protocol.getName());
        serverMappings.remove(serverInfo);
        serverMappings.add(protocol);
        System.out.println("已注册服务：" + protocol.getName());
    }

    private static ServerRegisterProtocol getServerInfo(String serverName) {
        return serverMappings.stream().filter(s -> serverName.equals(s.getName())).findFirst().orElse(null);
    }

    public static int getServerPort(String serverName) {
        ServerRegisterProtocol serverInfo = getServerInfo(serverName);
        return serverInfo == null ? 0 : serverInfo.getPort();
    }

    public static String getServerHost(String serverName) {
        ServerRegisterProtocol serverInfo = getServerInfo(serverName);
        return serverInfo == null ? "" : serverInfo.getHost();
    }

    public static void main(String[] args) {
        new RegistryServer().start();
    }
}
