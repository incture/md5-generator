package oneapp.incture.workbox.demo.workflow.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.entity.ProcessConfigDo;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.workflow.dto.CustomProcessCreationDto;
import oneapp.incture.workbox.demo.workflow.dto.ProcessConfigTbDto;
import oneapp.incture.workbox.demo.workflow.util.WorkflowCreationConstant;

@Repository
public class ProcessConfigDao extends BaseDao<ProcessConfigDo, ProcessConfigTbDto> {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	protected ProcessConfigDo importDto(ProcessConfigTbDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		ProcessConfigDo entity = new ProcessConfigDo();
		if (!ServicesUtil.isEmpty(fromDto.getProcessName()))
			entity.setProcessName(fromDto.getProcessName());
		if (!ServicesUtil.isEmpty(fromDto.getDescription()))
			entity.setDescription(fromDto.getDescription());
		if (!ServicesUtil.isEmpty(fromDto.getLabelName()))
			entity.setLabelName(fromDto.getLabelName());
		if (!ServicesUtil.isEmpty(fromDto.getOrigin()))
			entity.setOrigin(fromDto.getOrigin());
		if (!ServicesUtil.isEmpty(fromDto.getProcessDisplayName()))
			entity.setProcessDisplayName(fromDto.getProcessDisplayName());
		if (!ServicesUtil.isEmpty(fromDto.getProcessRequestId()))
			entity.setProcessRequestId(fromDto.getProcessRequestId());
		if (!ServicesUtil.isEmpty(fromDto.getProcessType()))
			entity.setProcessType(fromDto.getProcessType());
		if (!ServicesUtil.isEmpty(fromDto.getSla()))
			entity.setSla(fromDto.getSla());
		if (!ServicesUtil.isEmpty(fromDto.getCriticalDate()))
			entity.setCriticalDate(fromDto.getCriticalDate());
		if (!ServicesUtil.isEmpty(fromDto.getSubject()))
			entity.setSubject(fromDto.getSubject());
		if (!ServicesUtil.isEmpty(fromDto.getColor()))
			entity.setProcessColor(fromDto.getColor());
		if (!ServicesUtil.isEmpty(fromDto.getUrl()))
			entity.setUrl(fromDto.getUrl());
		return entity;
	}

	@Override
	protected ProcessConfigTbDto exportDto(ProcessConfigDo entity) {
		ProcessConfigTbDto processConfigDto = new ProcessConfigTbDto();
		processConfigDto.setProcessName(entity.getProcessName());
		if (!ServicesUtil.isEmpty(entity.getDescription()))
			processConfigDto.setDescription(entity.getDescription());
		if (!ServicesUtil.isEmpty(entity.getLabelName()))
			processConfigDto.setLabelName(entity.getLabelName());
		if (!ServicesUtil.isEmpty(entity.getOrigin()))
			processConfigDto.setOrigin(entity.getOrigin());
		if (!ServicesUtil.isEmpty(entity.getProcessColor()))
			processConfigDto.setColor(entity.getProcessColor());
		if (!ServicesUtil.isEmpty(entity.getProcessDisplayName()))
			processConfigDto.setProcessDisplayName(entity.getProcessDisplayName());
		if (!ServicesUtil.isEmpty(entity.getProcessRequestId()))
			processConfigDto.setProcessRequestId(entity.getProcessRequestId());
		if (!ServicesUtil.isEmpty(entity.getProcessType()))
			processConfigDto.setProcessType(entity.getProcessType());
		if (!ServicesUtil.isEmpty(entity.getSla()))
			processConfigDto.setSla(entity.getSla());
		if (!ServicesUtil.isEmpty(entity.getSubject()))
			processConfigDto.setSubject(entity.getSubject());
		if (!ServicesUtil.isEmpty(entity.getSla())) {
			processConfigDto.setSla(entity.getSla());
			int day = Integer.parseInt(entity.getSla()) / 24;
			int hours = Integer.parseInt(entity.getSla()) % 24;
			processConfigDto.setSlaDays(day);
			processConfigDto.setSlaHours(hours);
		}
		if (!ServicesUtil.isEmpty(entity.getCriticalDate())) {
			processConfigDto.setCriticalDate(entity.getCriticalDate());
			int day = Integer.parseInt(entity.getCriticalDate()) / 24;
			int hours = Integer.parseInt(entity.getCriticalDate()) % 24;
			processConfigDto.setCriticalDateDays(day);
			processConfigDto.setCriticalDateHours(hours);
		}
		if (!ServicesUtil.isEmpty(entity.getUrl()))
			processConfigDto.setUrl(entity.getUrl());
		return processConfigDto;
	}

	public void saveOrUpdateProcessConfig(ProcessConfigTbDto processConfigDto) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			if (!ServicesUtil.isEmpty(processConfigDto)) {
				
				session.saveOrUpdate(importDto(processConfigDto));
				session.flush();
				session.clear();
				tx.commit();
				session.close();
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW][INSERTING CUSTOM ATTRIBUTES] ERROR:" + e.getMessage());
		}
	}

	public String updateProcessConfig(CustomProcessCreationDto updateProcessDto) {

		String result = WorkflowCreationConstant.FAILURE;
		int sla = 0;
		int criticalDate = 0;

		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			if (updateProcessDto.getProcessDetail().getSlaDays() != null)
				sla = 24 * updateProcessDto.getProcessDetail().getSlaDays();
			if (updateProcessDto.getProcessDetail().getSlaHours() != null)
				sla = sla + updateProcessDto.getProcessDetail().getSlaHours();
			if (updateProcessDto.getProcessDetail().getCriticalDateDays() != null)
				criticalDate = 24 * updateProcessDto.getProcessDetail().getCriticalDateDays();
			if (updateProcessDto.getProcessDetail().getCriticalDateHours() != null)
				criticalDate = criticalDate + updateProcessDto.getProcessDetail().getCriticalDateHours();

			System.err.println("[WBP-Dev]SLA : " + sla + "critical : " + criticalDate);

			String updateString = "UPDATE PROCESS_CONFIG_TB SET ";
			String setSla = "SLA= :val3 ,PROCESS_COLOR= :val4";
			String setCriticalDate = " ,CRITICAL_DATE= :val5";
			String setUrl = ",URL= :url";
			String whereClause = " WHERE PROCESS_NAME= :val1";

			Query updateNumberOfLevel = session
					.createSQLQuery(updateString + setSla + setCriticalDate + setUrl + whereClause);
			updateNumberOfLevel.setParameter("val1", updateProcessDto.getProcessDetail().getProcessName());
			updateNumberOfLevel.setParameter("val3", String.valueOf(sla));
			updateNumberOfLevel.setParameter("val4", updateProcessDto.getProcessDetail().getColor());
			updateNumberOfLevel.setParameter("val5", String.valueOf(criticalDate));
			updateNumberOfLevel.setParameter("url", ServicesUtil.isEmpty(updateProcessDto.getProcessDetail().getUrl())
					? null : updateProcessDto.getProcessDetail().getUrl());
			updateNumberOfLevel.executeUpdate();
			tx.commit();
			session.close();

			result = WorkflowCreationConstant.SUCCESS;
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][WORKFLOW UPDATION][PROCESS DETAIL]ERROR:" + e.getMessage());
		}
		return result;
	}

	@SuppressWarnings({ "unchecked" })
	public ProcessConfigTbDto getProcessDetail(String processName, String processType) {

		try {
			String query = "select p from ProcessConfigDo p where p.processName= :processName";
			if (!processType.isEmpty() && processType.equals(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN)) {
				query = query + " and p.origin= :origin";
			}
			System.err.println("[WBP-Dev]query : " + query);
			Query configQuery = this.getSession().createQuery(query);
			configQuery.setParameter("processName", processName);
			if (!processType.isEmpty() && processType.equals(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN)) {
				configQuery.setParameter("origin", WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN);
			}
			List<ProcessConfigDo> processConfigDo = configQuery.list();
			if (!processConfigDo.isEmpty()) {
				ProcessConfigTbDto processConfigDto = exportDto(processConfigDo.get(0));
				return processConfigDto;

			} else {
				System.err.println("[WBP-Dev]retruning null");
				return null;
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][WORFLOW DETAIL][PROCESS DETAIL]ERROR:" + e.getMessage());
			return null;
		}
	}

	public List<String> validateProcess(List<String> processName) {
		String resp = WorkflowCreationConstant.FAILURE;
		Query checkProcessExists = getSession().createSQLQuery(
				"SELECT PROCESS_NAME,ORIGIN FROM PROCESS_CONFIG_TB " + "WHERE PROCESS_NAME in (:processName) ");
		checkProcessExists.setParameterList("processName", processName);
		// if (((BigInteger) checkProcessExists.uniqueResult()).intValue() > 0)
		// result = WorkflowCreationConstant.SUCCESS;
		List<Object[]> result = checkProcessExists.list();
		List<String> processList = new ArrayList<>();
		for (Object[] obj : result) {

			if (!ServicesUtil.isEmpty(obj[1])) {
				if (WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN.equals(obj[1].toString()))
					processList.add(!ServicesUtil.isEmpty(obj[0]) ? obj[0].toString() : "");
			}
		}
		return processList;
	}

	public String validateProcess(String processName) {
		String result = WorkflowCreationConstant.FAILURE;
		Query checkProcessExists = getSession()
				.createSQLQuery("SELECT COUNT(*) FROM PROCESS_CONFIG_TB " + "WHERE PROCESS_NAME in (:processName) ");
		checkProcessExists.setParameter("processName", processName);
		if (((BigInteger) checkProcessExists.uniqueResult()).intValue() > 0)
			result = WorkflowCreationConstant.SUCCESS;

		return result;
	}

	public void deleteInConfigTb(List<String> processName) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String deleteInPCT = "DELETE FROM PROCESS_CONFIG_TB WHERE PROCESS_NAME IN (:processName)";
		Query deleteInPCTQuery = session.createSQLQuery(deleteInPCT);
		deleteInPCTQuery.setParameterList("processName", processName);
		deleteInPCTQuery.executeUpdate();
		tx.commit();
		session.close();
	}

	@SuppressWarnings({ "unchecked" })
	public List<String> getRequestIdList() {
		Query requestIdTokenQry = getSession().createSQLQuery("SELECT PROCESS_REQUEST_ID FROM PROCESS_CONFIG_TB");
		List<String> requestIds = requestIdTokenQry.list();
		return requestIds;
	}

	@SuppressWarnings({ "unchecked" })
	public List<ProcessConfigTbDto> getProcessDetailList(String processType) {

		List<ProcessConfigTbDto> configTbDtos = new ArrayList<>();
		try {
			String query = "select p from ProcessConfigDo p";

			if (!ServicesUtil.isEmpty(processType)) {
				if (processType.equalsIgnoreCase(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN)) {
					query = query + " where p.origin= :origin";
				} else if (processType.equalsIgnoreCase(WorkflowCreationConstant.SYSTEM_ORIGIN)) {
					query = query + " where p.origin!= :origin";
				}
			}

			query = query + " order by case when processDisplayName = 'All' then 1 else 2 end,processDisplayName asc";
			Query configQuery = this.getSession().createQuery(query);

			if (!ServicesUtil.isEmpty(processType)
					&& !WorkflowCreationConstant.ORIGIN_ALL.equalsIgnoreCase(processType)) {
				configQuery.setParameter("origin", WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN);

			}

			List<ProcessConfigDo> processConfigDo = configQuery.list();
			if (!ServicesUtil.isEmpty(processConfigDo)) {
				for (ProcessConfigDo processDo : processConfigDo) {
					ProcessConfigTbDto processConfigDto = exportDto(processDo);
					configTbDtos.add(processConfigDto);
				}
				return configTbDtos;
			} else {
				System.err.println("[WBP-Dev]retruning null");
				return null;
			}
		} catch (

		Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][WORFLOW DETAIL][PROCESS DETAIL]ERROR:" + e.getMessage());
			return null;
		}
	}

	public String getAfeNexus(String processName) {
		Query getAfeNexusOrder = getSession()
				.createQuery("select do.orderType from AfeNexusOrderDo do where do.processName='" + processName + "'");
		String orderType = (String) getAfeNexusOrder.uniqueResult();
		return orderType;
	}

	public void updateAfeNexusOrder(String processName,String orderType) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query getAfeNexusOrder = session
				.createSQLQuery("update AFE_NEXUS_ORDER set ORDER_TYPE='"+orderType+"' where PROCESS_NAME='" + processName + "'");
		getAfeNexusOrder.executeUpdate();
		tx.commit();
		session.close();
		
	}

}
