package util;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.net.InetSocketAddress;

/**
 * @author Huang Sen
 * @date 2019/6/26
 * @description
 */
public class NettyUtils {

    /**
     * 获取请求端IP地址
     */
    public static InetSocketAddress getClientNetAddress(ChannelHandlerContext ctx) {
        return (InetSocketAddress) ctx.channel().remoteAddress();
    }

    public static ChannelPipeline initPipelineForRpc(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 8, 0, 8))
                .addLast(new LengthFieldPrepender(8))
                .addLast("encoder", new ObjectEncoder())
                .addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
        return pipeline;
    }
}
