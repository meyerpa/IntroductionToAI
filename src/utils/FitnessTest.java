package utils;

import ga.GeneticAlgorithm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FitnessTest {

    @Test
    void calculateFitness() {
        double x = 2.;
        double y = 2.;
        Fitness fit = new Fitness();
        double result = fit.calculateFitness(x, y);
        assertEquals(fit.calculateFitness(x, y), 0.0);

    }
}