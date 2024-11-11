package org.example;

import io.jenetics.EnumGene;
import io.jenetics.Mutator;
import io.jenetics.PartiallyMatchedCrossover;
import io.jenetics.Phenotype;
import io.jenetics.engine.*;
import io.jenetics.util.ISeq;
import io.jenetics.util.RandomRegistry;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

import static java.util.Objects.requireNonNull;

public class SubsetSum implements Problem<ISeq<Integer>, EnumGene<Integer>, Integer> {
    private ISeq<Integer> basicSet;
    private int size;

    @Override
    public Function<ISeq<Integer>, Integer> fitness() {
       return subset -> Math.abs(subset.stream().mapToInt(Integer::intValue).sum());
    }

    @Override
    public Codec<ISeq<Integer>, EnumGene<Integer>> codec() {
        return Codecs.ofSubSet(basicSet, size);
    }

    public SubsetSum(ISeq<Integer> basicSet, int size) {
        this.basicSet = requireNonNull(basicSet);
        this.size = size;
    }

    public static SubsetSum of(int n, int k, RandomGenerator random) {


        HashSet<Integer> tempo = random.doubles()
                .limit(n)
                .mapToObj(d -> {
                    System.out.printf("%f %f\n", d, ((d-0.5)*n));
                    return (int) ((d-0.5)*n);
                } )
                .collect(HashSet::new, HashSet::add, HashSet::addAll) ;
        ISeq<Integer> defSet  = ISeq.of(tempo);
        return new SubsetSum(defSet, k);
    }

    public static void main(String[] args) {
        RandomGenerator lma= RandomRegistry.random();
        SubsetSum problem = of(500, 15, lma);
        Engine<EnumGene<Integer>, Integer> engine = Engine.builder(problem)
                .minimizing()
                .maximalPhenotypeAge(5)
                .alterers(new PartiallyMatchedCrossover<>(0.4), new Mutator<>(0.3))
                .build();

        Phenotype<EnumGene<Integer>, Integer> best = engine.stream()
                .limit(Limits.bySteadyFitness(55))
                .collect(EvolutionResult.toBestPhenotype());

        System.out.println(best);
    }

}
