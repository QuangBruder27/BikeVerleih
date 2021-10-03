package htwb.ai.reportservice.Repo;

import htwb.ai.reportservice.Entity.Report;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportRepository extends MongoRepository<Report, String> {
    Report findByReportId(String id);

}
