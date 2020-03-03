package com.weiwu.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ServiceLogAspect {

    public Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);


    @Around("execution(* com.weiwu.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint pjp) throws Throwable {

        logger.info("====== 开始执行 ======");
        logger.info("{}：{}",pjp.getTarget().getClass(),pjp.getSignature().getName());

        long beginTime = System.currentTimeMillis();

        Object o = pjp.proceed();

        long endTime = System.currentTimeMillis();

        long takeTime = endTime - beginTime;

        if (takeTime > 3000) {
            logger.error("==== 执行结束，耗时：{} ====",takeTime);
        } else {
            logger.info("==== 执行结束，耗时：{} ====",takeTime);
        }

        return o;
    }
}
