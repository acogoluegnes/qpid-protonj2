/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.qpid.proton4j.engine.test.matchers;

import org.apache.qpid.proton4j.amqp.Symbol;
import org.apache.qpid.proton4j.amqp.UnsignedLong;
import org.apache.qpid.proton4j.engine.test.peer.FrameType;
import org.apache.qpid.proton4j.engine.test.peer.FrameWithNoPayloadMatchingHandler;
import org.hamcrest.Matcher;

public class DispositionMatcher extends FrameWithNoPayloadMatchingHandler {

    /**
     * Note that the ordinals of the Field enums match the order specified in
     * the AMQP spec
     */
    public enum Field {
        ROLE, FIRST, LAST, SETTLED, STATE, BATCHABLE,
    }

    public DispositionMatcher() {
        super(FrameType.AMQP, ANY_CHANNEL, UnsignedLong.valueOf(0x0000000000000015L), Symbol.valueOf("amqp:disposition:list"));
    }

    @Override
    public DispositionMatcher onCompletion(Runnable onCompletion) {
        super.onCompletion(onCompletion);
        return this;
    }

    public DispositionMatcher withRole(Matcher<?> m) {
        getMatchers().put(Field.ROLE, m);
        return this;
    }

    public DispositionMatcher withFirst(Matcher<?> m) {
        getMatchers().put(Field.FIRST, m);
        return this;
    }

    public DispositionMatcher withLast(Matcher<?> m) {
        getMatchers().put(Field.LAST, m);
        return this;
    }

    public DispositionMatcher withSettled(Matcher<?> m) {
        getMatchers().put(Field.SETTLED, m);
        return this;
    }

    public DispositionMatcher withState(Matcher<?> m) {
        getMatchers().put(Field.STATE, m);
        return this;
    }

    public DispositionMatcher withBatchable(Matcher<?> m) {
        getMatchers().put(Field.BATCHABLE, m);
        return this;
    }

    public Object getReceivedRole() {
        return getReceivedFields().get(Field.ROLE);
    }

    public Object getReceivedFirst() {
        return getReceivedFields().get(Field.FIRST);
    }

    public Object getReceivedLast() {
        return getReceivedFields().get(Field.LAST);
    }

    public Object getReceivedSettled() {
        return getReceivedFields().get(Field.SETTLED);
    }

    public Object getReceivedState() {
        return getReceivedFields().get(Field.STATE);
    }

    public Object getReceivedBatchable() {
        return getReceivedFields().get(Field.BATCHABLE);
    }

    @Override
    protected Enum<?> getField(int fieldIndex) {
        return Field.values()[fieldIndex];
    }
}
