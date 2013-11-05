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

package org.la4j.matrix.dense;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.la4j.factory.Basic2DFactory;
import org.la4j.matrix.Matrix;
import org.la4j.matrix.source.MatrixSource;
import org.la4j.matrix.source.UnsafeMatrixSource;
import org.la4j.vector.Vector;
import org.la4j.vector.dense.BasicVector;

public class Basic2DMatrix extends AbstractBasicMatrix implements DenseMatrix {

    private static final long serialVersionUID = 4071505L;

    private double self[][];

    public Basic2DMatrix() {
        this(0, 0);
    }

    public Basic2DMatrix(Matrix matrix) {
        this(new UnsafeMatrixSource(matrix));
    }

    public Basic2DMatrix(MatrixSource source) {
        this(source.rows(), source.columns());

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
               self[i][j] = source.get(i, j);
            }
        }
    }

    public Basic2DMatrix(int rows, int columns) {
        this(new double[rows][columns]);
    }

    public Basic2DMatrix(double array[][]) {
        super(new Basic2DFactory(), array.length, array.length == 0 
                ? 0: array[0].length);

        this.self = array;
    }

    @Override
    public double unsafe_get(int i, int j) {
        return self[i][j];
    }

    @Override
    public void unsafe_set(int i, int j, double value) {
        self[i][j] = value;
    }

    @Override
    public void resize(int rows, int columns) {

        if (rows < 0 || columns < 0) {
            throw new IllegalArgumentException("Wrong dimensions: " 
                    + rows + "x" + columns);
        }

        if (this.rows == rows && this.columns == columns) {
            return;
        }

        if (this.rows >= rows && this.columns >= columns) {
            this.rows = rows;
            this.columns = columns;

            return;
        }

        double newSelf[][] = new double[rows][columns];

        for (int i = 0; i < this.rows; i++) {
            System.arraycopy(self[i], 0, newSelf[i], 0, this.columns);
        }

        this.rows = rows;
        this.columns = columns;

        self = newSelf;
    }

    @Override
    public void swapRows(int i, int j) {
        ensureIndexInRows(i);
        ensureIndexInRows(j);

        if (i != j) {
            double tmp[] = self[i];
            self[i] = self[j];
            self[j] = tmp;
        }
    }

    @Override
    public void swapColumns(int i, int j) {
        ensureIndexInColumns(i);
        ensureIndexInColumns(j);

        if (i != j) {
            for (int ii = 0; ii < rows; ii++) {
                double tmp = self[ii][i];
                self[ii][i] = self[ii][j];
                self[ii][j] = tmp;
            }
        }
    }

    @Override
    public Vector getRow(int i) {
        ensureIndexInRows(i);

        double result[] = new double[columns];

        System.arraycopy(self[i], 0, result, 0, columns);

        return new BasicVector(result);
    }

    @Override
    public double[][] toArray() {

        double result[][] = new double[rows][columns];

        for (int i = 0; i < rows; i++) {
            System.arraycopy(self[i], 0, result[i], 0, columns);
        }

        return result;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

        out.writeInt(rows);
        out.writeInt(columns);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                out.writeDouble(self[i][j]);
            }
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException {

        rows = in.readInt();
        columns = in.readInt();

        self = new double[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                self[i][j] = in.readDouble();
            }
        }
    }
}
