package rpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * 远程调用协议
 *
 * @author Huang Sen
 * @date 2019/6/26
 * @description
 */
@Data
public class RpcProtocol implements Serializable {
    private String serverName;
    private String serviceName;
    private String methodName;
    private Class<?>[] paramTypes;
    private Object[] paramValue;
}
