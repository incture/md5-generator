package oneapp.incture.workbox.demo.adapter_base.dao;

import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dto.LayoutAttributesTemplateDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ValueHelpTemplateDto;
import oneapp.incture.workbox.demo.adapter_base.entity.LayoutAttributesTemplate;
import oneapp.incture.workbox.demo.adapter_base.entity.ValueHelpTemplate;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Repository
public class ValueHelpTemplateDao extends BaseDao<ValueHelpTemplate, ValueHelpTemplateDto>{

	@Override
	protected ValueHelpTemplate importDto(ValueHelpTemplateDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		// TODO Auto-generated method stub
		ValueHelpTemplate value=new ValueHelpTemplate();
		if(!ServicesUtil.isEmpty(fromDto.getValue()))
			value.setValue(fromDto.getValue());
		if(!ServicesUtil.isEmpty(fromDto.getValueHelpId()))
			value.setValueHelpId(fromDto.getValueHelpId());
			
		return value;
	}

	@Override
	protected ValueHelpTemplateDto exportDto(ValueHelpTemplate entity) {
		// TODO Auto-generated method stub
		ValueHelpTemplateDto value=new ValueHelpTemplateDto();
		if(!ServicesUtil.isEmpty(entity.getValue()))
			value.setValue(entity.getValue());
		if(!ServicesUtil.isEmpty(entity.getValueHelpId()))
			value.setValueHelpId(entity.getValueHelpId());
			
		return value;
	}

}
