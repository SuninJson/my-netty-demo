package sen.netty.consumer.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import rpc.protocol.RpcProtocol;
import sen.netty.consumer.ConsumerServer;
import sen.netty.provider.config.ProviderApiProperties;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Huang Sen
 * @date 2019/6/27
 * @description
 */
public class RpcProxyHandler implements InvocationHandler {

    private Object response;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        RpcProtocol rpcProtocol = new RpcProtocol();
        rpcProtocol.setServerName(ProviderApiProperties.SERVER_NAME);
        rpcProtocol.setServiceName(this.getClass().getSimpleName());
        rpcProtocol.setMethodName(method.getName());
        rpcProtocol.setParamTypes(method.getParameterTypes());
        rpcProtocol.setParamValue(args);

        ConsumerServer.getInstance().doRpc(rpcProtocol);
        return response;
    }

    public RpcResultHandler getResultHandlerInstance() {
        return new RpcResultHandler();
    }

    private class RpcResultHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            response = msg;
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
        }
    }
}
