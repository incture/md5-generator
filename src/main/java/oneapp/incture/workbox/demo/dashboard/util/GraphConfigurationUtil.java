package oneapp.incture.workbox.demo.dashboard.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.entity.GraphConfigurationDo;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.dashboard.dto.GraphDataCountDto;
import oneapp.incture.workbox.demo.dashboard.dto.GraphDataDto;
import oneapp.incture.workbox.demo.inbox.dao.InboxFilterDao;


@Component
public class GraphConfigurationUtil implements CustomGraphConstant{

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public List<GraphDataDto> getGraphCount(GraphConfigurationDo graphConfigurationDo, Integer page) {
		List<GraphDataDto> graphDataDtos = null;
		List<String> listOfString = null;
		Map<String,String> filterNames = new HashMap<>();
		try{
			System.err.println("graphConfigurationDo"+graphConfigurationDo);

			String listParameter = "";
			if(TASK_COUNT.equalsIgnoreCase(graphConfigurationDo.getxParameter()))
				listParameter = Y_AXIS;
			else if(TASK_COUNT.equalsIgnoreCase(graphConfigurationDo.getyParameter()))
				listParameter = X_AXIS;

			String selectQuery = getSelectQuery(X_AXIS.equals(listParameter)?graphConfigurationDo.getxParameter():
				graphConfigurationDo.getyParameter());

			String countQury = COUNT_QUERY;

			String graphTypeQuery = "";
			Integer parameterMax = X_AXIS.equals(listParameter)?graphConfigurationDo.getxAxisTopValue():graphConfigurationDo.getyAxisTopValue();
			Integer categoryMax = X_AXIS.equals(listParameter)?graphConfigurationDo.getyAxisTopValue():graphConfigurationDo.getxAxisTopValue();
			
			if(!ServicesUtil.isEmpty(parameterMax)){
				if(parameterMax == 0)
					parameterMax = MAX_COUNT;
			}
			if(!ServicesUtil.isEmpty(categoryMax)){
				if(categoryMax == 0)
					categoryMax = MAX_COUNT;
			}
				
			if(CREATED_ON.equals(X_AXIS.equals(listParameter)?graphConfigurationDo.getxParameter():graphConfigurationDo.getyParameter())
					|| DUE_DATE.equals(X_AXIS.equals(listParameter)?graphConfigurationDo.getxParameter():graphConfigurationDo.getyParameter())
					|| COMPLETED_ON.equals(X_AXIS.equals(listParameter)?graphConfigurationDo.getxParameter():graphConfigurationDo.getyParameter())
					||CREATED_ON.equals(X_AXIS.equals(listParameter)?graphConfigurationDo.getyCategory():graphConfigurationDo.getxCategory())
					|| DUE_DATE.equals(X_AXIS.equals(listParameter)?graphConfigurationDo.getyCategory():graphConfigurationDo.getxCategory())
					|| COMPLETED_ON.equals(X_AXIS.equals(listParameter)?graphConfigurationDo.getyCategory():graphConfigurationDo.getxCategory()))
			{

				String parameter = X_AXIS.equals(listParameter)?graphConfigurationDo.getxParameter():graphConfigurationDo.getyParameter();
				String parameterFilter = X_AXIS.equals(listParameter)?graphConfigurationDo.getxFilter():graphConfigurationDo.getyFilter();
				String category = X_AXIS.equals(listParameter)?graphConfigurationDo.getyCategory():graphConfigurationDo.getxCategory();
				String categoryFilter = X_AXIS.equals(listParameter)?graphConfigurationDo.getyFilter():graphConfigurationDo.getxFilter();
				
				String filter ="month";
				String param = "";
				String cat = "";
				String catFilter = "";
				Integer catMax = MAX_COUNT;
				Integer offset = 0;
				Boolean flag = false;
				if(CREATED_ON.equals(parameter) || DUE_DATE.equals(parameter) || COMPLETED_ON.equals(parameter)){
					param = parameter;
					filter = ServicesUtil.isEmpty(parameterFilter)?"month":parameterFilter;
					cat = ServicesUtil.isEmpty(category)?"":category;
					catFilter = ServicesUtil.isEmpty(categoryFilter)?"":categoryFilter;
					catMax = ServicesUtil.isEmpty(categoryMax)?MAX_COUNT:categoryMax;
					if(!ServicesUtil.isEmpty(page))
						offset = (page-1)*catMax;
					flag = false;
				}else
				{
					param = category;
					filter = ServicesUtil.isEmpty(categoryFilter)?"month":categoryFilter;
					cat = ServicesUtil.isEmpty(parameter)?"":parameter;
					catFilter = ServicesUtil.isEmpty(parameterFilter)?"":parameterFilter;
					catMax = ServicesUtil.isEmpty(parameterMax)?MAX_COUNT:parameterMax;
					if(!ServicesUtil.isEmpty(page))
						offset = (page-1)*catMax;
					flag = true;
				}

				System.err.println("param"+param);
				System.err.println("filter"+filter);
				System.err.println("cat"+cat);
				System.err.println("catFilter"+catFilter);

				List<String> categoryQry = null;
				if(!ServicesUtil.isEmpty(cat)){
					categoryQry =  getCategoryTimeQry(cat, catFilter);
				}
				System.err.println("categoryQry"+categoryQry);
				switch(param){

				case COMPLETED_ON:
					graphTypeQuery = graphTypeQuery+getCompletedOn(categoryQry,filter,cat,catMax,offset);
					break;
				case CREATED_ON:
					graphTypeQuery = graphTypeQuery+getCreatedOn(categoryQry,filter,cat,catMax,offset);
					break;
				case DUE_DATE:
					graphTypeQuery = graphTypeQuery+getDueDate(categoryQry,filter,cat,catMax,offset);
					break;
				}

				System.err.println("[WBP-DEV] CUSTOM GRAPH QUERY :"+graphTypeQuery);
				Query countQuery = this.getSession().createSQLQuery(graphTypeQuery);
				List<Object[]> result = countQuery.list();

				if(!ServicesUtil.isEmpty(result)){
					if (PMCConstant.GRAPH_TREND_WEEK.equals(filter)) {
						listOfString = ServicesUtil.getPreviousDates();
					}else{
						listOfString = ServicesUtil.getPreviousMonthsName();
					}
					if (listOfString != null){
						if(result.size() == 1 && ServicesUtil.isEmpty(cat))
							graphDataDtos = getGraphDataDtoDateType(result.get(0),listParameter,listOfString,
									(PMCConstant.GRAPH_TREND_WEEK.equals(filter))
									? listOfString.size() - 1 : listOfString.size());
						else{
							graphDataDtos = getGraphDataDtoDateType(result,listParameter,listOfString,
									(PMCConstant.GRAPH_TREND_WEEK.equals(filter))
									? listOfString.size() - 1 : listOfString.size(),flag);
						}
					}
				}

			}else{
				List<String> categoryQry = getCategoryQry(graphConfigurationDo,listParameter,categoryMax);
				
				graphTypeQuery = prepareGraphQry(graphConfigurationDo,listParameter,categoryQry,countQury,parameterMax,page);

				String category = X_AXIS.equals(listParameter)?graphConfigurationDo.getyCategory():graphConfigurationDo.getxCategory();
				String categoryFilter = X_AXIS.equals(listParameter)?graphConfigurationDo.getyFilter():graphConfigurationDo.getxFilter();

				String[] filters = null;
				if(!ServicesUtil.isEmpty(categoryFilter)){
					filters= categoryFilter.split(",");
					filterNames = getFilterNames(category,categoryFilter);
				}
				System.err.println("[WBP-DEV] CUSTOM GRAPH QUERY :"+selectQuery+graphTypeQuery);
				Query countQuery = this.getSession().createSQLQuery(selectQuery+graphTypeQuery);
				
				List<Object[]> result = countQuery.list();

				if(!ServicesUtil.isEmpty(result)){
					graphDataDtos = getGraphDataDto(result,listParameter,categoryQry.size(),filters,filterNames);
				}
			}

		}catch (Exception e) {
			System.err.println("[WBP-DEV]GraphConfigurationUtil.getGraphCount() ERROR :"+e);
			e.printStackTrace();
		}
		return graphDataDtos;
	}

	private List<String> getCategoryTimeQry(String cat,String catFilter) {
		List<String> categoryQryList = new ArrayList<>();
		String category = cat;
		String categoryFilter = catFilter;

		if(!ServicesUtil.isEmpty(category)){
			String filter = categoryFilter.replaceAll(",", "','");
			switch(category){
			case PROCESS_LIST:{
				categoryQryList.add(" AND PCT.PROCESS_NAME = PC.PROCESS_NAME ");
				if(ServicesUtil.isEmpty(filter))
					categoryQryList.add(" FROM PROCESS_CONFIG_TB PC ");
				else
					categoryQryList.add(" FROM PROCESS_CONFIG_TB PC where PC.PROCESS_NAME in ('"+filter+"') ");
				break;
			}
			case GROUP_LIST:{
				categoryQryList.add(" AND TW.GROUP_ID = G.GROUP_ID ");
				if(ServicesUtil.isEmpty(filter))
					categoryQryList.add(" FROM GROUPS G ");
				else
					categoryQryList.add(" FROM GROUPS G where G.GROUP_ID in ('"+filter+"') ");
				break;
			}
			case USER_LIST:{
				categoryQryList.add(" AND TW.task_owner = UIM.USER_ID ");
				if(ServicesUtil.isEmpty(filter))
					categoryQryList.add(" FROM USER_IDP_MAPPING UIM ");
				else
					categoryQryList.add(" FROM USER_IDP_MAPPING UIM where UIM.USER_ID in ('"+filter+"') ");
				break;
			}
			case STATUS:{
				categoryQryList.add(" AND TE.STATUS = = CC.CONSTANT_NAME ");
				if(ServicesUtil.isEmpty(filter))
					categoryQryList.add(" FROM CROSS_CONSTANTS CC ");
				else
					categoryQryList.add(" FROM CROSS_CONSTANTS CC where CC.CONSTANT_NAME in ('"+filter+"') ");
				break;
			}
			case ORIGIN:{
				categoryQryList.add(" AND TE.ORIGIN = CC.CONSTANT_NAME ");
				if(ServicesUtil.isEmpty(filter))
					categoryQryList.add(" FROM CROSS_CONSTANTS CC ");
				else
					categoryQryList.add(" FROM CROSS_CONSTANTS CC where CC.CONSTANT_NAME in ('"+filter+"') ");
				break;
			}
			case CREATED_BY:{
				categoryQryList.add(" AND pe.STARTED_BY = UIM.USER_ID ");
				if(ServicesUtil.isEmpty(filter))
					categoryQryList.add(" FROM USER_IDP_MAPPING UIM ");
				else
					categoryQryList.add(" FROM USER_IDP_MAPPING UIM where UIM.USER_ID in ('"+filter+"') ");
				break;
			}
			case TASK_STATE:{
				categoryQryList.add(" where  task_sla =cc.constant_value ");
				if(ServicesUtil.isEmpty(filter))
					categoryQryList.add(" FROM cross_constants cc where cc.constant_id = 'te.comp_deadline' ");
				else
					categoryQryList.add(" FROM cross_constants cc where cc.constant_id = 'te.comp_deadline' and "
							+ " cc.constant_name in ('"+filter+"') ");
				break;
			}
			}
		}
		System.err.println("categoryQryList"+categoryQryList.toString());
		return categoryQryList;
	}

	private String prepareGraphQry(GraphConfigurationDo graphConfigurationDo,String listParameter,List<String> categoryQry 
			,String countQury, Integer parameterMax, Integer page) {
		String graphTypeQuery ="";
		parameterMax = ServicesUtil.isEmpty(parameterMax)?MAX_COUNT:parameterMax;
		Integer offset = 0;
		if(!ServicesUtil.isEmpty(page))
			offset = (page-1)*parameterMax;
		switch(X_AXIS.equals(listParameter)?graphConfigurationDo.getxParameter():graphConfigurationDo.getyParameter()){

		case PROCESS_LIST:
			graphTypeQuery = graphTypeQuery+getProcessListQuery(categoryQry,countQury,parameterMax,offset);
			break;
		case GROUP_LIST:
			graphTypeQuery = graphTypeQuery+getGroupListQuery(categoryQry,countQury,parameterMax,offset);
			break;
		case USER_LIST:
			graphTypeQuery = graphTypeQuery+getUserListQuery(categoryQry,countQury,parameterMax,offset);
			break;
		case STATUS:
			graphTypeQuery = graphTypeQuery+getStatusQuery(categoryQry,countQury,parameterMax,offset);
			break;
		case ORIGIN:
			graphTypeQuery = graphTypeQuery+getOriginQuery(categoryQry,countQury,parameterMax,offset);
			break;
		case TASK_STATE:
			graphTypeQuery = graphTypeQuery+getTaskStateQuery(categoryQry,countQury,parameterMax,offset);
			break;
		case CREATED_BY:
			graphTypeQuery = graphTypeQuery+getCreatedBy(categoryQry,countQury,parameterMax,offset);
			break;
		}
		return graphTypeQuery;
	}

	@SuppressWarnings("unchecked")
	private Map<String,String> getFilterNames(String category, String categoryFilter) {
		String query ="";
		Map<String,String> filterNames = new HashMap<String, String>();
		categoryFilter = categoryFilter.replaceAll(",", "','");
		switch(category){
		case PROCESS_LIST:{
			query ="SELECT PROCESS_DISPLAY_NAME,PROCESS_NAME FROM PROCESS_CONFIG_TB WHERE PROCESS_NAME IN ('"+categoryFilter+"')";
			break;
		}
		case GROUP_LIST:{
			query ="SELECT GROUP_NAME,GROUP_ID FROM GROUPS WHERE GROUP_ID IN ('"+categoryFilter+"')";
			break;
		}
		case USER_LIST:{
			query ="SELECT USER_FIRST_NAME || ' '|| USER_LAST_NAME,USER_ID FROM USER_IDP_MAPPING WHERE USER_ID IN ('"+categoryFilter+"')";
			break;
		}
		case STATUS:{
			query ="SELECT CONSTANT_VALUE,CONSTANT_NAME FROM CROSS_CONSTANTS WHERE CONSTANT_ID = 'te.status' AND CONSTANT_NAME IN ('"+categoryFilter+"')";
			break;
		}
		case ORIGIN:{
			query ="SELECT CONSTANT_VALUE,CONSTANT_NAME FROM CROSS_CONSTANTS WHERE CONSTANT_ID = 'te.origin' AND CONSTANT_NAME IN ('"+categoryFilter+"')";
			break;
		}
		case CREATED_BY:{
			query ="SELECT USER_FIRST_NAME || ' '|| USER_LAST_NAME,USER_ID FROM USER_IDP_MAPPING WHERE USER_ID IN ('"+categoryFilter+"')";
			break;
		}
		case TASK_STATE:{
			query ="SELECT CONSTANT_VALUE,CONSTANT_NAME FROM CROSS_CONSTANTS WHERE CONSTANT_ID = 'te.comp_deadline' AND CONSTANT_NAME IN ('"+categoryFilter+"')";
			break;
		}
		}
		Query q = this.getSession().createSQLQuery(query);
		List<Object[]> result = q.list();
		
		for (Object[] obj : result) {
			filterNames.put(ServicesUtil.isEmpty(obj[1])?"":obj[1].toString(), ServicesUtil.isEmpty(obj[0])?"":obj[0].toString());
		}
		
		return filterNames;
	}

	private String getDueDate(List<String> categoryQry, String duration, String param, Integer catMax, Integer offset) {
		List<String> listOfString = null;
		String queryString = getSelectQuery(param);
		String catQry = "";
		String fromQry = "";
		String taskStateQry = "";
		String orderBy = " ORDER BY (";
		if(!ServicesUtil.isEmpty(param))
		{
			queryString = queryString +" , ";
			if(TASK_STATE.equals(param))
				taskStateQry = categoryQry.get(0);
			else
				catQry = categoryQry.get(0);
			fromQry = categoryQry.get(1);

		}else{
			queryString = queryString +" '' , ";
			fromQry = " from dummy";
		}
		String subQuery = "";
		
		if (PMCConstant.GRAPH_TREND_WEEK.equals(duration)) {

			listOfString = ServicesUtil.getPreviousDates();
			for (int i = 0; i < listOfString.size() - 1; i++) {
				subQuery += "(select count(*) from (SELECT distinct request_id,te.event_id "
						+ ", CASE WHEN TE.COMPLETED_AT IS NULL THEN (CASE WHEN CURRENT_TIMESTAMP > TE.COMP_DEADLINE "
						+ "THEN 'Sla Breached' WHEN CURRENT_TIMESTAMP > ADD_SECONDS(TE.COMP_DEADLINE,-PCT.CRITICAL_DATE*60*60) AND  "
						+ "CURRENT_TIMESTAMP < TE.COMP_DEADLINE THEN 'Critical' ELSE 'In Time' END) ELSE (CASE WHEN TE.COMPLETED_AT > "
						+ "TE.COMP_DEADLINE THEN 'Sla Breached' ELSE 'In Time' END) END AS TASK_SLA  ";

				subQuery += InboxFilterDao.getCommonJoinQuery();
				subQuery += InboxFilterDao.getCommonConditionQuery(PMCConstant.PANEL_TEMPLATE_ID_ALLTASKS,
						null);

				subQuery += " "
						+ catQry+" "
						+ "and te.SLA_DUE_DATES between "
						+ "'" + listOfString.get(i) + " 00:00:00' " + "and '" + listOfString.get(i)
						+ " 23:59:59')"+taskStateQry
						+ ") as d" + (i + 1) + ",";
				
				orderBy = orderBy+"d"+(i + 1)+"+";
			}
		} else if (PMCConstant.GRAPH_TREND_MONTH.equals(duration)) {

			listOfString = ServicesUtil.getPreviousMonths();
			for (int i = 0; i < 3; i++) {
				subQuery += "(select count(*) from (SELECT distinct request_id,te.event_id "
						+ ", CASE WHEN TE.COMPLETED_AT IS NULL THEN (CASE WHEN CURRENT_TIMESTAMP > TE.COMP_DEADLINE "
						+ "THEN 'Sla Breached' WHEN CURRENT_TIMESTAMP > ADD_SECONDS(TE.COMP_DEADLINE,-PCT.CRITICAL_DATE*60*60) AND  "
						+ "CURRENT_TIMESTAMP < TE.COMP_DEADLINE THEN 'Critical' ELSE 'In Time' END) ELSE (CASE WHEN TE.COMPLETED_AT > "
						+ "TE.COMP_DEADLINE THEN 'Sla Breached' ELSE 'In Time' END) END AS TASK_SLA  ";

				subQuery += InboxFilterDao.getCommonJoinQuery();
				subQuery += InboxFilterDao.getCommonConditionQuery(PMCConstant.PANEL_TEMPLATE_ID_ALLTASKS,
						null);

				subQuery += " " //AND (TE.STATUS = 'READY' OR (TE.CUR_PROC = TW.TASK_OWNER AND TE.STATUS = 'RESERVED'))
						+ catQry+" "
						+ "and te.SLA_DUE_DATES between '"
						+ ServicesUtil.monthStartDate(listOfString.get(i)) + "' and '"
						+ ServicesUtil.monthEndDate(listOfString.get(i)) + "')"+taskStateQry
						+ ") as m" + (i + 1) + ",";
				
				orderBy = orderBy+"m"+(i + 1)+"+";
			}
			listOfString = ServicesUtil.getPreviousMonthsName();
		}

		queryString += subQuery.substring(0, subQuery.length() - 1) + fromQry + orderBy.substring(0, orderBy.length() - 1)+") desc limit "+catMax +" offset "+offset;
		System.err.println(queryString);
		return queryString;
	}

	private String getCreatedOn(List<String> categoryQry, String duration, String param, Integer catMax, Integer offset) {
		List<String> listOfString = null;
		String queryString = getSelectQuery(param);
		String catQry = "";
		String fromQry = "";
		String taskStateQry = "";
		String orderBy = " ORDER BY (";
		if(!ServicesUtil.isEmpty(param))
		{
			queryString = queryString +" , ";
			if(TASK_STATE.equals(param))
				taskStateQry = categoryQry.get(0);
			else
				catQry = categoryQry.get(0);
			fromQry = categoryQry.get(1);

		}else{
			queryString = queryString +" '' , ";
			fromQry = " from dummy";
		}
		String subQuery = "";
		
		if (PMCConstant.GRAPH_TREND_WEEK.equals(duration)) {

			listOfString = ServicesUtil.getPreviousDates();
			for (int i = 0; i < listOfString.size() - 1; i++) {
				subQuery += "(select count(*) from (SELECT distinct request_id,te.event_id "
						+ ", CASE WHEN TE.COMPLETED_AT IS NULL THEN (CASE WHEN CURRENT_TIMESTAMP > TE.COMP_DEADLINE "
						+ "THEN 'Sla Breached' WHEN CURRENT_TIMESTAMP > ADD_SECONDS(TE.COMP_DEADLINE,-PCT.CRITICAL_DATE*60*60) AND  "
						+ "CURRENT_TIMESTAMP < TE.COMP_DEADLINE THEN 'Critical' ELSE 'In Time' END) ELSE (CASE WHEN TE.COMPLETED_AT > "
						+ "TE.COMP_DEADLINE THEN 'Sla Breached' ELSE 'In Time' END) END AS TASK_SLA  ";

				subQuery += InboxFilterDao.getCommonJoinQuery();
				subQuery += InboxFilterDao.getCommonConditionQuery(PMCConstant.PANEL_TEMPLATE_ID_ALLTASKS,
						null);

				subQuery += " "
						+ catQry+" "
						+ "and te.created_at between "
						+ "'" + listOfString.get(i) + " 00:00:00' " + "and '" + listOfString.get(i)
						+ " 23:59:59')"+taskStateQry
						+ ") as d" + (i + 1) + ",";
				
				orderBy = orderBy+"d"+(i + 1)+"+";
			}
		} else if (PMCConstant.GRAPH_TREND_MONTH.equals(duration)) {

			listOfString = ServicesUtil.getPreviousMonths();
			for (int i = 0; i < 3; i++) {
				subQuery += "(select count(*) from (SELECT distinct request_id,te.event_id "
						+ ", CASE WHEN TE.COMPLETED_AT IS NULL THEN (CASE WHEN CURRENT_TIMESTAMP > TE.COMP_DEADLINE "
						+ "THEN 'Sla Breached' WHEN CURRENT_TIMESTAMP > ADD_SECONDS(TE.COMP_DEADLINE,-PCT.CRITICAL_DATE*60*60) AND  "
						+ "CURRENT_TIMESTAMP < TE.COMP_DEADLINE THEN 'Critical' ELSE 'In Time' END) ELSE (CASE WHEN TE.COMPLETED_AT > "
						+ "TE.COMP_DEADLINE THEN 'Sla Breached' ELSE 'In Time' END) END AS TASK_SLA  ";

				subQuery += InboxFilterDao.getCommonJoinQuery();
				subQuery += InboxFilterDao.getCommonConditionQuery(PMCConstant.PANEL_TEMPLATE_ID_ALLTASKS,
						null);

				subQuery += " "
						+ catQry+" "
						+ "and te.created_at between '"
						+ ServicesUtil.monthStartDate(listOfString.get(i)) + "' and '"
						+ ServicesUtil.monthEndDate(listOfString.get(i)) + "')"+taskStateQry
						+ ") as m" + (i + 1) + ",";
				
				orderBy = orderBy+"m"+(i + 1)+"+";
			}
			listOfString = ServicesUtil.getPreviousMonthsName();
		}

		queryString += subQuery.substring(0, subQuery.length() - 1) + fromQry + orderBy.substring(0, orderBy.length() - 1)+") desc limit "+catMax +" offset "+offset;
		System.err.println(queryString);
		return queryString;
	}

	private String getCompletedOn(List<String> categoryQry,String duration, String param, Integer catMax, Integer offset) {
		List<String> listOfString = null;
		String queryString = getSelectQuery(param);
		String catQry = "";
		String fromQry = "";
		String taskStateQry = "";
		String orderBy = " ORDER BY (";
		if(!ServicesUtil.isEmpty(param))
		{
			queryString = queryString +" , ";
			if(TASK_STATE.equals(param))
				taskStateQry = categoryQry.get(0);
			else
				catQry = categoryQry.get(0);
			fromQry = categoryQry.get(1);

		}else{
			queryString = queryString +" '' , ";
			fromQry = " from dummy";
		}
		String subQuery = "";
		if (PMCConstant.GRAPH_TREND_WEEK.equals(duration)) {

			listOfString = ServicesUtil.getPreviousDates();
			for (int i = 0; i < listOfString.size() - 1; i++) {
				subQuery += "(select count(*) from (SELECT distinct request_id,te.event_id"
						+ ", CASE WHEN TE.COMPLETED_AT IS NULL THEN (CASE WHEN CURRENT_TIMESTAMP > TE.COMP_DEADLINE "
						+ "THEN 'Sla Breached' WHEN CURRENT_TIMESTAMP > ADD_SECONDS(TE.COMP_DEADLINE,-PCT.CRITICAL_DATE*60*60) AND  "
						+ "CURRENT_TIMESTAMP < TE.COMP_DEADLINE THEN 'Critical' ELSE 'In Time' END) ELSE (CASE WHEN TE.COMPLETED_AT > "
						+ "TE.COMP_DEADLINE THEN 'Sla Breached' ELSE 'In Time' END) END AS TASK_SLA  ";

				subQuery += InboxFilterDao.getCommonJoinQuery();
				subQuery += InboxFilterDao.getCommonConditionQuery(PMCConstant.PANEL_TEMPLATE_ID_ALLTASKS,
						null);

				subQuery += " "
						+ catQry+" "
						+" and te.completed_at between "
						+ "'" + listOfString.get(i) + " 00:00:00' " + "and '" + listOfString.get(i)
						+ " 23:59:59')"+taskStateQry
						+ ") as d" + (i + 1) + ",";
				
				orderBy = orderBy+"d"+(i + 1)+"+";
			}
		} else if (PMCConstant.GRAPH_TREND_MONTH.equals(duration)) {

			listOfString = ServicesUtil.getPreviousMonths();
			for (int i = 0; i < 3; i++) {
				subQuery += "(select count(*) from (SELECT distinct request_id,te.event_id"
						+ ", CASE WHEN TE.COMPLETED_AT IS NULL THEN (CASE WHEN CURRENT_TIMESTAMP > TE.COMP_DEADLINE "
						+ "THEN 'Sla Breached' WHEN CURRENT_TIMESTAMP > ADD_SECONDS(TE.COMP_DEADLINE,-PCT.CRITICAL_DATE*60*60) AND  "
						+ "CURRENT_TIMESTAMP < TE.COMP_DEADLINE THEN 'Critical' ELSE 'In Time' END) ELSE (CASE WHEN TE.COMPLETED_AT > "
						+ "TE.COMP_DEADLINE THEN 'Sla Breached' ELSE 'In Time' END) END AS TASK_SLA  ";

				subQuery += InboxFilterDao.getCommonJoinQuery();
				subQuery += InboxFilterDao.getCommonConditionQuery(PMCConstant.PANEL_TEMPLATE_ID_ALLTASKS,
						null);

				subQuery += " "
						+ catQry+" "
						+ " and te.completed_at between '"
						+ ServicesUtil.monthStartDate(listOfString.get(i)) + "' and '"
						+ ServicesUtil.monthEndDate(listOfString.get(i)) + "')"+taskStateQry
						+ ") as m" + (i + 1) + ",";
				
				orderBy = orderBy+"m"+(i + 1)+"+";
			}
			listOfString = ServicesUtil.getPreviousMonthsName();
		}
		
		queryString += subQuery.substring(0, subQuery.length() - 1) + fromQry + orderBy.substring(0, orderBy.length() - 1)+") desc limit "+catMax +" offset "+offset;
		System.err.println(queryString);
		return queryString;
	}

	private List<String> getCategoryQry(GraphConfigurationDo graphConfigurationDo, String listParameter, Integer categoryMax) {
		StringBuilder categoryQry = null;
		List<String> categoryQryList = new ArrayList<>();
		String category = X_AXIS.equals(listParameter)?graphConfigurationDo.getyCategory():graphConfigurationDo.getxCategory();
		String categoryFilter = X_AXIS.equals(listParameter)?graphConfigurationDo.getyFilter():graphConfigurationDo.getxFilter();
		Integer count = ServicesUtil.isEmpty(categoryMax)?MAX_COUNT:categoryMax;
		Integer i = 0;
		
		if(!ServicesUtil.isEmpty(category) || !ServicesUtil.isEmpty(categoryFilter)){
			String[] filters = categoryFilter.split(",");
			if(!ServicesUtil.isEmpty(X_AXIS.equals(listParameter)?graphConfigurationDo.getyCategory():graphConfigurationDo.getxCategory()))
			{
				for (String filter : filters) {
					categoryQry = new StringBuilder("");
					switch(category){
					case PROCESS_LIST:{
						categoryQry = categoryQry.append(" AND PCT.PROCESS_NAME = '");
						categoryQry.append(filter);
						categoryQry.append("' ");
						break;
					}
					case GROUP_LIST:{
						categoryQry = categoryQry.append(" AND TW.GROUP_ID = '");
						categoryQry.append(filter);
						categoryQry.append("' ");
						break;
					}
					case USER_LIST:{
						categoryQry.append(" AND tw.task_owner = '");
						categoryQry.append(filter);
						categoryQry.append("' ");
						break;
					}
					case STATUS:{
						categoryQry = categoryQry.append(" AND TE.STATUS = '");
						categoryQry.append(filter);
						categoryQry.append("' ");
						break;
					}
					case ORIGIN:{
						categoryQry = categoryQry.append(" AND TE.ORIGIN = '");
						categoryQry.append(filter);
						categoryQry.append("' ");
						break;
					}
					case CREATED_BY:{
						categoryQry = categoryQry.append(" AND pe.STARTED_BY = '");
						categoryQry.append(filter);
						categoryQry.append("' ");
						break;
					}
					case TASK_STATE:{
						if(ON_TIME.equals(filter)){
							categoryQry = categoryQry.append(" AND CURRENT_TIMESTAMP < ADD_SECONDS(comp_deadline, - ("
									+ "PCT.CRITICAL_DATE*60*60)) ");
						}else if(CRITICAL.equals(filter))
						{
							categoryQry = categoryQry.append(" and current_timestamp between comp_deadline and "
									+ "ADD_SECONDS(comp_deadline, - ("
									+ "PCT.CRITICAL_DATE*60*60)) ");
						}else if(SLA_BREACHED.equals(filter)){
							categoryQry = categoryQry.append(" AND CURRENT_TIMESTAMP > comp_deadline ");
						}
						break;
					}
					}

					if(count<i){
						break;
					}
					categoryQryList.add(categoryQry.toString());
					i++;
				}
			}
		}
		System.err.println("categoryQryList"+categoryQryList.toString());
		return categoryQryList;
	}

	private String getTaskStateQuery(List<String> categoryQry, String countQry, Integer parameterMax, Integer offset) {
		StringBuilder filterQry = new StringBuilder("");
		String orderBy ="";
		String taskStateQuery = countQry+" ";

		if(!ServicesUtil.isEmpty(categoryQry)){
			for(int i = 0; i<categoryQry.size();i++){
				filterQry.append(taskStateQuery);
				filterQry.append(categoryQry.get(i));

				filterQry.append(")  COUNTS"+(i+1));
				filterQry.append(" where task_sla =cc.constant_value) as counts"+(i+1));
				orderBy = orderBy+"COUNTS"+(i+1)+"+";
			}
		}else{
			filterQry.append(taskStateQuery);
			filterQry.append(")  COUNTS1");
			filterQry.append(" where task_sla =cc.constant_value) as counts1");
			orderBy = orderBy+"COUNTS1+";
		}


		filterQry.append(" from cross_constants cc where cc.constant_id = 'te.comp_deadline' ORDER BY ("+orderBy.substring(0, orderBy.length() - 1)+") DESC limit "+parameterMax + " offset "+offset);
		System.err.println(filterQry.toString());
		return filterQry.toString();
	}

	@SuppressWarnings("unchecked")
	public String getListParameter(GraphConfigurationDo graphConfigurationDo) {
		Query q = this.getSession().createSQLQuery("SELECT CONSTANT_VALUE FROM CROSS_CONSTANTS WHERE CONSTANT_ID = 'graphParam'");
		List<String> parameters = q.list();

		if(parameters.contains(graphConfigurationDo.getxParameter())){
			if(!ServicesUtil.isEmpty(graphConfigurationDo.getxCategory()))
				return Y_AXIS;
			else if(!ServicesUtil.isEmpty(graphConfigurationDo.getyCategory()))
				return X_AXIS;
			else
				return X_AXIS;
		}

		if(parameters.contains(graphConfigurationDo.getyParameter())){
			if(!ServicesUtil.isEmpty(graphConfigurationDo.getyCategory()))
				return X_AXIS;
			else if(!ServicesUtil.isEmpty(graphConfigurationDo.getxCategory()))
				return Y_AXIS;
			else 
				return Y_AXIS;
		}
		return "";
	}

	private List<GraphDataDto> getGraphDataDto(List<Object[]> result, String listParameter, Integer count, String[] filters, Map<String,String> filterNames) {
		List<GraphDataDto> graphDataDtos = new ArrayList<GraphDataDto>();
		GraphDataDto graphDataDto = null;
		List<GraphDataCountDto> graphDataCountDtos = null;
		GraphDataCountDto graphDataCountDto = null;
		for (Object[] obj : result) {
			graphDataCountDtos = new ArrayList<>();
			graphDataDto = new GraphDataDto();
			if(ServicesUtil.isEmpty(filters) || count<1){
				graphDataDto.setxValue(obj[0].toString());
				graphDataDto.setxDisplayValue(obj[1].toString());
				graphDataDto.setyValue(ServicesUtil.getBigDecimal(obj[2]).toPlainString());
			}else{
				for(int i=0;i<count;i++){
					//				if(X_AXIS.equals(listParameter)){
					graphDataDto.setxValue(obj[0].toString());
					graphDataDto.setxDisplayValue(obj[1].toString());

					graphDataCountDto = new GraphDataCountDto();
					graphDataCountDto.setId(filters[i].toString());
					graphDataCountDto.setIdDisplayName(filterNames.get(graphDataCountDto.getId()));
					graphDataCountDto.setCount((BigInteger)(obj[i+2]));

					//					graphDataDto.setyValue(ServicesUtil.getBigDecimal(obj[1]).toPlainString());
					//				}else{
					//					graphDataDto.setyValue(obj[0].toString());
					//					graphDataDto.setyDisplayValue(obj[2].toString());
					////					graphDataDto.setxValue(ServicesUtil.getBigDecimal(obj[1]).toPlainString());
					//					graphDataCountDto = new GraphDataCountDto();
					//					graphDataCountDto.setId(filters[i].toString());
					//					graphDataCountDto.setIdDisplayName("");
					//					graphDataCountDto.setCount((BigInteger)(obj[i+2]));
					//				}
					graphDataCountDtos.add(graphDataCountDto);
				}
			}
			graphDataDto.setGraphDataCountDto(graphDataCountDtos);
			graphDataDtos.add(graphDataDto);
		}
		return graphDataDtos;
	}

	private List<GraphDataDto> getGraphDataDtoDateType(Object[] obj, String listParameter,
			List<String> listOfDates, int value) {
		List<GraphDataDto> graphDataDtos = new ArrayList<GraphDataDto>();
		GraphDataDto graphDataDto = null;

		for (int i = 0; i < value; i++) {
			graphDataDto = new GraphDataDto();

//			if(X_AXIS.equals(listParameter)){
				graphDataDto.setxValue(listOfDates.get(i));
				graphDataDto.setxDisplayValue(listOfDates.get(i));
				graphDataDto.setyValue(ServicesUtil.getBigDecimal(obj[i+1]).toPlainString());
//			}else{
//				graphDataDto.setyValue(listOfDates.get(i));
//				graphDataDto.setyDisplayValue(listOfDates.get(i));
//				graphDataDto.setxValue(ServicesUtil.getBigDecimal(obj[i+1]).toPlainString());
//			}
			graphDataDtos.add(graphDataDto);
		}
		return graphDataDtos;
	}

	private List<GraphDataDto> getGraphDataDtoDateType(List<Object[]> result, String listParameter,
			List<String> listOfDates, int value, Boolean flag) {
		List<GraphDataDto> graphDataDtos = new ArrayList<GraphDataDto>();
		GraphDataDto graphDataDto = null;
		GraphDataCountDto graphDataCountDto = null;
		List<GraphDataCountDto> graphDataCountDtos = null;
		if(!flag){
			for (int i = 0; i < value; i++) {
				graphDataDto = new GraphDataDto();

				graphDataDto.setxValue(listOfDates.get(i));
				graphDataDto.setxDisplayValue(listOfDates.get(i));
				graphDataCountDtos = new ArrayList<>();

				for (Object[] obj : result) {
					graphDataCountDto = new GraphDataCountDto();
					graphDataCountDto.setId(obj[0].toString());
					graphDataCountDto.setIdDisplayName(obj[1].toString());
					graphDataCountDto.setCount((BigInteger)(obj[i+2]));
					graphDataCountDtos.add(graphDataCountDto);
				}
				graphDataDto.setGraphDataCountDto(graphDataCountDtos);
				graphDataDtos.add(graphDataDto);
			}
		}else {
			for (Object[] obj : result) {

				graphDataDto = new GraphDataDto();

				graphDataDto.setxValue(obj[0].toString());
				graphDataDto.setxDisplayValue(obj[1].toString());
				graphDataCountDtos = new ArrayList<>();
				for (int i = 0; i < value; i++) {
					graphDataCountDto = new GraphDataCountDto();
					graphDataCountDto.setId(listOfDates.get(i));
					graphDataCountDto.setIdDisplayName(listOfDates.get(i));
					graphDataCountDto.setCount((BigInteger)(obj[i+2]));
					graphDataCountDtos.add(graphDataCountDto);
				}
				graphDataDto.setGraphDataCountDto(graphDataCountDtos);
				graphDataDtos.add(graphDataDto);
			}
		}
		return graphDataDtos;
	}

	private String getOriginQuery(List<String> categoryQry, String countQry, Integer parameterMax, Integer offset) {
		StringBuilder filterQry = new StringBuilder("");
		String orderBy = "";
		String originQuery = countQry+" "
				+ " AND TE.ORIGIN = CC.CONSTANT_NAME ";
		if(!ServicesUtil.isEmpty(categoryQry)){
			for(int i = 0; i<categoryQry.size();i++){
				filterQry.append(originQuery);
				filterQry.append(categoryQry.get(i));

				filterQry.append(") ) COUNTS"+(i+1));
				orderBy = orderBy+"COUNTS"+(i+1)+"+";
			}
		}else{
			filterQry.append(originQuery);
			filterQry.append(") ) COUNTS1");
			orderBy = orderBy+"COUNTS1+";
		}
		filterQry.append(" "
				+ "FROM CROSS_CONSTANTS CC WHERE CC.CONSTANT_ID = 'te.origin' ORDER BY ("+orderBy.substring(0, orderBy.length() - 1)+") DESC limit "+parameterMax + " offset "+offset);
		return filterQry.toString();
	}

	private String getCreatedBy(List<String> categoryQry, String countQry, Integer parameterMax, Integer offset) {
		StringBuilder filterQry = new StringBuilder("");
		String orderBy ="";
		String createdByQuery = countQry+" "
				+ " AND pe.STARTED_BY = UIM.USER_ID ";

		if(!ServicesUtil.isEmpty(categoryQry)){
			for(int i = 0; i<categoryQry.size();i++){
				filterQry.append(createdByQuery);
				filterQry.append(categoryQry.get(i));

				filterQry.append(") ) COUNTS"+(i+1));
				orderBy = orderBy+"COUNTS"+(i+1)+"+";
			}
		}else{
			filterQry.append(createdByQuery);
			filterQry.append(") ) COUNTS1");
			orderBy = orderBy+"COUNTS1+";
		}

		filterQry.append(" "
				+ "FROM USER_IDP_MAPPING UIM ORDER BY ("+orderBy.substring(0, orderBy.length() - 1)+") DESC limit "+parameterMax + " offset "+offset);
		return filterQry.toString();
	}

	private String getStatusQuery(List<String> categoryQry, String countQry, Integer parameterMax, Integer offset) {
		StringBuilder filterQry = new StringBuilder("");
		String orderBy ="";
		String statusQuery = countQry+" "
				+ "AND TE.STATUS = CC.CONSTANT_NAME ";//AND (PE.STATUS <> 'CANCELED') AND TE.COMP_DEADLINE 
														//IS NOT NULL AND TW.TASK_OWNER IS NOT NULL 

		if(!ServicesUtil.isEmpty(categoryQry)){
			for(int i = 0; i<categoryQry.size();i++){
				filterQry.append(statusQuery);
				filterQry.append(categoryQry.get(i));

				filterQry.append(") ) COUNTS"+(i+1));
				orderBy = orderBy+"COUNTS"+(i+1)+"+";
			}
		}else{
			filterQry.append(statusQuery);
			filterQry.append(") ) COUNTS1");
			orderBy = orderBy+"COUNTS1+";
		}

		filterQry.append(" "
				+ "FROM CROSS_CONSTANTS CC WHERE CC.CONSTANT_ID = 'te.status' ORDER BY ("+orderBy.substring(0, orderBy.length() - 1)+") DESC limit "+parameterMax + " offset "+offset);
		return filterQry.toString();
	}

	private String getUserListQuery(List<String> categoryQry, String countQry, Integer parameterMax, Integer offset) {
		StringBuilder filterQry = new StringBuilder("");
		String orderBy ="";
		String userListQuery = countQry+" "
				+ " AND tw.task_owner = UIM.USER_ID ";

		if(!ServicesUtil.isEmpty(categoryQry)){
			for(int i = 0; i<categoryQry.size();i++){
				filterQry.append(userListQuery);
				filterQry.append(categoryQry.get(i));

				filterQry.append(") ) COUNTS"+(i+1));
				orderBy = orderBy+"COUNTS"+(i+1)+"+";
			}
		}else{
			filterQry.append(userListQuery);
			filterQry.append(") ) COUNTS1");
			orderBy = orderBy+"COUNTS1+";
		}

		filterQry.append(" "
				+ "FROM USER_IDP_MAPPING UIM ORDER BY ("+orderBy.substring(0, orderBy.length() - 1)+") DESC limit "+parameterMax + " offset "+offset);
		return filterQry.toString();
	}

	private String getGroupListQuery(List<String> categoryQry, String countQry, Integer parameterMax, Integer offset) {
		StringBuilder filterQry = new StringBuilder("");
		String orderBy ="";
		String groupListQuery = countQry+" "
				+ " AND TW.GROUP_ID = G.GROUP_ID ";

		if(!ServicesUtil.isEmpty(categoryQry)){
			for(int i = 0; i<categoryQry.size();i++){
				filterQry.append(groupListQuery);
				filterQry.append(categoryQry.get(i));

				filterQry.append(") ) COUNTS"+(i+1));
				orderBy = orderBy+"COUNTS"+(i+1)+"+";
			}
		}else{
			filterQry.append(groupListQuery);
			filterQry.append(") ) COUNTS1");
			orderBy = orderBy+"COUNTS1+";
		}
		filterQry.append(" "
				+ "FROM GROUPS G ORDER BY ("+orderBy.substring(0, orderBy.length() - 1)+") DESC limit "+parameterMax + " offset "+offset);
		return filterQry.toString();
	}

	private String getProcessListQuery(List<String> categoryQry, String countQry, Integer parameterMax, Integer offset) {
		StringBuilder filterQry = new StringBuilder("");
		String orderBy = "";
		String graphTypeQuery = countQry+" "
				+ " AND PCT.PROCESS_NAME = PC.PROCESS_NAME ";

		if(!ServicesUtil.isEmpty(categoryQry)){
			for(int i = 0; i<categoryQry.size();i++){
				filterQry.append(graphTypeQuery);
				filterQry.append(categoryQry.get(i));

				filterQry.append(") ) COUNTS"+(i+1));
				orderBy = orderBy+"COUNTS"+(i+1)+"+";
			}
		}else{
			filterQry.append(graphTypeQuery);
			filterQry.append(") ) COUNTS1");
			orderBy = orderBy+"COUNTS1+";
		}

		filterQry.append(" "
				+ "FROM PROCESS_CONFIG_TB PC WHERE PROCESS_NAME NOT IN ('ALL') ORDER BY ("+orderBy.substring(0, orderBy.length() - 1)+") DESC limit "+parameterMax + " offset "+offset);

		return filterQry.toString();
	}

	private String getSelectQuery(String listParameter) {
		String select = "SELECT DISTINCT ";

		if(PROCESS_LIST.equals(listParameter))
			select += "PC.PROCESS_NAME ,PC.PROCESS_DISPLAY_NAME ";
		else if(GROUP_LIST.equals(listParameter))
			select += "G.GROUP_ID,G.GROUP_NAME  ";
		else if(STATUS.equals(listParameter) || ORIGIN.equals(listParameter) || TASK_STATE.equals(listParameter))
			select += "CC.CONSTANT_NAME,CC.CONSTANT_VALUE  ";
		else if(USER_LIST.equals(listParameter) || CREATED_BY.equals(listParameter))
			select += "UIM.USER_ID, UIM.USER_FIRST_NAME ||' '|| UIM.USER_LAST_NAME  ";

		return select;
	}
	
	
	public BigDecimal getTotalCount(GraphConfigurationDo graphConfigurationDo){
		String listParameter = "";
		if(TASK_COUNT.equalsIgnoreCase(graphConfigurationDo.getxParameter()))
			listParameter = Y_AXIS;
		else if(TASK_COUNT.equalsIgnoreCase(graphConfigurationDo.getyParameter()))
			listParameter = X_AXIS;

		String parameter = X_AXIS.equals(listParameter)?graphConfigurationDo.getxParameter():graphConfigurationDo.getyParameter();
		
		String query = "";
		switch(parameter){
		case PROCESS_LIST:{
			query ="SELECT DISTINCT COUNT(*) FROM PROCESS_CONFIG_TB ";
			break;
		}
		case GROUP_LIST:{
			query ="SELECT DISTINCT COUNT(*) FROM GROUPS ";
			break;
		}
		case USER_LIST:{
			query ="SELECT DISTINCT COUNT(*) FROM USER_IDP_MAPPING ";
			break;
		}
		case STATUS:{
			query ="SELECT DISTINCT COUNT(*) FROM CROSS_CONSTANTS WHERE CONSTANT_ID = 'te.status' ";
			break;
		}
		case ORIGIN:{
			query ="SELECT DISTINCT COUNT(*) FROM CROSS_CONSTANTS WHERE CONSTANT_ID = 'te.origin' ";
			break;
		}
		case CREATED_BY:{
			query ="SELECT DISTINCT COUNT(*) FROM USER_IDP_MAPPING ";
			break;
		}
		case TASK_STATE:{
			query ="SELECT DISTINCT COUNT(*) FROM CROSS_CONSTANTS WHERE CONSTANT_ID = 'te.comp_deadline' ";
			break;
		}
		case COMPLETED_ON:
		case DUE_DATE:
		case CREATED_ON:
			return null;
		}
		Query q = this.getSession().createSQLQuery(query);
		Object result = q.uniqueResult();
		return ServicesUtil.getBigDecimal(result);
	}

}
