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
package org.apache.qpid.proton4j.amqp.driver.matchers.messaging;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;

import org.apache.qpid.proton4j.amqp.driver.codec.messaging.Target;
import org.apache.qpid.proton4j.amqp.driver.matchers.ListDescribedTypeMatcher;

public class TargetMatcher extends ListDescribedTypeMatcher {

    public TargetMatcher() {
        super(Target.Field.values().length, Target.DESCRIPTOR_CODE, Target.DESCRIPTOR_SYMBOL);
    }

    public TargetMatcher(org.apache.qpid.proton4j.amqp.messaging.Target target) {
        super(Target.Field.values().length, Target.DESCRIPTOR_CODE, Target.DESCRIPTOR_SYMBOL);

        addSourceMatchers(target);
    }

    @Override
    protected Class<?> getDescribedTypeClass() {
        return Target.class;
    }

    private void addSourceMatchers(org.apache.qpid.proton4j.amqp.messaging.Target target) {
        if (target.getAddress() != null) {
            addFieldMatcher(Target.Field.ADDRESS, equalTo(target.getAddress()));
        } else {
            addFieldMatcher(Target.Field.ADDRESS, nullValue());
        }

        if (target.getDurable() != null) {
            addFieldMatcher(Target.Field.DURABLE, equalTo(target.getDurable().getValue()));
        } else {
            addFieldMatcher(Target.Field.DURABLE, nullValue());
        }

        if (target.getExpiryPolicy() != null) {
            addFieldMatcher(Target.Field.EXPIRY_POLICY, equalTo(target.getExpiryPolicy().getPolicy()));
        } else {
            addFieldMatcher(Target.Field.EXPIRY_POLICY, nullValue());
        }

        if (target.getTimeout() != null) {
            addFieldMatcher(Target.Field.TIMEOUT, equalTo(target.getTimeout()));
        } else {
            addFieldMatcher(Target.Field.TIMEOUT, nullValue());
        }

        addFieldMatcher(Target.Field.DYNAMIC, equalTo(target.getDynamic()));

        if (target.getDynamicNodeProperties() != null) {
            addFieldMatcher(Target.Field.DYNAMIC_NODE_PROPERTIES, equalTo(target.getDynamicNodeProperties()));
        } else {
            addFieldMatcher(Target.Field.DYNAMIC_NODE_PROPERTIES, nullValue());
        }

        if (target.getCapabilities() != null) {
            addFieldMatcher(Target.Field.CAPABILITIES, equalTo(target.getCapabilities()));
        } else {
            addFieldMatcher(Target.Field.CAPABILITIES, nullValue());
        }
    }
}
