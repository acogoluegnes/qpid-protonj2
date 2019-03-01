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
package org.apache.qpid.proton4j.engine.test.basictypes;

import org.apache.qpid.proton4j.amqp.Symbol;

public class TxnCapability {

    public static final Symbol LOCAL_TRANSACTIONS = Symbol.valueOf("amqp:local-transactions");
    public static final Symbol DISTRIBUTED_TRANSACTIONS = Symbol.valueOf("amqp:distributed-transactions");
    public static final Symbol PROMOTABLE_TRANSACTIONS = Symbol.valueOf("amqp:promotable-transactions");
    public static final Symbol MULTI_TXNS_PER_SSN = Symbol.valueOf("amqp:multi-txns-per-ssn");
    public static final Symbol MULTI_SSNS_PER_TXN = Symbol.valueOf("amqp:multi-ssns-per-txn");

    private TxnCapability() {
        // No instances
    }
}
