package demo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * The protocol to implement in this section is the TIME protocol.
 * It is different from the previous examples in that it sends a message, which contains a 32-bit integer, without receiving any requests and closes the connection once the message is sent.
 * In this example, you will learn how to construct and send a message, and to close the connection on completion.
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * Because we are going to ignore any received data but to send a message as soon as a connection is established, we cannot use the channelRead() method this time.
     * Instead, we should override the channelActive() method.
     *
     * the channelActive() method will be invoked when a connection is established and ready to generate traffic.
     */
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        //分配缓冲区
        final ByteBuf byteBuf = ctx.alloc().buffer(4);

        //2208988800：是1970-01-01 00:00.00距离1900-01-01  00:00.00的秒数
        /*
         * write 32-bit integer that represents the current time
         *
         * But wait, where's the flip?
         * Didn't we used to call java.nio.ByteBuffer.flip() before sending a message in NIO?
         * ByteBuf does not have such a method because it has two pointers;
         * one for read operations and the other for write operations.
         * The writer index increases when you write something to a ByteBuf while the reader index does not change.
         * The reader index and the writer index represents where the message starts and ends respectively.
         *
         * In contrast, NIO buffer does not provide a clean way to figure out where the message content starts and ends without calling the flip method.
         * You will be in trouble when you forget to flip the buffer because nothing or incorrect data will be sent.
         * Such an error does not happen in Netty because we have different pointer for different operation types.
         * You will find it makes your life much easier as you get used to it -- a life without flipping out!
         */
        byteBuf.writeInt((int) (System.currentTimeMillis() + 2208988800L));

        final ChannelFuture channelFuture = ctx.writeAndFlush(byteBuf);

        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                assert channelFuture == future;
                ctx.close();
            }
        });
    }

    /**
     * The exceptionCaught() event handler method is called with a Throwable when an exception was raised by Netty due to an I/O error or by a handler implementation due to the exception thrown while processing events.
     * In most cases, the caught exception should be logged and its associated channel should be closed here, although the implementation of this method can be different depending on what you want to do to deal with an exceptional situation.
     * For example, you might want to send a response message with an error code before closing the connection
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
