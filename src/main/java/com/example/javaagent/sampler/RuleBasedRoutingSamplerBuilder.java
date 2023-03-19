package com.example.javaagent.sampler;

import static java.util.Objects.requireNonNull;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import java.util.ArrayList;
import java.util.List;

public final class RuleBasedRoutingSamplerBuilder {
    private final List<SamplingRule> rules = new ArrayList<>();
    private final SpanKind kind;
    private final Sampler defaultDelegate;

    RuleBasedRoutingSamplerBuilder(SpanKind kind, Sampler defaultDelegate) {
        this.kind = kind;
        this.defaultDelegate = defaultDelegate;
    }

    public RuleBasedRoutingSamplerBuilder drop(AttributeKey<String> attributeKey, String pattern) {
        return customize(attributeKey, pattern, Sampler.alwaysOff());
    }

    public RuleBasedRoutingSamplerBuilder customize(AttributeKey<String> attributeKey, String pattern, Sampler sampler) {
        rules.add(new SamplingRule(attributeKey,pattern,sampler));
        return this;
    }

    public RuleBasedRoutingSamplerBuilder recordAndSample(AttributeKey<String> attributeKey, String pattern) {
        return customize(attributeKey, pattern, Sampler.alwaysOn());
    }

    public RuleBasedRoutingSampler build() {
        return new RuleBasedRoutingSampler(rules, kind, defaultDelegate);
    }
}