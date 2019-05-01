package com.github.viktorpenelski.springkafkamongodemo;

import com.github.viktorpenelski.springkafkamongodemo.jsonstore.web.JsonstoreController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringKafkaMongoDemoApplicationTests {

	@Autowired
	private JsonstoreController controller;

	@Test
	public void contextLoads() {
		Assert.notNull(controller, "JsonstoreController should be initialized!");
	}

}
