package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ResourceControllerTest extends AbstractControllerTest {

    static final String RESOURCES = "/resources/css/style.css";

    @Test
    void getStyle() throws Exception {
        perform(MockMvcRequestBuilders.get(RESOURCES))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.valueOf("text/css")));
    }
}
