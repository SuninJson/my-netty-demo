package rpc.protocol;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 服务注册协议
 *
 * @author Huang Sen
 * @date 2019/6/26
 * @description
 */
@Data
public class ServerRegisterProtocol implements Serializable {
    private String name;
    private String host;
    private int port;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServerRegisterProtocol that = (ServerRegisterProtocol) o;

        if (port != that.port) return false;
        if (!Objects.equals(name, that.name)) return false;
        return Objects.equals(host, that.host);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + port;
        return result;
    }
}
