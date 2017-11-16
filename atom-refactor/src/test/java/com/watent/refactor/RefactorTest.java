package com.watent.refactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

public class RefactorTest {

    @Test
    public void fluxTest() {

        Flux.just("Hello", "World").subscribe(System.out::println);
        Flux.fromArray(new Integer[]{1, 2, 3}).subscribe(System.out::println);
        Flux.empty().subscribe(System.out::println);
        Flux.range(1, 10).subscribe(System.out::println);
        Flux.interval(Duration.of(10, ChronoUnit.SECONDS)).subscribe(System.out::println);
    }

    @Test
    public void generateTest() {
        Flux.generate(sink -> {
            sink.next("Hello");
            sink.complete();
        }).subscribe(System.out::println);


        final Random random = new Random();
        Flux.generate(ArrayList::new, (list, sink) -> {
            int value = random.nextInt(100);
            list.add(value);
            sink.next(value);
            if (list.size() == 10) {
                sink.complete();
            }
            return list;
        }).subscribe(System.out::println);

    }

    @Test
    public void createTest() {
        Flux.create(sink -> {
            for (int i = 0; i < 10; i++) {
                sink.next(i);
            }
            sink.complete();
        }).subscribe(System.out::println);
    }


    @Test
    public void monoTest() {
        Mono.fromSupplier(() -> "Hello").subscribe(System.out::println);
        Mono.justOrEmpty(Optional.of("Hello")).subscribe(System.out::println);
        Mono.create(sink -> sink.success("Hello")).subscribe(System.out::println);
    }

    /**
     * 集合
     */
    @Test
    public void bufferTest() {

        Flux.range(1, 100).buffer(20).subscribe(System.out::println);
        // Flux.interval(Duration.ofMillis(1000)).buffer(10).take(2).toStream().forEach(System.out::println);
        Flux.range(1, 10).bufferUntil(i -> i % 2 == 0).subscribe(System.out::println);
        Flux.range(1, 10).bufferWhile(i -> i % 2 == 0).subscribe(System.out::println);
    }

    /**
     * 过滤元素
     */
    @Test
    public void filterTest() {
        Flux.range(1, 10).filter(i -> i % 2 == 0).subscribe(System.out::println);
    }

    /**
     * 集到另外的 Flux
     */
    @Test
    public void windowTest() {
        Flux.range(1, 100).window(20).subscribe(System.out::println);
    }

    /**
     * 合并
     */
    @Test
    public void zipWithTest() {

        Flux.just("a", "b")
                .zipWith(Flux.just("c", "d"))
                .subscribe(System.out::println);
        Flux.just("a", "b")
                .zipWith(Flux.just("c", "d"), (s1, s2) -> String.format("%s-%s", s1, s2))
                .subscribe(System.out::println);
    }

    /**
     * 提取
     */
    @Test
    public void takeTest() {

        Flux.range(1, 1000).take(10).subscribe(System.out::println);
        System.out.println("---");
        Flux.range(1, 1000).takeLast(10).subscribe(System.out::println);
        System.out.println("---");
        Flux.range(1, 1000).takeWhile(i -> i < 10).subscribe(System.out::println);
        System.out.println("---");
        Flux.range(1, 1000).takeUntil(i -> i == 10).subscribe(System.out::println);
    }

    /**
     * 累计
     */
    @Test
    public void reduceTest() {
        Flux.range(1, 100).reduce((x, y) -> x + y).subscribe(System.out::println);
        Flux.range(1, 100).reduceWith(() -> 100, (x, y) -> x + y).subscribe(System.out::println);//初始值
    }

    /**
     * 多个流合并成一个 Flux 序列
     */
    @Test
    public void mergeTest() {

        Flux.merge(Flux.interval(Duration.ZERO, Duration.ofMillis(100)).take(5), Flux.interval(Duration.ofMillis(50), Duration.ofMillis(100)).take(5))
                .toStream()
                .forEach(System.out::println);
        System.out.println("---");
        Flux.mergeSequential(Flux.interval(Duration.ZERO, Duration.ofMillis(100)).take(5), Flux.interval(Duration.ofMillis(50), Duration.ofMillis(100)).take(5))
                .toStream()
                .forEach(System.out::println);

    }

    /**
     * 流中的每个元素转换成一个流 合并之前就已经订阅了所有的流
     */
    @Test
    public void flatMapTest() {

        Flux.just(5, 10)
                .flatMap(x -> Flux.interval(Duration.ofMillis(x * 10), Duration.ofMillis(100)).take(x))
                .toStream()
                .forEach(System.out::println);
    }

    /**
     * 把流中的每个元素转换成一个流 concatMap 对转换之后的流的订阅是动态进行
     */
    @Test
    public void concatMapTest() {

        Flux.just(5, 10)
                .concatMap(x -> Flux.interval(Duration.ofMillis(x * 10), Duration.ofMillis(100)).take(x))
                .toStream()
                .forEach(System.out::println);

    }

    /**
     * 所有流中的最新产生的元素合并成一个新的元素 作为返回结果流中的元素
     */
    @Test
    public void combineLatestTest() {
        Flux.combineLatest(
                Arrays::toString,
                Flux.interval(Duration.ofMillis(100)).take(5),
                Flux.interval(Duration.ofMillis(50), Duration.ofMillis(100)).take(5)
        ).toStream().forEach(System.out::println);
    }

    /**
     * 处理正常和错误消息
     */
    @Test
    public void msgTest1() {
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .subscribe(System.out::println, System.err::println);
    }

    /**
     * 出现错误时返回默认值
     */
    @Test
    public void msgTest11() {

        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .onErrorReturn(0)
                .subscribe(System.out::println);
    }

    /**
     * 根据异常类型来选择流
     */
    @Test
    public void msgTest2() {

        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalArgumentException()))
                .onErrorResume(e -> {
                    if (e instanceof IllegalStateException) {
                        return Mono.just(0);
                    } else if (e instanceof IllegalArgumentException) {
                        return Mono.just(-1);
                    }
                    return Mono.empty();
                })
                .subscribe(System.out::println);
    }


    /**
     * retry 操作符来进行重试
     */
    @Test
    public void msgTest3() {

        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .retry(1)
                .subscribe(System.out::println);
    }

    /**
     * 调度器
     */
    @Test
    public void schedulerTest() {

        Flux.create(sink -> {
            sink.next(Thread.currentThread().getName());
            sink.complete();
        })
                .publishOn(Schedulers.single())
                .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
                .publishOn(Schedulers.elastic())
                .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
                .subscribeOn(Schedulers.parallel())
                .toStream()
                .forEach(System.out::println);
    }

    /**
     * 测试 元素是否符合预期
     */
    @Test
    public void stepVerifierTest() {
        StepVerifier.create(Flux.just("a", "b"))
                .expectNext("a")
                .expectNext("b")
                .verifyComplete();
    }

    /**
     * 虚拟时钟
     */
    @Test
    public void stepVerifierTimeTest() {

        StepVerifier.withVirtualTime(() -> Flux.interval(Duration.ofHours(4), Duration.ofDays(1)).take(2))
                .expectSubscription()
                .expectNoEvent(Duration.ofHours(4))
                .expectNext(0L)
                .thenAwait(Duration.ofDays(1))
                .expectNext(1L)
                .verifyComplete();
    }

    /**
     * 控制流中元素的产生
     */
    @Test
    public void testPublisherTest() {

        final TestPublisher<String> testPublisher = TestPublisher.create();
        testPublisher.next("a");
        testPublisher.next("b");
        testPublisher.complete();

        StepVerifier.create(testPublisher)
                .expectNext("a")
                .expectNext("b")
                .expectComplete();
    }


    public void debugTest() {

        // Hooks.onEachOperator(providedHook -> providedHook.subscribe(System.out::println));
    }

    /**
     * 检查点测试 检查点名称会出现在异常堆栈信息中
     */
    @Test
    public void checkpointTest() {

        //Flux.just(1, 0).map(x -> 1 / x).subscribe(System.out::println);
        Flux.just(1, 0).map(x -> 1 / x).checkpoint("test").subscribe(System.out::println);
    }

    @Test
    public void logTest() {

        Flux.range(1, 2).log("Range").subscribe(System.out::println);
    }

    /**
     * 冷序列 冷序列的含义是不论订阅者在何时订阅该序列 总是能收到序列中产生的全部消息
     * 热序列 持续不断地产生消息订阅者只能获取到在其订阅之后产生的消息
     */
    @Test
    public void hotTest() throws InterruptedException {

        final Flux<Long> source = Flux.interval(Duration.ofMillis(1000))
                .take(10)
                .publish()
                .autoConnect();
        source.subscribe();
        Thread.sleep(5000);
        source
                .toStream()
                .forEach(System.out::println);
    }
}

