package oneapp.incture.workbox.demo.adhocTask.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adhocTask.dto.UserRoleDto;



@Repository
public class UserRoleDao {

	@Autowired
	SessionFactory sessionFactory;
	
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@SuppressWarnings("unchecked")
	public List<UserRoleDto> getAllUsersRole() {
		List<UserRoleDto> userRoles = new ArrayList<UserRoleDto>();
		UserRoleDto userRoleDto = null;
		try{
			String qeryStr = "SELECT DISTINCT ROLE_ID,ROLE_NAME FROM USER_ROLES";
			Query qry = this.getSession().createSQLQuery(qeryStr);
			List<Object[]> result = qry.list();
			
			for (Object[] obj : result) {
				userRoleDto = new UserRoleDto();
				userRoleDto.setRoleId(ServicesUtil.asString(obj[0]));
				userRoleDto.setRoleName(ServicesUtil.asString(obj[1]));
				userRoles.add(userRoleDto);
			}
		}catch (Exception e) {
			System.err.println("[WBP-Dev] Error Fetching userRoles from Database"+e);
		}
		return userRoles;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getRoleDetail() {
		Map<String,String> roles = new HashMap<>();
		
		Query qry= this.getSession().createSQLQuery("SELECT DISTINCT ROLE_NAME,ROLE_ID FROM USER_ROLES");
		List<Object[]> list = qry.list();
		
		for (Object[] obj : list) {
			roles.put(obj[1].toString(), obj[0].toString());
	
		}
		return roles;
	}

	
}
