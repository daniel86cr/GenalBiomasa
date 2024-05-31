package com.dina;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.dina.genasoft.GenasoftApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GenasoftApplication.class)
@WebAppConfiguration
public class GenasoftApplicationTests {

    @Test
    public void contextLoads() {
    }

}
