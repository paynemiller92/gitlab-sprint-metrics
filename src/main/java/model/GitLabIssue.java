package model;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Represents one issue within GitLab.
 */

public class GitLabIssue {
    @SerializedName("id")
    private Integer uniqueId;

    @SerializedName("iid")
    private Integer projectScopeId;

    @SerializedName("project_id")
    private Integer projectId;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("state")
    private String state;

    @SerializedName("labels")
    private List<String> labels;

    @SerializedName("milestone")
    private Milestone milestone;

    @SerializedName("closed_at")
    private String closeDate;

    @SerializedName("web_url")
    private String url;

    /**
     * The unique id of the issue. This id is
     * unique across the entire GitLab instance.
     * @return the id.
     */

    public Integer getUniqueId() {
        return uniqueId;
    }

    /**
     * The id of the issue that is unique to the
     * project.
     * @return the id.
     */

    public Integer getProjectScopeId() {
        return projectScopeId;
    }

    /**
     * The unique id of the project, relative
     * to a GitLab instance.
     * @return the id associated with the project.
     */

    public Integer getProjectId() {
        return projectId;
    }

    /**
     * The title of the issue.
     * @return the issue's title.
     */

    public String getTitle() {
        return title;
    }

    /**
     * The description of the issue.
     * @return the issue's description.
     */

    public String getDescription() {
        return description;
    }

    /**
     * Returns the state of the issue. Either opened or closed.
     * @return the state.
     */

    public String getState() {
        return state;
    }

    /**
     * All of the labels applied to the issue.
     * @return the {@link List} of labels.
     */

    public List<String> getLabels() {
        return labels;
    }

    /**
     * The {@link Milestone} that the issue falls within.
     * @return the Milestone object.
     */

    public Milestone getMilestone() {
        return milestone;
    }

    /**
     * Retrieves the string timestamp of when
     * the issue was closed.
     * @return the string timestamp of the close date.
     */

    public String getCloseDateString() {
        return closeDate;
    }

    /**
     * Retrieves the URL for the issue. The issue's
     * detail page contains all the info related to the
     * issue; such as: Story Points, Labels, Milestones, etc.
     * @return the issue's URL.
     */

    public String getUrl() {
        return url;
    }

    public String getPlatform() {
        String platform = "Not Specified";
        for (String label: this.labels) {
            Boolean isAndroid = label.toLowerCase().trim().equals("Android".toLowerCase().trim());
            Boolean isiOS = label.toLowerCase().trim().equals("iOS".toLowerCase().trim());
            Boolean isUx = label.toLowerCase().trim().equals("UX".toLowerCase().trim());
            Boolean isCloud = label.toLowerCase().trim().contains("Cloud".toLowerCase().trim());
            Boolean isInfrastructure = label.toLowerCase().trim().equals("Infrastructure".toLowerCase().trim());
            Boolean isConsole = label.toLowerCase().trim().contains("Console".toLowerCase().trim());
            if (isAndroid || isiOS || isUx || isCloud || isInfrastructure || isConsole) {
                platform = label;
            }
        }
        return platform;
    }

    public String getPriority() {
        String priority = "Not Specified";
        for (String label: this.labels) {
            Boolean isLow = label.toLowerCase().trim().equals("Low".toLowerCase().trim());
            Boolean isMedium = label.toLowerCase().trim().equals("Medium".toLowerCase().trim());
            Boolean isHigh = label.toLowerCase().trim().equals("High".toLowerCase().trim());
            Boolean isCritical = label.toLowerCase().trim().equals("Critical".toLowerCase().trim());
            if (isLow || isMedium || isHigh || isCritical) {
                priority = label;
            }
        }
        return priority;
    }

    public String getType() {
        String issueType = "Not Specified";
        for (String label: this.labels) {
            Boolean isStory = label.toLowerCase().trim().equals("Story".toLowerCase().trim());
            Boolean isTask = label.toLowerCase().trim().contains("Task".toLowerCase().trim());
            Boolean isDefect = label.toLowerCase().trim().equals("Defect".toLowerCase().trim());
            if (isStory || isTask || isDefect) {
                issueType = label;
            }
        }
        return issueType;
    }

    /**
     *
     * @return
     */

    public Boolean isClosed() {
        if (state == null) {
            return false;
        }
        return this.state.toLowerCase().trim().equals("closed".toLowerCase().trim());
    }

    public Date getCloseDate() {
        if (this.closeDate == null || this.closeDate.isEmpty()) {
            return null;
        }
        return dateFromString(this.closeDate);
    }

    private Date dateFromString(String s) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
