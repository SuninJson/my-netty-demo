package sen.rpc.registry.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import rpc.protocol.ServerRegisterProtocol;
import sen.rpc.registry.RegistryServer;

public class ServerRegisterHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ServerRegisterProtocol) {
            //注册服务
            doRegisterService(ctx, (ServerRegisterProtocol) msg);
        }
        ctx.fireChannelRead(msg);
    }

    /**
     * 注册服务
     */
    private void doRegisterService(ChannelHandlerContext ctx, ServerRegisterProtocol msg) {
        RegistryServer.addService(msg);
        ctx.writeAndFlush("服务注册中心回复：Registration Complete");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}