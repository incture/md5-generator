package oneapp.incture.workbox.demo.adapter_base.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dto.ProcessConfigDto;
import oneapp.incture.workbox.demo.adapter_base.entity.ProcessConfigDo;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Repository("ProcessConfigDao")
//////@Transactional
public class ProcessConfigDao extends BaseDao<ProcessConfigDo, ProcessConfigDto> {

	private static final Logger logger = LoggerFactory.getLogger(ProcessConfigDao.class);
	
	@Override
	protected ProcessConfigDo importDto(ProcessConfigDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		ProcessConfigDo entity = new ProcessConfigDo();
		if (!ServicesUtil.isEmpty(fromDto.getProcessName()))
			entity.setProcessName(fromDto.getProcessName());
		if (!ServicesUtil.isEmpty(fromDto.getLabelName()))
			entity.setLabelName(fromDto.getLabelName());
		if (!ServicesUtil.isEmpty(fromDto.getSla()))
			entity.setSla(fromDto.getSla());
		if (!ServicesUtil.isEmpty(fromDto.getUrgentSla()))
			entity.setUrgentSla(fromDto.getUrgentSla());
		if (!ServicesUtil.isEmpty(fromDto.getUserGroup()))
			entity.setUserGroup(fromDto.getUserGroup());
		if (!ServicesUtil.isEmpty(fromDto.getUserRole()))
			entity.setUserRole(fromDto.getUserRole());
		/*if (!ServicesUtil.isEmpty(fromDto.getProcessConfigId()))
			entity.setProcessConfigId(fromDto.getProcessConfigId());
		 */if (!ServicesUtil.isEmpty(fromDto.getLaneCount()))
			 entity.setLaneCount(fromDto.getLaneCount());
		 if (!ServicesUtil.isEmpty(fromDto.getProcessDisplayName()))
			 entity.setProcessDisplayName(fromDto.getProcessDisplayName());
		 if (!ServicesUtil.isEmpty(fromDto.getSubject()))
			 entity.setSubject(fromDto.getSubject());
		 if (!ServicesUtil.isEmpty(fromDto.getDescription()))
			 entity.setDescription(fromDto.getDescription());
		 if (!ServicesUtil.isEmpty(fromDto.getOrigin()))
			 entity.setOrigin(fromDto.getOrigin());
		 if (!ServicesUtil.isEmpty(fromDto.getCriticalDate()))
			 entity.setCriticalDate(fromDto.getCriticalDate());

		 return entity;
	}

	@Override
	protected ProcessConfigDto exportDto(ProcessConfigDo entity) {
		ProcessConfigDto processEventsDto = new ProcessConfigDto();
		if (!ServicesUtil.isEmpty(entity.getProcessName())){
			processEventsDto.setProcessName(entity.getProcessName());
			processEventsDto.setKey("'"+entity.getProcessName()+"'");
		}if (!ServicesUtil.isEmpty(entity.getLabelName()))
			processEventsDto.setLabelName(entity.getLabelName());
		if (!ServicesUtil.isEmpty(entity.getSla()))
			processEventsDto.setSla(entity.getSla());
		if (!ServicesUtil.isEmpty(entity.getUrgentSla()))
			processEventsDto.setUrgentSla(entity.getUrgentSla());
		if (!ServicesUtil.isEmpty(entity.getUserGroup()))
			processEventsDto.setUserGroup(entity.getUserGroup());
		if (!ServicesUtil.isEmpty(entity.getUserRole()))
			processEventsDto.setUserRole(entity.getUserRole());
		if (!ServicesUtil.isEmpty(entity.getProcessDisplayName()))
			processEventsDto.setProcessDisplayName(entity.getProcessDisplayName());
		if (!ServicesUtil.isEmpty(entity.getLaneCount()))
			processEventsDto.setLaneCount(entity.getLaneCount());
		if (!ServicesUtil.isEmpty(entity.getSubject()))
			processEventsDto.setSubject(entity.getSubject());
		if (!ServicesUtil.isEmpty(entity.getDescription()))
			processEventsDto.setDescription(entity.getDescription());
		 if (!ServicesUtil.isEmpty(entity.getOrigin()))
			 processEventsDto.setOrigin(entity.getOrigin());
		 if (!ServicesUtil.isEmpty(entity.getCriticalDate()))
			 processEventsDto.setCriticalDate(entity.getCriticalDate());
		/*if(!ServicesUtil.isEmpty(entity.getProcessConfigId()))
			processEventsDto.setProcessConfigId(entity.getProcessConfigId());
		 */
		logger.error("[PMC][process_config_det] : "+processEventsDto.toString());
		return processEventsDto;
	}

	@SuppressWarnings("unchecked")
	public List<ProcessConfigDto> getAllProcessConfigEntry() throws NoResultFault {
		List<ProcessConfigDto> processLabelDtos = new ArrayList<ProcessConfigDto>();
		//String queryName = "select pl.processName, pl.labelName, pl.userGroup, pl.processDisplayName,pl.userRole from ProcessConfigDo pl";
		String queryName = "select pl from ProcessConfigDo pl";

		List<ProcessConfigDo> doList = this.getSession().createQuery(queryName).list();
		if (!ServicesUtil.isEmpty(doList)) {
			String processName = "";
			ProcessConfigDto dto = new ProcessConfigDto();
			for (ProcessConfigDo entity : doList) {
				if (!entity.getProcessName().equals("ALL")) {
					if (!ServicesUtil.isEmpty(processName)) {
						processName = processName + "','" + entity.getProcessName();
					} else {
						processName = entity.getProcessName();
					}
					processLabelDtos.add(exportDto(entity));
				} else {
					dto = exportDto(entity);
				}
			}
			dto.setKey("'" + processName + "'");
			if (!ServicesUtil.isEmpty(processLabelDtos)) {
				processLabelDtos.add(dto);
			}
		}
		return processLabelDtos;
	}


	@SuppressWarnings("unchecked")
	public List<ProcessConfigDto> getAllProcessConfigEntryByRole(String userRole) throws NoResultFault {
		List<ProcessConfigDto> processLabelDtos = new ArrayList<ProcessConfigDto>();
//		String queryName = "select pl from ProcessConfigDo pl where (pl.userRole like '%" + userRole
//				+ "%') or ( pl.processName='ALL')";
		Criteria criteria = this.getSession().createCriteria(ProcessConfigDo.class);
		if(!ServicesUtil.isEmpty(userRole))
			criteria.add(Restrictions.eq("userRole", userRole));
		List<ProcessConfigDo> doList = criteria.list();
		if (!ServicesUtil.isEmpty(doList)) {
			String processName = "";
			ProcessConfigDto dto = new ProcessConfigDto();
			for (ProcessConfigDo entity : doList) {
				if (!entity.getProcessName().equals("ALL")) {
					if (!ServicesUtil.isEmpty(processName)) {
						processName = processName + "','" + entity.getProcessName();
					} else {
						processName = entity.getProcessName();
					}
					processLabelDtos.add(exportDto(entity));
				} else {
					dto = exportDto(entity);
				}
			}
			dto.setKey("'" + processName + "'");
			if (!ServicesUtil.isEmpty(processLabelDtos)) {
				processLabelDtos.add(dto);
			}
		}
		return processLabelDtos;
	}

	public List<String> getAllConfiguredProcesses(){
		List<String> processes=new ArrayList<>();
		String query=" select PROCESS_NAME,LABEL_NAME from PROCESS_CONFIG_TB ";
		List<Object[]> list=new ArrayList<>();
		try {
			list=this.getSession().createSQLQuery(query).list();
			for(Object[] obj:list){
				processes.add(ServicesUtil.isEmpty(obj[0])?null : (String)obj[0]);
			}
		}catch(Exception e){
			System.err.println("ProcessConfigDao.getAllConfiguredProcesses() "+e);
			processes.add(e.toString());
		}
		return processes;
		
	}

	public Boolean checkForProcessExistance(String processName) {

		
		String query=" select PROCESS_NAME,LABEL_NAME from PROCESS_CONFIG_TB where PROCESS_NAME='"+processName+"'";
		List<Object[]> list=new ArrayList<>();
		try {
			list=this.getSession().createSQLQuery(query).list();
			if(list.size()>0)
			return true;
			
		}catch(Exception e){
			System.err.println("ProcessConfigDao.getAllConfiguredProcesses() "+e);
			
		}
		return false;
		
	
	}
}
