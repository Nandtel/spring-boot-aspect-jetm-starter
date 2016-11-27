package com.example.timer;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import etm.core.monitor.EtmPoint;


/**
 * AOP interceptor for timeService
 *
 * @Created 4/3/2016
 */
@Aspect
@Component
public class TimerInterceptor {

    @Autowired
    TimerService timerService;

    @Around("@annotation(com.example.timer.TimeDelta)")
    public Object logAction(ProceedingJoinPoint pjp) throws Throwable {

        Signature signature = pjp.getSignature();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(signature.getDeclaringTypeName());
        stringBuilder.append(".");
        stringBuilder.append(signature.getName());
        EtmPoint etmPoint = timerService.createPoint(stringBuilder.toString());

        Object proceed = null;
        try {
            proceed = pjp.proceed();
        } finally {
            timerService.submit(etmPoint);
        }

        return proceed;
    }

}
