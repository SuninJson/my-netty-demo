package sen.rpc.provider;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import rpc.bean.ServerRegisterInfo;
import rpc.config.CommonProperties;
import rpc.protocol.ServerRegisterProtocol;
import sen.rpc.provider.annotation.RpcService;
import sen.rpc.provider.config.ProjectProperties;
import sen.rpc.provider.handler.LocalInvokeHandler;
import sen.rpc.provider.handler.ServerRegisterHandler;
import util.ClassUtil;
import util.NettyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Huang Sen
 * @date 2019/6/26
 * @description
 */
public class ProviderServer {

    private static final List<Object> services = new ArrayList<>();

    public ProviderServer() {
        initService();
    }

    /**
     * 初始化service
     */
    private void initService() {
        List<Class<?>> classList = ClassUtil.getAllClassByAnnotation(ProjectProperties.SERVICE_PACKAGE, RpcService.class);
        for (Class<?> c : classList) {
            try {
                Object instance = c.newInstance();
                services.add(instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

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
                                    .addLast(new LocalInvokeHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(ProjectProperties.PORT).sync();
            System.out.println("Provider Server have already started");

            //将该服务注册到服务注册中心
            serverRegister();

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 将服务注册至服务注册中心
     */
    private void serverRegister() {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        NettyUtils.initPipelineForRpc(ch)
                                .addLast("registerHandler", new ServerRegisterHandler());
                    }
                })
                .option(ChannelOption.TCP_NODELAY, true);

        try {
            ChannelFuture future = bootstrap.connect(CommonProperties.REGISTRY_IP, CommonProperties.REGISTRY_PORT).sync();

            ServerRegisterProtocol registerProtocol = wrapRegisterInfoAsProtocol();
            future.channel().writeAndFlush(registerProtocol).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将注册信息包装为传输协议
     */
    private ServerRegisterProtocol wrapRegisterInfoAsProtocol() {
        ServerRegisterProtocol registerProtocol = new ServerRegisterProtocol();
        ArrayList<ServerRegisterInfo> registerInfos = new ArrayList<>();
        ServerRegisterInfo registerInfo = new ServerRegisterInfo();
        registerInfo.setServerName(this.getClass().getSimpleName());
        registerInfos.add(registerInfo);
        registerProtocol.setServerRegisterInfos(registerInfos);
        return registerProtocol;
    }

    public static void main(String[] args) {
        new ProviderServer().start();
    }
}
