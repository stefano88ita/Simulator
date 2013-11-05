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

package org.la4j.vector;

import java.io.Externalizable;

import org.la4j.factory.Factory;
import org.la4j.vector.functor.VectorFunction;
import org.la4j.vector.functor.VectorPredicate;
import org.la4j.vector.functor.VectorProcedure;

public interface Vector extends Externalizable {

    /**
     * Get i-th element of vector;
     * 
     * @param i
     *            element number
     * @return the i-th element
     */
    double get(int i);

    /**
     * 
     * @param i
     * @param value
     */
    void set(int i, double value);

    /**
     * 
     * @param i
     * @return
     */
    double unsafe_get(int i);

    /**
     * 
     * @param i
     * @param j
     * @param value
     */
    void unsafe_set(int i, double value);

    /**
     * Assigns all elements to specified <code>value</code>.
     * 
     * @param value
     */
    void assign(double value);

    /**
     * Returns the length of this vector.
     * 
     * @return the length of this vector
     */
    int length();

    /**
     * Resizes this vector to new <code>length</code>.
     * 
     * @param length
     */
    void resize(int length);

    /**
     * 
     * @param d
     * @return
     */
    Vector add(double value);

    /**
     * 
     * @param value
     * @param factory
     * @return
     */
    Vector add(double value, Factory factory);

    /**
     * 
     * @param v
     * @return
     */
    Vector add(Vector vector);

    /**
     * 
     * @param vector
     * @param factory
     * @return
     */
    Vector add(Vector vector, Factory factory);

    /**
     * 
     * @param v
     * @return
     */
    Vector unsafe_add(Vector vector);

    /**
     * 
     * @param vector
     * @param factory
     * @return
     */
    Vector unsafe_add(Vector vector, Factory factory);

    /**
     * 
     * @param d
     * @return
     */
    Vector multiply(double value);

    /**
     * 
     * @param value
     * @param factory
     * @return
     */
    Vector multiply(double value, Factory factory);

    /**
     * 
     * @param v
     * @return
     */
    Vector multiply(Vector vector);

    /**
     * 
     * @param vector
     * @param factory
     * @return
     */
    Vector multiply(Vector vector, Factory factory);

    /**
     * 
     * @param v
     * @return
     */
    Vector unsafe_multiply(Vector vector);

    /**
     * 
     * @param vector
     * @param factory
     * @return
     */
    Vector unsafe_multiply(Vector vector, Factory factory);

    /**
     * 
     * @param d
     * @return
     */
    Vector subtract(double value);

    /**
     * 
     * @param value
     * @param factory
     * @return
     */
    Vector subtract(double value, Factory factory);

    /**
     * 
     * @param v
     * @return
     */
    Vector subtract(Vector vector);

    /**
     * 
     * @param vector
     * @param factory
     * @return
     */
    Vector subtract(Vector vector, Factory factory);

    /**
     * 
     * @param v
     * @return
     */
    Vector unsafe_subtract(Vector vector);

    /**
     * 
     * @param vector
     * @param factory
     * @return
     */
    Vector unsafe_subtract(Vector vector, Factory factory);

    /**
     * 
     * @param d
     * @return
     */
    Vector div(double value);

    /**
     * 
     * @param value
     * @param factory
     * @return
     */
    Vector div(double value, Factory factory);

    /**
     * 
     * @param v
     * @return
     */
    double product(Vector vector);

    /**
     * Calculates the norm of this vector.
     * 
     * @return the norm of this vector
     */
    double norm();

    /**
     * Normalizes this vector.
     * 
     * @return the normalized copy of this vector
     */
    Vector normalize();

    /**
     * Normalizes this vector with specified factory.
     * 
     * @param factory
     * @return the normalized factory-created copy of this vector
     */
    Vector normalize(Factory factory);

    /**
     * 
     * @param i
     * @param j
     */
    void swap(int i, int j);

    /**
     * 
     * @return
     */
    Vector blank();

    /**
     * 
     * @param factory
     * @return
     */
    Vector blank(Factory factory);

    /**
     * Returns the copy of this vector.
     * 
     * @return the copy of this vector
     */
    Vector copy();

    /**
     * Returns the copy of this vector created with specified factory.
     * 
     * @param factory
     *            with witch new vector will be created
     * @return the copy of this vector created with specified factory
     */
    Vector copy(Factory factory);

    /**
     * 
     * @param procedure
     */
    void each(VectorProcedure procedure);

    /**
     * 
     * @param function
     */
    Vector transform(VectorFunction function);

    /**
     * 
     * @param function
     */
    Vector transform(VectorFunction function, Factory factory);

    /**
     * 
     * @param predicate
     * @return
     */
    boolean is(VectorPredicate predicate);
}
