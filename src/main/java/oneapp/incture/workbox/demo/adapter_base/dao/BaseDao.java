package oneapp.incture.workbox.demo.adapter_base.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.MessageUIDto;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.RecordExistFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;


/**
 * The <code>BaseDao</code> abstract class comprise abstract functions for CRUD
 * operations and a few utility functions for child
 * <code>Data Access Objects<code>
 * 
 * @version 2, 21-June-2012
 * @since CR8313
 */
@Repository("BaseDao")
//@Transactional
public abstract class BaseDao<E extends BaseDo, D extends BaseDto>  {

	private static final Logger logger = LoggerFactory.getLogger(BaseDao.class);

	protected final boolean isNotQuery = false;
	protected final boolean isQuery = true;
	
	private final String recordExist = "Record already exist ";
	private final String noRecordFound = "No record found: ";
	
	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	// <WRAPPER OVER BASIC CRUD ONES, WITH IMPORT AND EXPORT FUNCTIONS>

	/**
	 * @param dto
	 *            the record to be created
	 * @throws ExecutionFault
	 *             in case for fatal error
	 * @throws InvalidInputFault
	 *             wrong inputs
	 * @throws NoResultFault
	 */
	public void create(D dto) throws ExecutionFault, InvalidInputFault,
	NoResultFault {
		persist(importDto(EnOperation.CREATE, dto));
	}

	/**
	 * @param dto
	 *            input object
	 * @return single record based on the objects primary key
	 * @throws ExecutionFault
	 *             in case for fatal error
	 * @throws InvalidInputFault
	 *             even key is missing
	 * @throws NoResultFault
	 *             when record could be retrieved
	 */
	public D getByKeys(D dto) throws ExecutionFault, InvalidInputFault,
	NoResultFault {
		return exportDto(getByKeysForFK(dto));
	}

	/**
	 * @param dto
	 *            input object
	 * @return the entity, mainly used for setting FK
	 */
	public E getByKeysForFK(D dto) throws ExecutionFault, InvalidInputFault,
	NoResultFault {
		return find(importDto(EnOperation.RETRIEVE, dto));
	}

	/**
	 * @param dto
	 *            the record to be updated
	 * @return the updated record
	 * @throws ExecutionFault
	 *             in case for fatal error
	 * @throws InvalidInputFault
	 *             wrong inputs
	 * @throws NoResultFault
	 */
	public void update(D dto) throws ExecutionFault, InvalidInputFault,
	NoResultFault {
		getByKeysForFK(dto);
		merge(importDto(EnOperation.UPDATE, dto));
	}

	public void delete(D dto) throws ExecutionFault, InvalidInputFault,
	NoResultFault {
		remove(getByKeysForFK(dto));
	}

	// </WRAPPER OVER BASIC CRUD ONES, WITH IMPORT AND EXPORT FUNCTIONS>

	// <BASIC CRUD OPERATIONS>
	protected void persist(E pojo) throws ExecutionFault {
		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.persist(pojo);
			tx.commit();
			session.close();
		} catch (Exception e) {
			MessageUIDto faultInfo = new MessageUIDto();
			logger.error(e.getMessage());
			faultInfo.setMessage(e.getMessage());
			String message = "Create of " + pojo.getClass().getSimpleName()
					+ " with keys " + pojo.getPrimaryKey() + PMCConstant.FAILED;
			throw new ExecutionFault(message, faultInfo, e);
		}
	}

	@SuppressWarnings("unchecked")
	protected E find(E pojo) throws ExecutionFault, NoResultFault {
		E result = null;
		try {
			String primaryKey = (String) pojo.getPrimaryKey();
			result = (E) getSession().load(pojo.getClass(),primaryKey );
		} catch (Exception e) {
			logger.error("Exception in find"+e.getMessage());
			// In case of connection or other JPA ones.
			MessageUIDto faultInfo = new MessageUIDto();
			faultInfo.setMessage(e.getMessage());
			String message = "Retrieve of " + pojo.getClass().getSimpleName()
					+ " by keys " + pojo.getPrimaryKey() + PMCConstant.FAILED;
			throw new ExecutionFault(message, faultInfo, e);
		}
		if (result == null) {
			throw new NoResultFault(noRecordFound
					+ pojo.getClass().getSimpleName() + "#"
					+ pojo.getPrimaryKey());
		}
		return result;
	}

	protected void merge(E pojo) throws ExecutionFault {
		try {
			getSession().update(pojo);
		} catch (Exception e) {
			logger.error(e.getMessage());
			MessageUIDto faultInfo = new MessageUIDto();
			faultInfo.setMessage(e.getMessage());
			String message = "Update of " + pojo.getClass().getSimpleName()
					+ " having keys " + pojo.getPrimaryKey() + PMCConstant.FAILED;
			throw new ExecutionFault(message, faultInfo, e);
		}
	}

	protected void saveOrUpdate(E pojo) throws ExecutionFault {
		try {
			getSession().saveOrUpdate(pojo);
		} catch (Exception e) {
			logger.error(e.getMessage());
			MessageUIDto faultInfo = new MessageUIDto();
			faultInfo.setMessage(e.getMessage());
			String message = "Save Or Update of " + pojo.getClass().getSimpleName()
					+ " having keys " + pojo.getPrimaryKey() + PMCConstant.FAILED;
			throw new ExecutionFault(message, faultInfo, e);
		}
	}

	protected void remove(E pojo) throws ExecutionFault {
		try {
			getSession().delete(pojo);
		} catch (Exception e) {
			MessageUIDto faultInfo = new MessageUIDto();
			faultInfo.setMessage(e.getMessage());
			String message = "Delete of " + pojo.getClass().getSimpleName()
					+ " having keys " + pojo.getPrimaryKey() + PMCConstant.FAILED;
			throw new ExecutionFault(message, faultInfo, e);
		}
	}

	// </BASIC CRUD OPERATIONS>
	// <SIGNATURE FOR DATA TRANSFER FUNCTIONS>
	private E importDto(EnOperation operation, D fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		if (fromDto != null) {
			fromDto.validate(operation);
			return importDto(fromDto);
		}
		throw new InvalidInputFault("Empty DTO passed");
	}

	/**
	 * @param fromDto
	 *            Data object from which data needs to be copied to a new entity
	 */
	protected abstract E importDto(D fromDto) throws InvalidInputFault,
	ExecutionFault, NoResultFault;

	/**
	 * @param entity
	 *            Copies data back to a new transfer object from entity
	 */
	protected abstract D exportDto(E entity);

	protected List<D> exportDtoList(Collection<E> listDo) {
		List<D> returnDtos = null;
		if (!ServicesUtil.isEmpty(listDo)) {
			returnDtos = new ArrayList<>(listDo.size());
			for (Iterator<E> iterator = listDo.iterator(); iterator.hasNext();) {
				returnDtos.add(exportDto(iterator.next()));
			}
		}
		return returnDtos;
	}

	/**
	 * Its negation logic over getByKeys.
	 * 
	 * @param dto
	 * @throws ExecutionFault
	 * @throws RecordExistFault
	 * @throws InvalidInputFault
	 */
	protected void entityExist(D dto) throws ExecutionFault, RecordExistFault,
	InvalidInputFault {
		try {
			getByKeys(dto);
			throw new RecordExistFault(recordExist, buildRecordExistFault(dto));
		} catch (NoResultFault e) {
			logger.error(e.getMessage());
		}
	}

	// </SIGNATURE FOR DATA TRANSFER FUNCTIONS>

	protected String uuidGen(BaseDto dto, Class<? extends BaseDo> classDo)
			throws ExecutionFault {
		return UUID.randomUUID().toString();
		
	}

	/**
	 * 
	 * @param BaseDto
	 */
	private MessageUIDto buildRecordExistFault(BaseDto BaseDto) {
		StringBuilder sb = new StringBuilder(recordExist);
		if (BaseDto != null) {
			sb.append(BaseDto.toString());
		}
		MessageUIDto messageUIDto = new MessageUIDto();
		messageUIDto.setMessage(sb.toString());
		return messageUIDto;
	}

	/**
	 * 
	 * @param doName
	 * @param parameters
	 */
	@SuppressWarnings("unchecked")
	public List<D> getAllResults(String doName, Object... parameters)
			throws NoResultFault {
		String queryName = "SELECT p FROM " + doName + " p ";
		Query query =  getSession().createQuery(queryName);
		List<E> returnList = query.list();
		if (ServicesUtil.isEmpty(returnList)) {
			throw new NoResultFault(ServicesUtil.buildNoRecordMessage(
					queryName, parameters));
		}
		logger.error("returnList: "+returnList);
		return exportDtoList(returnList);
	}

	/**
	 * 
	 * @param doName
	 * @param parameters
	 */
	@SuppressWarnings("unchecked")
	public List<D> getAllReportAegingResults(String doName, Object... parameters)
			throws NoResultFault {
		String queryName = "SELECT p FROM " + doName + " p order by p.lowerSegment";
		Query query =  getSession().createQuery(queryName);
		List<E> returnList = query.list();
		if (ServicesUtil.isEmpty(returnList)) {
			throw new NoResultFault(ServicesUtil.buildNoRecordMessage(
					queryName, parameters));
		}
		logger.error("returnList: "+returnList);
		return exportDtoList(returnList);
	}

	/**
	 * 
	 * @param doName
	 * @param columnName
	 * @param parameters
	 */
	@SuppressWarnings("unchecked")
	public List<D> getAllActiveResults(String doName, String columnName, String value,Object... parameters)
			throws NoResultFault {
		String queryName = "SELECT p FROM " + doName + " p" + " where p."+columnName+" = "+value+" ";
		logger.error("queryName - "+queryName);
		Query query =  getSession().createQuery(queryName);
		List<E> returnList = query.list();
		if (ServicesUtil.isEmpty(returnList)) {
			throw new NoResultFault(ServicesUtil.buildNoRecordMessage(
					queryName, parameters));
		}
		return exportDtoList(returnList);
	}

	/**
	 * 
	 * @param doName
	 * @param columnName
	 * @param value
	 * @param parameters
	 */
	@SuppressWarnings("unchecked")
	public List<D> getSpecificActiveResults(String doName, String columnName, String value,Object... parameters)
			throws NoResultFault {
		String queryName = "SELECT p FROM " + doName + " p" + " where p."+columnName+" =:value";
		logger.error("queryName: "+queryName);
		logger.error("[pmc][ReportAging][details] :"+doName +",   "+columnName+",   "+value);
		Query query =  getSession().createQuery(queryName);
		query.setParameter("value", value.trim());
		List<E> returnList = query.list();
		if (ServicesUtil.isEmpty(returnList)) {
			throw new NoResultFault(ServicesUtil.buildNoRecordMessage(
					queryName, parameters));
		}          
		logger.error("returnList size: "+returnList.size());
		return exportDtoList(returnList);
	}

	/**
	 * 
	 * @param doName
	 * @param columnName
	 * @param value
	 * @param parameters
	 */
	@SuppressWarnings("unchecked")
	public List<D> getSpecificConfigResults(String doName, String columnName, String value, Object... params)
			throws NoResultFault {
		String queryName = "SELECT p FROM " + doName + " p" + " WHERE p." + columnName
				+ " =:value order by p.lowerSegment";
		logger.error("queryName: " + queryName);
		logger.error("[pmc][ReportAging][details] :" + doName + ",   " + columnName + ",   " + value);
		Query query =  getSession().createQuery(queryName);
		query.setParameter("value", value.trim());
		List<E> returnList = query.list();
		if (ServicesUtil.isEmpty(returnList)) {
			throw new NoResultFault(ServicesUtil.buildNoRecordMessage(queryName, params));
		}
		logger.error("return List Size : " + returnList.size());
		return exportDtoList(returnList);
	}
}