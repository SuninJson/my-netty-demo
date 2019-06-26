package sen.rpc.registry.bean;

import lombok.Data;

import java.util.List;

/**
 * @author Huang Sen
 * @date 2019/6/26
 * @description
 */
@Data
public class ServiceInfo {
    private String ip;
    private String name;
    private List<MethodInfo> methodInfos;

    public boolean isSameService(String clientIP, String serviceName) {
        return ip.equals(clientIP) && name.equals(serviceName);
    }
}
