package io.github.qwzhang01.dsecurity.benchmark;

import io.github.qwzhang01.dsecurity.encrypt.shield.DefaultEncryptionAlgo;
import io.github.qwzhang01.dsecurity.kit.StringUtil;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * JMH Benchmark tests for encryption performance
 * 
 * Run with: mvn test -Dtest=EncryptionBenchmark
 * Or: java -jar target/benchmarks.jar EncryptionBenchmark
 * 
 * Results are saved to target/benchmark-results.json
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
@Fork(value = 1, warmups = 1)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
public class EncryptionBenchmark {

    private DefaultEncryptionAlgo encryptionAlgo;
    private String shortText;
    private String mediumText;
    private String longText;
    private String encryptedShort;
    private String encryptedMedium;
    private String encryptedLong;

    @Setup
    public void setup() {
        encryptionAlgo = new DefaultEncryptionAlgo();
        
        // Test data of various sizes
        shortText = "13800138000";           // 11 chars (phone number)
        mediumText = "test@example.com";     // 16 chars (email)
        longText = "A".repeat(1000);         // 1000 chars (large field)
        
        // Pre-encrypt for decryption benchmarks
        encryptedShort = encryptionAlgo.encrypt(shortText);
        encryptedMedium = encryptionAlgo.encrypt(mediumText);
        encryptedLong = encryptionAlgo.encrypt(longText);
    }

    // ============ Encryption Benchmarks ============

    @Benchmark
    public void encryptShortText(Blackhole bh) {
        bh.consume(encryptionAlgo.encrypt(shortText));
    }

    @Benchmark
    public void encryptMediumText(Blackhole bh) {
        bh.consume(encryptionAlgo.encrypt(mediumText));
    }

    @Benchmark
    public void encryptLongText(Blackhole bh) {
        bh.consume(encryptionAlgo.encrypt(longText));
    }

    // ============ Decryption Benchmarks ============

    @Benchmark
    public void decryptShortText(Blackhole bh) {
        bh.consume(encryptionAlgo.decrypt(encryptedShort));
    }

    @Benchmark
    public void decryptMediumText(Blackhole bh) {
        bh.consume(encryptionAlgo.decrypt(encryptedMedium));
    }

    @Benchmark
    public void decryptLongText(Blackhole bh) {
        bh.consume(encryptionAlgo.decrypt(encryptedLong));
    }

    // ============ Round-trip Benchmarks ============

    @Benchmark
    public void encryptDecryptRoundTrip(Blackhole bh) {
        String encrypted = encryptionAlgo.encrypt(shortText);
        String decrypted = encryptionAlgo.decrypt(encrypted);
        bh.consume(decrypted);
    }

    // ============ String Util Benchmarks ============

    @Benchmark
    public void camelToUnderscore(Blackhole bh) {
        bh.consume(StringUtil.camelToUnderscore("phoneNumber"));
    }

    @Benchmark
    public void underscoreToCamel(Blackhole bh) {
        bh.consume(StringUtil.underscoreToCamel("phone_number"));
    }

    /**
     * Main method to run benchmarks directly
     */
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(EncryptionBenchmark.class.getSimpleName())
                .resultFormat(ResultFormatType.JSON)
                .result("target/benchmark-results.json")
                .build();

        new Runner(opt).run();
    }
}
