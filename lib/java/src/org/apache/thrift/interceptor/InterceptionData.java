package org.apache.thrift.interceptor;

import org.apache.thrift.ProcessFunction;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;

public class InterceptionData<I> {
    private I iface;
    private ProcessFunction fn;
    private long startExecutionTime;
    private TException exception;

    private InterceptionData() {
    }

    public long getStartExecutionTime() {
        return startExecutionTime;
    }

    public TException getException() {
        return exception;
    }

    public String getMethodName() throws TInterceptionException {
        if (fn == null) {
            throw new TInterceptionException("ProcessFunction is empty");
        }

        return fn.getMethodName();
    }

    public TBase getArgsInstance() throws TInterceptionException {
        if (fn == null) {
            throw new TInterceptionException("ProcessFunction is empty");
        }

        return fn.getEmptyArgsInstance();
    }

    public TBase getResult() throws TInterceptionException {
        if (iface == null) {
            throw new TInterceptionException("Interface is empty");
        }

        if (fn == null) {
            throw new TInterceptionException("ProcessFunction is empty");
        }

        try {
            return fn.getResult(iface, fn.getEmptyArgsInstance());
        } catch (TException e) {
            throw new TInterceptionException(e.getMessage());
        }
    }


    public static InterceptionData.Builder newBuilder() {
        return new InterceptionData<>().new Builder();
    }

    public class Builder {

        private Builder() {
        }

        public Builder iface(I iFace) {
            InterceptionData.this.iface = iFace;

            return this;
        }

        public Builder processFunction(ProcessFunction fn) {
            InterceptionData.this.fn = fn;

            return this;
        }

        public Builder startExecutionTime(long ts) {
            InterceptionData.this.startExecutionTime = ts;

            return this;
        }

        public Builder exception(TException e) {
            InterceptionData.this.exception = e;

            return this;
        }

        public InterceptionData build() {
            return InterceptionData.this;
        }
    }
}
