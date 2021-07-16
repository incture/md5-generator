package oneapp.incture.workbox.demo.adapter_base.dto;

import java.util.ArrayList;
import java.util.List;

public class ProcessConfigurationDto {

	List<ProcessDefinationDto> processDefinationData=new ArrayList<ProcessDefinationDto>();
	
	List<String> configuredProcesses=new ArrayList<>();
	
	

	public ProcessConfigurationDto() {
		super();
	}

	public ProcessConfigurationDto(List<ProcessDefinationDto> processDefinationData, List<String> configuredProcesses) {
		super();
		this.processDefinationData = processDefinationData;
		this.configuredProcesses = configuredProcesses;
	}

	public List<String> getConfiguredProcesses() {
		return configuredProcesses;
	}

	public void setConfiguredProcesses(List<String> configuredProcesses) {
		this.configuredProcesses = configuredProcesses;
	}

	public List<ProcessDefinationDto> getProcessDefinationData() {
		return processDefinationData;
	}

	public void setProcessDefinationData(List<ProcessDefinationDto> processDefinationData) {
		this.processDefinationData = processDefinationData;
	}

	@Override
	public String toString() {
		return "ProcessConfigurationDto [processDefinationData=" + processDefinationData + ", configuredProcesses="
				+ configuredProcesses + "]";
	}
	
	
	
	
}
