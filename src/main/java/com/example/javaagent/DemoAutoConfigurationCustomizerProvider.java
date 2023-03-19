/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.example.javaagent;

import com.example.javaagent.sampler.RuleBasedRoutingSampler;
import com.example.javaagent.sampler.RuleBasedRoutingSamplerBuilder;
import com.google.auto.service.AutoService;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizer;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.trace.SdkTracerProviderBuilder;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import com.example.restserviceobjects.ConfigurationInterface;


/**
 * This is one of the main entry points for Instrumentation Agent's customizations. It allows
 * configuring the {@link AutoConfigurationCustomizer}. See the {@link
 * #customize(AutoConfigurationCustomizer)} method below.
 *
 * <p>Also see https://github.com/open-telemetry/opentelemetry-java/issues/2022
 *
 * @see AutoConfigurationCustomizerProvider
// * @see DemoPropagatorProvider
 */
@Configuration
@AutoService(AutoConfigurationCustomizerProvider.class)
public class DemoAutoConfigurationCustomizerProvider
    implements AutoConfigurationCustomizerProvider {


  @Autowired(required = false)
  ConfigurationInterface configurationInterface;


  @Override
  public void customize(AutoConfigurationCustomizer autoConfiguration) {
    autoConfiguration.addTracerProviderCustomizer(this::configureSdkTracerProvider);
//        .addPropertiesSupplier(this::getDefaultProperties);
  }

  public static ConfigProperties configProperties;

  private SdkTracerProviderBuilder configureSdkTracerProvider(SdkTracerProviderBuilder tracerProvider, ConfigProperties config) {


    System.out.println(configurationInterface == null);

    configProperties = config;
    System.out.println("check ver 7");
    System.out.println(config);

    return tracerProvider.setSampler(Sampler.parentBased(
            RuleBasedRoutingSampler.builder(SpanKind.SERVER, Sampler.alwaysOn())
//                    .drop(AttributeKey.stringKey("http.target"), "/get1.*")
                    .drop(AttributeKey.stringKey("http.target"), "/get3.*")
                    .build()));

  }

//  private Map<String, String> getDefaultProperties() {
//    Map<String, String> properties = new HashMap<>();
////    properties.put("otel.exporter.otlp.endpoint", "http://backend:8080");
////    properties.put("otel.exporter.otlp.insecure", "true");
////    properties.put("otel.config.max.attrs", "16");
//    properties.put("otel.traces.sampler", "demo");
//    return properties;
//  }
}
