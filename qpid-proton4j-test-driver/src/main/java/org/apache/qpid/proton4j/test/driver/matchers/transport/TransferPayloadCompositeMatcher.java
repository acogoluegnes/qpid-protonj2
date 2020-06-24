/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package org.apache.qpid.proton4j.test.driver.matchers.transport;

import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.qpid.proton4j.test.driver.codec.primitives.Binary;
import org.apache.qpid.proton4j.test.driver.matchers.messaging.ApplicationPropertiesMatcher;
import org.apache.qpid.proton4j.test.driver.matchers.messaging.HeaderMatcher;
import org.apache.qpid.proton4j.test.driver.matchers.messaging.MessageAnnotationsMatcher;
import org.apache.qpid.proton4j.test.driver.matchers.messaging.PropertiesMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;

import io.netty.buffer.ByteBuf;

/**
 * Used to verify the Transfer frame payload, i.e the sections of the AMQP
 * message such as the header, properties, and body sections.
 */
public class TransferPayloadCompositeMatcher extends TypeSafeMatcher<ByteBuf> {

    private HeaderMatcher msgHeadersMatcher;
    private String msgHeaderMatcherFailureDescription;
    private MessageAnnotationsMatcher msgAnnotationsMatcher;
    private String msgAnnotationsMatcherFailureDescription;
    private PropertiesMatcher propsMatcher;
    private String propsMatcherFailureDescription;
    private Matcher<Binary> msgContentMatcher;
    private String msgContentMatcherFailureDescription;
    private ApplicationPropertiesMatcher appPropsMatcher;
    private String appPropsMatcherFailureDescription;
    private Matcher<Integer> payloadLengthMatcher;
    private String payloadLenthMatcherFailureDescription;

    public TransferPayloadCompositeMatcher() {
    }

    @Override
    protected boolean matchesSafely(final ByteBuf receivedBinary) {
        int origLength = receivedBinary.readableBytes();
        int bytesConsumed = 0;

        // Length Matcher
        if (payloadLengthMatcher != null) {
            try {
                assertThat("Payload length should match", origLength, payloadLengthMatcher);
            } catch (Throwable t) {
                payloadLenthMatcherFailureDescription = "\nPayload Lenfth Matcher generated throwable: " + t;

                return false;
            }
        }

        // MessageHeader Section
        if (msgHeadersMatcher != null) {
            ByteBuf msgHeaderEtcSubBinary = receivedBinary.slice(bytesConsumed, origLength - bytesConsumed);
            try {
                bytesConsumed += msgHeadersMatcher.verify(msgHeaderEtcSubBinary);
            } catch (Throwable t) {
                msgHeaderMatcherFailureDescription = "\nActual encoded form of remaining bytes passed to MessageHeaderMatcher: " + msgHeaderEtcSubBinary;
                msgHeaderMatcherFailureDescription += "\nMessageHeaderMatcher generated throwable: " + t;

                return false;
            }
        }

        // MessageAnnotations Section
        if (msgAnnotationsMatcher != null) {
            ByteBuf msgAnnotationsEtcSubBinary = receivedBinary.slice(bytesConsumed, origLength - bytesConsumed);
            try {
                bytesConsumed += msgAnnotationsMatcher.verify(msgAnnotationsEtcSubBinary);
            } catch (Throwable t) {
                msgAnnotationsMatcherFailureDescription = "\nActual encoded form of remaining bytes passed to MessageAnnotationsMatcher: "
                    + msgAnnotationsEtcSubBinary;
                msgAnnotationsMatcherFailureDescription += "\nMessageAnnotationsMatcher generated throwable: " + t;

                return false;
            }
        }

        // Properties Section
        if (propsMatcher != null) {
            ByteBuf propsEtcSubBinary = receivedBinary.slice(bytesConsumed, origLength - bytesConsumed);
            try {
                bytesConsumed += propsMatcher.verify(propsEtcSubBinary);
            } catch (Throwable t) {
                propsMatcherFailureDescription = "\nActual encoded form of remaining bytes passed to PropertiesMatcher: " + propsEtcSubBinary;
                propsMatcherFailureDescription += "\nPropertiesMatcher generated throwable: " + t;

                return false;
            }
        }

        // Application Properties Section
        if (appPropsMatcher != null) {
            ByteBuf appPropsEtcSubBinary = receivedBinary.slice(bytesConsumed, origLength - bytesConsumed);
            try {
                bytesConsumed += appPropsMatcher.verify(appPropsEtcSubBinary);
            } catch (Throwable t) {
                appPropsMatcherFailureDescription = "\nActual encoded form of remaining bytes passed to ApplicationPropertiesMatcher: " + appPropsEtcSubBinary;
                appPropsMatcherFailureDescription += "\nApplicationPropertiesMatcher generated throwable: " + t;

                return false;
            }
        }

        // Message Content Body Section, already a Matcher<Binary>
        if (msgContentMatcher != null) {
            ByteBuf msgContentBodyEtcSubBinary = receivedBinary.slice(bytesConsumed, origLength - bytesConsumed);
            boolean contentMatches = msgContentMatcher.matches(msgContentBodyEtcSubBinary);
            if (!contentMatches) {
                Description desc = new StringDescription();
                msgContentMatcher.describeTo(desc);
                msgContentMatcher.describeMismatch(msgContentBodyEtcSubBinary, desc);

                msgContentMatcherFailureDescription = "\nMessageContentMatcher mismatch Description:";
                msgContentMatcherFailureDescription += desc.toString();

                return false;
            }
        }

        // TODO: we will need figure out a way to determine how many bytes the
        // MessageContentMatcher did/should consume when it comes time to handle
        // footers
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a Binary encoding of a Transfer frames payload, containing an AMQP message");
    }

    @Override
    protected void describeMismatchSafely(ByteBuf item, Description mismatchDescription) {
        mismatchDescription.appendText("\nActual encoded form of the full Transfer frame payload: ").appendValue(item);

        // Payload Length
        if (payloadLenthMatcherFailureDescription != null) {
            mismatchDescription.appendText("\nPayloadLengthMatcherFailed!");
            mismatchDescription.appendText(payloadLenthMatcherFailureDescription);
            return;
        }

        // MessageHeaders Section
        if (msgHeaderMatcherFailureDescription != null) {
            mismatchDescription.appendText("\nMessageHeadersMatcherFailed!");
            mismatchDescription.appendText(msgHeaderMatcherFailureDescription);
            return;
        }

        // MessageAnnotations Section
        if (msgAnnotationsMatcherFailureDescription != null) {
            mismatchDescription.appendText("\nMessageAnnotationsMatcherFailed!");
            mismatchDescription.appendText(msgAnnotationsMatcherFailureDescription);
            return;
        }

        // Properties Section
        if (propsMatcherFailureDescription != null) {
            mismatchDescription.appendText("\nPropertiesMatcherFailed!");
            mismatchDescription.appendText(propsMatcherFailureDescription);
            return;
        }

        // Application Properties Section
        if (appPropsMatcherFailureDescription != null) {
            mismatchDescription.appendText("\nApplicationPropertiesMatcherFailed!");
            mismatchDescription.appendText(appPropsMatcherFailureDescription);
            return;
        }

        // Message Content Body Section
        if (msgContentMatcherFailureDescription != null) {
            mismatchDescription.appendText("\nContentMatcherFailed!");
            mismatchDescription.appendText(msgContentMatcherFailureDescription);
            return;
        }
    }

    public void setHeadersMatcher(HeaderMatcher msgHeadersMatcher) {
        this.msgHeadersMatcher = msgHeadersMatcher;
    }

    public void setMessageAnnotationsMatcher(MessageAnnotationsMatcher msgAnnotationsMatcher) {
        this.msgAnnotationsMatcher = msgAnnotationsMatcher;
    }

    public void setPropertiesMatcher(PropertiesMatcher propsMatcher) {
        this.propsMatcher = propsMatcher;
    }

    public void setApplicationPropertiesMatcher(ApplicationPropertiesMatcher appPropsMatcher) {
        this.appPropsMatcher = appPropsMatcher;
    }

    public void setMessageContentMatcher(Matcher<Binary> msgContentMatcher) {
        this.msgContentMatcher = msgContentMatcher;
    }

    public void setPayloadLengthMatcher(Matcher<Integer> payloadLengthMatcher) {
        this.payloadLengthMatcher = payloadLengthMatcher;
    }
}