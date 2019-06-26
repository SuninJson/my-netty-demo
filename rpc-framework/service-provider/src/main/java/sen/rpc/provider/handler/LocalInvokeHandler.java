package sen.rpc.provider.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Huang Sen
 * @date 2019/6/26
 * @description
 */
public class LocalInvokeHandler extends ChannelInboundHandlerAdapter {

    /**
     * 调用本地方法并将结果返回给调用端
     *
     * @param msg {@link rpc.protocol.RpcProtocol}
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //todo
        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
