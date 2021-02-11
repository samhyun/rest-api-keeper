package com.example.restapikeeper.interceptor;

import com.example.restapikeeper.RestApiKeeper;
import com.example.restapikeeper.controller.TodoController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class RestApiKeeperInterceptorTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // 테스트를 위해 제한 시간을 2초, 제한 횟수를 3회로 설정
        RestApiKeeper keeper = new RestApiKeeper(2000, 3);

        mockMvc = MockMvcBuilders.standaloneSetup(TodoController.class)
                .addInterceptors(new RestApiKeeperInterceptor(keeper))
                .build();
    }


    @Test
    void too_many_request_test() throws Exception {
        // 저장되어 있는 요청 내역이 없기 때문에 최초 2번은 정상적으로 동작해야한다.
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/todos")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/todos")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        Thread.sleep(1000);

        // 위에 두개의 요청 내역이 저장되어 있기 때문에 세번째 호출은 정상, 네번째 요청의 상태는 TooManyRequests(429) 여야 한다. .
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/todos")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/todos")
        ).andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isTooManyRequests());

        Thread.sleep(1000);

        // 최초 2회의 요청 후 2초가 지난 후 이기 때문에 5번째 요청시에는 세번째 요청만 남아 있으므로 정상 처리 되어야한다.
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/todos")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}
