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
package org.messaginghub.amqperative.futures;

import java.util.concurrent.Future;

import org.messaginghub.amqperative.impl.ClientException;

/**
 * Factory for client future instances that will create specific versions based on
 * configuration.
 */
public abstract class ClientFutureFactory {

    public static final String PROVIDER_FUTURE_TYPE_KEY = "futureType";

    private static final String OS_NAME = System.getProperty("os.name");
    private static final String WINDOWS_OS_PREFIX = "Windows";
    private static final boolean IS_WINDOWS = isOsNameMatch(OS_NAME, WINDOWS_OS_PREFIX);

    private static final String CONSERVATIVE = "conservative";
    private static final String BALANCED = "balanced";
    private static final String PROGRESSIVE = "progressive";

    /**
     * Create a new ClientFutureFactory instance based on the given type name.
     *
     * @param futureType
     * 		the future type whose factory should be returned.
     *
     * @return a new {@link ClientFutureFactory} that will be used to create the desired future types.
     */
    public static ClientFutureFactory create(final String futureType) {
        if (futureType == null || futureType.isEmpty()) {
            if (Runtime.getRuntime().availableProcessors() < 4) {
                return new ConservativeProviderFutureFactory();
            } else if (isWindows()) {
                return new BalancedProviderFutureFactory();
            } else {
                return new ProgressiveProviderFutureFactory();
            }
        }

        switch (futureType.toLowerCase()) {
            case CONSERVATIVE:
                return new ConservativeProviderFutureFactory();
            case BALANCED:
                return new BalancedProviderFutureFactory();
            case PROGRESSIVE:
                return new ProgressiveProviderFutureFactory();
            default:
                throw new IllegalArgumentException(
                    "No ClientFuture implementation with name " + futureType + " found");
        }
    }

    public static <T> Future<T> completedFuture(T result) {
        BalancedClientFuture<T> future = new BalancedClientFuture<T>();
        future.complete(result);

        return future;
    }

    /**
     * @return a new ClientFuture instance.
     */
    public abstract <V> ClientFuture<V> createFuture();

    /**
     * @param synchronization
     * 		The {@link ClientSynchronization} to assign to the returned {@link ClientFuture}.
     *
     * @return a new ClientFuture instance.
     */
    public abstract <V> ClientFuture<V> createFuture(ClientSynchronization synchronization);

    /**
     * @return a ClientFuture that treats failures as success calls that simply complete the operation.
     */
    public abstract <V> ClientFuture<V> createUnfailableFuture();

    //----- Internal support methods -----------------------------------------//

    private static boolean isWindows() {
        return IS_WINDOWS;
    }

    private static boolean isOsNameMatch(final String currentOSName, final String osNamePrefix) {
        if (currentOSName == null || currentOSName.isEmpty()) {
            return false;
        }

        return currentOSName.startsWith(osNamePrefix);
    }

    //----- ClientFutureFactory implementation -----------------------------//

    private static class ConservativeProviderFutureFactory extends ClientFutureFactory {

        @Override
        public <V> ClientFuture<V> createFuture() {
            return new ConservativeClientFuture<V>();
        }

        @Override
        public <V> ClientFuture<V> createFuture(ClientSynchronization synchronization) {
            return new ConservativeClientFuture<V>(synchronization);
        }

        @Override
        public <V> ClientFuture<V> createUnfailableFuture() {
            return new ConservativeClientFuture<V>() {

                @Override
                public void failed(ClientException t) {
                    this.complete(null);
                }
            };
        }
    }

    private static class BalancedProviderFutureFactory extends ClientFutureFactory {

        @Override
        public <V> ClientFuture<V> createFuture() {
            return new BalancedClientFuture<V>();
        }

        @Override
        public <V> ClientFuture<V> createFuture(ClientSynchronization synchronization) {
            return new BalancedClientFuture<V>(synchronization);
        }

        @Override
        public <V> ClientFuture<V> createUnfailableFuture() {
            return new BalancedClientFuture<V>() {

                @Override
                public void failed(ClientException t) {
                    this.complete(null);
                }
            };
        }
    }

    private static class ProgressiveProviderFutureFactory extends ClientFutureFactory {

        @Override
        public <V> ClientFuture<V> createFuture() {
            return new ProgressiveClientFuture<V>();
        }

        @Override
        public <V> ClientFuture<V> createFuture(ClientSynchronization synchronization) {
            return new ProgressiveClientFuture<V>(synchronization);
        }

        @Override
        public <V> ClientFuture<V> createUnfailableFuture() {
            return new ProgressiveClientFuture<V>() {

                @Override
                public void failed(ClientException t) {
                    this.complete(null);
                }
            };
        }
    }
}
