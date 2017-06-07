package com.wasp.gateway.enquiry.account.controller

import com.paymentcomponents.common.request.AccountEnquiryRequest
import com.paymentcomponents.common.response.AccountEnquiryResponse
import com.wasp.gateway.enquiry.account.service.AccountEnquiryService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.http.HttpStatus

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by aodunlami on 5/19/17.
 */
@RestController
@RequestMapping("/v1/account/accountenquiry")
class AccountEnquiryController {

    private RestTemplate restTemplate
    private AccountEnquiryService accountEnquiryService

    @Autowired
    AccountEnquiryController(RestTemplate restTemplate, AccountEnquiryService accountEnquiryService) {
        this.restTemplate = restTemplate
        this.accountEnquiryService = accountEnquiryService
    }


    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "initiateAccountEnquiry", notes = "Account enquiry initiation")
    @ApiResponses([
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    ])
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody AccountEnquiryResponse initiateAccountEnquiry(
            @RequestBody AccountEnquiryRequest accountEnquiryRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return accountEnquiryService.initiateAccountEnquiry(accountEnquiryRequest, httpServletRequest.servletPath)
    }

}
