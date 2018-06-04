package service;

import model.GitLabIssue;
import model.Milestone;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface GitLabService {
    @GET("projects/{projectId}/milestones/{milestoneId}/issues?per_page=100")
    Call<List<GitLabIssue>> getIssuesInMilestone(@Path("projectId")Integer projectId,
                                                 @Path("milestoneId") Integer milestoneId);

    @GET("projects/{projectId}/milestones")
    Call<List<Milestone>> getAllMilestones(@Path("projectId")Integer projectId);
}
