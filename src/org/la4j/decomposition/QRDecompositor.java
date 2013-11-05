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

package org.la4j.decomposition;

import org.la4j.factory.Factory;
import org.la4j.matrix.Matrices;
import org.la4j.matrix.Matrix;
import org.la4j.vector.Vector;

public class QRDecompositor implements MatrixDecompositor {

    @Override
    public Matrix[] decompose(Matrix matrix, Factory factory) {

        if (matrix.rows() < matrix.columns()) {
            throw new IllegalArgumentException("Wrong matrix size: " 
                    +  "rows < columns");
        }

        Matrix qr = matrix.copy();

        Vector rdiag = factory.createVector(qr.columns());

        for (int k = 0; k < qr.columns(); k++) {

            double norm = 0.0;

            for (int i = k; i < qr.rows(); i++) {
                norm = hypot(norm, qr.unsafe_get(i, k));
            }

            if (Math.abs(norm) > Matrices.EPS) {

                if (qr.unsafe_get(k, k) < 0.0) {
                    norm = -norm;
                }

                for (int i = k; i < qr.rows(); i++) {
                    qr.unsafe_set(i, k, qr.unsafe_get(i, k) / norm);
                }

                // TODO: create a matrix method for that operation 
                qr.unsafe_set(k, k, qr.unsafe_get(k, k) + 1.0);

                for (int j = k + 1; j < qr.columns(); j++) {

                    double summand = 0.0;

                    for (int i = k; i < qr.rows(); i++) {
                        summand += qr.unsafe_get(i, k) * qr.unsafe_get(i, j);
                    }

                    summand = -summand / qr.unsafe_get(k, k);

                    for (int i = k; i < qr.rows(); i++) {
                        qr.unsafe_set(i, j, qr.unsafe_get(i, j) + summand 
                                      * qr.unsafe_get(i, k));
                    }
                }
            }

            rdiag.unsafe_set(k, norm);
        }

        Matrix q = qr.blank(factory);

        for (int k = q.columns() - 1; k >= 0; k--) {

            q.unsafe_set(k, k, 1.0);

            for (int j = k; j < q.columns(); j++) {

                if (Math.abs(qr.unsafe_get(k, k)) > Matrices.EPS) {

                    double summand = 0.0;

                    for (int i = k; i < q.rows(); i++) {
                        summand += qr.unsafe_get(i, k) * q.unsafe_get(i, j);
                    }

                    summand = -summand / qr.unsafe_get(k, k);

                    for (int i = k; i < q.rows(); i++) {
                        q.unsafe_set(i, j, q.unsafe_get(i, j) 
                                     + (summand * qr.unsafe_get(i, k)));
                    }
                }
            }
        }

        Matrix r = qr.blank(factory);

        for (int i = 0; i < r.columns(); i++) {
            for (int j = i; j < r.columns(); j++) {
                if (i < j) {
                    r.unsafe_set(i, j, -qr.unsafe_get(i, j));
                } else if (i == j) {
                    r.unsafe_set(i, j, rdiag.unsafe_get(i));
                }
            }
        }

        // TODO: fix it

        return new Matrix[] { q.multiply(-1), r };
    }

    private double hypot(double a, double b) {

        double result;

        if (Math.abs(a) > Math.abs(b)) {
            result = b / a;
            result = Math.abs(a) * Math.sqrt(1 + result * result);
        } else if (b != 0) {
            result = a / b;
            result = Math.abs(b) * Math.sqrt(1 + result * result);
        } else {
            result = 0.0;
        }

        return result;
    }
}
