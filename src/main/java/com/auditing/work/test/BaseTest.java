package com.auditing.work.test;

import com.auditing.work.enums.ResultCodeEnum;
import com.auditing.work.result.BaseResult;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/spring-service.xml","classpath:/META-INF/spring/mybatis.xml" })
public class BaseTest {

    protected void buildSuccessResult(BaseResult result){
        result.setSuccess(true);
        result.setResultCode(ResultCodeEnum.SUCCESS.getCode());
        result.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
    }
}
