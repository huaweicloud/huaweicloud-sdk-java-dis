/*
 * Copyright 2002-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.dis.core.auth.signer.internal;

import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;

/**
 * Holds the signing key and the number of days since epoch for the date for
 * which the signing key was generated.
 */
@Contract(threading = ThreadingBehavior.IMMUTABLE)
public final class SignerKey {
    private final long numberOfDaysSinceEpoch;
    private final byte[] signingKey;

    public SignerKey(long numberOfDaysSinceEpoch, byte[] signingKey) {
        if (numberOfDaysSinceEpoch <= 0L) {
            throw new IllegalArgumentException("Not able to cache signing key. Signing date to be cached is invalid");
        }
        if (signingKey == null) {
            throw new IllegalArgumentException("Not able to cache signing key. Signing Key to be cached are null");
        }
        this.numberOfDaysSinceEpoch = numberOfDaysSinceEpoch;
        this.signingKey = signingKey.clone();
    }

    /**
     * Returns the number of days since epoch for the date used for generating
     * signing key.
     */
    public long getNumberOfDaysSinceEpoch() {
        return numberOfDaysSinceEpoch;
    }

    /**
     * Returns a copy of the signing key.
     */
    public byte[] getSigningKey() {
        return signingKey.clone();
    }
}