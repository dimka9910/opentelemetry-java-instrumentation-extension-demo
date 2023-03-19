package com.example.javaagent.sampler;

import com.example.javaagent.DemoAutoConfigurationCustomizerProvider;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.trace.data.LinkData;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;

import java.util.List;


public final class RuleBasedRoutingSampler implements Sampler {
    private final List<SamplingRule> rules;
    private final SpanKind kind;
    private final Sampler fallback;

    RuleBasedRoutingSampler(List<SamplingRule> rules, SpanKind kind, Sampler fallback) {
        this.kind = kind;
        this.fallback = fallback;
        this.rules = rules;
    }

    public static RuleBasedRoutingSamplerBuilder builder(SpanKind kind, Sampler fallback) {
        return new RuleBasedRoutingSamplerBuilder(kind,fallback);
    }

    @Override
    public SamplingResult shouldSample(
            Context parentContext,
            String traceId,
            String name,
            SpanKind spanKind,
            Attributes attributes,
            List<LinkData> parentLinks) {

        ConfigProperties configProperties = DemoAutoConfigurationCustomizerProvider.configProperties;

        if (kind != spanKind) {
            return fallback.shouldSample(parentContext, traceId, name, spanKind, attributes, parentLinks);
        }
        for (SamplingRule samplingRule : rules) {
            String attributeValue = attributes.get(samplingRule.attributeKey);
            if (attributeValue == null) {
                continue;
            }
            if (samplingRule.pattern.matcher(attributeValue).find()) {
                return samplingRule.delegate.shouldSample(
                        parentContext, traceId, name, spanKind, attributes, parentLinks);
            }
        }
        return fallback.shouldSample(parentContext, traceId, name, spanKind, attributes, parentLinks);
    }



    @Override
    public String getDescription() {
        return "RuleBasedRoutingSampler";
    }
}