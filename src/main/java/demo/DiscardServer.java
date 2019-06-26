package demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DiscardServer {
    private int port = 8080;

    public DiscardServer(int port) {
        this.port = port;
    }

    public void run() throws Exception{
        System.out.println("Discard provider start...");

        /*
         * NioEventLoopGroup is a multithreaded event loop that handles I/O operation.
         * Netty provides various EventLoopGroup implementations for different kind of transports.
         * We are implementing a provider-side application in this example, and therefore two NioEventLoopGroup will be used.
         * The first one, often called 'boss', accepts an incoming connection.
         * The second one, often called 'worker', handles the traffic of the accepted connection once the boss accepts the connection and registers the accepted connection to the worker.
         * How many Threads are used and how they are mapped to the created Channels depends on the EventLoopGroup implementation and may be even configurable via a constructor.
         */
        //创建BOSS线程组以及worker线程组对象
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //配置启动参数
            /*
             * ServerBootstrap is a helper class that sets up a provider.
             * You can set up the provider using a Channel directly.
             * However, please note that this is a tedious process, and you do not need to do that in most cases.
             */
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    /*
                     * Here, we specify to use the NioServerSocketChannel class which is used to instantiate a new Channel to accept incoming connections.
                     */
                    .channel(NioServerSocketChannel.class)
                    /*
                     * The demo.handler specified here will always be evaluated by a newly accepted Channel.
                     * The ChannelInitializer is a special demo.handler that is purposed to help a user configure a new Channel.
                     * It is most likely that you want to configure the ChannelPipeline of the new Channel by adding some handlers such as demo.DiscardServerHandler to implement your network application.
                     * As the application gets complicated, it is likely that you will add more handlers to the pipeline and extract this anonymous class into a top-level class eventually.
                     */
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DiscardServerHandler());
                        }
                    })
                    /*
                     * You can also set the parameters which are specific to the Channel implementation.
                     * We are writing a TCP/IP provider, so we are allowed to set the socket options such as tcpNoDelay and keepAlive.
                     * Please refer to the api docs of ChannelOption and the specific ChannelConfig implementations to get an overview about the supported ChannelOptions.
                     */
                    /*
                     * option() is for the NioServerSocketChannel that accepts incoming connections.
                     * childOption() is for the Channels accepted by the parent ServerChannel, which is NioServerSocketChannel in this case.
                     */
                    //BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //启用心跳保活机制。在双方TCP套接字建立连接后（即都进入ESTABLISHED状态）并且在两个小时左右上层没有任何数据传输的情况下，这套机制才会被激活
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            //绑定端口并开始接收连接
            ChannelFuture future = bootstrap.bind(port).sync();

            //等待Server Socket关闭
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        new DiscardServer(port).run();
    }
}
