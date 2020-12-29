import com.lisz.bean.Dog;
import com.lisz.bean.User;
import com.lisz.dao.DogDao;
import com.lisz.dao.UserDao;
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

public class MyTest2 {
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
			DogDao dao = session.getMapper(DogDao.class);
			Dog dog = dao.findById(1);
			System.out.println(dog);
			System.out.println(dog.getUser());
		}
	}

	@Test
	public void testGetUserByIdWithDogs() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession()) {
			UserDao dao = session.getMapper(UserDao.class);
			User user = dao.getUserByIdWithDogs(1);
			System.out.println(user);
			System.out.println(user.getDogs());
		}
	}
}
