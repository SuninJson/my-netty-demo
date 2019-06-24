package tomcat;

/**
 * @author Huang Sen
 * @date 2019/6/20
 * @description
 */
public class HelloController extends SenServlet{

    void doGet(SenRequest request, SenResponse response) {
        this.doPost(request,response);
    }

    void doPost(SenRequest request, SenResponse response) {
        response.write("Hello,I'm HelloController");
    }
}
