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
package org.apache.qpid.proton4j.transport.impl;

import java.io.IOException;
import java.nio.Buffer;

import org.apache.qpid.proton4j.buffer.ProtonBuffer;
import org.apache.qpid.proton4j.buffer.ProtonByteBuffer;
import org.apache.qpid.proton4j.transport.FrameParser;
import org.apache.qpid.proton4j.transport.exceptions.IOExceptionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * State based Frame reader that is used in the NIO based transports where
 * AMQP frames can come in in partial or overlapping forms.
 */
public class AmqpFrameParser implements FrameParser {

    private static final Logger LOG = LoggerFactory.getLogger(AmqpFrameParser.class);

    private static final byte AMQP_FRAME_SIZE_BYTES = 4;
    private static final byte AMQP_HEADER_BYTES = 8;

    private ProtonTransport transport;
    private FrameParser currentParser;

    public AmqpFrameParser(final ProtonTransport transport) {
        this.transport = transport;
    }

    @Override
    public void parse(ProtonBuffer incoming) throws IOException {

        if (incoming == null || !incoming.isReadable()) {
            return;
        }

        if (currentParser == null) {
            currentParser = initializeHeaderParser();
        }

        // Parser stack will run until current incoming data has all been consumed.
        currentParser.parse(incoming);
    }

    @Override
    public void reset() {
        currentParser = initializeHeaderParser();
    }

    private void validateFrameSize(int frameSize, int currentLimit) throws IOException {
        if (frameSize > transport.getMaxFrameSize()) {
            throw IOExceptionSupport.createFrameSizeException(frameSize, currentLimit);
        }
    }

    //----- Prepare the current frame parser for use -------------------------//

    private FrameParser initializeHeaderParser() {
        headerReader.reset(AMQP_HEADER_BYTES);
        return headerReader;
    }

    private FrameParser initializeFrameLengthParser() {
        frameSizeReader.reset(AMQP_FRAME_SIZE_BYTES);
        return frameSizeReader;
    }

    private FrameParser initializeContentReader(int contentLength) {
        contentReader.reset(contentLength);
        return contentReader;
    }

    //----- Frame parser implementations -------------------------------------//

    private interface FrameParser {

        void parse(ProtonBuffer incoming) throws IOException;

        void reset(int nextExpectedReadSize);
    }

    private final FrameParser headerReader = new FrameParser() {

        private final ProtonBuffer header = new ProtonByteBuffer(AMQP_HEADER_BYTES, AMQP_HEADER_BYTES);

        @Override
        public void parse(ProtonBuffer incoming) throws IOException {
//            int length = Math.min(incoming.getReadableBytes(), header.length - header.offset);
//
//            incoming.get(header.data, header.offset, length);
//            header.offset += length;
//
//            if (header.offset == AMQP_HEADER_BYTES) {
//                header.reset();
//                AmqpHeader amqpHeader = new AmqpHeader(header.deepCopy(), false);
//                currentParser = initializeFrameLengthParser();
//                frameSink.onFrame(amqpHeader);
//                if (incoming.hasRemaining()) {
//                    currentParser.parse(incoming);
//                }
//            }
        }

        @Override
        public void reset(int nextExpectedReadSize) {
            header.clear();
        }
    };

    private final FrameParser frameSizeReader = new FrameParser() {

        private int frameSize;
        private int multiplier;

        @Override
        public void parse(ProtonBuffer incoming) throws IOException {

            while (incoming.isReadable()) {
                frameSize += ((incoming.readByte() & 0xFF) << --multiplier * Byte.SIZE);

                if (multiplier == 0) {
                    LOG.trace("Next incoming frame length: {}", frameSize);
                    validateFrameSize(frameSize, transport.getMaxFrameSize());
                    currentParser = initializeContentReader(frameSize);
                    if (incoming.isReadable()) {
                        currentParser.parse(incoming);
                        return;
                    }
                }
            }
        }

        @Override
        public void reset(int nextExpectedReadSize) {
            multiplier = AMQP_FRAME_SIZE_BYTES;
            frameSize = 0;
        }
    };

    private final FrameParser contentReader = new FrameParser() {

        private Buffer frame;

        @Override
        public void parse(ProtonBuffer incoming) throws IOException {
//            int length = Math.min(incoming.getReadableBytes(), frame.getLength() - frame.offset);
//            incoming.get(frame.data, frame.offset, length);
//            frame.offset += length;
//
//            if (frame.offset == frame.length) {
//                LOG.trace("Contents of size {} have been read", frame.length);
//                frame.reset();
//                frameSink.onFrame(frame);
//                if (currentParser == this) {
//                    currentParser = initializeFrameLengthParser();
//                }
//                if (incoming.hasRemaining()) {
//                    currentParser.parse(incoming);
//                }
//            }
        }

        @Override
        public void reset(int nextExpectedReadSize) {
            // Allocate a new Buffer to hold the incoming frame.  We must write
            // back the frame size value before continue on to read the indicated
            // frame size minus the size of the AMQP frame size header value.
//            frame = new Buffer(nextExpectedReadSize);
//            frame.bigEndianEditor().writeInt(nextExpectedReadSize);
//
//            // Reset the length to total length as we do direct write after this.
//            frame.length = frame.data.length;
        }
    };
}
