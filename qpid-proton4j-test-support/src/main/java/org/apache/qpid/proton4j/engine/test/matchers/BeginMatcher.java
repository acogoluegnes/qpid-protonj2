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

public class BeginMatcher extends FrameWithNoPayloadMatchingHandler {

    /**
     * Note that the ordinals of the Field enums match the order specified in
     * the AMQP spec
     */
    public enum Field {
        REMOTE_CHANNEL, NEXT_OUTGOING_ID, INCOMING_WINDOW, OUTGOING_WINDOW, HANDLE_MAX, OFFERED_CAPABILITIES, DESIRED_CAPABILITIES, PROPERTIES,
    }

    public BeginMatcher() {
        super(FrameType.AMQP, ANY_CHANNEL, UnsignedLong.valueOf(0x0000000000000011L), Symbol.valueOf("amqp:begin:list"));
    }

    @Override
    public BeginMatcher onCompletion(Runnable onCompletion) {
        super.onCompletion(onCompletion);
        return this;
    }

    public BeginMatcher withRemoteChannel(Matcher<?> m) {
        getMatchers().put(Field.REMOTE_CHANNEL, m);
        return this;
    }

    public BeginMatcher withNextOutgoingId(Matcher<?> m) {
        getMatchers().put(Field.NEXT_OUTGOING_ID, m);
        return this;
    }

    public BeginMatcher withIncomingWindow(Matcher<?> m) {
        getMatchers().put(Field.INCOMING_WINDOW, m);
        return this;
    }

    public BeginMatcher withOutgoingWindow(Matcher<?> m) {
        getMatchers().put(Field.OUTGOING_WINDOW, m);
        return this;
    }

    public BeginMatcher withHandleMax(Matcher<?> m) {
        getMatchers().put(Field.HANDLE_MAX, m);
        return this;
    }

    public BeginMatcher withOfferedCapabilities(Matcher<?> m) {
        getMatchers().put(Field.OFFERED_CAPABILITIES, m);
        return this;
    }

    public BeginMatcher withDesiredCapabilities(Matcher<?> m) {
        getMatchers().put(Field.DESIRED_CAPABILITIES, m);
        return this;
    }

    public BeginMatcher withProperties(Matcher<?> m) {
        getMatchers().put(Field.PROPERTIES, m);
        return this;
    }

    public Object getReceivedRemoteChannel() {
        return getReceivedFields().get(Field.REMOTE_CHANNEL);
    }

    public Object getReceivedNextOutgoingId() {
        return getReceivedFields().get(Field.NEXT_OUTGOING_ID);
    }

    public Object getReceivedIncomingWindow() {
        return getReceivedFields().get(Field.INCOMING_WINDOW);
    }

    public Object getReceivedOutgoingWindow() {
        return getReceivedFields().get(Field.OUTGOING_WINDOW);
    }

    public Object getReceivedHandleMax() {
        return getReceivedFields().get(Field.HANDLE_MAX);
    }

    public Object getReceivedOfferedCapabilities() {
        return getReceivedFields().get(Field.OFFERED_CAPABILITIES);
    }

    public Object getReceivedDesiredCapabilities() {
        return getReceivedFields().get(Field.DESIRED_CAPABILITIES);
    }

    public Object getReceivedProperties() {
        return getReceivedFields().get(Field.PROPERTIES);
    }

    @Override
    protected Enum<?> getField(int fieldIndex) {
        return Field.values()[fieldIndex];
    }
}
