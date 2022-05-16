package esi.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import esi.backend.model.Request;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(roles = "MANAGER")
    public void getAllRequestsManagerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/requests"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void getAllRequestsCustomerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/requests"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("manager1")
    public void getAllRequestsByCarIdManagerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1/requests"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer1")
    public void getAllRequestsByCarIdCustomerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1/requests"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("manager1")
    public void getRequestByCarIdManagerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1/requests/a82bc31b-dead-6a5d-ad65-90865d1e13b2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer1")
    public void getRequestByCarIdCustomerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1/requests/a82bc31b-dead-6a5d-ad65-90865d1e13b2"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(roles = "MANAGER")
    public void postRequestManagerTest() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new Request())))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("customer1")
    public void postRequestCustomerTest() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new Request())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("manager1")
    public void putRequestManagerTest() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1/requests/a82bc31b-dead-6a5d-ad65-90865d1e13b2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new Request())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer1")
    public void putRequestCustomerTest() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1/requests/a82bc31b-dead-6a5d-ad65-90865d1e13b2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new Request())))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("customer2")
    public void putRequestWrongCustomerTest() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1/requests/a82bc31b-dead-6a5d-ad65-90865d1e13b2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new Request())))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("manager1")
    public void getRequestsByCustomerIdManagerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/customers/1/requests"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer1")
    public void getRequestsByCustomerIdCustomerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/customers/1/requests"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer1")
    public void getRequestByCustomerIdWrongCustomerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/customers/2/requests"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

}

