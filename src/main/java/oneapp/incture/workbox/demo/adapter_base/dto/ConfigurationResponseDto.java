package oneapp.incture.workbox.demo.adapter_base.dto;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationResponseDto {

	private List<TaskTemplateTableDto> taskTemplateTableDtos = new ArrayList<>();
	private List<TemplateTableDto> templateTableDtos = new ArrayList<>();
	private List<LayoutTemplateDto> layoutTemplateDtos = new ArrayList<>();
	private List<LayoutAttributesTemplateDto> layoutAttributesTemplateDtos = new ArrayList<>();
	private String templateId;
	private String layoutId;
	
	public String getLayoutId() {
		return layoutId;
	}
	public void setLayoutId(String layoutId) {
		this.layoutId = layoutId;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public ConfigurationResponseDto() {
		super();
	}
	public ConfigurationResponseDto(List<TaskTemplateTableDto> taskTemplateTableDtos,
			List<TemplateTableDto> templateTableDtos, List<LayoutTemplateDto> layoutTemplateDtos,
			List<LayoutAttributesTemplateDto> layoutAttributesTemplateDtos) {
		super();
		this.taskTemplateTableDtos = taskTemplateTableDtos;
		this.templateTableDtos = templateTableDtos;
		this.layoutTemplateDtos = layoutTemplateDtos;
		this.layoutAttributesTemplateDtos = layoutAttributesTemplateDtos;
	}
	public ConfigurationResponseDto(List<TaskTemplateTableDto> taskTemplateTableDtos,
			List<TemplateTableDto> templateTableDtos, List<LayoutTemplateDto> layoutTemplateDtos,
			List<LayoutAttributesTemplateDto> layoutAttributesTemplateDtos, String templateId, String layoutId) {
		super();
		this.taskTemplateTableDtos = taskTemplateTableDtos;
		this.templateTableDtos = templateTableDtos;
		this.layoutTemplateDtos = layoutTemplateDtos;
		this.layoutAttributesTemplateDtos = layoutAttributesTemplateDtos;
		this.templateId = templateId;
		this.layoutId = layoutId;
	}
	@Override
	public String toString() {
		return "ConfigurationResponseDto [taskTemplateTableDtos=" + taskTemplateTableDtos + ", templateTableDtos="
				+ templateTableDtos + ", layoutTemplateDtos=" + layoutTemplateDtos + ", layoutAttributesTemplateDtos="
				+ layoutAttributesTemplateDtos + ", templateId=" + templateId + ", layoutId=" + layoutId + "]";
	}
	public List<TaskTemplateTableDto> getTaskTemplateTableDtos() {
		return taskTemplateTableDtos;
	}
	public void setTaskTemplateTableDtos(List<TaskTemplateTableDto> taskTemplateTableDtos) {
		this.taskTemplateTableDtos = taskTemplateTableDtos;
	}
	public List<TemplateTableDto> getTemplateTableDtos() {
		return templateTableDtos;
	}
	public void setTemplateTableDtos(List<TemplateTableDto> templateTableDtos) {
		this.templateTableDtos = templateTableDtos;
	}
	public List<LayoutTemplateDto> getLayoutTemplateDtos() {
		return layoutTemplateDtos;
	}
	public void setLayoutTemplateDtos(List<LayoutTemplateDto> layoutTemplateDtos) {
		this.layoutTemplateDtos = layoutTemplateDtos;
	}
	public List<LayoutAttributesTemplateDto> getLayoutAttributesTemplateDtos() {
		return layoutAttributesTemplateDtos;
	}
	public void setLayoutAttributesTemplateDtos(List<LayoutAttributesTemplateDto> layoutAttributesTemplateDtos) {
		this.layoutAttributesTemplateDtos = layoutAttributesTemplateDtos;
	}
	
	
	
	
}
