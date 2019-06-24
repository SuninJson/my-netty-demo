package tomcat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Huang Sen
 * @date 2019/6/20
 * @description
 */
public class Tomcat {

    private int port = 8080;
    private ServerSocket serverSocket;
    public static final Map<String, SenServlet> servletMapping = new HashMap<String, SenServlet>();


    /*
      加载配置文件，初始化servletMapping
     */
    static {
        try {
            //读取配置文件信息
            Properties web = new Properties();
            String projectPath = Tomcat.class.getResource("/").getPath();
            web.load(new FileInputStream(projectPath + "web.properties"));

            //通过配置文件实例化Controller
            for (Map.Entry<Object, Object> entry : web.entrySet()) {
                String key = entry.getKey().toString();
                if (key.endsWith(".url")) {
                    String url = entry.getValue().toString();

                    String servletName = key.replaceAll("\\.url$", "");
                    String className = web.getProperty(servletName + ".className");

                    servletMapping.put(url, (SenServlet) Class.forName(className).newInstance());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        //使用Netty与Web端保持通信
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    //主线程处理通道
                    .channel(NioServerSocketChannel.class)
                    //子线程处理者
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel clientSocketChannel) {
                            //无锁化串行编程
                            clientSocketChannel.pipeline().addLast(new HttpResponseEncoder());
                            clientSocketChannel.pipeline().addLast(new HttpRequestDecoder());
                            clientSocketChannel.pipeline().addLast(new SenTomcatHandler());
                        }
                    })
                    //设置主线程最大线程数量
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //设置子线程保持长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            //启动服务器
            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("Tomcat 已启动，监听的端口为：" + port);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new Tomcat().start();
    }
}
