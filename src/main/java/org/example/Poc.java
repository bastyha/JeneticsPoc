package org.example;

import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.util.Factory;

public class Poc {

    private static double Fitness(final Genotype<DoubleGene> a) {
        double l_a = a.get(0).as(DoubleChromosome.class).toArray()[0];
        double l_b = a.get(0).as(DoubleChromosome.class).toArray()[1];
        return l_b*l_a;
//        System.out.println(a.get(1));
//        System.out.println(a.get(0).as(DoubleChromosome.class).toArray()[0]);
//        System.out.println("****");
//        return l_a;
    }



    public static void main(String[] args) {

        final Factory<Genotype<DoubleGene>> dtf =
                Genotype.of(
                        DoubleChromosome.of(-5.0, 20.0, 2 )
                );
//        final Factory<Genotype<DoubleGene>> dtf =
//                Genotype.of(
//                        DoubleChromosome.of(-5.0, 20.0 ),
//                        DoubleChromosome.of(20.0, 30.0)
//                        );
        System.out.println("Factory: "+dtf);
        final Engine<DoubleGene, Double> engine = Engine.builder(Poc::Fitness, dtf)
                .survivorsSelector(new TournamentSelector<>(5))
                .offspringSelector(new StochasticUniversalSelector<>())
                .alterers(new Mutator<>(0.315), new SinglePointCrossover<>(0.16))
                .build();
        final Phenotype<DoubleGene, Double> a =engine
                .stream()
                .limit((aktualisGeneracio)-> {
                    boolean out = Math.abs(aktualisGeneracio.bestFitness() - aktualisGeneracio.worstFitness()) > 0.01;
                    System.out.println(aktualisGeneracio.generation()+": "+aktualisGeneracio.genotypes());
                    // System.out.println(out +"  "+alma.generation() + "   "+alma.worstFitness()+ "   "+alma.bestFitness());
                    return out || aktualisGeneracio.generation()<20;
                })
                .limit(200)
                .collect(EvolutionResult.toBestPhenotype());
        System.out.println("Result: " +a);
    }
}
