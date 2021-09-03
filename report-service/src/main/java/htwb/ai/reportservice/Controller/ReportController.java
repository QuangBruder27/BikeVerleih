package htwb.ai.reportservice.Controller;

import htwb.ai.reportservice.Entity.Report;
import htwb.ai.reportservice.Handler.IReportHandler;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    private IReportHandler handler;

    @Autowired
    public ReportController(IReportHandler iReportHandler){
        handler = iReportHandler;
    }

    /**
     * GET /report
     *
     * @return the collection of the report
     */
    @GetMapping()
    public List<Report> getAll(){
        return handler.findAll();
    }

    /**
     * GET /report/id
     * @param id the index of the lyric
     * @return ResponseEntity with the  specified report in body if successful
     * @throws IOException
     */
    @GetMapping(value="/{reportId}")
    public ResponseEntity<Report> getReportbyId(
            @PathVariable(value="reportId") String id) throws IOException {
        Report report = handler.findReportById(id.toString());
        System.out.println("ObjectID:"+report.getReportId());
        if (report != null) return  new ResponseEntity<Report>(report, HttpStatus.OK);
        return ResponseEntity.notFound().eTag("Report not found").build();
    }

    /**
     * DELETE /report/id
     * remove the specified report
     * @param id
     * @return ResponseEntity
     */
    @DeleteMapping(value="/{id}")
    public ResponseEntity deleteReport(@PathVariable("id") ObjectId id) {
        System.out.println("delete Report func");
        if (handler.deleteReport(id.toString())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * POST /report
     * Add the new report
     * @param report the new lyric in Payload
     * @return ResponseEntity with the location of new report in body if successful.
     */
    @PostMapping
    public ResponseEntity postReport(@RequestParam("bikeId") String bikeId,
                                     @RequestParam("customId") String customId,
                                     @RequestParam("note") String note,
                                     @Param("image") MultipartFile image) throws IOException {
        System.out.println("post mapping func");
        Report report = new Report();
        report.setBikeId(bikeId);
        report.setCustomId(customId);
        report.setNote(note);
        if (image != null && !image.isEmpty()) {
            System.out.println("image exists.");
            report.setImage(new Binary(BsonBinarySubType.BINARY, image.getBytes()));
        } else {
            System.out.println("image is empty");
        }

        Report newReport = handler.addReport(report);
        if (null != newReport) {
            System.out.println("success");
            return ResponseEntity.created(URI.create("/report/" + newReport.getReportId())).build();
        } else {
            System.out.println("add fails");
            return ResponseEntity.badRequest().body("Adding fails");
        }


    }



}