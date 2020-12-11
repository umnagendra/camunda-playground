package xyz.nagendra.camundaplayground;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    @Before(value = "execution(public void org.camunda.bpm.engine.delegate.JavaDelegate+.execute(org.camunda.bpm.engine.delegate.DelegateExecution)) && args(execution)")
    public void before(JoinPoint joinPoint, DelegateExecution execution) {
        MDC.put("trackingId", "tws_34c92414-c306-4d30-bd57-086fd43e412a");
        System.out.println("---- Before method: " + joinPoint.getSignature());
        System.out.println("---- Before method: " + execution.getBusinessKey());

        execution.getVariableNames().forEach(System.out::println);

        // if we badly want to use logger, we can do it
        final Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        logger.info("-------------------- Before method --------------------");
    }

    @After(value = "execution(public void org.camunda.bpm.engine.delegate.JavaDelegate+.execute(org.camunda.bpm.engine.delegate.DelegateExecution))")
    public void after(JoinPoint joinPoint) {
        MDC.remove("trackingId");
        System.out.println("++++ After method: " + joinPoint.getSignature().toShortString());

        // if we badly want to use logger, we can do it
        final Logger logger = LoggerFactory.getLogger(joinPoint.getSourceLocation().getWithinType());
        logger.info("++++++++++++++++++++ After method ++++++++++++++++++++");
    }

    @Pointcut(value = "execution(public void org.camunda.bpm.engine.delegate.JavaDelegate+.execute(org.camunda.bpm.engine.delegate.DelegateExecution)) && args(execution)")
    private void delegateExecuteMethods(DelegateExecution execution) {
        // pointcut definition
    }

    @Around(value = "delegateExecuteMethods(execution)")
    public Object applyTrackingIdAround(ProceedingJoinPoint joinPoint, DelegateExecution execution) throws Throwable {
        // before
        System.out.println(Thread.currentThread().getName() + ": ******** Around method (BEFORE) " + execution.getProcessInstanceId() + " ********");
        MDC.put("trackingId", "tws_34c92414-c306-4d30-bd57-086fd43e412a");
        MDC.put("trackingUUID", "34c92414-c306-4d30-bd57-086fd43e412a");

        try {
            return joinPoint.proceed();
        } finally {
            // after
            // before
            System.out.println(Thread.currentThread().getName() + ": ******** Around method (AFTER) ********");
            MDC.remove("trackingId");
        }
    }
}
