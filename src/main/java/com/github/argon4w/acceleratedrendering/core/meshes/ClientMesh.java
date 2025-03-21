package com.github.argon4w.acceleratedrendering.core.meshes;

import com.github.argon4w.acceleratedrendering.core.backends.buffers.IClientBuffer;
import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.builders.IAcceleratedVertexConsumer;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import net.minecraft.client.renderer.RenderType;

import java.nio.ByteBuffer;

public class ClientMesh implements IMesh {

    private final int size;
    private final ByteBuffer vertexBuffer;

    public ClientMesh(int size, ByteBuffer vertexBuffer) {
        this.size = size;
        this.vertexBuffer = vertexBuffer;
    }

    @Override
    public void write(
            IAcceleratedVertexConsumer extension,
            int color,
            int light,
            int overlay
    ) {
        extension.addClientMesh(
                vertexBuffer,
                size,
                color,
                light,
                overlay
        );
    }

    public static class Builder implements IMesh.Builder {

        public static final Builder INSTANCE = new Builder();

        private Builder() {

        }

        @Override
        public MeshCollector newMeshCollector(RenderType renderType) {
            return new MeshCollector(
                    this,
                    renderType.format,
                    new SimpleClientBuffer(),
                    0
            );
        }

        @Override
        public IMesh build(MeshCollector collector) {
            int vertexCount = collector.getVertexCount();

            if (vertexCount == 0) {
                return EmptyMesh.INSTANCE;
            }

            ByteBuffer byteBuffer = collector.getBuffer().byteBuffer();

            if (byteBuffer == null) {
                return EmptyMesh.INSTANCE;
            }

            return new ClientMesh(vertexCount, byteBuffer);
        }

        public record SimpleClientBuffer(ByteBufferBuilder builder) implements IClientBuffer {

            public SimpleClientBuffer() {
                this(new ByteBufferBuilder(36 * 32));
            }

            @Override
            public long reserve(long bytes) {
                return builder.reserve((int) bytes);
            }

            @Override
            public ByteBuffer byteBuffer() {
                return builder.build().byteBuffer();
            }
        }
    }
}
