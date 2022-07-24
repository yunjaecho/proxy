package hello.proxy.config.v3_proxfactory.advice;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class LogTraceAdvice implements MethodInterceptor {

    private final LogTrace logTrace;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TraceStatus status = null;

        try {
            Method method = invocation.getMethod();

            String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";

            status = logTrace.begin(message);

            Object result = invocation.proceed();

            logTrace.end(status);
            return result;
        } catch(Exception ex) {
            logTrace.exception(status, ex);
            throw ex;
        }
    }
}
