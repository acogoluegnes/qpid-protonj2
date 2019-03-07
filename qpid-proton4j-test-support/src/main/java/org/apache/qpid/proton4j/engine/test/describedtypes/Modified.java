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
package org.apache.qpid.proton4j.engine.test.describedtypes;

import org.apache.qpid.proton4j.amqp.DescribedType;
import org.apache.qpid.proton4j.amqp.Symbol;
import org.apache.qpid.proton4j.amqp.UnsignedLong;
import org.apache.qpid.proton4j.engine.test.peer.ListDescribedType;

public class Modified extends ListDescribedType {

    public static final Symbol DESCRIPTOR_SYMBOL = Symbol.valueOf("amqp:modified:list");
    public static final UnsignedLong DESCRIPTOR_CODE = UnsignedLong.valueOf(0x0000000000000027L);

    private static final int FIELD_DELIVERY_FAILED = 0;
    private static final int FIELD_UNDELIVERABLE_HERE = 1;
    private static final int FIELD_MESSAGE_ANNOTATIONS = 2;

    public Modified(Object... fields) {
        super(3);
        int i = 0;
        for (Object field : fields) {
            getFields()[i++] = field;
        }
    }

    @Override
    public Symbol getDescriptor() {
        return DESCRIPTOR_SYMBOL;
    }

    public Modified setDeliveryFailed(Object o) {
        getFields()[FIELD_DELIVERY_FAILED] = o;
        return this;
    }

    public Modified setUndeliverableHere(Object o) {
        getFields()[FIELD_UNDELIVERABLE_HERE] = o;
        return this;
    }

    public Modified setMessageAnnotations(Object o) {
        getFields()[FIELD_MESSAGE_ANNOTATIONS] = o;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof DescribedType)) {
            return false;
        }

        DescribedType d = (DescribedType) obj;
        if (!(DESCRIPTOR_CODE.equals(d.getDescriptor()) || DESCRIPTOR_SYMBOL.equals(d.getDescriptor()))) {
            return false;
        }

        Object described = getDescribed();
        Object described2 = d.getDescribed();
        if (described == null) {
            return described2 == null;
        } else {
            return described.equals(described2);
        }
    }

    @Override
    public int hashCode() {
        // This is a hack, but we aren't going to hash lots of these test
        // objects.
        return 1;
    }
}
