package com.lisz.dao;

import com.lisz.bean.Dog;

public interface DogDao {
	//@ResultMap("myDog")
	public Dog findById(Integer id);
}
