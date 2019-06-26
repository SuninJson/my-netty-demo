package sen.rpc.registry.bean;

import lombok.Data;

@Data
public class MethodInfo {
    private String methodName;
    private String[] paramTypes;
}