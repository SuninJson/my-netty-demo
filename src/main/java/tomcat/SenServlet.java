package tomcat;

/**
 * @author Huang Sen
 * @date 2019/6/20
 * @description
 */
public abstract class SenServlet {

    public void doService(SenRequest request, SenResponse response) throws Exception {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            doGet(request, response);
        } else {
            doPost(request, response);
        }
    }

    abstract void doGet(SenRequest request, SenResponse response);

    abstract void doPost(SenRequest request, SenResponse response);
}
