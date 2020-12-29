import com.lisz.bean.User;
import com.lisz.dao.UserDao;
import com.lisz.dao.UserDaoAnnotation;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTest {
	private static final String RESOURCE = "mybatis-config.xml";
	private InputStream inputStream;
	private SqlSessionFactory sqlSessionFactory;

	@Before
	public void setup() {
		try {
			inputStream = Resources.getResourceAsStream(RESOURCE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
	}

	@Test
	public void testSelect() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession()) {
			//UserDaoAnnotation dao = session.getMapper(UserDaoAnnotation.class);
			UserDao dao = session.getMapper(UserDao.class);
			User user = dao.findById(1);
			System.out.println(user);
			// 动态代理类：org.apache.ibatis.binding.MapperProxy@5178056b
			//System.out.println(dao);
		}
	}

	@Test
	public void testSelect2() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession()) {
			//UserDaoAnnotation dao = session.getMapper(UserDaoAnnotation.class);
			UserDao dao = session.getMapper(UserDao.class);
			Map<Object, Object> map = dao.findById2(1);
			System.out.println(map);
			// 动态代理类：org.apache.ibatis.binding.MapperProxy@5178056b
			System.out.println(dao);
		}
	}

	@Test
	public void testSelectAll() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession()) {
			UserDao dao = session.getMapper(UserDao.class);
			List<User> users = dao.selectAll();
			System.out.println(users);
		}
	}

	@Test
	public void testSelectAll2() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession()) {
			UserDao dao = session.getMapper(UserDao.class);
			Map<String, User> map = dao.selectAll2();
			System.out.println(map);
		}
	}

	@Test
	public void testSelectUsersByScore() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession()) {
			UserDao dao = session.getMapper(UserDao.class);
			User user = new User();
			user.setScore(110.00);
			List<User> users = dao.selectUsersByScore(user);
			System.out.println(users);
		}
	}

	@Test
	public void testSelectUsersByScoreAndId1() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession()) {
			UserDao dao = session.getMapper(UserDao.class);
			List<User> users = dao.selectUsersByScoreAndId1(1, 110.00);
			System.out.println(users);
		}
	}

	@Test
	public void testSelectUsersByScoreAndId2() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession()) {
			UserDao dao = session.getMapper(UserDao.class);
			List<User> users = dao.selectUsersByScoreAndId2(1, 110.00);
			System.out.println(users);
		}
	}

	@Test
	public void testSelectUsersByScoreAndId3() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession()) {
			UserDao dao = session.getMapper(UserDao.class);
			Map<String, Object> map = new HashMap<>();
			map.put("id", 1);
			map.put("score", 110.00);
			List<User> users = dao.selectUsersByScoreAndId3(map);
			System.out.println(users);
		}
	}

	@Test
	public void testSave() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession()) {// sqlSessionFactory.openSession(true) 自动提交
			UserDao dao = session.getMapper(UserDao.class);
			User user = new User();
			user.setName("yijing");
			user.setJob("SDE");
			user.setBirthDate(new Date("1991/09/15"));
			user.setEmail("yijing@gmail.com");
			user.setScore(100.00);
			user.setCreatedAt(new Date("2020/12/13"));
			user.setModifiedAt(new Date("2020/12/13"));
			Integer res = dao.save(user);
			session.commit();
			System.out.println("Result: " + res);
			System.out.println(user);
		}
	}

	@Test
	public void testUpdate() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserDao dao = session.getMapper(UserDao.class);
			User user = new User();
			user.setId(1);
			user.setScore(120.00);
			dao.update(user);
			System.out.println(user);
		}
	}

	@Test
	public void testDelete() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			UserDao dao = session.getMapper(UserDao.class);
			Integer delete = dao.deleteById(9);
			System.out.println(delete);
		}
	}
}
