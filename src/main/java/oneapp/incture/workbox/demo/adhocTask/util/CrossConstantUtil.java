package oneapp.incture.workbox.demo.adhocTask.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adhocTask.dao.CrossConstantDao;
import oneapp.incture.workbox.demo.adhocTask.dto.CrossConstantDto;
import oneapp.incture.workbox.demo.adhocTask.dto.CrossConstantResponseDto;

@Component
public class CrossConstantUtil {

	@Autowired
	CrossConstantDao crossConstantDao;

	static int initialValue = 0;

	 Map<String, String> ppdCostCenterValueMap;

	public CrossConstantResponseDto fetchConstants(String name) {
		CrossConstantResponseDto responseDto = new CrossConstantResponseDto();
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(TaskCreationConstant.FAILURE);
		resp.setStatus(TaskCreationConstant.FAILURE);
		resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
		responseDto.setResponseMessage(resp);
		List<CrossConstantDto> listOfCrossConstant = null;
		CrossConstantDto crossConstantDto = null;

		// PPD changes
		{
			ppdCostCenterValueMap = crossConstantDao.getPPDAvaibaleBudget();

			// if (initialValue == 0) {
			// ppdCostCenterValueMap = new HashMap<>();
			// ppdCostCenterValueMap.put("4YULJ59001", "500000");
			// ppdCostCenterValueMap.put("3DE1TY0011", "500000");
			// ppdCostCenterValueMap.put("4GYUKI003", "500000");
			// initialValue++;
			// }
		}

		try {
			List<Object[]> constantsDetail = crossConstantDao.getConstants(name);
			listOfCrossConstant = new ArrayList<CrossConstantDto>();
			if (!ServicesUtil.isEmpty(constantsDetail)) {

				for (Object[] obj : constantsDetail) {
					crossConstantDto = new CrossConstantDto();
					crossConstantDto.setId(name);

					if (("af2gd3051h2a".equals(name) || "h3bd6iacb25j".equals(name) || "bede8c13d82d".equals(name)
							|| "b4df760ei5jb".equals(name)))
						crossConstantDto
								.setName(obj[0].toString() + "@" + ppdCostCenterValueMap.get(obj[1].toString()));
					else
						crossConstantDto.setName(obj[0].toString());

					crossConstantDto.setValue(obj[1].toString());

					listOfCrossConstant.add(crossConstantDto);
				}
			}

			resp.setMessage(TaskCreationConstant.SUCCESS);
			resp.setStatus(TaskCreationConstant.SUCCESS);
			resp.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);

			responseDto.setDto(listOfCrossConstant);
			responseDto.setResponseMessage(resp);
		} catch (Exception e) {

		}
		return responseDto;
	}

}
