package sen.rpc.registry.handler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import rpc.protocol.RpcProtocol;
import rpc.utils.RpcUtils;
import sen.rpc.registry.RegistryServer;

@Slf4j
public class RpcServiceHandler extends ChannelInboundHandlerAdapter {

    private RpcProtocol rpcProtocol;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RpcProtocol) {
            rpcProtocol = (RpcProtocol) msg;
            //处理远程调用请求
            Object response = doRpc();
            System.out.println(String.format("收到<%s>的返回结果：%s", rpcProtocol.getServerName(), JSON.toJSONString(response)));
            System.out.println("将结果返回给Consumer\n");

            ctx.writeAndFlush(response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    private Object doRpc() {
        String serverName = rpcProtocol.getServerName();
        String serverHost = RegistryServer.getServerHost(serverName);
        int serverPort = RegistryServer.getServerPort(serverName);

        if (!serverHost.isEmpty()) {
            return communicateWithServer(serverHost, serverPort);
        } else {
            throw new RuntimeException("服务注册中心的回复：未找到提供方法的服务");
        }
    }

    private Object communicateWithServer(String host, int port) {
        return RpcUtils.rpcServerService(host, port, rpcProtocol);
    }

}