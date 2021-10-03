package htwb.ai.reportservice.Controller;

import htwb.ai.reportservice.Entity.Report;
import htwb.ai.reportservice.Repo.ReportRepository;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    static String baseUrl = "http://localhost";

    @Autowired
    private ReportRepository reportRepository;

    // Request 1
    @PostMapping
    public ResponseEntity postReport(@RequestHeader String currentId,
                                     @RequestBody Report payloadReport) {
        if(!currentId.equals(payloadReport.getCustomerId()))
            return ResponseEntity.badRequest().body("Customer Id mismatch");

        System.out.println("post mapping func: "+payloadReport);
        if(!payloadReport.isAcceptable()){
            return ResponseEntity.badRequest().body("Wrong format");
        }
        Report newReport = reportRepository.save(payloadReport);
        if (null != newReport) {
            System.out.println("success");
            addPoints(5,newReport);
            return ResponseEntity.ok(newReport);
        } else {
            System.out.println("add fails");
            return ResponseEntity.badRequest().body("Adding fails");
        }
    }


    // Request 2
    @PutMapping(value="/{reportId}")
    public ResponseEntity uploadImageToReport(@RequestHeader String Authorization,
                                              @RequestParam("image") MultipartFile image,
                                              @PathVariable(value="reportId") String id) throws IOException {
        System.out.println("Header: "+Authorization);
        System.out.println("uploadImageToReport func");
        if (image == null || image.isEmpty()) return ResponseEntity.badRequest().body("Wrong format");
        if (reportRepository.existsById(id)) {
            Report newReport = reportRepository.findByReportId(id);
            newReport.setImage(new Binary(BsonBinarySubType.BINARY, image.getBytes()));
            Report lastReport = reportRepository.save(newReport);
            if(null != lastReport){
                System.out.println("success");
                addPoints(5,newReport);
                return ResponseEntity.created(URI.create("/report/" + newReport.getReportId())).build();
            } else {
                System.out.println("upload Image fails");
                return ResponseEntity.badRequest().body("upload fails");
            }

        } else {
            System.out.println("Not found");
            return ResponseEntity.notFound().build();
        }
    }


    public void addPoints(Integer point, Report report){
        if (report.getCustomerId().equals("Tester")) return;
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrl+ ":8200/bonus/add/"+report.getCustomerId()+"/"+point;
        restTemplate.exchange(url, HttpMethod.PUT, null, String.class);
    }



    /**
     * GET /report/id
     * @param id the index of the report
     * @return ResponseEntity with the  specified report in body if successful
     * @throws IOException
     */
    /*
    @GetMapping(value="/{reportId}")
    public ResponseEntity<Report> getReportbyId(
            @PathVariable(value="reportId") String id) throws IOException {
        Report report = handler.findReportById(id.toString());
        System.out.println("ObjectID:"+report.getReportId());
        if (report != null) return  new ResponseEntity<Report>(report, HttpStatus.OK);
        return ResponseEntity.notFound().eTag("Report not found").build();
    }

     */




}