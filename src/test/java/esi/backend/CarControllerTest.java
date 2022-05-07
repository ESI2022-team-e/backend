package esi.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import esi.backend.model.Car;
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
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(roles = "MANAGER")
    public void getAllCarsManagerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/cars"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void getAllCarsCustomerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/cars"))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(roles = "MANAGER")
    public void getCarManagerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void getCarCustomerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1"))
                .andDo(print())
                .andExpect(status().isOk());
    }



    @Test
    @WithMockUser(roles = "MANAGER")
    public void postCarManagerTest() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new Car())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void postCarCustomerTest() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new Car())))
                .andDo(print())
                .andExpect(status().isForbidden());
    }


    @Test
    @WithUserDetails("manager1")
    public void putCarManagerTest() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new Car())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer1")
    public void putCarCustomerTest() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new Car())))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

/*
    @Test
    @WithUserDetails("manager1")
    public void deleteCarManagerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

 */

    @Test
    @WithUserDetails("customer1")
    public void deleteCarCustomerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

}

