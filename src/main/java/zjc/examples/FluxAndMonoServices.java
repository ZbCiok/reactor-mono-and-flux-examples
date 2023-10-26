package zjc.examples;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class FluxAndMonoServices {

    // Example #1
    public void monoExample1() {
        // create a Mono
        Mono<String> mono = Mono.just("Hello");

        // subscribe to a Mono
        mono.subscribe();
    }

    // Example #2
    public void monoExample2() {
        // create a Mono
        Mono<String> mono = Mono.just("Hello");

        // subscribe to a Mono
        mono.subscribe(data -> System.out.println(data));
    }

    // Example #3
    public void monoExample3() {
        // create a Mono
        Mono<String> mono = Mono.just("Hello");

        // subscribe to a Mono
        mono.subscribe(
                data -> System.out.println(data), // onNext
                err -> System.out.println(err),  // onError
                () -> System.out.println("Completed!") // onComplete
        );
    }

    // Example #4
    public void monoExample4() {
        // create a Mono
        Mono<String> mono = Mono.fromSupplier(() -> {
            throw new RuntimeException("Exception occurred!");
        });

        // subscribe to a Mono
        mono.subscribe(
                data -> System.out.println(data), // onNext
                err -> System.out.println("ERROR: " + err),  // onError
                () -> System.out.println("Completed!") // onComplete
        );
    }

    // Example #5
    private Mono<String> fruitsMonoZipWith() {
        var fruits = Mono.just("Mango");
        var veggies = Mono.just("Tomato");

        return fruits.zipWith(veggies,
                (first,second) -> first+second).log();
    }
    public void monoExample5() {
        FluxAndMonoServices fluxAndMonoServices
                = new FluxAndMonoServices();

        fluxAndMonoServices.fruitsMonoZipWith()
                .subscribe(s -> {
                    System.out.println("Mono -> s = " + s);
                });
    }

    // Example #6
    private Mono<List<String>> fruitMonoFlatMap() {
        return Mono.just("Mango")
                .flatMap(s -> Mono.just(List.of(s.split(""))))
                .log();
    }
    private Flux<String> fruitMonoFlatMapMany() {
        return Mono.just("Mango")
                .flatMapMany(s -> Flux.just(s.split("")))
                .log();
    }
    public void monoFluxExample6() {
        FluxAndMonoServices fluxAndMonoServices
                = new FluxAndMonoServices();

        fluxAndMonoServices.fruitMonoFlatMap()
                .subscribe(s -> {
                    System.out.println("s = " + s);
                });

        fluxAndMonoServices.fruitMonoFlatMapMany()
                .subscribe(s -> {
                    System.out.println("Mono -> s = " + s);
                });
    }

    // Example #7
    public void fluxExample7() {
        // Set up a Flux that produces three values when a subscriber attaches.
        Flux<Integer> ints = Flux.range(1, 3);

        // Subscribe in the simplest way.
        ints.subscribe();
    }

    // Example #8
    public void fluxExample8() {
        // Set up a Flux that produces three values when a subscriber attaches.
        Flux<Integer> ints = Flux.range(1, 3);

        // Subscribe with a subscriber that will print the values.
        ints.subscribe(i -> System.out.println(i));
    }

    // Example #9
    public void fluxExample9() {
        Flux<Integer> ints = Flux.range(1, 4)
                // We need a map so that we can handle some values differently.
                .map(i -> {
                    // For most values, return the value.
                    if (i <= 3) return i;
                    // For one value, force an error.
                    throw new RuntimeException("Got to 4");
                });
        // Subscribe with a subscriber that includes an error handler.
        ints.subscribe(i -> System.out.println(i),
                error -> System.err.println("Error: " + error));
    }

    // Example #10
    public void fluxExample10() {
        Flux<Integer> ints = Flux.range(1, 4);

        // Subscribe with a Subscriber that includes a handler for completion events.
        ints.subscribe(i -> System.out.println(i),
                error -> System.err.println("Error " + error),
                () -> System.out.println("Done"));
    }

    // Example #11
    private Flux<String> fruitsFluxFilter(int number) {
        return Flux.fromIterable(List.of("zjc","examples","Flux"))
                .filter(s -> s.length() > number);
    }
    public void fluxExample11() {
        FluxAndMonoServices fluxAndMonoServices = new FluxAndMonoServices();

        fluxAndMonoServices.fruitsFluxFilter(2)
                .subscribe(s -> {
                    System.out.println("s = " + s);
                });
    }


    // Example #12
    private Flux<String> stringsFluxFlatMap() {
        return Flux.fromIterable(List.of("zjc","examples","Flux"))
                .flatMap(s -> Flux.just(s.split("")))
                .log();
    }

    private Flux<String> stringsFluxFlatMapAsync() {
        return Flux.fromIterable(List.of("zjc","examples","Flux"))
                .flatMap(s -> Flux.just(s.split(""))
                        .delayElements(Duration.ofMillis(
                                new Random().nextInt(1000)
                        )))
                .log();
    }
    public void fluxExample12() {
        FluxAndMonoServices fluxAndMonoServices
                = new FluxAndMonoServices();

        fluxAndMonoServices.stringsFluxFlatMap()
                .subscribe(s -> {
                    System.out.println("s = " + s);
                });

        fluxAndMonoServices.stringsFluxFlatMapAsync()
                .subscribe(s -> {
                    System.out.println("s = " + s);
                });
    }

    // Example #13
    private Flux<String> fruitsFluxTransform(int number) {

        Function<Flux<String>,Flux<String>> filterData
                = data -> data.filter(s -> s.length() > number);

        return Flux.fromIterable(List.of("zjc12","ex1234","Flux12"))
                .transform(filterData)
                .log();
        //.filter(s -> s.length() > number);
    }
    private Flux<String> fruitsFluxTransformDefaultIfEmpty(int number) {

        Function<Flux<String>,Flux<String>> filterData
                = data -> data.filter(s -> s.length() > number);

        return Flux.fromIterable(List.of("zjc12","ex1234","Flux12"))
                .transform(filterData)
                .defaultIfEmpty("Default")
                .log();

    }
    private Flux<String> fruitsFluxTransformSwitchIfEmpty(int number) {

        Function<Flux<String>,Flux<String>> filterData
                = data -> data.filter(s -> s.length() > number);

        return Flux.fromIterable(List.of("zjc12","ex1234","Flux12"))
                .transform(filterData)
                .switchIfEmpty(Flux.just("123456789","1234 12345")
                        .transform(filterData))
                .log();
    }
    public void fluxExample13() {
        FluxAndMonoServices fluxAndMonoServices
                = new FluxAndMonoServices();

        fluxAndMonoServices.fruitsFluxTransform(5)
                .subscribe(s -> {
                    System.out.println("s = " + s);
                });

        fluxAndMonoServices.fruitsFluxTransformDefaultIfEmpty(6)
                .subscribe(s -> {
                    System.out.println("s = " + s);
                });

        fluxAndMonoServices.fruitsFluxTransformSwitchIfEmpty(6)
                .subscribe(s -> {
                    System.out.println("s = " + s);
                });
    }

    // Example #14
    private Flux<String> fruitsFluxConcat() {
        var chars = Flux.just("abcde","fghijk");
        var nrs = Flux.just("123456","78901");

        return Flux.concat(chars,nrs);
    }
    private Flux<String> fruitsFluxConcatWith() {
        var chars = Flux.just("abcde","fghijk");
        var nrs = Flux.just("123456","78901");

        return chars.concatWith(nrs);
    }
    private Flux<String> fruitsMonoConcatWith() {
        var chars = Mono.just("abcde");
        var nrs = Mono.just("123456");

        return chars.concatWith(nrs);
    }
    public void fluxExample14() {
        FluxAndMonoServices fluxAndMonoServices
                = new FluxAndMonoServices();

        fluxAndMonoServices.fruitsFluxConcat()
                .subscribe(s -> {
                    System.out.println("s = " + s);
                });

        fluxAndMonoServices.fruitsFluxConcatWith()
                .subscribe(s -> {
                    System.out.println("s = " + s);
                });

        fluxAndMonoServices.fruitsMonoConcatWith()
                .subscribe(s -> {
                    System.out.println("s = " + s);
                });
    }

    // Example #15
    private Flux<String> fruitsFluxMerge() {
        var chars = Flux.just("abcde","fghijk");
        var nrs = Flux.just("123456","78901");
        return Flux.merge(chars, nrs);
    }
    private Flux<String> fruitsFluxMergeWith() {
        var chars = Flux.just("abcde","fghijk");
        var nrs = Flux.just("123456","78901");
        return chars.mergeWith(nrs);
    }
    public void fluxExample15() {
        FluxAndMonoServices fluxAndMonoServices
                = new FluxAndMonoServices();

        fluxAndMonoServices.fruitsFluxMerge()
                .subscribe(s -> {
                    System.out.println("s = " + s);
                });

        fluxAndMonoServices.fruitsFluxMergeWith()
                .subscribe(s -> {
                    System.out.println("s = " + s);
                });
    }
}
