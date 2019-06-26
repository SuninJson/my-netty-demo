package sen.rpc.registry.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import rpc.bean.ServerRegisterInfo;
import rpc.protocol.ServerRegisterProtocol;
import sen.rpc.registry.RegistryServer;
import util.NettyUtils;

import java.net.InetSocketAddress;

public class ServerRegisterHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ServerRegisterProtocol) {
            //注册服务
            doRegisterService(ctx, (ServerRegisterProtocol) msg);
        }
    }

    /**
     * 注册服务
     */
    private void doRegisterService(ChannelHandlerContext ctx, ServerRegisterProtocol msg) {
        InetSocketAddress netAddress = NettyUtils.getClientNetAddress(ctx);
        for (ServerRegisterInfo serverRegisterInfo : msg.getServerRegisterInfos()) {
            RegistryServer.addService(netAddress, serverRegisterInfo);
        }
        ctx.writeAndFlush("服务注册中心回复：Registration Complete");
        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}