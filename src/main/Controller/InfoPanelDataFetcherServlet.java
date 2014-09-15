package Controller;

import Model.Constants;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

            Jedis jedis = new Jedis(Constants.INFO_STORE.getHost(), Constants.INFO_STORE.getPort());
            ArrayList<String> relevantInfoSnapshotSortedList = new ArrayList<String>(jedis.keys(hostPort + "*"));
            Collections.sort(relevantInfoSnapshotSortedList);
            Long[][] connectedClientInfo = new Long[5][relevantInfoSnapshotSortedList.size()];
            int timeStampNo = 0;

            for (String relevantSnapshotKey : relevantInfoSnapshotSortedList) {
                Map<String, String> infoSnapshot = jedis.hgetAll(relevantSnapshotKey);
                Long timeStamp = Long.parseLong(relevantSnapshotKey.replaceAll(hostPort, ""));
                connectedClientInfo[0][timeStampNo] = timeStamp;
                int seriesToBeSentCounter = 1;
                Long connectedClients = Long.parseLong(infoSnapshot.get("connected_clients"));
                connectedClientInfo[1][timeStampNo] = connectedClients;
                Long memoryUsed = Long.parseLong(infoSnapshot.get("used_memory"));
                connectedClientInfo[2][timeStampNo] = memoryUsed;
                String db0Keys = infoSnapshot.get("db0");
                Map<String,Long> db0Map = parseDbFieldOfInfo(db0Keys);
                connectedClientInfo[3][timeStampNo] = db0Map.get("keys");
                connectedClientInfo[4][timeStampNo] = db0Map.get("expires");
                timeStampNo++;
            }
            String connectedClientInfoJson = new Gson().toJson(connectedClientInfo);
            out.write(connectedClientInfoJson);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        doPost(req, resp);
    }
    private Map<String,Long> parseDbFieldOfInfo(String valueOfDbFiled){
        String[] valueAfterSplitting = valueOfDbFiled.split(",");
        Map<String,Long> mapAtDbField = new HashMap<String, Long>();
        try {
            for(String keyValPair:valueAfterSplitting){
                if(!keyValPair.contains("=")){
                    continue;
                }
                String subField = keyValPair.split("=")[0];
                String valueAtSubField = keyValPair.split("=")[1];
                Long valueOfTypeLong = Long.parseLong(valueAtSubField);
                mapAtDbField.put(subField,valueOfTypeLong);
            }
            return mapAtDbField;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


}
