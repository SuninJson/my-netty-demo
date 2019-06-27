package sen.netty.chat.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import sen.netty.chat.protocol.IMMessage;

/**
 * @author Huang Sen
 * @date 2019/6/26
 * @description
 */
public class TerminalServerHandler extends SimpleChannelInboundHandler<IMMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IMMessage msg) throws Exception {

    }
}
