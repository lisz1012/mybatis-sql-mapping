package com.lisz.dao;

import com.lisz.bean.Dog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DogDao {
	//@ResultMap("myDog")
	public Dog findById(Integer id);
	public Dog findByIdTwoSteps(@Param("id") Integer id);
	public List<Dog> findDogsByUserId(@Param("user_id") Integer userId);
	public List<Dog> findDogsByUserIds(@Param("user_ids") List<Integer> userIds);
	Integer updateWithDog(Dog dog);
}
