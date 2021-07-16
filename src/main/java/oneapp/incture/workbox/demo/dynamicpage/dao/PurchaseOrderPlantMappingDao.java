package oneapp.incture.workbox.demo.dynamicpage.dao;

import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.dynamicpage.dto.ProcessDetailsCatalogueMappingDto;
import oneapp.incture.workbox.demo.dynamicpage.entity.ProcessDetailsCatalogueMapping;

@Repository
public class PurchaseOrderPlantMappingDao {

	public ProcessDetailsCatalogueMapping importDto(ProcessDetailsCatalogueMappingDto dto) {
		ProcessDetailsCatalogueMapping entity = new ProcessDetailsCatalogueMapping();
		
		if(!ServicesUtil.isEmpty(dto)) {
			if(!ServicesUtil.isEmpty(dto.getCatalogueName()))
				entity.setCatalogueName(dto.getCatalogueName());
			if(!ServicesUtil.isEmpty(dto.getKey()))
				entity.setKey(dto.getKey());
			if(!ServicesUtil.isEmpty(dto.getGroupId()))
				entity.setGroupId(dto.getGroupId());
			if(!ServicesUtil.isEmpty(dto.getLabel()))
				entity.setLabel(dto.getLabel());
			if(!ServicesUtil.isEmpty(dto.getServiceUrl()))
				entity.setServiceUrl(dto.getServiceUrl());
			if(!ServicesUtil.isEmpty(dto.getDataType()))
				entity.setDataType(dto.getDataType());
			if(!ServicesUtil.isEmpty(dto.getDependency()))
				entity.setDependency(dto.getDependency());
			if(!ServicesUtil.isEmpty(dto.getEccKey()))
				entity.setEccKey(dto.getEccKey());
			if(!ServicesUtil.isEmpty(dto.getVisibility()))
				entity.setVisibility(dto.getVisibility());
			if(!ServicesUtil.isEmpty(dto.getEditability()))
				entity.setEditability(dto.getEditability());
			if(!ServicesUtil.isEmpty(dto.getIsMandatory()))
				entity.setIsMandatory(dto.getIsMandatory());
			
		}
		return entity;
	}
}
