package com.lisz.dao;

import com.lisz.bean.User;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserDao {
	// 可以写@Select/@Insert等注解，但是一旦xml文件里也有相同id（xml中id为这里的方法名）的设置，则会报错
	public Integer save(User user);
	public Integer update(User user);
	public Integer deleteById(Integer id);
	public User findById(Integer id);
	public Map<Object, Object> findById2(Integer id);
	public List<User> selectAll();
	@MapKey("id")
	public Map<String, User> selectAll2();
	public List<User> selectUsersByScore(User user);
	public List<User> selectUsersByScoreAndId1(@Param("id") Integer id, @Param("score") Double score);
	public List<User> selectUsersByScoreAndId2(Integer id, Double score);
	public List<User> selectUsersByScoreAndId3(Map<String, Object> map);

	public User getUserByIdWithDogs(@Param("id") Integer id);
}
