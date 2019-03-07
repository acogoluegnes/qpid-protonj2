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

public class OpenMatcher extends FrameWithNoPayloadMatchingHandler {

    /**
     * Note that the ordinals of the Field enums match the order specified in
     * the AMQP spec
     */
    public enum Field {
        CONTAINER_ID, HOSTNAME, MAX_FRAME_SIZE, CHANNEL_MAX, IDLE_TIME_OUT, OUTGOING_LOCALES, INCOMING_LOCALES, OFFERED_CAPABILITIES, DESIRED_CAPABILITIES, PROPERTIES,
    }

    public OpenMatcher() {
        super(FrameType.AMQP, ANY_CHANNEL, UnsignedLong.valueOf(0x0000000000000010L), Symbol.valueOf("amqp:open:list"));
    }

    @Override
    public OpenMatcher onCompletion(Runnable onCompletion) {
        super.onCompletion(onCompletion);
        return this;
    }

    public OpenMatcher withContainerId(Matcher<?> m) {
        getMatchers().put(Field.CONTAINER_ID, m);
        return this;
    }

    public OpenMatcher withHostname(Matcher<?> m) {
        getMatchers().put(Field.HOSTNAME, m);
        return this;
    }

    public OpenMatcher withMaxFrameSize(Matcher<?> m) {
        getMatchers().put(Field.MAX_FRAME_SIZE, m);
        return this;
    }

    public OpenMatcher withChannelMax(Matcher<?> m) {
        getMatchers().put(Field.CHANNEL_MAX, m);
        return this;
    }

    public OpenMatcher withIdleTimeOut(Matcher<?> m) {
        getMatchers().put(Field.IDLE_TIME_OUT, m);
        return this;
    }

    public OpenMatcher withOutgoingLocales(Matcher<?> m) {
        getMatchers().put(Field.OUTGOING_LOCALES, m);
        return this;
    }

    public OpenMatcher withIncomingLocales(Matcher<?> m) {
        getMatchers().put(Field.INCOMING_LOCALES, m);
        return this;
    }

    public OpenMatcher withOfferedCapabilities(Matcher<?> m) {
        getMatchers().put(Field.OFFERED_CAPABILITIES, m);
        return this;
    }

    public OpenMatcher withDesiredCapabilities(Matcher<?> m) {
        getMatchers().put(Field.DESIRED_CAPABILITIES, m);
        return this;
    }

    public OpenMatcher withProperties(Matcher<?> m) {
        getMatchers().put(Field.PROPERTIES, m);
        return this;
    }

    public Object getReceivedContainerId() {
        return getReceivedFields().get(Field.CONTAINER_ID);
    }

    public Object getReceivedHostname() {
        return getReceivedFields().get(Field.HOSTNAME);
    }

    public Object getReceivedMaxFrameSize() {
        return getReceivedFields().get(Field.MAX_FRAME_SIZE);
    }

    public Object getReceivedChannelMax() {
        return getReceivedFields().get(Field.CHANNEL_MAX);
    }

    public Object getReceivedIdleTimeOut() {
        return getReceivedFields().get(Field.IDLE_TIME_OUT);
    }

    public Object getReceivedOutgoingLocales() {
        return getReceivedFields().get(Field.OUTGOING_LOCALES);
    }

    public Object getReceivedIncomingLocales() {
        return getReceivedFields().get(Field.INCOMING_LOCALES);
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
