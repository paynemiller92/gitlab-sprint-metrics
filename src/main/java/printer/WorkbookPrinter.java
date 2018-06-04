package printer;

import model.GitLabIssue;
import model.Milestone;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorkbookPrinter {

    private final Integer TITLE_CELL_POSITION = 0;
    private final Integer POINT_CELL_POSITION = 1;
    private final Integer PLATFORM_CELL_POSITION = 2;
    private final Integer PRIORITY_CELL_POSITION = 3;
    private final Integer ISSUE_TYPE_CELL_POSITION = 4;
    private final Integer URL_CELL_POSITION = 5;

    private Workbook workbook;
    private Map<String, Double> pointsByPlatformMap;
    private Map<String, Double> pointsByPriorityMap;
    private Map<String, Double> pointsByIssueTypeMap;
    private Double totalCompletedPoints;
    private Double totalCommittedPoints;

    public WorkbookPrinter(Workbook workbook) {
        this.workbook = workbook;
        this.pointsByPlatformMap = new HashMap<String, Double>();
        this.pointsByPriorityMap = new HashMap<String, Double>();
        this.pointsByIssueTypeMap = new HashMap<String, Double>();
        this.totalCommittedPoints = 0.0;
        this.totalCompletedPoints = 0.0;
    }

    public void printMilestones(List<Milestone> milestones) {
        for (Milestone milestone : milestones) {
            workbook.createSheet(milestone.getTitle());
        }
    }

    public void printMilestoneHeader(Milestone milestone) {
        Sheet sheet = workbook.getSheet(milestone.getTitle());
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(TITLE_CELL_POSITION).setCellValue("Issue Title");
        headerRow.createCell(POINT_CELL_POSITION).setCellValue("Story Points");
        headerRow.createCell(PLATFORM_CELL_POSITION).setCellValue("Platform");
        headerRow.createCell(PRIORITY_CELL_POSITION).setCellValue("Priority");
        headerRow.createCell(ISSUE_TYPE_CELL_POSITION).setCellValue("Issue Type");
        headerRow.createCell(URL_CELL_POSITION).setCellValue("Issue URL");
    }

    public void printIssues(List<GitLabIssue> issues) {
        this.pointsByPlatformMap = new HashMap<String, Double>();
        this.pointsByPriorityMap = new HashMap<String, Double>();
        this.pointsByIssueTypeMap = new HashMap<String, Double>();
        this.totalCommittedPoints = 0.0;
        this.totalCompletedPoints = 0.0;
        Integer rowIndex = 1;
        for (GitLabIssue issue : issues) {
            Sheet sheet = workbook.getSheet(issue.getMilestone().getTitle());
            Row row = sheet.createRow(rowIndex);
            printIssueInRow(issue, row);
            rowIndex++;
        }
    }

    private void printIssueInRow(GitLabIssue issue, Row row) {
        printTitleCellForIssue(issue, row);
        printStoryPointCellForIssue(issue, row);
        printPlatformCellForIssue(issue, row);
        printPriorityCellForIssue(issue, row);
        printIssueTypeCellForIssue(issue, row);
        printUrlCell(issue, row);
    }

    private void printTitleCellForIssue(GitLabIssue issue, Row row) {
        Cell titleCell = row.createCell(TITLE_CELL_POSITION);
        titleCell.setCellValue(issue.getTitle());
    }

    private void printStoryPointCellForIssue(GitLabIssue issue, Row row) {
        Cell pointCell = row.createCell(POINT_CELL_POSITION);
        if (issue.getDescription() != null) {
            pointCell.setCellValue(extractPointValue(issue));
        } else {
            pointCell.setCellValue(0.0);
        }
    }

    private void printPlatformCellForIssue(GitLabIssue issue, Row row) {
        Cell platformCell = row.createCell(PLATFORM_CELL_POSITION);
        platformCell.setCellValue(issue.getPlatform());

        Double platformPoints = pointsByPlatformMap.get(issue.getPlatform());
        Double points;
        if (issue.getDescription() != null) {
            points = extractPointValue(issue);
        } else {
            points = 0.0;
        }
        if (platformPoints != null) {
            platformPoints += points;
            pointsByPlatformMap.put(issue.getPlatform(), platformPoints);
        } else {
            pointsByPlatformMap.put(issue.getPlatform(), points);
        }
    }

    private void printPriorityCellForIssue(GitLabIssue issue, Row row) {
        String priority = issue.getPriority();
        Cell priorityCell = row.createCell(PRIORITY_CELL_POSITION);
        priorityCell.setCellValue(priority);

        Double priorityPoints = pointsByPriorityMap.get(priority);
        Double points;
        if (issue.getDescription() != null) {
            points = extractPointValue(issue);
        } else {
            points = 0.0;
        }
        if(priorityPoints != null) {
            priorityPoints += points;
            pointsByPriorityMap.put(priority, priorityPoints);
        } else {
            pointsByPriorityMap.put(priority, points);
        }
    }

    private void printIssueTypeCellForIssue(GitLabIssue issue, Row row) {
        String issueType = issue.getType();
        Cell issueCell = row.createCell(ISSUE_TYPE_CELL_POSITION);
        issueCell.setCellValue(issueType);

        Double issueTypePoints = pointsByIssueTypeMap.get(issueType);
        Double points;
        if (issue.getDescription() != null) {
            points = extractPointValue(issue);
        } else {
            points = 0.0;
        }
        if (issueTypePoints != null) {
            issueTypePoints += points;
            pointsByIssueTypeMap.put(issueType, issueTypePoints);
        } else {
            pointsByIssueTypeMap.put(issueType, points);
        }
    }

    private void printUrlCell(GitLabIssue issue, Row row) {
        CreationHelper helper = workbook.getCreationHelper();
        CellStyle hlinkstyle = workbook.createCellStyle();
        Font hyperlinkFont = workbook.createFont();
        hyperlinkFont.setUnderline(XSSFFont.U_SINGLE);
        hyperlinkFont.setColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
        hlinkstyle.setFont(hyperlinkFont);
        XSSFHyperlink link = (XSSFHyperlink)helper.createHyperlink(HyperlinkType.URL);
        link.setAddress(issue.getUrl());
        Cell urlCell = row.createCell(URL_CELL_POSITION);
        urlCell.setCellValue(issue.getUrl());
        urlCell.setCellStyle(hlinkstyle);
        urlCell.setHyperlink(link);
    }

    public void writeWorkbook() {
        File file = new File("target/report.xlsx");
        try {
            OutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            outputStream.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Double extractPointValue(GitLabIssue issue) {
        Double points = 0.0;
        Pattern pattern = Pattern.compile("^\\d*\\.?\\d* points$", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(issue.getDescription());
        if (matcher.find()) {
            try {
                points = Double.valueOf(matcher.group(0).replace("points", "").trim());
            } catch (NumberFormatException e) {
                points = 0.0;
            }
        }
        return points;
    }

    public void printSummary(Milestone milestone, List<GitLabIssue> issues) {
        Sheet sheet = workbook.getSheet(milestone.getTitle());
        Integer rowIndex = issues.size() + 2;
        printPlatformStatistics(sheet, rowIndex);
        rowIndex += pointsByPlatformMap.size() + 2;
        printPriorityStatistics(sheet, rowIndex);
        rowIndex += pointsByPriorityMap.size() + 2;
        printIssueStatistics(sheet, rowIndex);
        rowIndex += 10;
        printPlatformChart(sheet, rowIndex);
        rowIndex += 30;
        printIssueTypeChart(sheet, rowIndex);
        rowIndex += 30;
        printPriorityChart(sheet, rowIndex);

    }

    private void printCompletedPoints(Sheet sheet, Integer rowIndex) {
        Row headerRow = sheet.createRow(rowIndex);
        headerRow.createCell(0).setCellValue("Completed Points");
        headerRow.createCell(1).setCellValue("Target Points");
        Row valueRow = sheet.createRow(rowIndex);
        valueRow.createCell(0).setCellValue(totalCompletedPoints);
        valueRow.createCell(1).setCellValue(totalCommittedPoints);
    }

    private void printPlatformStatistics(Sheet sheet, Integer rowIndex) {
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue("Platform");
        row.createCell(1).setCellValue("Total Completed Points");
        rowIndex++;
        printRowsFromMap(sheet, pointsByPlatformMap, rowIndex);
        rowIndex++;
    }

    private void printPriorityStatistics(Sheet sheet, Integer rowIndex) {
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue("Priority");
        row.createCell(1).setCellValue("Total Completed Points");
        rowIndex++;
        printRowsFromMap(sheet, pointsByPriorityMap, rowIndex);
        rowIndex++;
    }

    private void printRowsFromMap(Sheet sheet, Map<String, Double> map, Integer rowIndex) {
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            Row platformRow = sheet.createRow(rowIndex);
            platformRow.createCell(0).setCellValue(entry.getKey());
            platformRow.createCell(1).setCellValue(entry.getValue());
            rowIndex++;
        }
    }

    private void printIssueStatistics(Sheet sheet, Integer rowIndex) {
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue("Issue Type");
        row.createCell(1).setCellValue("Total Completed Points");
        rowIndex++;
        printRowsFromMap(sheet, pointsByIssueTypeMap, rowIndex);
        rowIndex++;
    }

    private void printPlatformChart(Sheet sheet, Integer rowIndex) {
        JFreeChart chart = ChartPrinter.createBarChart(pointsByPlatformMap, "Points by Platform", "Platform");
        writeChart(sheet, chart, rowIndex, 2);
    }

    private void printIssueTypeChart(Sheet sheet, Integer rowIndex) {
        JFreeChart chart = ChartPrinter.createPieChart(pointsByIssueTypeMap, "Points by Type");
        writeChart(sheet, chart, rowIndex, 2);
    }

    private void printPriorityChart(Sheet sheet, Integer rowIndex) {
        JFreeChart chart = ChartPrinter.createPieChart(pointsByPriorityMap, "Points by Priority");
        writeChart(sheet, chart, rowIndex, 2);
    }

    private void writeChart(Sheet sheet, JFreeChart chart, Integer row, Integer column) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ChartUtils.writeChartAsJPEG(outputStream, chart, 640, 480);
            int chartId = workbook.addPicture(outputStream.toByteArray(), Workbook.PICTURE_TYPE_PNG);
            outputStream.close();
            Drawing drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = new XSSFClientAnchor();
            anchor.setCol1(column);
            anchor.setRow1(row);
            Picture picture = drawing.createPicture(anchor, chartId);
            picture.resize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
