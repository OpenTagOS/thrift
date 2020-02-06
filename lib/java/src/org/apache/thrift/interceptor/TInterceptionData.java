/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.thrift.interceptor;

import org.apache.thrift.ProcessFunction;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;

/**
 * <code>TInterceptionData</code> is a data class which pass information
 * from <code>TInterceptor</code>
 */
public class TInterceptionData<I> {
    private I iface;
    private ProcessFunction fn;
    private TException exception;


    private TInterceptionData() {
    }

    public TException getException() {
        return exception;
    }

    /**
     * Get method name which was called
     *
     * @return
     * @throws TInterceptionException if <code>ProcessFunction</code> is null
     */
    public String getMethodName() throws TInterceptionException {
        if (fn == null) {
            throw new TInterceptionException("ProcessFunction is empty");
        }

        return fn.getMethodName();
    }

    /**
     * Get arguments for a method which where set
     *
     * @return
     * @throws TInterceptionException if <code>ProcessFunction</code> is null
     */
    public TBase getArgsInstance() throws TInterceptionException {
        if (fn == null) {
            throw new TInterceptionException("ProcessFunction is empty");
        }

        return fn.getEmptyArgsInstance();
    }

    /**
     * Get result of a method
     *
     * @return
     * @throws TInterceptionException if <code>ProcessFunction</code> or
     * <code>IFace</code> is null
     */
    public TBase getResult() throws TInterceptionException {
        if (iface == null) {
            throw new TInterceptionException("IFace is not set");
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


    public static TInterceptionData.Builder newBuilder() {
        return new TInterceptionData<>().new Builder();
    }

    public class Builder {

        private Builder() {
        }

        public Builder iface(I iFace) {
            TInterceptionData.this.iface = iFace;

            return this;
        }

        public Builder processFunction(ProcessFunction fn) {
            TInterceptionData.this.fn = fn;

            return this;
        }

        public Builder exception(TException e) {
            TInterceptionData.this.exception = e;

            return this;
        }

        public TInterceptionData build() {
            return TInterceptionData.this;
        }
    }
}
