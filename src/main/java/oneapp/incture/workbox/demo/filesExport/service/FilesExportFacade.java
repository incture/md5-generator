package oneapp.incture.workbox.demo.filesExport.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessConfigDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessConfigDto;
import oneapp.incture.workbox.demo.adapter_base.dto.WorkBoxDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.inbox.dto.AdvanceFilterDetailDto;
import oneapp.incture.workbox.demo.inbox.dto.InboxFilterDto;
import oneapp.incture.workbox.demo.inbox.dto.WorkboxRequestFilterDto;
import oneapp.incture.workbox.demo.inbox.dto.WorkboxResponseDto;
import oneapp.incture.workbox.demo.inbox.sevices.WorkboxFacade;

@Service("FilesExportFacade")
public class FilesExportFacade implements FilesExportFacadeLocal {

	@Autowired
	WorkboxFacade workbox;

	@Autowired
	ProcessConfigDao processConfigDao;

	@Autowired
	CustomAttributeDao attrDao;

	@Override
	public String exportInboxAsFile(InboxFilterDto filterPayload,Token token) {
		XSSFWorkbook workBook = new XSSFWorkbook();
		XSSFSheet sheet = workBook.createSheet();
		String encodedString = "";

		try {
			if (sheet.getLastRowNum() == 0) {
				XSSFRow headerRow = sheet.createRow(0);
				XSSFCell cell = headerRow.createCell(0);
				cell.setCellValue("REQUEST ID");
				cell = headerRow.createCell(1);
				cell.setCellValue("PROCESS NAME");
				cell = headerRow.createCell(2);
				cell.setCellValue("CREATED BY");
				cell = headerRow.createCell(3);
				cell.setCellValue("DESCRIPTION");
				cell = headerRow.createCell(4);
				cell.setCellValue("STATUS");

			}
			int r = sheet.getLastRowNum() + 1;
			WorkboxResponseDto response = workbox.getWorkboxFilterDataNew(filterPayload, false,token);
			if (!ServicesUtil.isEmpty(response.getWorkBoxDtos())) {
				for (WorkBoxDto tmDto : response.getWorkBoxDtos()) {
					XSSFRow row = sheet.createRow(r++);

					XSSFCell cell = row.createCell(0);
					cell.setCellValue(tmDto.getRequestId());
					cell = row.createCell(1);
					cell.setCellValue(tmDto.getProcessDisplayName());
					cell = row.createCell(2);
					cell.setCellValue(tmDto.getStartedBy());
					cell = row.createCell(3);
					cell.setCellValue(tmDto.getTaskDescription());
					cell = row.createCell(4);
					cell.setCellValue(tmDto.getStatus());

				}
			}
			File file = new File("InboxReport.xlsx");
			OutputStream out = new FileOutputStream(file);
			workBook.write(out);
			out.flush();
			out.close();
			byte[] fileContent = Files.readAllBytes(file.toPath());
			encodedString = Base64.getEncoder().encodeToString(fileContent);
		} catch (Exception e) {
			encodedString = "ERROR";
		}
		return encodedString;
	}

	@SuppressWarnings("resource")
	@Override
	public File exportInboxAsFile2(InboxFilterDto filterPayload,Token token) {

		System.err.println("FilesExportFacade.exportInboxAsFile2() filterPayload" + filterPayload);
		Workbook workBook = new XSSFWorkbook();
		XSSFSheet sheet = (XSSFSheet) workBook.createSheet();
		String encodedString = "";
		File file = new File("InboxReport.xlsx");
		try {
			WorkboxResponseDto response = workbox.getWorkboxFilterDataNew(filterPayload, false,token);

			if (sheet.getLastRowNum() == 0) {
				XSSFRow headerRow = sheet.createRow(0);
				XSSFCell cell = headerRow.createCell(0);
				cell.setCellValue("REQUEST ID");
				cell = headerRow.createCell(1);
				cell.setCellValue("PROCESS NAME");
				cell = headerRow.createCell(2);
				cell.setCellValue("CREATED BY");
				cell = headerRow.createCell(3);
				cell.setCellValue("DESCRIPTION");
				cell = headerRow.createCell(4);
				cell.setCellValue("STATUS");

			}

			int r = sheet.getLastRowNum() + 1;
			if (!ServicesUtil.isEmpty(response.getWorkBoxDtos())) {
				for (WorkBoxDto tmDto : response.getWorkBoxDtos()) {
					System.err.println("FilesExportFacade.exportInboxAsFile2() tmDto" + tmDto);

					XSSFRow row = sheet.createRow(r++);

					XSSFCell cell = row.createCell(0);
					cell.setCellValue(tmDto.getRequestId());
					cell = row.createCell(1);
					cell.setCellValue(tmDto.getProcessDisplayName());
					cell = row.createCell(2);
					cell.setCellValue(tmDto.getStartedBy());
					cell = row.createCell(3);
					cell.setCellValue(tmDto.getTaskDescription());
					cell = row.createCell(4);
					cell.setCellValue(tmDto.getStatus());

				}
			}
			file = new File("InboxReport.xlsx");
			System.err.println("FilesExportFacade.exportInboxAsFile2() file while empty" + file.toString());
			System.err.println("FilesExportFacade.exportInboxAsFile2() workbook sheet:" + workBook.getNumberOfSheets());

			OutputStream out = new FileOutputStream(file);
			workBook.write(out);
			out.flush();
			out.close();

			System.err.println("FilesExportFacade.exportInboxAsFile2() file after out:" + out);

			byte[] fileContent = Files.readAllBytes(file.toPath());
			encodedString = Base64.getEncoder().encodeToString(fileContent);
		} catch (Exception e) {
			System.err.println("FilesExportFacade.exportInboxAsFile2()" + e.getMessage());
			encodedString = "ERROR";
		}
		System.err.println("FilesExportFacade.exportInboxAsFile2() end of method");

		return file;
	}

	@Override
	public File exportInboxAsFileWithCA(InboxFilterDto filterPayload,Token token) {

		attrDao.getCustomAttributes("");
		System.err.println("FilesExportFacade.exportInboxAsFileWithCA() filterPayload" + filterPayload);
		Workbook workBook = new XSSFWorkbook();
		XSSFSheet sheet = (XSSFSheet) workBook.createSheet();
		String encodedString = "";
		List<String> listOfProcesses = new ArrayList<String>();
		File file = new File("InboxReport.xlsx");
		try {

			WorkboxRequestFilterDto advancedFilterDto = filterPayload.getAdvanceFilter();
			AdvanceFilterDetailDto filterDto = new AdvanceFilterDetailDto();

			if (!ServicesUtil.isEmpty(advancedFilterDto) && !ServicesUtil.isEmpty(advancedFilterDto.getFilterMap())
					&& advancedFilterDto.getFilterMap().containsKey("pe.NAME")) {

				String processName = advancedFilterDto.getFilterMap().containsKey("pe.NAME")
						? advancedFilterDto.getFilterMap().get("pe.NAME").getValue() : "";

				listOfProcesses = Arrays.asList(processName.split(","));

			} else {

				if (ServicesUtil.isEmpty(advancedFilterDto)) {
					advancedFilterDto = new WorkboxRequestFilterDto();
					filterPayload.setAdvanceFilter(advancedFilterDto);
				}
				if (ServicesUtil.isEmpty(filterPayload.getAdvanceFilter().getFilterMap())) {
					advancedFilterDto.setFilterMap(new HashMap());
				}

				filterDto.setCondition("in");
				filterDto.setDataType("STRING");
				filterDto.setOperator("AND");
				filterDto.setLevel("pe");
				filterPayload.getAdvanceFilter().getFilterMap().put("pe.NAME", filterDto);

				List<ProcessConfigDto> processes = processConfigDao.getAllProcessConfigEntry();
				for (ProcessConfigDto processConfigDto : processes) {
					listOfProcesses.add("'" + processConfigDto.getProcessName() + "'");

				}
			}
			System.err.println("FilesExportFacade.exportInboxAsFileWithCA() listOfProcesses" + listOfProcesses);

			for (String process : listOfProcesses) {
				filterPayload.getAdvanceFilter().getFilterMap().get("pe.NAME").setValue(process);
				WorkboxResponseDto response = workbox.getWorkboxFilterDataNew(filterPayload, false, token);
				System.err.println("Sheet name" + process.replaceAll("[^\\dA-Za-z ]", ""));
				XSSFSheet sheet1 = (XSSFSheet) workBook.createSheet(process.replaceAll("[^\\dA-Za-z ]", ""));

				if (sheet1.getLastRowNum() == 0) {
					XSSFRow headerRow = sheet1.createRow(0);
					XSSFCell cell = headerRow.createCell(0);
					cell.setCellValue("REQUEST ID");
					cell = headerRow.createCell(1);
					cell.setCellValue("PROCESS NAME");
					cell = headerRow.createCell(2);
					cell.setCellValue("CREATED BY");
					cell = headerRow.createCell(3);
					cell.setCellValue("DESCRIPTION");
					cell = headerRow.createCell(4);
					cell.setCellValue("STATUS");

				}

				ArrayNode arrayNode = response.getCustomAttributes();
				System.err.println("FilesExportFacade.exportInboxAsFileWithCA() arrayNode" + arrayNode);

				for (JsonNode jsonNode : arrayNode) {
					System.err.println("FilesExportFacade.exportInboxAsFileWithCA() jsonNode" + jsonNode);
				}

				int r = sheet.getLastRowNum() + 1;
				if (!ServicesUtil.isEmpty(response.getWorkBoxDtos())) {
					for (WorkBoxDto tmDto : response.getWorkBoxDtos()) {
						System.err.println("FilesExportFacade.exportInboxAsFile2() tmDto" + tmDto);

						XSSFRow row = sheet.createRow(r++);

						XSSFCell cell = row.createCell(0);
						cell.setCellValue(tmDto.getRequestId());
						cell = row.createCell(1);
						cell.setCellValue(tmDto.getProcessDisplayName());
						cell = row.createCell(2);
						cell.setCellValue(tmDto.getStartedBy());
						cell = row.createCell(3);
						cell.setCellValue(tmDto.getTaskDescription());
						cell = row.createCell(4);
						cell.setCellValue(tmDto.getStatus());

					}
				}
			}
			file = new File("InboxReport.xlsx");
			System.err.println("FilesExportFacade.exportInboxAsFile2() file while empty" + file.toString());
			System.err.println("FilesExportFacade.exportInboxAsFile2() workbook sheet:" + workBook.getNumberOfSheets());

			OutputStream out = new FileOutputStream(file);
			workBook.write(out);
			out.flush();
			out.close();

			System.err.println("FilesExportFacade.exportInboxAsFile2() file after out:" + out);

			byte[] fileContent = Files.readAllBytes(file.toPath());
			encodedString = Base64.getEncoder().encodeToString(fileContent);
		} catch (Exception e) {
			System.err.println("catch FilesExportFacade.exportInboxAsFile2()" + e.getMessage());
			encodedString = "ERROR";
		}
		System.err.println("FilesExportFacade.exportInboxAsFile2() end of method");

		return file;

	}

	@Override
	public File exportInboxAsFileWithCAAsMap(InboxFilterDto filterPayload,Token token) {

		System.err.println("FilesExportFacade.exportInboxAsFileWithCA() filterPayload" + filterPayload);
		Workbook workBook = new XSSFWorkbook();
		XSSFSheet sheet = (XSSFSheet) workBook.createSheet();
		List<XSSFSheet> sheets = new ArrayList<>();
		String encodedString = "";
		List<String> listOfProcesses = new ArrayList<String>();
		String processListAsString = "";
		File file = new File("InboxReport.xlsx");

		try {

			WorkboxRequestFilterDto advancedFilterDto = filterPayload.getAdvanceFilter();
			AdvanceFilterDetailDto filterDto = new AdvanceFilterDetailDto();

			// checking if paylod already has processes selected----------
			if (!ServicesUtil.isEmpty(advancedFilterDto) && !ServicesUtil.isEmpty(advancedFilterDto.getFilterMap())
					&& advancedFilterDto.getFilterMap().containsKey("pe.NAME")) {

				String processName = advancedFilterDto.getFilterMap().containsKey("pe.NAME")
						? advancedFilterDto.getFilterMap().get("pe.NAME").getValue() : "";
				processListAsString = processName;
				listOfProcesses = Arrays.asList(processName.split(","));
				processName = null;

			} else {

				if (ServicesUtil.isEmpty(advancedFilterDto)) {
					advancedFilterDto = new WorkboxRequestFilterDto();
					filterPayload.setAdvanceFilter(advancedFilterDto);
				}
				if (ServicesUtil.isEmpty(filterPayload.getAdvanceFilter().getFilterMap())) {
					advancedFilterDto.setFilterMap(new HashMap());
				}

				filterDto.setCondition("in");
				filterDto.setDataType("STRING");
				filterDto.setOperator("AND");
				filterDto.setLevel("pe");
				List<ProcessConfigDto> processes = processConfigDao.getAllProcessConfigEntry();
				for (ProcessConfigDto processConfigDto : processes) {
					listOfProcesses.add("'" + processConfigDto.getProcessName() + "'");
					processListAsString += "'" + processConfigDto.getProcessName() + "',";
				}
				if (!ServicesUtil.isEmpty(processListAsString)) {
					processListAsString = processListAsString.substring(0, processListAsString.length() - 1);

				}
				filterDto.setValue(processListAsString);
				filterPayload.getAdvanceFilter().getFilterMap().put("pe.NAME", filterDto);

			}
			filterDto = null;
			advancedFilterDto = null;

			//

			// creating sheets for each process
			for (String process : listOfProcesses) {

				System.err.println("Sheet name" + process.replaceAll("[^\\dA-Za-z ]", ""));
				List<String> processAttr = attrDao.getKeysFromTemplate(process.replaceAll("'", ""));
				sheet = (XSSFSheet) workBook.createSheet(process.replaceAll("[^\\dA-Za-z ]", "").toLowerCase());
				if (sheet.getLastRowNum() == 0) {
					XSSFRow headerRow = sheet.createRow(0);
					XSSFCell cell = headerRow.createCell(0);
					cell.setCellValue("REQUEST ID");
					cell = headerRow.createCell(1);
					cell.setCellValue("PROCESS NAME");
					cell = headerRow.createCell(2);
					cell.setCellValue("CREATED BY");
					cell = headerRow.createCell(3);
					cell.setCellValue("DESCRIPTION");
					cell = headerRow.createCell(4);
					cell.setCellValue("STATUS");

					int i = 5;
					for (String string : processAttr) {
						cell = headerRow.createCell(i);
						cell.setCellValue(string);
						i++;
					}

				}

				sheets.add(sheet);

			}

			System.err.println("FilesExportFacade.exportInboxAsFile2() workbook sheet:" + workBook.getNumberOfSheets());

			// get task dtos
			WorkboxResponseDto response = workbox.getWorkboxFilterDataNew(filterPayload, false, token);

			// getting custom attributes
			Map<String, List<CustomAttributeValue>> customMap = attrDao
					.getCustomAttributesAsMap(processListAsString.toUpperCase());
			System.err.println("FilesExportFacade.exportInboxAsFileWithCAAsMap() customMap.size()" + customMap.size());

			int r = sheet.getLastRowNum() + 1;
			if (!ServicesUtil.isEmpty(response.getWorkBoxDtos())) {

				for (WorkBoxDto tmDto : response.getWorkBoxDtos()) {
					System.err.println("FilesExportFacade.exportInboxAsFile2() tmDto" + tmDto);

					XSSFRow row = (XSSFRow) workBook
							.getSheet(tmDto.getProcessName().replaceAll("[^\\dA-Za-z ]", "").toLowerCase())
							.createRow(r++);

					XSSFCell cell = row.createCell(0);
					cell.setCellValue(tmDto.getRequestId());
					cell = row.createCell(1);
					cell.setCellValue(tmDto.getProcessDisplayName());
					cell = row.createCell(2);
					cell.setCellValue(tmDto.getStartedBy());
					cell = row.createCell(3);
					cell.setCellValue(tmDto.getTaskDescription());
					cell = row.createCell(4);
					cell.setCellValue(tmDto.getStatus());

					List<CustomAttributeValue> customAttrList = customMap.get(tmDto.getTaskId());
					int i = 5;
					for (CustomAttributeValue customAttributeValue : customAttrList) {
						cell = row.createCell(i);
						cell.setCellValue(customAttributeValue.getAttributeValue());
						i++;
					}

				}
			}

			file = new File("InboxReport.xlsx");
			System.err.println("FilesExportFacade.exportInboxAsFile2() file while empty" + file.toString());
			System.err.println("FilesExportFacade.exportInboxAsFile2() workbook sheet:" + workBook.getNumberOfSheets());

			OutputStream out = new FileOutputStream(file);
			workBook.write(out);
			out.flush();
			out.close();

			System.err.println("FilesExportFacade.exportInboxAsFile2() file after out:" + out);

			byte[] fileContent = Files.readAllBytes(file.toPath());
			encodedString = Base64.getEncoder().encodeToString(fileContent);

			encodedString = "";
		} catch (

		Exception e) {
			System.err.println("catch FilesExportFacade.exportInboxAsFile2()" + e.getMessage());
			encodedString = "ERROR";
		}
		System.err.println("FilesExportFacade.exportInboxAsFile2() end of method");

		return file;

	}

	@Override
	public File exportInboxAsFileWithRowToColumn(InboxFilterDto filterPayload,Token token) {

		System.err.println("FilesExportFacade.exportInboxAsFileWithCA() filterPayload" + filterPayload);
		Workbook workBook = new XSSFWorkbook();
		XSSFSheet sheet = (XSSFSheet) workBook.createSheet();
		List<XSSFSheet> sheets = new ArrayList<>();
		String encodedString = "";
		List<String> listOfProcesses = new ArrayList<String>();
		String processListAsString = "";
		File file = new File("InboxReport.xlsx");
		List<String> processAttr = null;
		String selectQueryForCA = "";
		String caseQueryForCA = "";

		try {

			WorkboxRequestFilterDto advancedFilterDto = filterPayload.getAdvanceFilter();
			AdvanceFilterDetailDto filterDto = new AdvanceFilterDetailDto();

			// checking if paylod already has processes selected----------
			if (!ServicesUtil.isEmpty(advancedFilterDto) && !ServicesUtil.isEmpty(advancedFilterDto.getFilterMap())
					&& advancedFilterDto.getFilterMap().containsKey("pe.NAME")) {

				String processName = advancedFilterDto.getFilterMap().containsKey("pe.NAME")
						? advancedFilterDto.getFilterMap().get("pe.NAME").getValue() : "";
				processListAsString = processName;
				listOfProcesses = Arrays.asList(processName.split(","));
				processName = null;

			} else {

				if (ServicesUtil.isEmpty(advancedFilterDto)) {
					advancedFilterDto = new WorkboxRequestFilterDto();
					filterPayload.setAdvanceFilter(advancedFilterDto);
				}
				if (ServicesUtil.isEmpty(filterPayload.getAdvanceFilter().getFilterMap())) {
					advancedFilterDto.setFilterMap(new HashMap());
				}

				filterDto.setCondition("in");
				filterDto.setDataType("STRING");
				filterDto.setOperator("AND");
				filterDto.setLevel("pe");
				List<ProcessConfigDto> processes = processConfigDao.getAllProcessConfigEntry();
				for (ProcessConfigDto processConfigDto : processes) {
					listOfProcesses.add("'" + processConfigDto.getProcessName() + "'");
					processListAsString += "'" + processConfigDto.getProcessName() + "',";
				}
				if (!ServicesUtil.isEmpty(processListAsString)) {
					processListAsString = processListAsString.substring(0, processListAsString.length() - 1);

				}

				filterPayload.getAdvanceFilter().getFilterMap().put("pe.NAME", filterDto);

			}
			filterDto = null;
			advancedFilterDto = null;

			processAttr = attrDao.getActiveKeysFromTemplate("");
			sheet = (XSSFSheet) workBook.createSheet("InboxReport");
			if (sheet.getLastRowNum() == 0) {
				XSSFRow headerRow = sheet.createRow(0);
				XSSFCell cell = headerRow.createCell(0);
				cell.setCellValue("REQUEST ID");
				cell = headerRow.createCell(1);
				cell.setCellValue("PROCESS NAME");
				cell = headerRow.createCell(2);
				cell.setCellValue("CREATED BY");
				cell = headerRow.createCell(3);
				cell.setCellValue("DESCRIPTION");
				cell = headerRow.createCell(4);
				cell.setCellValue("STATUS");

				int i = 5;

				for (String string : processAttr) {
					selectQueryForCA += ",cavf._" + string.replaceAll("[^a-zA-Z0-9]", "") + i + "_";
					caseQueryForCA += ",max(CASE WHEN key = '" + string + "' THEN attr_value END) AS _"
							+ string.replaceAll("[^a-zA-Z0-9]", "") + i + "_";
					cell = headerRow.createCell(i);
					cell.setCellValue(string);
					i++;
				}

			}

			if (!ServicesUtil.isEmpty(processAttr))
				selectQueryForCA = selectQueryForCA + " from ( select task_Id as event_id " + caseQueryForCA
						+ " FROM custom_attr_values GROUP BY task_id ) as cavf join task_events te on te.event_id=cavf.event_id ";
			caseQueryForCA = "";
			System.err.println("FilesExportFacade.exportInboxAsFileWithRowToColumn() parsing");
			filterPayload.getAdvanceFilter().getFilterMap().get("pe.NAME").setValue(processListAsString);

			// get task dtos
			List<Object[]> resultList = workbox.getWorkboxFilterDataForFileExport(filterPayload, false, "",
					selectQueryForCA,token);
			selectQueryForCA = "";
			int r = sheet.getLastRowNum() + 1;
			if (!ServicesUtil.isEmpty(resultList)) {

				for (Object[] obj : resultList) {

					System.err.println("FilesExportFacade.exportInboxAsFile2() resu");

					XSSFRow row = (XSSFRow) sheet.createRow(r++);

					XSSFCell cell = row.createCell(0);
					cell.setCellValue(obj[0] == null ? null : (String) obj[0]);
					cell = row.createCell(1);
					cell.setCellValue(obj[1] == null ? null : (String) obj[1]);
					cell = row.createCell(2);
					cell.setCellValue(obj[6] == null ? null : (String) obj[6]);
					cell = row.createCell(3);
					cell.setCellValue(obj[3] == null ? null : (String) obj[3]);
					cell = row.createCell(4);
					cell.setCellValue(obj[8] == null ? null : (String) obj[8]);
					for (int i = 28; i < obj.length; i++) {

						cell.setCellValue(obj[i] == null ? null : (String) obj[i]);
						cell = row.createCell(i);

					}

				}

			}

			System.err.println("FilesExportFacade.exportInboxAsFile2() workbook sheet:" + workBook.getNumberOfSheets());

			file = new File("InboxReport.xlsx");
			System.err.println("FilesExportFacade.exportInboxAsFile2() file while empty" + file.toString());
			System.err.println("FilesExportFacade.exportInboxAsFile2() workbook sheet:" + workBook.getNumberOfSheets());

			OutputStream out = new FileOutputStream(file);
			workBook.write(out);
			out.flush();
			out.close();

			System.err.println("FilesExportFacade.exportInboxAsFile2() file after out:" + out);

			byte[] fileContent = Files.readAllBytes(file.toPath());
			encodedString = Base64.getEncoder().encodeToString(fileContent);

			encodedString = "";
		} catch (

		Exception e) {
			System.err.println("catch FilesExportFacade.exportInboxAsFile2()" + e.getMessage());
			encodedString = "ERROR";
		}
		System.err.println("FilesExportFacade.exportInboxAsFile2() end of method");

		return file;

	}

}
