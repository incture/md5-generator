package oneapp.incture.workbox.demo.versionControl.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.versionControl.dto.TechnicalInfoDto;
import oneapp.incture.workbox.demo.versionControl.dto.TypesDetailDto;
import oneapp.incture.workbox.demo.versionControl.dto.VersionControlDto;
import oneapp.incture.workbox.demo.versionControl.dto.VersionDetailDto;
import oneapp.incture.workbox.demo.versionControl.dto.VersionsDto;
import oneapp.incture.workbox.demo.versionControl.entity.VersionControlDo;
import oneapp.incture.workbox.demo.versionControl.util.VersionControlConstant;

@Repository("VersionControlDao")
public class VersionControlDao extends BaseDao<VersionControlDo, VersionControlDto> {

	private static final int _HIBERNATE_BATCH_SIZE = 300;
	
	@Autowired
	private SessionFactory sessionFactory;

	protected VersionControlDo importDto(VersionControlDto fromDto) {
		VersionControlDo versionControlDo = null;
		if (!ServicesUtil.isEmpty(fromDto)) {
			versionControlDo = new VersionControlDo();
			versionControlDo.setVersionId(fromDto.getVersionId());
			versionControlDo.setVersionNumber(fromDto.getVersionNumber());
			versionControlDo.setProjectCode(fromDto.getProjectCode());
			versionControlDo.setProjectName(fromDto.getProjectName());
			versionControlDo.setDateOfRelease(fromDto.getDateOfRelease());
			versionControlDo.setDetailType(fromDto.getDetailType());
			versionControlDo.setLabelDesc(fromDto.getLabelDesc());
			versionControlDo.setDescription(fromDto.getDescription());
			versionControlDo.setLinkLabel(fromDto.getLinkLabel());
			versionControlDo.setLink(fromDto.getLink());
			versionControlDo.setDocumentId(fromDto.getDocumentId());
			versionControlDo.setLanguage(fromDto.getLanguage());
			versionControlDo.setOsDetails(fromDto.getOsDetails());
			versionControlDo.setAuthor(fromDto.getAuthor());
			versionControlDo.setApplicationSize(fromDto.getApplicationSize());
			versionControlDo.setUsers(fromDto.getUsers());
			versionControlDo.setFrontendVersion(fromDto.getFrontendVersion());
			versionControlDo.setGitDetails(fromDto.getGitDetails());
		}
		return versionControlDo;
	}

	protected VersionControlDto exportDto(VersionControlDo entity) {
		VersionControlDto versionControlDto = null;
		if (!ServicesUtil.isEmpty(entity)) {
			versionControlDto = new VersionControlDto();

			versionControlDto.setVersionId(entity.getVersionId());
			versionControlDto.setVersionNumber(entity.getVersionNumber());
			versionControlDto.setProjectCode(entity.getProjectCode());
			versionControlDto.setProjectName(entity.getProjectName());
			versionControlDto.setDateOfRelease(entity.getDateOfRelease());
			versionControlDto.setDetailType(entity.getDetailType());
			versionControlDto.setLabelDesc(entity.getLabelDesc());
			versionControlDto.setDescription(entity.getDescription());
			versionControlDto.setLinkLabel(entity.getLinkLabel());
			versionControlDto.setLink(entity.getLink());
			versionControlDto.setDocumentId(entity.getDocumentId());
			versionControlDto.setLanguage(entity.getLanguage());
			versionControlDto.setOsDetails(entity.getOsDetails());
			versionControlDto.setAuthor(entity.getAuthor());
			versionControlDto.setApplicationSize(entity.getApplicationSize());
			versionControlDto.setUsers(entity.getUsers());
			versionControlDto.setFrontendVersion(entity.getFrontendVersion());
			versionControlDto.setGitDetails(entity.getGitDetails());
		}
		return versionControlDto;
	}

	public void saveOrUpdateVersion(List<VersionControlDto> list) {
		System.err.println("[WBP-Dev][Workbox][started][save or update][usermapping]");

		try {
			if (!ServicesUtil.isEmpty(list) && list.size() > 0) {
				Session session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				for (int i = 0; i < list.size(); i++) {
					session.saveOrUpdate(importDto(list.get(i)));
					if (i % _HIBERNATE_BATCH_SIZE == 0 && i > 0) {
						session.flush();
						session.clear();
					}
				}
				tx.commit();
				session.close();
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][Workbox][Batch insert][userTaskMapping]" + e.getLocalizedMessage());
		}
		System.err.println("[WBP-Dev][Workbox][Ended][save or update][userMapping]");

	}

	public void saveOrUpdateVersion(VersionControlDto list) {
		System.err.println("[WBP-Dev][Workbox][started][save or update][usermapping]");

		
		try {
			if (!ServicesUtil.isEmpty(list)) {
				Session session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				session.saveOrUpdate(importDto(list));
				tx.commit();
				session.close();
			}
			// session.close();
		} catch (Exception e) {
			System.err.println("[WBP-Dev][Workbox][Batch insert][userTaskMapping]" + e.getLocalizedMessage());
		}
		System.err.println("[WBP-Dev][Workbox][Ended][save or update][userMapping]");

	}

	public List<VersionsDto> getAllVersions() {

		List<VersionsDto> versionControlDtos = new ArrayList<>();
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
		simpleDateFormat1.setTimeZone(TimeZone.getTimeZone("IST"));

		String fetchStr = "SELECT distinct VERSION_NUMBER,DATE_OF_RELEASE FROM VERSION_CONTROL order by DATE_OF_RELEASE DESC";

		System.err.println("[WBP-Dev][fetchNotificationConfigDetails] fetchStr : " + fetchStr);

		try {
			Query q = getSession().createSQLQuery(fetchStr);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {

				VersionsDto versionControlDto = new VersionsDto();
				versionControlDto.setDateofRelease(
						obj[1] == null ? null : simpleDateFormat1.format(ServicesUtil.resultAsDate(obj[1])));
				versionControlDto.setVersionNumber((obj[0] == null ? null : (String) obj[0]));
				versionControlDtos.add(versionControlDto);

			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][VersionControlDao][getAllVersions] Error : " + e.getMessage());
		}

		return versionControlDtos;
	}

	public VersionDetailDto getVersionDetails(String versionNumber) {

		List<TypesDetailDto> whatsNew = new ArrayList<>();
		List<TypesDetailDto> bugFixes = new ArrayList<>();
		List<TypesDetailDto> improvements = new ArrayList<>();
		TechnicalInfoDto technicalInfoDto = null;
		VersionDetailDto versionControlDto = new VersionDetailDto();

		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
		simpleDateFormat1.setTimeZone(TimeZone.getTimeZone("IST"));

		String fetchStr = "SELECT PROJECT_CODE,PROJECT_NAME,DATE_OF_RELEASE,DETAIL_TYPE,LABEL_DESC,DESCRIPTION,LINK_LABEL, "
				+ "LINK,DOCUMENT_ID,LANGUAGE,OS_DETAILS,AUTHOR,APPLICATION_SIZE,USERS,FRONTEND_VERSION,GIT_DETAILS,VERSION_ID "
				+ "FROM VERSION_CONTROL where 	VERSION_NUMBER = '" + versionNumber + "'";

		System.err.println("[WBP-Dev][fetchNotificationConfigDetails] fetchStr : " + fetchStr);

		try {
			Query q = getSession().createSQLQuery(fetchStr);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {

				if (ServicesUtil.isEmpty(technicalInfoDto)) {
					technicalInfoDto = new TechnicalInfoDto();

					technicalInfoDto.setVersionNumber(versionNumber);
					technicalInfoDto.setDateofRelease(
							obj[2] == null ? null : simpleDateFormat1.format(ServicesUtil.resultAsDate(obj[2])));
					technicalInfoDto.setLanguage((obj[9] == null ? null : (String) obj[9]));
					technicalInfoDto.setOsDetails((obj[10] == null ? null : (String) obj[10]));
					technicalInfoDto.setAuthor((obj[11] == null ? null : (String) obj[11]));
					technicalInfoDto.setApplicationSize((obj[12] == null ? null : (String) obj[12]));
					technicalInfoDto.setUsers((obj[13] == null ? null : (String) obj[13]));
					technicalInfoDto.setFrontendVersion((obj[14] == null ? null : (String) obj[14]));
					technicalInfoDto.setGitDetails((obj[15] == null ? null : (String) obj[15]));

					versionControlDto.setTechnicalInformation(technicalInfoDto);
					versionControlDto.setVersionNumber(versionNumber);
					versionControlDto.setProjectCode((obj[0] == null ? null : (String) obj[0]));
					versionControlDto.setProjectName((obj[1] == null ? null : (String) obj[1]));

				}

				if ((obj[3] == null ? null : VersionControlConstant.WHATS_NEW.equalsIgnoreCase((String) obj[3]))) {
					TypesDetailDto newDetailDto = new TypesDetailDto();

					newDetailDto.setDetailType((obj[3] == null ? null : (String) obj[3]));
					newDetailDto.setLabelDesc((obj[4] == null ? null : (String) obj[4]));
					newDetailDto.setDescription((obj[5] == null ? null : (String) obj[5]));
					newDetailDto.setLinkLabel((obj[6] == null ? null : (String) obj[6]));
					newDetailDto.setLink((obj[7] == null ? null : (String) obj[7]));
					newDetailDto.setDocumentId((obj[8] == null ? null : (String) obj[8]));
					newDetailDto.setVersionId((obj[16] == null ? null : (String) obj[16]));

					whatsNew.add(newDetailDto);
				}

				if ((obj[3] == null ? null : VersionControlConstant.BUG_FIXES.equalsIgnoreCase((String) obj[3]))) {
					TypesDetailDto bugFixDetailDto = new TypesDetailDto();

					bugFixDetailDto.setDetailType((obj[3] == null ? null : (String) obj[3]));
					bugFixDetailDto.setLabelDesc((obj[4] == null ? null : (String) obj[4]));
					bugFixDetailDto.setDescription((obj[5] == null ? null : (String) obj[5]));
					bugFixDetailDto.setLinkLabel((obj[6] == null ? null : (String) obj[6]));
					bugFixDetailDto.setLink((obj[7] == null ? null : (String) obj[7]));
					bugFixDetailDto.setDocumentId((obj[8] == null ? null : (String) obj[8]));
					bugFixDetailDto.setVersionId((obj[16] == null ? null : (String) obj[16]));

					bugFixes.add(bugFixDetailDto);
				}

				if ((obj[3] == null ? null : VersionControlConstant.IMPROVEMENTS.equalsIgnoreCase((String) obj[3]))) {
					TypesDetailDto improveDetailDto = new TypesDetailDto();

					improveDetailDto.setDetailType((obj[3] == null ? null : (String) obj[3]));
					improveDetailDto.setLabelDesc((obj[4] == null ? null : (String) obj[4]));
					improveDetailDto.setDescription((obj[5] == null ? null : (String) obj[5]));
					improveDetailDto.setLinkLabel((obj[6] == null ? null : (String) obj[6]));
					improveDetailDto.setLink((obj[7] == null ? null : (String) obj[7]));
					improveDetailDto.setDocumentId((obj[8] == null ? null : (String) obj[8]));
					improveDetailDto.setVersionId((obj[16] == null ? null : (String) obj[16]));

					improvements.add(improveDetailDto);
				}

			}
			versionControlDto.setWhatsNew(whatsNew);
			versionControlDto.setBugFixes(bugFixes);
			versionControlDto.setImprovements(improvements);

		} catch (Exception e) {
			System.err.println("[WBP-Dev][VersionControlDao][getVersionDetails] Error : " + e.getMessage());
		}

		return versionControlDto;
	}

	public String getCurrentVersion() {

		Query fetchNameQry = this.getSession()
				.createSQLQuery("SELECT VERSION_NUMBER FROM VERSION_CONTROL ORDER BY date_of_release DESC LIMIT 1");
		String currentVersion = (String) fetchNameQry.uniqueResult();
		System.err.println("[getCurrentVersion][currentVersion]" + currentVersion);

		return currentVersion;
	}

	public void updateVersion(VersionControlDto dto) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			String query = "update VERSION_CONTROL set DOCUMENT_ID = '" + dto.getDocumentId() + "' where VERSION_ID = '"
					+ dto.getVersionId() + "'";
			session.createSQLQuery(query).executeUpdate();
			tx.commit();
			session.close();
			System.err.println("[updateVersion][query]" + query);
		} catch (Exception e) {
			System.err.println("[WBP-Dev][updateVersion] error updateVersion " + e.getMessage());
		}

	}

	public List<String> getDocumentID(String policyNumber) {
		return null;

	}

}