package oneapp.incture.workbox.demo.dynamicpage.dao;

import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.dynamicpage.dto.CatalogueGroupMappingDto;
import oneapp.incture.workbox.demo.dynamicpage.entity.CatalogueGroupMapping;

@Repository
public class PlantGroupMappingDao {

	public CatalogueGroupMapping importDto(CatalogueGroupMappingDto dto) {
		CatalogueGroupMapping entity = new CatalogueGroupMapping();
		if(!ServicesUtil.isEmpty(dto)) {
			if(!ServicesUtil.isEmpty(dto.getGroupId()))
				entity.setGroupId(dto.getGroupId());
			if(!ServicesUtil.isEmpty(dto.getCatalogueName()))
				entity.setCatalogueName(dto.getCatalogueName());
			if(!ServicesUtil.isEmpty(dto.getGroupType()))
				entity.setGroupType(dto.getGroupType());
			if(!ServicesUtil.isEmpty(dto.getTitle()))
				entity.setTitle(dto.getTitle());
			if(!ServicesUtil.isEmpty(dto.getEditability()))
				entity.setEditability(dto.getEditability());
			if(!ServicesUtil.isEmpty(dto.getVisibility()))
				entity.setVisibility(dto.getVisibility());		
		}
		return entity;
	}
	
	public CatalogueGroupMappingDto exportDto(CatalogueGroupMapping entity) {
		CatalogueGroupMappingDto dto = new CatalogueGroupMappingDto();
		if(!ServicesUtil.isEmpty(entity)) {
			if(!ServicesUtil.isEmpty(entity.getGroupId()))
				dto.setGroupId(entity.getGroupId());
			if(!ServicesUtil.isEmpty(entity.getGroupType()))
				dto.setGroupType(entity.getGroupType());
			if(!ServicesUtil.isEmpty(entity.getCatalogueName()))
				dto.setCatalogueName(entity.getCatalogueName());
			if(!ServicesUtil.isEmpty(entity.getTitle()))
				dto.setTitle(entity.getTitle());
			if(!ServicesUtil.isEmpty(entity.getEditability()))
				dto.setEditability(entity.getEditability());
			if(!ServicesUtil.isEmpty(entity.getVisibility()))
				dto.setVisibility(entity.getVisibility());
		}
		return dto;
	}
}
