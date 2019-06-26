package sen.rpc.registry.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import rpc.protocol.RpcProtocol;

public class RpcServiceHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RpcProtocol) {
            //处理远程调用请求
            doRpc(ctx, (RpcProtocol) msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    private void doRpc(ChannelHandlerContext ctx, RpcProtocol msg) {
        System.out.println("对进行远程调用");
        ctx.fireChannelRead(msg);
    }
}