package com.chumakoff.mealvoting.testsupport.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public abstract class ApiControllerTest {
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Autowired
    private MockMvc mockMvc;

    protected ResultActions perform(RequestBuilder builder) throws Exception {
        return mockMvc.perform(builder).andDo(print());
    }

    protected ResultActions performGetRequest(String url) throws Exception {
        return perform(getRequest(url));
    }

    protected MockHttpServletRequestBuilder getRequest(String url) {
        return MockMvcRequestBuilders.get(url);
    }

    protected MockHttpServletRequestBuilder postRequest(String url) {
        return acceptsAndRespondsWithJSON(MockMvcRequestBuilders.post(url));
    }

    protected MockHttpServletRequestBuilder patchRequest(String url) {
        return acceptsAndRespondsWithJSON(MockMvcRequestBuilders.patch(url));
    }

    protected MockHttpServletRequestBuilder putRequest(String url) {
        return acceptsAndRespondsWithJSON(MockMvcRequestBuilders.put(url));
    }

    protected MockHttpServletRequestBuilder deleteRequest(String url) {
        return MockMvcRequestBuilders.delete(url);
    }

    public String buildJSON(Object object) throws Exception {
        return mapper.writeValueAsString(object);
    }

    protected <T> T parseJsonResponse(ResultActions result, Class<T> klass) throws Exception {
        return mapper.readValue(getResponseJSON(result), klass);
    }

    protected <T> List<T> parseJsonResponseAsList(ResultActions result, Class<T> klass) throws Exception {
        return mapper.readerForListOf(klass).readValue(getResponseJSON(result));
    }

    private String getResponseJSON(ResultActions result) throws UnsupportedEncodingException {
        return result.andReturn().getResponse().getContentAsString();
    }

    private MockHttpServletRequestBuilder acceptsAndRespondsWithJSON(MockHttpServletRequestBuilder request) {
        return request.contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }
}
