package sen.rpc.provider.config;

import sen.rpc.provider.service.BaseService;

/**
 * @author Huang Sen
 * @date 2019/6/26
 * @description
 */
public class ProjectProperties {

    public static final String SERVICE_PACKAGE = BaseService.class.getPackage().getName();
    public static final int SERVER_PORT = 8090;

    public static final String SERVER_HOST = "127.0.0.1";
}