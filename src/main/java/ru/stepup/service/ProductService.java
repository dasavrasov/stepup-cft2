package ru.stepup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.stepup.model.*;
import ru.stepup.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductRegisterRepository productRegisterRepository;
    private final AgreementRepository agreementRepository;
    private final ProductClassRepository productClassRepository;
    private final ProductRegisterTypeRepository productRegisterTypeRepository;
    // other repositories

    @Autowired
    public ProductService(ProductRepository productRepository, ProductRegisterRepository productRegisterRepository, AgreementRepository agreementRepository, ProductClassRepository productClassRepository, ProductRegisterTypeRepository productRegisterTypeRepository /*, other repositories */) {
        this.productRepository = productRepository;
        this.productRegisterRepository = productRegisterRepository;
        this.agreementRepository = agreementRepository;
        this.productClassRepository = productClassRepository;
        this.productRegisterTypeRepository = productRegisterTypeRepository;
        // initialize other repositories
    }

    public ProductInstanceResponse createInstance(ProductInstanceRequest request) throws Exception {

        // Parse ProductInstanceRequest and map fields to model classes
        // Create and save entities
        if (request.getInstanceId()==null) {
            //создание ЭП

            //Шап 1.1 проверка tpp_product на дубли
            Product product = productRepository.findByNumber(request.getContractNumber());
            if (product != null) {
                throw new Exception("Параметр " + request.getContractNumber() + " договора " + product.getNumber() + " уже существует для ЭП с ИД " + request.getInstanceId());
            }
            //Шаг 1.2 проверка таблицы ДС (agreement) на дубли
            for (int i = 0; i < request.getInstanceArrangement().size(); i++) {
                Agreement agreement = agreementRepository.findByNumber(request.getInstanceArrangement().get(i).getNumber());
                if (agreement != null) {
                    throw new Exception("Параметр " + i + " Дополнительного соглашения (сделки) " + request.getInstanceArrangement().get(i).getNumber() + " уже существует для ЭП с ИД " + request.getInstanceId());
                }
            }
            //Шаг 1.3 по КодуПродукта найти связанные записи в каталоге Типа регистра
            ProductClass productClass = productClassRepository.findByValue(request.getProductCode());
            List<ProductRegisterType> existingProductRegisterTypes = productRegisterTypeRepository.findByAccountTypeAndProductClass_Value("Клиентский", request.getProductCode());
            if (productClass == null || existingProductRegisterTypes.isEmpty()) {
                throw new Exception("Код продукта " + request.getProductCode() + " не найден в Каталоге продуктов tpp_ref_product_class");
            }
            //Шаг 1.4 добавить строку в таблицу ЭП (product)
            product = new Product();
            product.setType(request.getProductType());
            product.setNumber(request.getContractNumber());
            product.setProductCodeId(productClass.getInternalId().longValue());
            product.setClientId(Long.parseLong(request.getMdmCode()));
            product.setType(existingProductRegisterTypes.get(0).getAccountType());
            product.setNumber(request.getContractNumber());
            product.setPriority(request.getPriority().longValue());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime contractDate = LocalDateTime.parse(request.getContractDate(), formatter);
            product.setDateOfConclusion(contractDate);
            product = productRepository.save(product);
            //Шаг 1.5 добавить строку в таблицу ПР (product_register)
            for (int i = 0; i < existingProductRegisterTypes.size(); i++) {
                ProductRegister productRegister = new ProductRegister();
                productRegister.setProductId(product.getId().longValue());
                productRegister.setProductRegisterType(existingProductRegisterTypes.get(i));
                productRegister.setState("Открыт");
                productRegisterRepository.save(productRegister);
            }
        }
        else {
            //создаени дополнительного соглашения к ЭП
            //Шап 2.1 проверка tpp_product на существование ЭП
            Optional<Product> productOptional = productRepository.findById(request.getInstanceId());
            if (!productOptional.isPresent()) {
                throw new Exception("Экземпляр продукта с ID " + request.getInstanceId() + " не найден");
            }
            Product product = productOptional.get();
            //Шаг 2.2 проверка таблицы ДС (agreement) на дубли
            for (int i = 0; i < request.getInstanceArrangement().size(); i++) {
                Agreement agreement = agreementRepository.findByNumber(request.getInstanceArrangement().get(i).getNumber());
                if (agreement != null) {
                    throw new Exception("Параметр " + i + " Дополнительного соглашения (сделки) " + request.getInstanceArrangement().get(i).getNumber() + " уже существует для ЭП с ИД " + request.getInstanceId());
                }
            }
            //Шаг 2.3 добавить строку в таблицу ДС (agreement)
            for (int i = 0; i < request.getInstanceArrangement().size(); i++) {
                Agreement agreement = new Agreement();
                agreement.setProduct(product);
                agreement.setNumber(request.getInstanceArrangement().get(i).getNumber());
                agreement.setArrangementType(request.getInstanceArrangement().get(i).getArrangementType());
                agreement.setShedulerJobId(request.getInstanceArrangement().get(i).getShedulerJobId().longValue());
                agreement.setOpeningDate(LocalDateTime.parse(request.getInstanceArrangement().get(i).getOpeningDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                agreement.setClosingDate(LocalDateTime.parse(request.getInstanceArrangement().get(i).getClosingDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                agreement.setCancelDate(LocalDateTime.parse(request.getInstanceArrangement().get(i).getCancelDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                agreement.setValidityDuration(request.getInstanceArrangement().get(i).getValidityDuration().longValue());
                agreement.setCancellationReason(request.getInstanceArrangement().get(i).getCancellationReason());
                agreement.setStatus(request.getInstanceArrangement().get(i).getStatus());
                agreement.setInterestCalculationDate(LocalDateTime.parse(request.getInstanceArrangement().get(i).getInterestCalculationDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                agreement.setInterestRate(BigDecimal.valueOf(request.getInstanceArrangement().get(i).getInterestRate()));
                agreement.setCoefficient(BigDecimal.valueOf(request.getInstanceArrangement().get(i).getCoefficient()));
                agreement.setMinimumInterestRate(BigDecimal.valueOf(request.getInstanceArrangement().get(i).getMinimumInterestRate()));
                agreement.setMinimumInterestRateCoefficient(new BigDecimal(request.getInstanceArrangement().get(i).getMinimumInterestRateCoefficient()));

                agreementRepository.save(agreement);
            }
        }

        ProductInstanceResponse response = new ProductInstanceResponse();
        return response;
    }
}