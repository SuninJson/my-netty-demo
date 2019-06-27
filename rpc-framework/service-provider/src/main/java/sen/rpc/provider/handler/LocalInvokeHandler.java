package sen.rpc.provider.handler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import rpc.protocol.RpcProtocol;
import sen.rpc.provider.ProviderServer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
        if (msg instanceof RpcProtocol) {
            RpcProtocol protocol = (RpcProtocol) msg;

            System.out.println("开始调用本地方法：" + protocol.getMethodName());
            Object response = doInvoke(protocol);
            System.out.println("调用结果为："+ JSON.toJSONString(response));

            ctx.writeAndFlush(response);
        }
    }

    private Object doInvoke(RpcProtocol protocol) {
        try {
            Object serviceObj = ProviderServer.getServiceObj(protocol.getServiceName());
            Method method = serviceObj.getClass().getMethod(protocol.getMethodName(), protocol.getParamTypes());
            return method.invoke(serviceObj, protocol.getParamValue());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
