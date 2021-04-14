package vip.xiaonuo.sample;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;


/**
 * 基础测试类
 *
 * @author yubaoshan
 * @date 2017/5/21 16:10
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SnowySampleApp.class)
@WebAppConfiguration
//@Transactional(rollbackFor = Exception.class) //打开的话测试之后数据可自动回滚
public class BaseJunit {

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Before
    public void initDatabase() {
    }


}
