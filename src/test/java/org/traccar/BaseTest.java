package org.traccar;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.traccar.database.MediaManager;

import java.util.HashMap;
import java.util.Map;

public class BaseTest {

    public static class MockMediaManager extends MediaManager {
        Map<String, ByteBuf> files = new HashMap<>();

        MockMediaManager() {
            super("");
        }

        @Override
        public String writeFile(String uniqueId, ByteBuf buf, String extension) {
            String fileName = uniqueId + "/mock." + extension;
            files.put(fileName, Unpooled.copiedBuffer(buf));
            return fileName;
        }

        public ByteBuf readFile(String fileName) {
            return files.get(fileName);
        }
    }

    static {
        Context.init(new TestIdentityManager(), new MockMediaManager());
    }

}
