package oneapp.incture.workbox.demo.adminConsole.sevices;


import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adminConsole.dto.AdminControlDto;

public interface AdminControlFacadeLocal {

	
	public ResponseMessage deleteProcessConfig(String processName);

	public AdminControlDto getAdminConfigurationData();

	public ResponseMessage createUpdateDataAdmin(AdminControlDto adminControlDto);

	//public ResponseMessage createReportAging(ReportAgingDto dto);

	//ResponseMessage createProcessConfig(ProcessConfigDto dto);
}
