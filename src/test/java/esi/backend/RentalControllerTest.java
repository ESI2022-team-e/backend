package esi.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import esi.backend.model.Rental;
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
public class RentalControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(roles = "MANAGER")
    public void getAllRentalsManagerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/rentals"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void getAllRentalsCustomerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/rentals"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void getRentalManagerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1/rentals/a81bc81b-dead-6e5d-ad75-90865d1e13b1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer1")
    public void getRentalCustomerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1/rentals/a81bc81b-dead-6e5d-ad75-90865d1e13b1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer2")
    public void getRentalWrongCustomerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1/rentals/a81bc81b-dead-6e5d-ad75-90865d1e13b1"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithUserDetails("manager1")
    public void getAllRentalsByCustomerIdManagerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/customers/1/rentals"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer1")
    public void getAllRentalsByCustomerIdCustomerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/customers/1/rentals"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer2")
    public void getAllRentalsByCustomerIdWrongCustomerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/customers/1/rentals"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("manager1")
    public void getRentalByCustomerIdManagerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/customers/1/rentals/a81bc81b-ffff-6e5d-ad75-90865d1e13b1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer1")
    public void getRentalByCustomerIdCustomerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/customers/1/rentals/a81bc81b-ffff-6e5d-ad75-90865d1e13b1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer2")
    public void getRentalByCustomerIdWrongCustomerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/customers/1/rentals/a81bc81b-ffff-6e5d-ad75-90865d1e13b1"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void postRentalManagerTest() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new Rental())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void postRentalCustomerTest() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new Rental())))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void putRentalManagerTest() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1/rentals/a81bc81b-dead-6e5d-ad75-90865d1e13b1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new Rental())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void putRentalCustomerTest() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1/rentals/a81bc81b-dead-6e5d-ad75-90865d1e13b1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new Rental())))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}

