package leow.project4task2;
/**
 * @author Enliang (Leo) Wu
 * id: enliangw
 */

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;

// main servlet
// GET /activity: for a random activity
// GET /activity/doit?id=activityId: for choosing to do an activity specified by activityId
// GET /activity/dislike?id=activityId: dislike an activity and get another random one
@WebServlet(name = "activityProvider", urlPatterns = {"/activity", "/activity/doit", "/activity/dislike"})
public class ActivityProvider extends HttpServlet {
    private InfoStore store;

    public void init() {
        store = new InfoStore();
    }

    // process all get requests
    // Each method in the first parameter of makeJSONResponse will return a json formatted String
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userName = request.getParameter("user");
        if (Objects.equals(request.getServletPath(), "/activity")) {
            makeJSONResponse(getRandomActivity(userName, request, false, null), response);
        } else if (Objects.equals(request.getServletPath(), "/activity/doit")) {
            // get activityId from request parameters
            String activityId = request.getParameter("id");
            makeJSONResponse(doActivity(userName, request, activityId), response);
        } else if (Objects.equals(request.getServletPath(), "/activity/dislike")) {
            // get activityId from request parameters
            String activityId = request.getParameter("id");
            makeJSONResponse(dislikeActivity(userName, request, activityId), response);
        } else {
            makeJSONResponse("{\"result\": false, \"message\": \"404 not found.\"}", response);
        }
    }

    // Get a random activity by calling boredapi
    private String getRandomActivity(String userName, HttpServletRequest request, boolean dislike, String dislikeActivityId) {
        // declare the final result string
        String result = null;
        // use JSONObject to build result
        JSONObject json = new JSONObject();

        String requestTarget = "/activity";
        if (dislike) requestTarget = "/activity/dislike";

        try {
            String apiUrl = "http://www.boredapi.com/api/activity/";
            // open the api url and get its streamed data
            InputStream stream = new URL(apiUrl).openStream();
            // another JSONObject to get json formatted data from 3rd party api
            JSONObject jsonObject;

            // reference: https://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java
            BufferedReader rd = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            String jsonText = sb.toString();
            jsonObject = new JSONObject(jsonText);

            // get all fields
            String activityName = jsonObject.getString("activity");
            String activityId = jsonObject.getString("key");
            String activityType = jsonObject.getString("type");
            int participants = jsonObject.getInt("participants");
            float price = jsonObject.getFloat("price");
            String link = jsonObject.getString("link");
            float accessibility = jsonObject.getFloat("accessibility");

            // save activity to database
            BoringActivity activity = new BoringActivity(activityId, activityName, activityType);
            store.recordActivity(activity);

            // log request to 3rd party
            ActivityLog r3pLog = new ActivityLog();
            r3pLog.setActionName("Request API");
            r3pLog.setUserName(userName);
            r3pLog.setActionTime(LocalDateTime.now());
            r3pLog.setActivityId(activityId);
            r3pLog.setApiRequestInfo("GET " + apiUrl);
            r3pLog.setApiReplyInfo("REPLY: activity \"" + activityName + "\" with type \"" + activityType +
                    "\" KEY(" + activityId + ") price: " + price + ", participants: " +
                    participants + ", accessibility: " + accessibility +
                    ", link: " + link);
            store.recordInfo(r3pLog);

            // put to result with my own format result=true
            json.put("name", activityName);
            json.put("id", activityId);
            json.put("type", activityType);
            json.put("result", true);
            json.put("message", "");

            result = json.toString();

            //log client request
            ActivityLog crLog = new ActivityLog();
            crLog.setActionName("Client Request");
            crLog.setUserName(userName);
            crLog.setActionTime(LocalDateTime.now());
            if (dislike)
                crLog.setActivityId(dislikeActivityId);
            else
                crLog.setActivityId(activityId);
            crLog.setClientInfo(request.getHeader("User-Agent"));
            crLog.setClientRequestInfo("GET " + requestTarget);
            crLog.setClientReplyInfo("REPLY: name: " + activityName + ", id: " + activityId + ", type: " +
                    activityType + ", result: true, message: \"\"");
            store.recordInfo(crLog);
        } catch (Exception e) {
            // while getting some error result=false
            json.put("result", false);
            json.put("message", e.getMessage());

            //log client request
            ActivityLog crLog = new ActivityLog();
            crLog.setActionName("Client Request");
            crLog.setUserName(userName);
            crLog.setActionTime(LocalDateTime.now());
            crLog.setClientInfo(request.getHeader("User-Agent"));
            crLog.setClientRequestInfo("GET " + requestTarget);
            crLog.setClientReplyInfo("REPLY: result: false, message: \"" + e.getMessage() + "\"");
            store.recordInfo(crLog);
        }

        return result;
    }

    // User choose to do an activity
    // activityId: activity ID string
    private String doActivity(String userName, HttpServletRequest request, String activityId) {
        String result;

        JSONObject json = new JSONObject();

        if (null == activityId) {
            // in case of not providing activityId set result=false and gives 'Activity not provided' message
            json.put("result", false);
            json.put("message", "Activity not provided");

            //log client request
            ActivityLog crLog = new ActivityLog();
            crLog.setActionName("Client Request");
            crLog.setUserName(userName);
            crLog.setActionTime(LocalDateTime.now());
            crLog.setClientInfo(request.getHeader("User-Agent"));
            crLog.setClientRequestInfo("GET /activity/doit");
            crLog.setClientReplyInfo("REPLY: result: false, message: \"Activity not provided\"");
            store.recordInfo(crLog);
        } else {
            json.put("id", activityId);
            json.put("result", true);
            json.put("message", "");

            //log client request
            ActivityLog crLog = new ActivityLog();
            crLog.setActionName("Client Request");
            crLog.setUserName(userName);
            crLog.setActionTime(LocalDateTime.now());
            crLog.setActivityId(activityId);
            crLog.setClientInfo(request.getHeader("User-Agent"));
            crLog.setClientRequestInfo("GET /activity/doit");
            crLog.setClientReplyInfo("REPLY: id: " + activityId + ", result: true, message: \"\"");
            store.recordInfo(crLog);
        }

        result = json.toString();

        return result;
    }

    // User dislike an activity
    // activityId: activity ID string
    private String dislikeActivity(String userName, HttpServletRequest request, String activityId) {
        String result;

        JSONObject json = new JSONObject();

        if (null == activityId) {
            // in case of not providing activityId set result=false and gives 'Activity not provided' message
            json.put("result", false);
            json.put("message", "Activity not provided");
            result = json.toString();

            //log client request
            ActivityLog crLog = new ActivityLog();
            crLog.setActionName("Client Request");
            crLog.setUserName(userName);
            crLog.setActionTime(LocalDateTime.now());
            crLog.setClientInfo(request.getHeader("User-Agent"));
            crLog.setClientRequestInfo("GET /activity/dislike");
            crLog.setClientReplyInfo("REPLY: result: false, message: \"Activity not provided\"");
            store.recordInfo(crLog);
        } else {
            // use getRandomActivity to retrieve a random activity
            result = getRandomActivity(userName, request, true, activityId);
        }

        return result;
    }

    // make JSON response to remote client
    // json: a JSON string
    // response: current servlet response object
    private void makeJSONResponse(String json, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        // set the content type to application/json
        response.setContentType("application/json");
        // set the char code to utf-8
        response.setCharacterEncoding("utf-8");
        // out put json string to response
        out.print(json);
        // end the output stream
        out.flush();
    }

    public void destroy() {
    }
}
