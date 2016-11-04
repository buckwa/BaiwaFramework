package baiwa.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import baiwa.entity.UserAttempt;

@Repository("userAttemptDao")
public class UserAttemptDao {
	
	@Autowired JdbcTemplate jdbcTemplate;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public UserAttempt findByUsername (String username){
		username = username+"@kmitl.ac.th";
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT user_id , USERNAME, password ");
		sql.append(" FROM buckwauser ");
		sql.append(" WHERE username =  ? ");
		UserAttempt  userAttempt = null;
		try {
		userAttempt = (UserAttempt)jdbcTemplate.queryForObject(sql.toString(),new Object[] { username 
				},new BeanPropertyRowMapper<UserAttempt>(UserAttempt.class)) ;
		
		}catch (EmptyResultDataAccessException e) 
		{
			logger.warn("Can not find data with username: " + username);
		}
		logger.info("Returning user=" + userAttempt);
		return userAttempt;
		
	}
	
	public List<GrantedAuthority> findGrantedRoleByUserId(Long userID) {
		String sql = " SELECT d.role_name FROM buckwauser  AS a INNER JOIN buckwausergroup"
				+ " AS b  ON a.user_id = b.usergroup_id INNER JOIN buckwagrouprole AS c ON"
				+ " b.group_id = c.group_id INNER JOIN buckwarole AS d ON d.role_id =  c.role_id"
				+ " WHERE a.user_id = ? " ;
		
		List<GrantedAuthority> grantedRoleList = jdbcTemplate.query(sql, new Object[] { userID 
		}, new RowMapper<GrantedAuthority>() {

			@Override
			public GrantedAuthority mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new SimpleGrantedAuthority(rs.getString("role_name"));
			}
		});
		
		return grantedRoleList;
	}

}