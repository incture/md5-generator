package oneapp.incture.workbox.demo.adapter.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@ComponentScan("oneapp.incture")
@RequestMapping(value = "/workbox/test")
public class Test {
	@GetMapping("/import")
	public void mapReapExcelDatatoDB(@RequestParam("file") MultipartFile reapExcelDataFile) throws IOException {

		List<Test> tempStudentList = new ArrayList<Test>();
		XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);

//		for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
//			Test tempStudent = new Test();
//
//			XSSFRow row = worksheet.getRow(i);
//			System.err.println(row.getCell(0).getStringCellValue());
//		}
		XSSFRow row = worksheet.getRow(0);
        System.out.print(row.getCell(0).getStringCellValue());
	}

}
