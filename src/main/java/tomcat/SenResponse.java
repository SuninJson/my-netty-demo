package tomcat;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.io.UnsupportedEncodingException;

/**
 * @author Huang Sen
 * @date 2019/6/20
 * @description
 */
public class SenResponse {
    private ChannelHandlerContext ctx;
    private HttpRequest request;

    public SenResponse(ChannelHandlerContext ctx, HttpRequest request) {
        this.ctx = ctx;
        this.request = request;
    }

    public void write(String msg) {
        try {
            if (msg == null || msg.length() == 0) {
                return;
            }
            // 设置 http协议及请求头信息
            FullHttpResponse response = new DefaultFullHttpResponse(
                    // 设置http版本为1.1
                    HttpVersion.HTTP_1_1,
                    // 设置响应状态码
                    HttpResponseStatus.OK,
                    // 将输出值写出 编码为UTF-8
                    Unpooled.wrappedBuffer(msg.getBytes("UTF-8")));

            response.headers().set("Content-Type", "text/html;");
            ctx.write(response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            ctx.flush();
            ctx.close();
        }

    }
}
