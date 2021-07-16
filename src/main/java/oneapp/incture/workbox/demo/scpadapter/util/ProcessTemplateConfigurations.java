package oneapp.incture.workbox.demo.scpadapter.util;

import java.util.ArrayList;
import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.entity.LayoutAttributesTemplate;
import oneapp.incture.workbox.demo.adapter_base.entity.LayoutTemplateDo;
import oneapp.incture.workbox.demo.adapter_base.entity.ProcessContextEntity;
import oneapp.incture.workbox.demo.adapter_base.entity.TemplateTableDo;

public class ProcessTemplateConfigurations {

	List<LayoutTemplateDo> layouts = new ArrayList<>();
	List<LayoutAttributesTemplate> layoutAttrs = new ArrayList<>();
	List<TemplateTableDo> templateTabels = new ArrayList<>();

	ProcessContextEntity contextEntity = new ProcessContextEntity();

	public ProcessTemplateConfigurations() {
		super();
	}

	public ProcessTemplateConfigurations(List<LayoutTemplateDo> layouts, List<LayoutAttributesTemplate> layoutAttrs,
			List<TemplateTableDo> templateTabels, ProcessContextEntity contextEntity) {
		super();
		this.layouts = layouts;
		this.layoutAttrs = layoutAttrs;
		this.templateTabels = templateTabels;
		this.contextEntity = contextEntity;
	}

	public ProcessContextEntity getContextEntity() {
		return contextEntity;
	}

	public void setContextEntity(ProcessContextEntity contextEntity) {
		this.contextEntity = contextEntity;
	}

	public List<LayoutTemplateDo> getLayouts() {
		return layouts;
	}

	public void setLayouts(List<LayoutTemplateDo> layouts) {
		this.layouts = layouts;
	}

	public List<LayoutAttributesTemplate> getLayoutAttrs() {
		return layoutAttrs;
	}

	public void setLayoutAttrs(List<LayoutAttributesTemplate> layoutAttrs) {
		this.layoutAttrs = layoutAttrs;
	}

	public List<TemplateTableDo> getTemplateTabels() {
		return templateTabels;
	}

	public void setTemplateTabels(List<TemplateTableDo> templateTabels) {
		this.templateTabels = templateTabels;
	}

	@Override
	public String toString() {
		return "ProcessTemplateConfigurations [layouts=" + layouts + ", layoutAttrs=" + layoutAttrs
				+ ", templateTabels=" + templateTabels + ", contextEntity=" + contextEntity + "]";
	}

}
