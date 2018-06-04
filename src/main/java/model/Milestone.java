package model;

/*
"milestone": {
      "id": 6,
      "iid": 2,
      "project_id": 1,
      "title": "Sprint 13",
      "description": "",
      "state": "active",
      "created_at": "2018-05-25T13:28:38.494Z",
      "updated_at": "2018-05-25T13:28:38.494Z",
      "due_date": "2018-06-06",
      "start_date": "2018-05-24"
    }
 */

import com.google.gson.annotations.SerializedName;

/**
 * Represents one Milestone (commonly kown as a Sprint in Scrum)
 * within GitLab.
 */

public class Milestone {
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

    @SerializedName("created_at")
    private String creationTimestamp;

    @SerializedName("due_date")
    private String sprintEnd;

    @SerializedName("start_date")
    private String sprintStart;

    public Integer getUniqueId() {
        return uniqueId;
    }

    public Integer getProjectScopeId() {
        return projectScopeId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getState() {
        return state;
    }

    public String getCreationTimestamp() {
        return creationTimestamp;
    }

    public String getSprintEnd() {
        return sprintEnd;
    }

    public String getSprintStart() {
        return sprintStart;
    }
}
