package Controller;

import Model.Instance;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kartik.k on 9/8/2014.
 */
public class ServletHelper {
    public static Instance getInstanceFromServletContext(ServletContext servletContext,String curInstanceHostPort) {

        Map<String,Instance> instanceMap = (HashMap<String, Instance>)servletContext.getAttribute("instanceMap");
        if(instanceMap==null){
            System.out.println("instance map not found");
        }
        return instanceMap.get(curInstanceHostPort);
    }
}
