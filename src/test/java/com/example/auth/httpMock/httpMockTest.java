package com.example.auth.httpMock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.security.config.BeanIds;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class httpMockTest {

    private MockMvc mockMvc;


    @Autowired
    private WebApplicationContext webApplicationContext;

    /**
     * Spring Security Filter를 적용한다.
     * @param context
     * @return
     * @throws Exception
     */
    public static MockMvc getSecurityAppliedMockMvc(WebApplicationContext context) throws Exception {
        DelegatingFilterProxy delegateProxyFilter = new DelegatingFilterProxy();
        MockFilterConfig secFilterConfig = new MockFilterConfig(context.getServletContext(),
                BeanIds.SPRING_SECURITY_FILTER_CHAIN);
        delegateProxyFilter.init(secFilterConfig);

        return MockMvcBuilders.webAppContextSetup(context).addFilter(delegateProxyFilter).build();
    }

    @Before
    public void setUp() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc = getSecurityAppliedMockMvc(webApplicationContext);
    }

    @Test
    public void testGetHeaderWithJwt() throws Exception {

        mockMvc.perform(post("/api/authenticate")
                .param("username", "kndoll")
                .param("password", "1"))
                .andDo(print())
                .andExpect(header().exists("Authorization"));
    }

    @Test
    public void testPublicHello() throws Exception {

        mockMvc.perform(get("/api/public"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void testPrivateHello() throws Exception {

        // 토큰을 가져온다.
        ResultActions resultActions =  mockMvc.perform(post("/api/authenticate")
                .param("username", "kndoll")
                .param("password", "1"));

        String token = resultActions.andReturn().getResponse().getHeader("Authorization").toString();

        mockMvc.perform(get("/api/private")
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
