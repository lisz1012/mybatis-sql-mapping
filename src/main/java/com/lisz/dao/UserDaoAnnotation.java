package com.lisz.dao;

import com.lisz.bean.User;
import org.apache.ibatis.annotations.Select;


public interface UserDaoAnnotation {
	@Select("select * from user where id = #{id}")
	public User findById(Integer id);
}
