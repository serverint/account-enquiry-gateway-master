package com.wasp.gateway.enquiry.account.service

import com.paymentcomponents.common.log.RequestLogger
import com.paymentcomponents.common.models.Bank
import com.paymentcomponents.common.request.AccountEnquiryRequest
import com.paymentcomponents.common.request.subobjects.AccountInformation
import com.paymentcomponents.common.request.subobjects.PersonalInformation
import com.paymentcomponents.common.response.AccountEnquiryResponse
import com.wasp.gateway.enquiry.account.ERROR_CODES
import com.wasp.gateway.enquiry.account.exceptions.WaspApiValidationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

/**
 * Created by aodunlami on 5/19/17.
 */
@Service
class AccountEnquiryService {

    static final protected RequestLogger logger = new RequestLogger(getClass().getName());

    private RestTemplate restTemplate

    @Autowired
    AccountEnquiryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate
    }

    public AccountEnquiryResponse initiateAccountEnquiry(AccountEnquiryRequest accountEnquiryRequest, String context) {

        validationChecks(accountEnquiryRequest)

        Bank instructedBank = getBankByBankCode(accountEnquiryRequest.instructedInstitutionCode)

        ////restTemplate.exchange("http://account-enquiry-service" + context, HttpMethod.POST, new HttpEntity<AccountEnquiryRequest>(accountEnquiryRequest), new ParameterizedTypeReference<ResponseEntity>() {})

        AccountEnquiryResponse response = restTemplate.exchange(instructedBank.waspConnectUrl + context, HttpMethod.POST, new HttpEntity<AccountEnquiryRequest>(accountEnquiryRequest), new ParameterizedTypeReference<AccountEnquiryResponse>() {})

        return response
    }

    public static void validationChecks(AccountEnquiryRequest accountEnquiryRequest) {
        // ---------- value validations ----------
        //System.out.println(">>>>>>>>>Validating Request Acc Number :: ${accountEnquiryRequest.targetAccountNumber}")
        if (!accountEnquiryRequest.targetAccountNumber || accountEnquiryRequest.targetAccountNumber.is("")) {
            throw new WaspApiValidationException(ERROR_CODES.constraint_violation.toString(), "Target account must be specified.")
        }
        //System.out.println(">>>>>>>>Validating Request Institution Code :: ${accountEnquiryRequest.instructedInstitutionCode}")
        if (!accountEnquiryRequest.instructedInstitutionCode || accountEnquiryRequest.instructedInstitutionCode.is("")) {
            throw new WaspApiValidationException(ERROR_CODES.constraint_violation.toString(), "Target Institution must be specified.")
        }
        //System.out.println(">>>>>>>>Done with validation")


        // ---------- logical validations ----------



    }

    private Bank getBankByBankCode(String bankCode) {
        try {
            Bank bank = restTemplate.exchange("http://application-service/banks/search/by-central-bank-code?centralBankCode={bankCode}", HttpMethod.GET, new HttpEntity<Object>(null), new ParameterizedTypeReference<Bank>() {
            }, bankCode)?.body
            if (!bank.waspConnectUrl || bank.waspConnectUrl.isEmpty()) {
                throw new WaspApiValidationException(ERROR_CODES.instructed_bank_url_not_found.toString(), "Instructed bank $bankCode endpoint not found")
            }
            return bank
        } catch (Exception e) {
            throw new WaspApiValidationException(ERROR_CODES.instructed_bank_not_found.toString(), "Instructed bank $bankCode not found")
        }
    }
}
