package htwb.ai.reportservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.ai.reportservice.Entity.Report;
import htwb.ai.reportservice.Repo.ReportRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReportServiceApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ReportRepository reportRepository;

    @Test
    void postReportTest200() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId", "Tester");
        HttpEntity<String> entity = new HttpEntity<String>(asJsonString(initReport()), headers);
        ResponseEntity<Report> response = restTemplate.exchange("/report", HttpMethod.POST, entity, Report.class);
        System.out.println("Result: "+response);
        assertTrue(response.getStatusCode().value() == 200);
    }

    @Test
    void postReportTest400IdMismatch() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId", "Tester1");
        HttpEntity<String> entity = new HttpEntity<String>(asJsonString(initReport()), headers);
        ResponseEntity<String> response = restTemplate.exchange("/report", HttpMethod.POST, entity, String.class);
        System.out.println("Result: "+response);
        assertTrue(response.getBody().equals("Customer Id mismatch"));
    }

    @Test
    void postReportTest400WrongFormat() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("currentId", "Tester");
        Report report = initReport();
        report.setNote("");
        HttpEntity<String> entity = new HttpEntity<String>(asJsonString(report), headers);
        ResponseEntity<String> response = restTemplate.exchange("/report", HttpMethod.POST, entity, String.class);
        System.out.println("Result: "+response);
        assertTrue(response.getBody().equals("Wrong format"));
    }

    Report initReport(){
        Report report = new Report();
        report.setNote("Note for the unit test");
        report.setBikeId("BikeTest");
        report.setCustomerId("Tester");
        report.setSolved(false);
        return report;
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
