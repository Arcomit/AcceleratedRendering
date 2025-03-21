package com.github.argon4w.acceleratedrendering.core.programs;

import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.builders.AcceleratedBufferBuilder;

public interface IPolygonProgramDispatcher {

    int dispatch(AcceleratedBufferBuilder builder);
}
