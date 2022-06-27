package leow.project4task2;

/**
 * @author Enliang (Leo) Wu
 * id: enliangw
 */

import java.time.LocalDateTime;

// POJO class of operation log
public class ActivityLog {
    private String actionName;
    private String userName;
    private String clientInfo;
    private String clientRequestInfo;
    private String apiRequestInfo;
    private String apiReplyInfo;
    private String clientReplyInfo;
    private LocalDateTime actionTime;
    private String activityId;

    public String getActionName() {
        return this.actionName;
    }
    public void setActionName(String value) {
        this.actionName = value;
    }

    public String getUserName() { return this.userName; }
    public void setUserName(String value) { this.userName = value; }

    public String getClientInfo() {
        return this.clientInfo;
    }
    public void setClientInfo(String value) {
        this.clientInfo = value;
    }

    public String getClientRequestInfo() {
        return this.clientRequestInfo;
    }
    public void setClientRequestInfo(String value) {
        this.clientRequestInfo = value;
    }

    public String getApiRequestInfo() {
        return this.apiRequestInfo;
    }
    public void setApiRequestInfo(String value) {
        this.apiRequestInfo = value;
    }

    public String getApiReplyInfo() {
        return this.apiReplyInfo;
    }
    public void setApiReplyInfo(String value) {
        this.apiReplyInfo = value;
    }

    public String getClientReplyInfo() {
        return this.clientReplyInfo;
    }
    public void setClientReplyInfo(String value) {
        this.clientReplyInfo = value;
    }

    public LocalDateTime getActionTime() {
        return this.actionTime;
    }
    public void setActionTime(LocalDateTime value) {
        this.actionTime = value;
    }

    public String getActivityId() {
        return this.activityId;
    }
    public void setActivityId(String value) {
        this.activityId = value;
    }
}
