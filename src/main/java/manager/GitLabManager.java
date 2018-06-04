package manager;

import model.GitLabIssue;
import model.Milestone;
import retrofit2.Callback;
import service.GitLabService;

import java.util.List;
import java.util.Properties;

public class GitLabManager {
    private GitLabService service;
    private Properties properties;

    public GitLabManager(GitLabService service, Properties properties) {
        this.service = service;
        this.properties = properties;
    }

    public void getIssuesInMilestone(Milestone milestone, Callback<List<GitLabIssue>> callback) {
        this.service.getIssuesInMilestone(milestone.getProjectId(), milestone.getUniqueId())
                .enqueue(callback);
    }

    public void getAllMilestones(Callback<List<Milestone>> callback) {
        this.service.getAllMilestones(Integer.valueOf(properties.getProperty("projectId")))
                .enqueue(callback);
    }
}
