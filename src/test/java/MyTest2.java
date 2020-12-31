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
import java.util.*;

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

	@Test
	public void testFindDogByIdTwoSteps() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession()) {
			DogDao dao = session.getMapper(DogDao.class);
			Dog dog = dao.findByIdTwoSteps(1);
			System.out.println(dog);
		}
	}

	@Test
	public void testFindUserByIdTwoSteps() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession()) {
			UserDao dao = session.getMapper(UserDao.class);
			User user = dao.getUserByIdWithDogsTwoSteps(1);
			//System.out.println(user.getEmail());
			//System.out.println(user);
			//System.out.println(user.getDogs());
		}
	}

	@Test
	public void testGetUserByCondition() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession()) {
			UserDao dao = session.getMapper(UserDao.class);
			User user = new User();
			user.setId(1);
			user.setEmail("lisz@gmail.com");
//			user.setName("lisz");
			user = dao.getUserByCondition(user);
			//System.out.println(user.getEmail());
			System.out.println(user);
			//System.out.println(user.getDogs());
		}
	}

	@Test
	public void testFindDogsByUserIds() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession()) {
			DogDao dao = session.getMapper(DogDao.class);
			List<Integer> ids = new ArrayList<>();
			ids.add(1);
			ids.add(2);
			ids.add(8);
			List<Dog> dogs = dao.findDogsByUserIds(ids);
			System.out.println(dogs);
		}
	}

	// 测试一级缓存，只会发出一条SQL语句
	@Test
	public void testSqlSessionCache() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession()) {
			DogDao dao = session.getMapper(DogDao.class);
			Dog dog = dao.findById(1);
			System.out.println(dog);
			dog = dao.findById(1);
			System.out.println(dog);
		}
	}

	// 同一个方法开启了两个Session 不同的session互相独立，会发出两条SQL语句。注意，这里即使开了二级缓存，也会发出第二条SQL，这是因为
	// 二级缓存在Session关闭之后才生效
	@Test
	public void testSqlSessionCache2() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession();
				SqlSession session2 = sqlSessionFactory.openSession()) {
			DogDao dao = session.getMapper(DogDao.class);
			Dog dog = dao.findById(1);
			System.out.println(dog);
			DogDao dao2 = session2.getMapper(DogDao.class);
			Dog dog2 = dao2.findById(1);
			System.out.println(dog2);
		}
	}

	// 引用不变，但是里面的值变了，也不会用缓存，而是再发一条SQL
	@Test
	public void testSqlSessionCache3() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession()) {
			UserDao dao = session.getMapper(UserDao.class);
			User user = new User();
			user.setId(1);
			User user1 = dao.getUserByCondition(user);
			System.out.println(user1);
			user.setId(8);
			User user2 = dao.getUserByCondition(user);
			System.out.println(user2);
		}
	}

	// 中间有update修改了数据，也不会走缓存，会发另一条SQL查询语句,不管有没有commit
	@Test
	public void testSqlSessionCache4() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession()) {
			DogDao dao = session.getMapper(DogDao.class);
			Dog dog = dao.findById(1);
			System.out.println(dog);
			dog.setAge(3);
			Integer update = dao.updateWithDog(dog);
			//session.commit();
			System.out.println(update);
			dog = dao.findById(1);
			System.out.println(dog);
		}
	}

	// Session清除缓存了，当然会再发一条SQL
	@Test
	public void testSqlSessionCache5() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession()) {
			DogDao dao = session.getMapper(DogDao.class);
			Dog dog = dao.findById(1);
			System.out.println(dog);
			session.clearCache();
			dog = dao.findById(1);
			System.out.println(dog);
		}
	}

	// 同一条数据在另一个Session中被update了，也会再发一条，不管是否commit了
	@Test
	public void testSqlSessionCache6() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession();
			 SqlSession session2 = sqlSessionFactory.openSession()) {
			DogDao dao = session.getMapper(DogDao.class);
			Dog dog = dao.findById(1);
			System.out.println(dog);

			DogDao dao2 = session2.getMapper(DogDao.class);
			Dog dog2 = dao2.findById(1);
			dog2.setAge(4);
			Integer update = dao.updateWithDog(dog2);
			System.out.println(update);

			dog = dao.findById(1);
			System.out.println(dog);
		}
	}

	// 同一条数据在MySQL命令行中被手动update了，MyBatis并不知道这件事，所以仍然会从缓存中取值
	@Test
	public void testSqlSessionCache7() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession()) {
			DogDao dao = session.getMapper(DogDao.class);
			Dog dog = dao.findById(1);
			System.out.println(dog);

			Thread.sleep(10000); //此时手动在MySQL 命令行修改dog中id为1的记录

			dog = dao.findById(1);
			System.out.println(dog);
		}
	}

	// 二级缓存。注意User和Dog等bean对象要实现Serializable接口
	@Test
	public void testSqlSessionCache8() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession()) {
			DogDao dao = session.getMapper(DogDao.class);
			Dog dog = dao.findById(1);
			System.out.println(dog);
		}
		// 第二次就走缓存了，打印：Cache Hit Ratio [com.lisz.dao.DogDao]: 0.5
		try (SqlSession session2 = sqlSessionFactory.openSession()) {
			DogDao dao = session2.getMapper(DogDao.class);
			Dog dog = dao.findById(1);
			System.out.println(dog);
		}
	}

	// 二级缓存。先查询二级缓存，再查询一级缓存
	@Test
	public void testSqlSessionCache9() throws Exception{
		try (SqlSession session = sqlSessionFactory.openSession()) {
			DogDao dao = session.getMapper(DogDao.class);
			Dog dog = dao.findById(1);
			System.out.println(dog);
		}
		// 第二次就走缓存了，打印：Cache Hit Ratio [com.lisz.dao.DogDao]: 0.5
		try (SqlSession session2 = sqlSessionFactory.openSession()) {
			DogDao dao = session2.getMapper(DogDao.class);
			Dog dog = dao.findById(1);
			Dog dog2 = dao.findById(1);
			Dog dog3 = dao.findById(2);
			System.out.println(dog);
		}
		/*命中率只有二级缓存被启用的时候才会打印
		DEBUG [Test worker] - Cache Hit Ratio [com.lisz.dao.DogDao]: 0.5
		DEBUG [Test worker] - Cache Hit Ratio [com.lisz.dao.DogDao]: 0.6666666666666666
		DEBUG [Test worker] - Cache Hit Ratio [com.lisz.dao.DogDao]: 0.5
		 */
	}
}
