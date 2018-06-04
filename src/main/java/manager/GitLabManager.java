package manager;

import model.GitLabIssue;
import model.Milestone;
import retrofit2.Callback;
import service.GitLabService;

import java.util.List;
import java.util.Properties;

/**
 * The manager class that handles all GitLab operations. Such
 * operations include getting project milestones as well as
 * retrieving all issues within a milestone.
 */

public class GitLabManager {
    private GitLabService service;
    private Properties properties;

    /**
     * Instantiates a new GitLabManager object.
     * @param service The GitLab Service object. Created via Retrofit.
     * @param properties The Properties file object from where the
     *                   projectId is derived from.
     */

    public GitLabManager(GitLabService service, Properties properties) {
        this.service = service;
        this.properties = properties;
    }

    /**
     * Retrieves all the issues within a given {@link Milestone}.
     * @param milestone The milestone (also known as a Sprint).
     * @param callback The action to be performed upon receipt of a netowrk
     *                 response.
     */

    public void getIssuesInMilestone(Milestone milestone, Callback<List<GitLabIssue>> callback) {
        this.service.getIssuesInMilestone(milestone.getProjectId(), milestone.getUniqueId())
                .enqueue(callback);
    }

    /**
     *
     * @param callback The action to be performed upon receipt of a netowrk
     *                 response.
     */

    public void getAllMilestones(Callback<List<Milestone>> callback) {
        this.service.getAllMilestones(Integer.valueOf(properties.getProperty("projectId")))
                .enqueue(callback);
    }
}
