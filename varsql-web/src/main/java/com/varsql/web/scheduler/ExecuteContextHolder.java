package com.varsql.web.scheduler;

import com.varsql.core.common.constants.ExecuteType;

public final class ExecuteContextHolder {

    private static final ThreadLocal<ExecuteContext> CONTEXT = new ThreadLocal<>();

    private ExecuteContextHolder() {}

    public static void set(ExecuteType type, String jobName) {
        CONTEXT.set(new ExecuteContext(type, jobName));
    }

    public static boolean isQuartzThread() {
        ExecuteContext ctx = CONTEXT.get();
        return ctx != null;
    }

    public static ExecuteContext getContext() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}