package com.example.javaagent.sampler;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

class SamplingRule {
    final AttributeKey<String> attributeKey;
    final Sampler delegate;
    final Pattern pattern;

    SamplingRule(AttributeKey<String> attributeKey, String pattern, Sampler delegate) {
        this.attributeKey = attributeKey;
        this.pattern = Pattern.compile(pattern);
        this.delegate = delegate;
    }
}
