package oneapp.incture.workbox.demo.adapter_base.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.demo.adapter_base.dto.ProcessContextEntityDto;
import oneapp.incture.workbox.demo.adapter_base.entity.ProcessContextEntity;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Repository("ProcessContextDao")
public class ProcessContextDao extends BaseDao<ProcessContextEntity, ProcessContextEntityDto>{

	@Override
	protected ProcessContextEntity importDto(ProcessContextEntityDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		ProcessContextEntity entity = new ProcessContextEntity();
		if (!ServicesUtil.isEmpty(fromDto.getProcessName()))
			entity.setProcessName(fromDto.getProcessName());
		if (!ServicesUtil.isEmpty(fromDto.getContext()))
			entity.setContext(fromDto.getContext());
		return entity;
	}

	@Override
	protected ProcessContextEntityDto exportDto(ProcessContextEntity entity) {
		ProcessContextEntityDto fromDto = new ProcessContextEntityDto();
		if (!ServicesUtil.isEmpty(entity.getProcessName()))
			fromDto.setProcessName(entity.getProcessName());
		if (!ServicesUtil.isEmpty(entity.getContext()))
			fromDto.setContext(entity.getContext());
		return fromDto;
	}

	public ProcessContextEntity getProcessContextEntity(String processName){
		ProcessContextEntity entity=new ProcessContextEntity();
		try{
			List<Object[]> list=getSession().createSQLQuery("select \"Context\",\"PROCESS_NAME\" from PROCESS_CONTEXT where PROCESS_NAME='"+processName+"'").list();
		
		for(Object[] obj:list){
		 entity.setContext(ServicesUtil.isEmpty(obj[0])?null:(String)obj[0]);
		 entity.setProcessName(processName);
		 break;
		}
		}catch(Exception e){
			System.err.println("ProcessContextDao.getProcessContextEntity() error : "+e);
			e.printStackTrace();
		}
		return entity;
	}
	
	public String updateProcessContextEntity(ProcessContextEntity entity){
		try{
			getSession().saveOrUpdate(entity);
		}catch(Exception e){
			System.err.println("ProcessContextDao.updateProcessContextEntity() error : "+e);
			return PMCConstant.FAILURE;
		}
		return PMCConstant.SUCCESS;
	}
}
