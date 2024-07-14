package ru.stepup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stepup.model.*;
import ru.stepup.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductRegisterRepository productRegisterRepository;
    private final AgreementRepository agreementRepository;
    private final ProductClassRepository productClassRepository;
    private final ProductRegisterTypeRepository productRegisterTypeRepository;
    private final AccountPoolRepository accountPoolRepository;
    private final AccountRepository accountRepository;
    // other repositories

    @Autowired
    private ProductRegisterService productRegisterService;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductRegisterRepository productRegisterRepository, AgreementRepository agreementRepository, ProductClassRepository productClassRepository, ProductRegisterTypeRepository productRegisterTypeRepository, /*, other repositories */AccountPoolRepository accountPoolRepository, AccountRepository accountRepository) {
        this.productRepository = productRepository;
        this.productRegisterRepository = productRegisterRepository;
        this.agreementRepository = agreementRepository;
        this.productClassRepository = productClassRepository;
        this.productRegisterTypeRepository = productRegisterTypeRepository;
        this.accountPoolRepository = accountPoolRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductInstanceResponse createInstance(ProductInstanceRequest request) throws Exception {
        Product product = null;
        List<ProductRegister> productRegisterList = new ArrayList<>();
        List<Agreement> agreementList = new ArrayList<>();
        // Parse ProductInstanceRequest and map fields to model classes
        // Create and save entities
        if (request.getInstanceId()==null) {
            //создание ЭП

            //Шап 1.1 проверка tpp_product на дубли
            product = productRepository.findByNumber(request.getContractNumber());
            if (product != null) {
                throw new Exception("Параметр " + request.getContractNumber() + " договора " + product.getNumber() + " уже существует для ЭП с ИД " + product.getId());
            }
            //Шаг 1.2 проверка таблицы ДС (agreement) на дубли
            for (int i = 0; i < request.getInstanceArrangement().size(); i++) {
                Agreement agreement = agreementRepository.findByNumber(request.getInstanceArrangement().get(i).getNumber());
                if (agreement != null) {
                    throw new Exception("Параметр " + i + " Дополнительного соглашения (сделки) " + request.getInstanceArrangement().get(i).getNumber() + " уже существует для ЭП с ИД " + product.getId());
                }
            }
            //Шаг 1.3 по КодуПродукта найти связанные записи в каталоге Типа регистра
            ProductClass productClass = productClassRepository.findByValue(request.getProductCode());
            List<ProductRegisterType> productRegisterTypes = productRegisterTypeRepository.findByAccountTypeAndProductClass_Value("Клиентский", request.getProductCode());
            if (productClass == null || productRegisterTypes.isEmpty()) {
                throw new Exception("Код продукта " + request.getProductCode() + " не найден в Каталоге продуктов tpp_ref_product_class");
            }
            //Шаг 1.4 добавить строку в таблицу ЭП (product)
            product = new Product();
            product.setType(request.getProductType());
            product.setNumber(request.getContractNumber());
            try {
                product.setProductCodeId(productClass.getInternalId());
            } catch (Exception e) {
                ;
            }
            product.setClientId(Long.parseLong(request.getMdmCode()));
            product.setType(productRegisterTypes.get(0).getAccountType());
            product.setNumber(request.getContractNumber());
            try {
                product.setPriority(request.getPriority());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate contractDate = LocalDate.parse(request.getContractDate(), formatter);
            product.setDateOfConclusion(contractDate);
            product = productRepository.save(product);
            //Шаг 1.5 добавить строку в таблицу ПР (product_register)
            for (int i = 0; i < productRegisterTypes.size(); i++) {
                ProductRegisterType productRegisterType = productRegisterTypes.get(i);
                List<AccountPool> accountPools = accountPoolRepository.findByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegistryTypeCode(request.getBranchCode(), request.getIsoCurrencyCode(), request.getMdmCode(), request.getUrgencyCode(), productRegisterType.getValue());
                //если пул счетов не найден - просто не создаем ПР - в этом случае, можно создать ПР отдельно через вызов account/create
                if (!accountPools.isEmpty()) {
                    //если пул счетов найден - создаем ПР
                    AccountPool accountPool = accountPools.get(0);  // get the first returned record
                    List<Account> accounts = accountRepository.findByAccountPool(accountPool);

                    productRegisterList.add(productRegisterService.createAndSaveProductRegister(product.getId(), productRegisterTypes.get(i), accounts.get(0), request.getIsoCurrencyCode(), State.OPEN, accounts.get(0).getAccountNumber()));
                }
            }
        }
        else {
            //создаени дополнительного соглашения к ЭП
            //Шап 2.1 проверка tpp_product на существование ЭП
            Optional<Product> productOptional = productRepository.findById(request.getInstanceId());
            if (!productOptional.isPresent()) {
                throw new Exception("Экземпляр продукта с ID " + request.getInstanceId() + " не найден");
            }
            product = productOptional.get();
            //Шаг 2.2 проверка таблицы ДС (agreement) на дубли
            for (int i = 0; i < request.getInstanceArrangement().size(); i++) {
                Agreement agreement = agreementRepository.findByNumber(request.getInstanceArrangement().get(i).getNumber());
                if (agreement != null) {
                    throw new Exception("Параметр " + agreement.getNumber() + " Дополнительного соглашения (сделки) " + request.getInstanceArrangement().get(i).getNumber() + " уже существует для ЭП с ИД " + request.getInstanceId());
                }
            }
            //Шаг 2.3 добавить строку в таблицу ДС (agreement)
            for (int i = 0; i < request.getInstanceArrangement().size(); i++) {
                Agreement agreement = new Agreement();
                agreement.setProduct(product);
                agreement.setNumber(request.getInstanceArrangement().get(i).getNumber());
                agreement.setArrangementType(request.getInstanceArrangement().get(i).getArrangementType());
                try {
                    agreement.setShedulerJobId(request.getInstanceArrangement().get(i).getShedulerJobId());
                } catch (Exception e) {
                    ;
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                if (request.getInstanceArrangement().get(i).getOpeningDate() != null) {
                    LocalDate openingDate = null;
                    try {
                        openingDate = LocalDate.parse(request.getInstanceArrangement().get(i).getOpeningDate(), formatter);
                    } catch (DateTimeParseException e) {
                        throw new Exception("Ошибка в формате даты открытия сделки");
                    }
                    agreement.setOpeningDate(openingDate);
                } else {
                    throw new Exception("Параметр Даты открытия сделки не заполнен");
                }
                if (request.getInstanceArrangement().get(i).getClosingDate() != null) {
                    LocalDate closingDate = null;
                    try {
                        closingDate = LocalDate.parse(request.getInstanceArrangement().get(i).getClosingDate(), formatter);
                    } catch (DateTimeParseException e) {
                        throw new Exception("Ошибка в формате даты закрытия сделки");
                    }
                    agreement.setClosingDate(closingDate);
                }
                if (request.getInstanceArrangement().get(i).getCancelDate() != null) {
                    LocalDate cancelDate = null;
                    try {
                        cancelDate = LocalDate.parse(request.getInstanceArrangement().get(i).getCancelDate(), formatter);
                    } catch (DateTimeParseException e) {
                        throw new Exception("Ошибка в формате даты отмены сделки");
                    }
                    agreement.setClosingDate(cancelDate);
                }
                try {
                    agreement.setValidityDuration(request.getInstanceArrangement().get(i).getValidityDuration());
                } catch (Exception e) {
                    ;
                }
                agreement.setCancellationReason(request.getInstanceArrangement().get(i).getCancellationReason());
                agreement.setStatus(request.getInstanceArrangement().get(i).getStatus());

                if (request.getInstanceArrangement().get(i).getInterestCalculationDate() != null) {
                    LocalDate interestCalculationDate = null;
                    try {
                        interestCalculationDate = LocalDate.parse(request.getInstanceArrangement().get(i).getInterestCalculationDate(), formatter);
                    } catch (DateTimeParseException e) {
                        throw new Exception("Ошибка в формате даты расчета процентов");
                    }
                    agreement.setInterestCalculationDate(interestCalculationDate);
                }

                if (request.getInstanceArrangement().get(i).getInterestRate() != null) {
                    agreement.setInterestRate(BigDecimal.valueOf(request.getInstanceArrangement().get(i).getInterestRate()));
                }
                if (request.getInstanceArrangement().get(i).getCoefficient() != null) {
                    agreement.setCoefficient(BigDecimal.valueOf(request.getInstanceArrangement().get(i).getCoefficient()));
                }
                if (request.getInstanceArrangement().get(i).getCoefficientAction() != null) {
                    agreement.setMinimumInterestRate(BigDecimal.valueOf(request.getInstanceArrangement().get(i).getMinimumInterestRate()));
                }
                if (request.getInstanceArrangement().get(i).getMinimumInterestRateCoefficient() != null) {
                    agreement.setMinimumInterestRateCoefficient(new BigDecimal(request.getInstanceArrangement().get(i).getMinimumInterestRateCoefficient()));
                }
                agreementList.add(agreement);
                agreementRepository.save(agreement);
            }
        }

        ProductInstanceResponse response = new ProductInstanceResponse();
        ProductInstanceResponse.Data responseData = new ProductInstanceResponse.Data();
        if (product != null) {
            responseData.setInstanceId(product.getId().toString());
        }
        if (productRegisterList != null && !productRegisterList.isEmpty()) {
            List<String> registerIds = productRegisterList.stream()
                    .map(productRegister -> productRegister.getId().toString())
                    .collect(Collectors.toList());
            responseData.setRegisterId(registerIds);
        }
        if (agreementList != null && !agreementList.isEmpty()) {
            List<String> agreementIds = agreementList.stream()
                    .map(agreement -> agreement.getId().toString())
                    .collect(Collectors.toList());
            responseData.setSupplementaryAgreementId(agreementIds);
        }
        response.setData(responseData);

        return response;
    }
}