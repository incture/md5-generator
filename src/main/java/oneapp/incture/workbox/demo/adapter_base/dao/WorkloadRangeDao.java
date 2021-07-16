package oneapp.incture.workbox.demo.adapter_base.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dto.WorkloadRangeDto;
import oneapp.incture.workbox.demo.adapter_base.entity.WorkloadRangeDo;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Repository("WorkloadRangeDao")
//@//////@Transactional
public class WorkloadRangeDao extends BaseDao<WorkloadRangeDo, WorkloadRangeDto> {

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	protected WorkloadRangeDo importDto(WorkloadRangeDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		WorkloadRangeDo workloadRangeDo = new WorkloadRangeDo();
		if (!ServicesUtil.isEmpty(fromDto.getLoadType())) {
			workloadRangeDo.setLoadType(fromDto.getLoadType());
			if (!ServicesUtil.isEmpty(fromDto.getHighLimit()))
				workloadRangeDo.setHighLimit(fromDto.getHighLimit());
			if (!ServicesUtil.isEmpty(fromDto.getLowLimit()))
				workloadRangeDo.setLowLimit(fromDto.getLowLimit());
		}

		return workloadRangeDo;
	}

	@Override
	protected WorkloadRangeDto exportDto(WorkloadRangeDo entity) {
		WorkloadRangeDto workloadRangeDto = new WorkloadRangeDto();
		workloadRangeDto.setLoadType(entity.getLoadType());
		if (!ServicesUtil.isEmpty(entity.getHighLimit()))
			workloadRangeDto.setHighLimit(entity.getHighLimit());
		if (!ServicesUtil.isEmpty(entity.getLowLimit()))
			workloadRangeDto.setLowLimit(entity.getLowLimit());
		return workloadRangeDto;
	}

	public String updateWorkloadRange(WorkloadRangeDto dto) {
		String response = PMCConstant.FAILURE;
		try {
			System.err.println("[WBP-Dev]workloadRangeDtos 4" + dto.toString());

			String updateQuery = "UPDATE workload_tb set lower_limit=" + dto.getLowLimit() + ",upper_limit="
					+ dto.getHighLimit() + " where load_type='" + dto.getLoadType() + "'";
			System.err.println("[WBP-Dev][PMC][workloadRangeDao][updateQuery]" + updateQuery);

			int resultRows = this.getSession().createSQLQuery(updateQuery).executeUpdate();
			if (resultRows > 0) {
				return PMCConstant.SUCCESS;
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][PMC][workloadRangeDao][error]" + e);
		}
		return response;
	}

	public void saveOrUpdate(WorkloadRangeDto dto1) {
		try {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(importDto(dto1));
		tx.commit();
		session.close();
		}catch (Exception e) {
			System.err.println("Error in asving config"+e);
		}
	}
}
