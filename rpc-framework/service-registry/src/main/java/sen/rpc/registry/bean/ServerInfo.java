package sen.rpc.registry.bean;

import lombok.Data;

import java.util.Objects;

/**
 * @author Huang Sen
 * @date 2019/6/26
 * @description
 */
@Data
public class ServerInfo {
    private String name;
    private String ip;
    private int port;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerInfo that = (ServerInfo) o;
        return port == that.port &&
                Objects.equals(name, that.name) &&
                Objects.equals(ip, that.ip);
    }

}
