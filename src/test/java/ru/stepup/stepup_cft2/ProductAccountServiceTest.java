package ru.stepup.stepup_cft2;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.stepup.model.ProductInstanceRequest;
import ru.stepup.model.ProductInstanceResponse;
import ru.stepup.model.ProductRegistryRequest;
import ru.stepup.model.ProductRegistryResponse;
import ru.stepup.service.AccountService;
import ru.stepup.service.ProductService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductAccountServiceTest extends AbstractIntegrationTest {
    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    static {
        postgres.start();
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgres.getJdbcUrl(),
                    "spring.datasource.username=" + postgres.getUsername(),
                    "spring.datasource.password=" + postgres.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Autowired
    private ProductService productService;

    @Autowired
    private AccountService accountService;

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
        ProductInstanceRequest request=buildProductInstanceRequest("");
        ProductInstanceResponse response = null;
        try {
            response = productService.createInstance(request);
            if (response != null) {
                assertEquals("1", response.getData().getInstanceId());
            }
        } catch (Exception e) {
            Assertions.fail("Ошибка createInstance: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    @DisplayName("Создание второго ЭП с тем же номером контаракта")
    void createSecondInstanceTest() {
        ProductInstanceRequest request=buildProductInstanceRequest("");
        Exception exception = assertThrows(Exception.class, () -> productService.createInstance(request));
        assertEquals("Параметр РКО-ДОГ-ЮЛ-1 договора РКО-ДОГ-ЮЛ-1 уже существует для ЭП с ИД 1", exception.getMessage());
    }

    @Test
    @Order(3)
    @DisplayName("Создание ДС для ЭП")
    void createFirstAgreementTest() {
//        {
//            "instanceId": 1,
//                "productType": "договор",
//                "productCode": "03.012.002",
//                "registerType": "1",
//                "mdmCode": "13",
//                "contractNumber": "РКО-ДОГ-ЮЛ-1",
//                "contractDate": "2024-07-06",
//                "priority": 1,
//                "contractId": 456,
//                "BranchCode": "0021",
//                "IsoCurrencyCode": "500",
//                "urgencyCode": "00",
//                "additionalPropertiesVip": [],
//            "instanceArrangement":[
//            {
//                "Number":"РКО-ДОГ-ЮЛ-1/001",
//                    "openingDate":"2022-01-15"
//            },
//            {
//                "Number":"РКО-ДОГ-ЮЛ-1/002",
//                    "openingDate":"2022-03-20"
//            }
//   ]
//        }
        ProductInstanceRequest request=buildProductInstanceRequest("1");
        ProductInstanceRequest.InstanceArrangement instanceArrangement1 = new ProductInstanceRequest.InstanceArrangement();
        List<ProductInstanceRequest.InstanceArrangement> instanceArrangement= new ArrayList<>();
        instanceArrangement1.setNumber("РКО-ДОГ-ЮЛ-1/001");
        instanceArrangement1.setOpeningDate("2022-01-15");
        instanceArrangement.add(instanceArrangement1);
        ProductInstanceRequest.InstanceArrangement instanceArrangement2 = new ProductInstanceRequest.InstanceArrangement();
        instanceArrangement2.setNumber("РКО-ДОГ-ЮЛ-1/002");
        instanceArrangement2.setOpeningDate("2022-03-20");
        instanceArrangement.add(instanceArrangement2);
        request.setInstanceArrangement(instanceArrangement);
        ProductInstanceResponse response = null;
        try {
            response = productService.createInstance(request);
            if (response != null) {
                assertEquals("1", response.getData().getInstanceId());
                assertEquals(2,response.getData().getSupplementaryAgreementId().size());
                assertEquals("1",response.getData().getSupplementaryAgreementId().get(0));
                assertEquals("2",response.getData().getSupplementaryAgreementId().get(1));
            }
        } catch (Exception e) {
            Assertions.fail("Ошибка createInstance: " + e.getMessage());
        }

    }

    @Test
    @Order(4)
    @DisplayName("Создание второго ДС для ЭП с тем де номером")
    void createSecondAgreementTest() {
//        {
//            "instanceId": 1,
//                "productType": "договор",
//                "productCode": "03.012.002",
//                "registerType": "1",
//                "mdmCode": "13",
//                "contractNumber": "РКО-ДОГ-ЮЛ-1",
//                "contractDate": "2024-07-06",
//                "priority": 1,
//                "contractId": 456,
//                "BranchCode": "0021",
//                "IsoCurrencyCode": "500",
//                "urgencyCode": "00",
//                "additionalPropertiesVip": [],
//            "instanceArrangement":[
//            {
//                "Number":"РКО-ДОГ-ЮЛ-1/001",
//                    "openingDate":"2022-01-15"
//            },
//            {
//                "Number":"РКО-ДОГ-ЮЛ-1/002",
//                    "openingDate":"2022-03-20"
//            }
//   ]
//        }
        ProductInstanceRequest request = buildProductInstanceRequest("1");
        List<ProductInstanceRequest.InstanceArrangement> instanceArrangement = new ArrayList<>();
        ProductInstanceRequest.InstanceArrangement instanceArrangement1 = new ProductInstanceRequest.InstanceArrangement();
        instanceArrangement1.setNumber("РКО-ДОГ-ЮЛ-1/001");
        instanceArrangement1.setOpeningDate("2022-01-15");
        instanceArrangement.add(instanceArrangement1);
        ProductInstanceRequest.InstanceArrangement instanceArrangement2 = new ProductInstanceRequest.InstanceArrangement();
        instanceArrangement2.setNumber("РКО-ДОГ-ЮЛ-1/002");
        instanceArrangement2.setOpeningDate("2022-03-20");
        instanceArrangement.add(instanceArrangement2);
        request.setInstanceArrangement(instanceArrangement);
        ProductInstanceResponse response = null;

        Exception exception = assertThrows(Exception.class, () -> productService.createInstance(request));
        assertEquals("Параметр РКО-ДОГ-ЮЛ-1/001 Дополнительного соглашения (сделки) РКО-ДОГ-ЮЛ-1/001 уже существует для ЭП с ИД 1", exception.getMessage());
    }

    @Test
    @Order(5)
    @DisplayName("Создание ЭП, потом создание ПР для этого ЭП")
    void createInstanceAndAccountTest() {
        ProductInstanceRequest request = buildProductInstanceRequest("");
        request.setContractNumber("РКО-ДОГ-ЮЛ-2");
        ProductInstanceResponse response = null;
        try {
            response = productService.createInstance(request);
            if (response != null) {
                assertEquals("2", response.getData().getInstanceId());
            }
        } catch (Exception e) {
            Assertions.fail("Ошибка ProductService.createInstance: " + e.getMessage());
        }
        //создаем ПР
        if (response == null) {
            Assertions.fail("Ошибка ProductService.createInstance: response is null");
        }
        ProductRegistryRequest accountRequest = buildProductRegistryRequest(response.getData().getInstanceId());
        ProductRegistryResponse accountResponse = null;
        try {
            accountResponse = accountService.createInstance(accountRequest);
        } catch (Exception e) {
            Assertions.fail("Ошибка AccountService.createInstance: "+e.getMessage());
        }
        if (accountResponse != null) {
            assertEquals("1", accountResponse.getData().getAccountId());
        }
    }
    @Test
    @Order(6)
    @DisplayName("Cоздание ПР для ЭП с кодом регистра который уже сществует")
    void createExistingAccountTest() {
        ProductRegistryRequest accountRequest = buildProductRegistryRequest("2");
            ProductRegistryResponse accountResponse = null;
            Exception exception = assertThrows(Exception.class, () -> accountService.createInstance(accountRequest));
            assertEquals("Параметр 03.012.002_47533_ComSoLd тип регистра 03.012.002_47533_ComSoLd уже существует для ЭП с ИД 2", exception.getMessage());
    }

    private ProductInstanceRequest buildProductInstanceRequest(String instanceId) {
        ProductInstanceRequest request = new ProductInstanceRequest();
        try {
            if (instanceId.isEmpty() || instanceId == null)
                request.setInstanceId(null);
            else
                request.setInstanceId(Long.parseLong(instanceId));
        } catch (NumberFormatException e) {
            Assertions.fail("Ошибка парсинга ProductId: " + e.getMessage());
        }
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
        ProductInstanceRequest.AdditionalProperties additionalProperties = new ProductInstanceRequest.AdditionalProperties();
        request.setAdditionalProperties(additionalProperties);
        List<ProductInstanceRequest.InstanceArrangement> instanceArrangement = new ArrayList<>();
        request.setInstanceArrangement(instanceArrangement);
        return request;
    }

    private ProductRegistryRequest buildProductRegistryRequest(String instanceId) {
        ProductRegistryRequest accountRequest = new ProductRegistryRequest();
        try {
            accountRequest.setInstanceId(Long.parseLong(instanceId));
        } catch (NumberFormatException e) {
            Assertions.fail("Ошибка парсинга ProductId: " + e.getMessage());
        }
        accountRequest.setRegistryTypeCode("03.012.002_47533_ComSoLd");
        accountRequest.setAccountType("Клиентский");
        accountRequest.setCurrencyCode("800");
        accountRequest.setBranchCode("0022");
        accountRequest.setPriorityCode("00");
        accountRequest.setMdmCode("15");
        accountRequest.setClientCode(null);
        accountRequest.setTrainRegion(null);
        accountRequest.setCounter(null);
        accountRequest.setSalesCode(null);
        return accountRequest;
    }
}
