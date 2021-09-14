package htwb.ai.reportservice.DAO;

import htwb.ai.reportservice.Entity.Report;
import htwb.ai.reportservice.Handler.IReportHandler;
import htwb.ai.reportservice.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReportDAO implements IReportHandler {

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    @Override
    public Report findReportById(String reportId){
        return reportRepository.findByReportId(reportId);
    }

    @Override
    public boolean isExisting(String reportId) {
        return findReportById(reportId) != null;
    }

    @Override
    public boolean deleteReport(String reportId) {
        System.out.println("ReportId= "+reportId);
        if (!this.isExisting(reportId)) {
            System.out.println(" report not found");
            return false;
        }
        reportRepository.delete(this.findReportById(reportId));
        return true;
    }

    @Override
    public boolean updateReport(Report report) {
        reportRepository.save(report);
        Report newReport = reportRepository.findByReportId(report.getReportId());
        return newReport.equals(report);
    }

    @Override
    public Report addReport(Report report) {
        System.out.println("Add report func");
        return reportRepository.save(report);
    }



}
