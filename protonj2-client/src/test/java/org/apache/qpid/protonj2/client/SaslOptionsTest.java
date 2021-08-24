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
package org.apache.qpid.protonj2.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SaslOptionsTest {

    @Test
    void testCreate() {
        SaslOptions options = new SaslOptions();

        assertNotNull(options.allowedMechanisms());
        assertTrue(options.allowedMechanisms().isEmpty());
        assertTrue(options.saslEnabled());
    }

    @Test
    void testCopy() {
        SaslOptions options = new SaslOptions();

        options.addAllowedMechanism("PLAIN");
        options.addAllowedMechanism("ANONYMOUS");
        options.saslEnabled(false);

        SaslOptions copy = options.clone();

        assertEquals(options.allowedMechanisms(), copy.allowedMechanisms());
        assertEquals(options.saslEnabled(), copy.saslEnabled());
    }

    @Test
    void testAllowedOptions() {
        SaslOptions options = new SaslOptions();

        assertNotNull(options.allowedMechanisms());
        assertTrue(options.allowedMechanisms().isEmpty());

        options.addAllowedMechanism("PLAIN");
        options.addAllowedMechanism("ANONYMOUS");

        assertFalse(options.allowedMechanisms().isEmpty());

        assertTrue(options.allowedMechanisms().contains("PLAIN"));
        assertTrue(options.allowedMechanisms().contains("ANONYMOUS"));
    }
}
