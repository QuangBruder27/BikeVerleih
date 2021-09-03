package htwb.ai.reportservice;

import htwb.ai.reportservice.Entity.Report;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportRepository extends MongoRepository<Report, String> {
    Report findByReportId(String id);

}
