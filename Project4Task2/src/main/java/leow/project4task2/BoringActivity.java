package leow.project4task2;
/**
 * @author Enliang (Leo) Wu
 * id: enliangw
 */
// POJO class of activity
public class BoringActivity {
    private String activityName;
    private String activityType;
    private String activityId;

    public String getActivityName() {
        return activityName;
    }
    public void setActivityName(String value) {
        activityName = value;
    }

    public String getActivityType() {
        return activityType;
    }
    public void setActivityType(String value) {
        activityType = value;
    }

    public String getActivityId() {
        return activityId;
    }
    public void setActivityId(String value) {
        activityId = value;
    }

    public BoringActivity() {

    }

    public BoringActivity(String _activityId, String _activityName, String _activityType) {
        activityId = _activityId;
        activityName = _activityName;
        activityType = _activityType;
    }
}
