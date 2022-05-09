package esi.backend;

import esi.backend.controller.CarController;
import esi.backend.controller.InvoiceController;
import esi.backend.controller.RentalController;
import esi.backend.controller.RequestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    CarController carController;

    @Autowired
    RequestController requestController;

    @Autowired
    RentalController rentalController;

    @Autowired
    InvoiceController invoiceController;

    @Test
    void contextLoads() {
        assertThat(carController).isNotNull();
        assertThat(requestController).isNotNull();
        assertThat(rentalController).isNotNull();
        assertThat(invoiceController).isNotNull();
    }

}
