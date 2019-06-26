package rpc.protocol;

import lombok.Data;
import rpc.bean.ServerRegisterInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 服务注册协议
 *
 * @author Huang Sen
 * @date 2019/6/26
 * @description
 */
@Data
public class ServerRegisterProtocol implements Serializable {
    List<ServerRegisterInfo> serverRegisterInfos;
}
