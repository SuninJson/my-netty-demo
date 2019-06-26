package rpc.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Huang Sen
 * @date 2019/6/26
 * @description
 */
@Data
public class ServerRegisterInfo implements Serializable {
    private String serverName;
}
