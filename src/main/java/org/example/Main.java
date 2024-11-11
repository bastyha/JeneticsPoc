package org.example;

import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.Genotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.util.Factory;

import java.util.random.RandomGenerator;

public class Main {
    public static Integer eval(Genotype<BitGene> gt){
       // System.out.println(gt);
        System.out.println("Genotype: " + gt.get(0).as(BitChromosome.class));
        return gt.chromosome().as(BitChromosome.class).bitCount();
    }

    public static void BitwiseGenetics(){
        Factory<Genotype<BitGene>> gtf = Genotype.of(BitChromosome.of(10, 0.5));
        System.out.println(gtf);
        Engine<BitGene,Integer> engine = Engine.builder(Main::eval, gtf).build();
        Genotype<BitGene> result = engine.stream()
                .limit(500)
                .collect(EvolutionResult.toBestGenotype());
        System.out.println(result);
    }
    public static void main(String[] args) {
        BitwiseGenetics();
    }
}