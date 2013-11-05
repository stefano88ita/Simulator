/*
 * Copyright 2011-2013, by Vladimir Kostyukov and Contributors.
 * 
 * This file is part of la4j project (http://la4j.org)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributor(s): -
 * 
 */

package org.la4j.linear;

import org.la4j.factory.Factory;
import org.la4j.matrix.Matrices;
import org.la4j.matrix.Matrix;
import org.la4j.vector.Vector;

public class SeidelSolver implements LinearSystemSolver {

    private static final long serialVersionUID = 4071505L;

    private final static int MAX_ITERATIONS = 1000000;

    @Override
    public Vector solve(LinearSystem linearSystem, Factory factory) {

        if (!suitableFor(linearSystem)) {
            throw new IllegalArgumentException();
        }

        Matrix a = linearSystem.coefficientsMatrix().copy();
        Vector b = linearSystem.rightHandVector();

        for (int i = 0; i < a.rows(); i++) {
            for (int j = 0; j < a.columns(); j++) {
                if (i != j)
                    a.unsafe_set(i, j, a.unsafe_get(i, j) / a.unsafe_get(i, i));
            }
        }

        Vector current = factory.createVector(linearSystem.variables());

        int iteration = 0;
        while (iteration < MAX_ITERATIONS && !linearSystem.isSolution(current)) {

            for (int i = 0; i < a.rows(); i++) {

                double summand = b.unsafe_get(i) / a.unsafe_get(i, i);
                for (int j = 0; j < a.columns(); j++) {
                    if (i != j) {
                        summand -= a.unsafe_get(i, j) * current.unsafe_get(j);
                    }
                }

                current.unsafe_set(i, summand);
            }

            iteration++;
        }

        if (iteration == MAX_ITERATIONS) {
            throw new IllegalArgumentException();
        }

        return current;
    }

    @Override
    public boolean suitableFor(LinearSystem linearSystem) {

        Matrix a = linearSystem.coefficientsMatrix();

        for (int i = 0; i < a.rows(); i++) {

            double sum = 0;
            for (int j = 0; j < a.columns(); j++) {
                if (i != j) {
                    sum += Math.abs(a.unsafe_get(i, j));
                }
            }

            if (sum > Math.abs(a.unsafe_get(i, i)) - Matrices.EPS) {
                return false;
            }
        }

        return true;
    }
}
