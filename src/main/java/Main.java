import manager.GitLabManager;
import manager.PropertiesManager;
import manager.RetrofitManager;
import model.GitLabIssue;
import model.Milestone;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import printer.WorkbookPrinter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.GitLabService;

import java.io.*;
import java.util.List;
import java.util.Properties;

public class Main {

    private static GitLabManager manager;
    private static GitLabService service;
    private static WorkbookPrinter printer;
    private static Workbook workbook;

    public static void main(String... args) {
        loadProperties();
        workbook = new XSSFWorkbook();
        printMilestoneTabs();
        try {
            Thread.sleep(15000L);
            printer.writeWorkbook();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void printMilestoneTabs() {
        Properties properties = PropertiesManager.getInstance().getProperties();
        service = RetrofitManager.getInstance(properties).getRetrofit().create(GitLabService.class);

        manager = new GitLabManager(service, properties);
        printer = new WorkbookPrinter(workbook);

        manager.getAllMilestones(new Callback<List<Milestone>>() {
            @Override
            public void onResponse(Call<List<Milestone>> call, Response<List<Milestone>> response) {
                List<Milestone> milestones = response.body();
                printer.printMilestones(milestones);
                if(milestones != null) {
                    for (Milestone milestone : milestones) {
                        printer.printMilestoneHeader(milestone);
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        printIssuesInMilestone(milestone);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Milestone>> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    private static void printIssuesInMilestone(final Milestone milestone) {
        manager.getIssuesInMilestone(milestone, new Callback<List<GitLabIssue>>() {
            @Override
            public void onResponse(Call<List<GitLabIssue>> call, Response<List<GitLabIssue>> response) {
                printer.printIssues(response.body());
                printer.printSummary(milestone, response.body());
            }

            @Override
            public void onFailure(Call<List<GitLabIssue>> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    private static void loadProperties() {
        Properties properties = new Properties();
        try {
            InputStream input = new FileInputStream("src/main/resources/config.properties");
            properties.load(input);
            PropertiesManager.getInstance().setProperties(properties);
        } catch (FileNotFoundException e) {
            System.err.println("The file config.properties was not found! Please add this file to src/main/resources!");
        } catch (IOException e) {
            System.err.println("There was a problem loading the InputStream!");
            e.printStackTrace();
        }
    }
}
