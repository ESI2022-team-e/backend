package esi.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpRequestAuthTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    // These tests actually work, and I didn't have heart to delete them
    // but as they use a real server to test, and I figured out
    // how to test with a less expensive mockup server
    // I commented out the @Test annotations so the tests won't be run.


    // -------------------------------------------------------
    // getAll<..> tests

    //@Test
    public void getAllCarsDoesNotRequireAuthenticationTest() {
        assertThat(this.restTemplate
                .getForObject(
                        "http://localhost:" + port + "/api/cars",
                        List.class))
                .isNotNull();
    }

    //@Test
    public void getAllRequestsRequiresAuthenticationTest() {
        assertThat(this.restTemplate
                .getForObject(
                        "http://localhost:" + port + "/api/requests",
                        String.class))
                .contains("Full authentication is required to access this resource");
    }

    //@Test
    public void getAllRequestsByCarIdRequiresAuthenticationTest() {
        assertThat(this.restTemplate
                .getForObject(
                        "http://localhost:" + port + "/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1/requests",
                        String.class))
                .contains("Full authentication is required to access this resource");
    }

    //@Test
    public void getAllRentalsRequiresAuthenticationTest() {
        assertThat(this.restTemplate
                .getForObject(
                        "http://localhost:" + port + "/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1/rentals",
                        String.class))
                .contains("Full authentication is required to access this resource");
    }

    //@Test
    public void getAllRentalsByCarIdRequiresAuthenticationTest() {
        assertThat(this.restTemplate
                .getForObject(
                        "http://localhost:" + port + "/api/rentals",
                        String.class))
                .contains("Full authentication is required to access this resource");
    }

    //@Test
    public void getAllInvoicesRequiresAuthenticationTest() {
        assertThat(this.restTemplate
                .getForObject(
                        "http://localhost:" + port + "/api/invoices",
                        String.class))
                .contains("Full authentication is required to access this resource");
    }

    // -------------------------------------------------------
    // get<...> tests

    //@Test
    public void getCarDoesNotRequireAuthenticationTest() {
        assertThat(this.restTemplate
                .getForObject(
                        "http://localhost:" + port + "/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1",
                        String.class))
                .contains("\"model\":\"Focus 1.6 Aut\"");
    }

    //@Test
    public void getRequestRequiresAuthenticationTest() {
        assertThat(this.restTemplate
                .getForObject(
                        "http://localhost:" + port + "/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1/requests/a82bc31b-dead-6a5d-ad65-90865d1e13b2",
                        String.class))
                .contains("Full authentication is required to access this resource");
    }

    //@Test
    public void getRentalRequiresAuthenticationTest() {
        assertThat(this.restTemplate
                .getForObject(
                        "http://localhost:" + port + "/api/cars/a81bc81b-dead-4e5d-abff-90865d1e13b1/rentals/a81bc81b-abcd-6e5d-ad75-90865d1e13b1",
                        String.class))
                .contains("Full authentication is required to access this resource");
    }

    //@Test
    public void getInvoiceRequiresAuthenticationTest() {
        assertThat(this.restTemplate
                .getForObject(
                        "http://localhost:" + port + "/api/invoices/dd06ca3f-613e-49c2-ae62-ab9d3f455194",
                        String.class))
                .contains("Full authentication is required to access this resource");
    }


}
