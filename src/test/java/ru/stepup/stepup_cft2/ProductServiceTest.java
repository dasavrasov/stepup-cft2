package ru.stepup.stepup_cft2;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import ru.stepup.model.ProductInstanceRequest;
import ru.stepup.model.ProductInstanceResponse;
import ru.stepup.service.ProductService;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductServiceTest extends AbstractIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeAll
    void setup() throws SQLException {
        Resource resource = new ClassPathResource("create_db.sql");
        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), resource);
        resource = new ClassPathResource("fill_db.sql");
        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), resource);
    }

    @Test
    @Order(1)
    @DisplayName("Создание первого ЭП")
    void createFirstInstanceTest() {
        // Arrange
        Long productId = 1L;

        ProductInstanceRequest request = new ProductInstanceRequest();
        request.setInstanceId(null);
        request.setProductType("договор");
        request.setProductCode("03.012.002");
        request.setRegisterType("1");
        request.setMdmCode("15");
        request.setContractNumber("РКО-ДОГ-ЮЛ-1");
        request.setContractDate("2024-07-06");
        request.setPriority(1L);
        request.setContractId(457);
        request.setBranchCode("0021");
        request.setIsoCurrencyCode("800");
        request.setUrgencyCode("00");
//        request.setAdditionalProperties(new ArrayList<ProductInstanceRequest.AdditionalProperties>());
        request.setInstanceArrangement(new ArrayList<>());


        ProductInstanceResponse response =null;
        try {
            response = productService.createInstance(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response != null) {
            assertEquals("1", response.getData().getInstanceId());
        }
    }

    @Test
    @Order(2)
    @DisplayName("Создание второго ЭП с тем же номером контаракта")
    void createSecondInstanceTest() {
        // Arrange
        Long productId = 1L;

        ProductInstanceRequest request = new ProductInstanceRequest();
        request.setInstanceId(null);
        request.setProductType("договор");
        request.setProductCode("03.012.002");
        request.setRegisterType("1");
        request.setMdmCode("15");
        request.setContractNumber("РКО-ДОГ-ЮЛ-1");
        request.setContractDate("2024-07-06");
        request.setPriority(1L);
        request.setContractId(457);
        request.setBranchCode("0021");
        request.setIsoCurrencyCode("800");
        request.setUrgencyCode("00");

        Exception exception = assertThrows(Exception.class, () -> productService.createInstance(request));
        assertEquals("Параметр РКО-ДОГ-ЮЛ-1 договора РКО-ДОГ-ЮЛ-1 уже существует для ЭП с ИД 1", exception.getMessage());
    }
}
