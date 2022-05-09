package esi.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import esi.backend.model.Invoice;
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
public class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(roles = "MANAGER")
    public void getAllInvoicesManagerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/invoices"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void getAllInvoicesCustomerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/invoices"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void getInvoiceManagerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/invoices/dd06ca3f-613e-49c2-ae62-ab9d3f455194"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void getInvoiceCustomerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/invoices/dd06ca3f-613e-49c2-ae62-ab9d3f455194"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("manager1")
    public void getInvoiceByRentalIdManagerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/customers/1/rentals/a81bc81b-ffff-6e5d-ad75-90865d1e13b1/invoices/dd06ca3f-614e-49c2-ae62-ab9d3f455194"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer1")
    public void getInvoiceByRentalIdCustomerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/customers/1/rentals/a81bc81b-ffff-6e5d-ad75-90865d1e13b1/invoices/dd06ca3f-614e-49c2-ae62-ab9d3f455194"))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @WithUserDetails("customer1")
    public void getInvoiceByRentalIdWrongCustomerTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/customers/2/rentals/a81bc81b-abcd-6e5d-ad75-90865d1e13b1/invoices/dd06ca3f-613e-49c2-ae62-ab9d3f455194"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser(roles = "MANAGER")
    public void postInvoiceManagerTest() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1/rentals/a81bc81b-dead-6e5d-ad75-90865d1e13b1/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new Invoice())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void postInvoiceCustomerTest() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1/rentals/a82bc31b-dead-6a5d-ad65-90865d1e13b2/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new Invoice())))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("manager1")
    public void putInvoiceManagerTest() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/customers/1/rentals/a81bc81b-ffff-6e5d-ad75-90865d1e13b1/invoices/dd06ca3f-614e-49c2-ae62-ab9d3f455194")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new Invoice())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer1")
    public void putInvoiceCustomerTest() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/customers/1/rentals/a81bc81b-ffff-6e5d-ad75-90865d1e13b1/invoices/dd06ca3f-614e-49c2-ae62-ab9d3f455194")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new Invoice())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("customer1")
    public void putInvoiceWrongCustomerTest() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/customers/2/rentals/a81bc81b-abcd-6e5d-ad75-90865d1e13b1/invoices/dd06ca3f-613e-49c2-ae62-ab9d3f455194")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new Invoice())))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

}

