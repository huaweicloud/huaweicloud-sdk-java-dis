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

package io.github.dis.core;

import org.apache.http.annotation.Contract;

import java.util.EnumMap;

/**
 * Client request options such as client markers for individual
 * {@link WebServiceRequest}s.
 */
@Contract()
public final class RequestClientOptions {
    /**
     * Default maximum buffer size: 128K + 1. Used to enable mark-and-reset for
     * non-mark-and-resettable non-file input stream for up to 128K memory
     * buffering by default. Add 1 to get around an implementation quirk of
     * BufferedInputStream.
     */
    public static final int DEFAULT_STREAM_BUFFER_SIZE = (1 << 17)+1;
    public enum Marker {
        /** 
         * Used to specify the http user_agent value.
         */
        USER_AGENT,
        ;
    }

    private final EnumMap<Marker,String> markers = new EnumMap<Marker,String>(Marker.class);
    /**
     * Used for mark-and-reset purposes during retry.
     */
    private int readLimit = DEFAULT_STREAM_BUFFER_SIZE;
    
    /**
     * @deprecated by {@link #getClientMarker(Marker)}.
     *
     * Returns the "USER_AGENT" marker as a space-delimited string.
     * 
     * @return The "User_AGENT" marker
     */
    @Deprecated
    public String getClientMarker() {
        return getClientMarker(Marker.USER_AGENT);
    }

	/**
	 * Returns the value of the specified marker; or null if there is no such value.
	 * 
	 * @param marker
	 *            marker
	 * @return The client marker
	 */
    public String getClientMarker(Marker marker) {
        return markers.get(marker);
    }

	/**
	 * Associates the given value with the given marker.
	 * 
	 * @param marker
	 *            marker
	 * @param value
	 *            value associates with the given marker
	 */
    public void putClientMarker(Marker marker, String value) {
        markers.put(marker, value);
    }

	/**
	 * @deprecated by {@link #appendUserAgent(String)}.
	 *
	 *             Adds a "USER_AGENT" client marker, if it wasn't already present.
	 * 
	 * @param clientMarker
	 *            The client marker
	 */
    @Deprecated
    public void addClientMarker(String clientMarker) {
        appendUserAgent(clientMarker);
    }

    /**
     * Appends a user agent to the USER_AGENT client marker.
     * 
     * @param userAgent user agent
     */
    public void appendUserAgent(String userAgent) {
        String marker = markers.get(Marker.USER_AGENT);
        if (marker == null) {
            marker = "";
        }
        marker = createUserAgentMarkerString(marker, userAgent);
        putClientMarker(Marker.USER_AGENT, marker);
    }

    /**
     * Appends the given client marker string to the existing one and returns it.
     */
    private String createUserAgentMarkerString(final String marker, String userAgent) {
        return marker.contains(userAgent) ? marker : marker + " " + userAgent;
    }

    /**
     * Returns the mark-and-reset read limit; defaults to
     * {@value #DEFAULT_STREAM_BUFFER_SIZE}.
     * 
     * @return read limit
     * @see java.io.InputStream#mark(int)
     */
    public final int getReadLimit() {
        return readLimit;
    }

    /**
     * Sets the optional mark-and-reset read limit used for signing and retry
     * purposes.
     *
     * @param readLimit read limit
     * @see java.io.InputStream#mark(int)
     */
    public final void setReadLimit(int readLimit) {
        this.readLimit = readLimit;
    }

    /**
     * Copy the internal states of this <code>RequestClientOptions</code> to the
     * target <code>RequestClientOptions</code>.
     * 
     * @param target target request client options.
     */
    void copyTo(RequestClientOptions target) {
        target.setReadLimit(getReadLimit());
        for (Marker marker: Marker.values())
            target.putClientMarker(marker, getClientMarker(marker));
    }
}