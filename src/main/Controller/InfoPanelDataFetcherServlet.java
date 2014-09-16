package Controller;

import Model.Constants;
import Model.Login;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by kartik.k on 9/12/2014.
 */
public class InfoPanelDataFetcherServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        ServletHelper.redirectIfLoginInvalid(request,response);
        String hostPort = (String) request.getSession().getAttribute("clickedInstanceHostPort") + ":";
        response.setContentType("text/html");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String fieldToBePlotted = request.getParameter("field");
            Long fromTimeStamp = Long.parseLong(request.getParameter("from"));
            Long toTimeStamp = Long.parseLong(request.getParameter("to"));

            Jedis jedis = new Jedis(Constants.INFO_STORE.getHost(), Constants.INFO_STORE.getPort());
            ArrayList<String> relevantInfoSnapshotSortedList = new ArrayList<String>(jedis.keys(hostPort + "*"));
            Collections.sort(relevantInfoSnapshotSortedList);
            Long[] fieldDataPoints = new Long[relevantInfoSnapshotSortedList.size()];
            int timeStampNo = 0;

            for (String relevantSnapshotKey : relevantInfoSnapshotSortedList) {
                Long timeStamp = Long.parseLong(relevantSnapshotKey.replaceAll(hostPort, ""));
                if(timeStamp<fromTimeStamp){
                    continue;
                }
                else if(timeStamp>toTimeStamp){
                    break;
                };
                if(fieldToBePlotted.equals("timeStamp")){
                    fieldDataPoints[timeStampNo] = timeStamp;
                }
                else if(fieldToBePlotted.equals("no_of_keys")){
                    String dbInfo = jedis.hget(relevantSnapshotKey,"db0");
                    String noOfKeysAsString = parseDbFieldOfInfo(dbInfo).get("keys");
                    fieldDataPoints[timeStampNo] = Long.parseLong(noOfKeysAsString);
                }
                else if(fieldToBePlotted.equals("no_of_expirable_keys")){
                    String dbInfo = jedis.hget(relevantSnapshotKey,"db0");
                    String noOfKeysAsString = parseDbFieldOfInfo(dbInfo).get("expires");
                    fieldDataPoints[timeStampNo] = Long.parseLong(noOfKeysAsString);
                }
                else {
                    Long fieldSingleDataPoint = Long.parseLong(jedis.hget(relevantSnapshotKey,fieldToBePlotted));
                    fieldDataPoints[timeStampNo] = fieldSingleDataPoint;
                }
                timeStampNo++;
            }
            String connectedClientInfoJson = new Gson().toJson(fieldDataPoints);
            out.write(connectedClientInfoJson);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        doPost(req, resp);
    }
    private Map<String,String> parseDbFieldOfInfo(String valueOfDbFiled){
        String[] valueAfterSplitting = valueOfDbFiled.split(",");
        Map<String,String> mapAtDbField = new HashMap<String, String>();
        try {
            for(String keyValPair:valueAfterSplitting){
                if(!keyValPair.contains("=")){
                    continue;
                }
                String subField = keyValPair.split("=")[0];
                String valueAtSubField = keyValPair.split("=")[1];
                mapAtDbField.put(subField,valueAtSubField);
            }
            return mapAtDbField;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


}
