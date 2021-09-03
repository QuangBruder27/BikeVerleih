package htwb.ai.reportservice.Handler;

import htwb.ai.reportservice.Entity.Report;
import org.bson.types.ObjectId;

import java.util.List;

public interface IReportHandler {
    /**
     * Find all objects of Lyric in DB
     * @return the list of Lyric
     */
    List<Report> findAll();
    Report findReportById(String id);
    boolean deleteReport(String id);
    boolean isExisting(String id);
    boolean updateReport(Report report);
    Report addReport(Report report);
}
