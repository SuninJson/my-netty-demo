package tomcat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @author Huang Sen
 * @date 2019/6/20
 * @description
 */
public class SenTomcatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) msg;
            SenRequest request = new SenRequest(ctx, httpRequest);
            SenResponse response = new SenResponse(ctx, httpRequest);

            String url = request.getUrl();

            if (Tomcat.servletMapping.containsKey(url)) {
                SenServlet servlet = Tomcat.servletMapping.get(url);
                servlet.doService(request, response);
            } else {
                throw new RuntimeException("404");
            }
        }
    }
}
