package tomcat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @author Huang Sen
 * @date 2019/6/20
 * @description
 */
public class SenRequest {
    private ChannelHandlerContext ctx;
    private HttpRequest request;

    public SenRequest(ChannelHandlerContext ctx, HttpRequest request) {
        this.ctx = ctx;
        this.request = request;
    }

    public String getUrl() {
        return request.uri();
    }

    public String getMethod() {
        return request.method().name();
    }
}
