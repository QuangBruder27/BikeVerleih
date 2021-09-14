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
     * @param
     * @return ResponseEntity with the location of new report in body if successful.
     */
    @PostMapping
    public ResponseEntity postReport(@RequestHeader String Authorization,@RequestBody Report payloadReport) {
        System.out.println("Header: "+Authorization);

        System.out.println("post mapping func: "+payloadReport);
        if(!payloadReport.isAcceptable()){
            System.out.println("Not accepted.");
            return ResponseEntity.badRequest().body("Wrong format");
        }
        Report newReport = handler.addReport(payloadReport);
        if (null != newReport) {
            System.out.println("success");
            return ResponseEntity.ok(newReport);
        } else {
            System.out.println("add fails");
            return ResponseEntity.badRequest().body("Adding fails");
        }
    }

    @PutMapping(value="/{reportId}")
    public ResponseEntity uploadImageToReport(@RequestHeader String Authorization,
                                              @RequestParam("image") MultipartFile image,
                                              @PathVariable(value="reportId") String id) throws IOException {
        System.out.println("Header: "+Authorization);
        System.out.println("uploadImageToReport func");
        if (image == null || image.isEmpty()) return ResponseEntity.badRequest().body("Wrong format");
        if (handler.isExisting(id)) {
            Report newReport = handler.findReportById(id);
            newReport.setImage(new Binary(BsonBinarySubType.BINARY, image.getBytes()));
            boolean isSucced = handler.updateReport(newReport);
            if(isSucced){
                System.out.println("success");
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


    //
    /*
         if (image != null && !image.isEmpty()) {
            System.out.println("image exists.");
            report.setImage(new Binary(BsonBinarySubType.BINARY, image.getBytes()));
        } else {
            System.out.println("image is empty");
        }
     */



}