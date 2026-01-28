package io.github.qwzhang01.dsecurity.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * JMH Benchmark tests for caching mechanisms used in the library
 * 
 * This benchmark demonstrates the effectiveness of caching in:
 * - Reflection metadata caching
 * - Algorithm instance caching
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Fork(value = 1, warmups = 1)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
public class CachingBenchmark {

    private ConcurrentHashMap<String, Object> cache;
    private String cachedKey;
    private String missKey;

    @Setup
    public void setup() {
        cache = new ConcurrentHashMap<>();
        cachedKey = "existingKey";
        missKey = "nonExistentKey";
        
        // Pre-populate cache
        cache.put(cachedKey, new Object());
    }

    @Benchmark
    public void cacheHit(Blackhole bh) {
        bh.consume(cache.get(cachedKey));
    }

    @Benchmark
    public void cacheMiss(Blackhole bh) {
        bh.consume(cache.get(missKey));
    }

    @Benchmark
    public void cacheComputeIfAbsent(Blackhole bh) {
        bh.consume(cache.computeIfAbsent("newKey" + System.nanoTime(), k -> new Object()));
    }

    @Benchmark
    public void cacheContainsKey(Blackhole bh) {
        bh.consume(cache.containsKey(cachedKey));
    }

    /**
     * Main method to run benchmarks directly
     */
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(CachingBenchmark.class.getSimpleName())
                .resultFormat(ResultFormatType.JSON)
                .result("target/caching-benchmark-results.json")
                .build();

        new Runner(opt).run();
    }
}
