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

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <code>TChainedInterceptor</code> is a <code>TInterceptor</code> allowing
 * to pass an interceptors for logging purposes.
 *
 * <p>To do so, you instantiate <code>TMultiplexedProcessor</code> and then
 * register this instance, as shown in the following example:</p>
 *
 * <blockquote><code>
 *     TMultiplexedProcessor processor = new TMultiplexedProcessor();
 *
 *     processor.registerInterceptor(new TChainedInterceptor(
 *              Arrays.asList(new LogInterceptor(), new JaegerInterceptor()))
 *     )
 *
 *     processor.registerProcessor(
 *         "Calculator",
 *         new Calculator.Processor(new CalculatorHandler()));
 *
 *      ...
 *
 * </code></blockquote>
 */
public class TChainedInterceptor implements TInterceptor {

    final private List<TInterceptor> interceptors;

    public TChainedInterceptor(List<TInterceptor> interceptors) {
        this.interceptors = Collections.unmodifiableList(interceptors);
    }

    @Override
    public void call(TInterceptionData interceptionData) {
        if (Objects.isNull(interceptors)) {
            return;
        }

        for (TInterceptor interceptor : interceptors) {
            interceptor.call(interceptionData);
        }
    }
}
