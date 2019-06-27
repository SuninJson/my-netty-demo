package sen.rpc.provider.service;

import sen.rpc.provider.annotation.RpcService;
import sen.netty.provider.api.ICalculateService;

/**
 * @author Huang Sen
 * @date 2019/6/26
 * @description
 */
@RpcService
public class CalculateService implements ICalculateService {
    public double add(double num1, double num2) {
        return num1 + num2;
    }

    public double mul(double num1, double num2) {
        return num1 * num2;
    }
}
