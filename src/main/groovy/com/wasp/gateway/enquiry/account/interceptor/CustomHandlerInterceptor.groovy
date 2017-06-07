package com.wasp.gateway.enquiry.account.interceptor

import com.wasp.gateway.enquiry.account.kafka.interfaces.LogsChannel
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

import com.paymentcomponents.common.log.RequestLogger
import com.paymentcomponents.common.models.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by aodunlami on 5/19/17.
 */
//@Component
class CustomHandlerInterceptor implements HandlerInterceptor{
    final protected RequestLogger logger = new RequestLogger(getClass().getName());
    //before the actual handler will be executed
    //@Autowired
    LogsChannel logsChannel

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        logger.info("Incoming Request ", httpServletRequest)
        if (httpServletResponse.getStatus() == 200) {
            return true;
        } else {
            addLog(httpServletRequest, httpServletResponse);
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if (httpServletResponse.getStatus() != 200 && httpServletResponse.getStatus() != 201) {
            addLog(httpServletRequest, httpServletResponse);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        addLog(httpServletRequest, httpServletResponse);
    }

    private void addLog(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            Log log = constructLog(httpServletRequest, httpServletResponse);
            logger.info("Write log to Kafka ", httpServletRequest, log)
            //logsChannel.output().send(MessageBuilder.withPayload(log).build())
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static Log constructLog(HttpServletRequest request, HttpServletResponse response) {
        Log log = new Log();
        //log.requestPayload = request.getReader()
        log.setRemoteAddress(request.getHeader("X-FORWARDED-FOR"));
        if (log.getRemoteAddress() == null) {
            log.setRemoteAddress(request.getRemoteAddr());
        }
        log.setPath(request.getServletPath());
        log.setMethod(request.getMethod().trim());
        log.setHttpStatusCode(String.valueOf(response.getStatus()));
//        log.setResponseErrorCode(request.getAttribute(Constants.DATAMATION_RESPONSE_ERROR_CODE) != null ? request.getAttribute(Constants.DATAMATION_RESPONSE_ERROR_CODE).toString() : null);
//        request.removeAttribute(Constants.DATAMATION_RESPONSE_ERROR_CODE);
//        log.setRequestDate((Date) request.getAttribute("startTime"));
        log.setRequestDate(new Date());
        log.setRequestEndDate(new Date());
        log.setDuration((log.getRequestEndDate().getTime() - log.getRequestDate().getTime()) / 1000.0000);
        log.setRequestId(UUID.randomUUID().toString().substring(0, 30)); // TODO REMOVE LATER
        log.setBankId("aaa") // TODO REMOVE LATER
        return log;
    }

}
