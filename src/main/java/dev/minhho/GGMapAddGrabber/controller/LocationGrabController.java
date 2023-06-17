package dev.minhho.GGMapAddGrabber.controller;

import com.google.maps.errors.ApiException;
import dev.minhho.GGMapAddGrabber.model.LocationObj;
import dev.minhho.GGMapAddGrabber.model.QueryObj;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
public class LocationGrabController {
    @PostMapping("/location")
    public ResponseEntity<byte[]> processLocationForm(@RequestParam("apiKey") String apiKey,
                                      @RequestParam("city") String city,
                                      @RequestParam("searchQuery") String searchQuery) throws IOException, InterruptedException, ApiException {
        // Add your logic here to process the form data and perform the desired actions
        // You can access the API key, city, and search query values in the method body
        QueryObj queryObj = new QueryObj(apiKey, city, searchQuery);
        List<LocationObj> locations = queryObj.fetchLocations(queryObj);

        // Create the Excel workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Location Results");

        // Create the header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Name");
        ((Row) headerRow).createCell(1).setCellValue("Address");
        headerRow.createCell(2).setCellValue("Latitude");
        headerRow.createCell(3).setCellValue("Longitude");

        // Populate the data rows
        int rowNum = 1;
        for (LocationObj location : locations) {
            Row dataRow = sheet.createRow(rowNum++);
            dataRow.createCell(0).setCellValue(location.getName());
            dataRow.createCell(1).setCellValue(location.getAddress());
            dataRow.createCell(2).setCellValue(location.getLat());
            dataRow.createCell(3).setCellValue(location.getLng());
        }

        // Auto-size the columns for better visibility
        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }

        // Prepare the Excel workbook for download
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }

        // Set the appropriate headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "results.xlsx");

        return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
    }
}
