/*
 * Copyright 2025 JDemetra+.
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved
 * by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *      https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package jdplus.advancedsa.base.api.tdarima;

import jdplus.toolkit.base.api.dictionaries.AtomicDictionary;
import jdplus.toolkit.base.api.dictionaries.ComplexDictionary;
import jdplus.toolkit.base.api.dictionaries.Dictionary;
import jdplus.toolkit.base.api.dictionaries.PrefixedDictionary;
import jdplus.toolkit.base.api.dictionaries.ResidualsDictionaries;
import jdplus.toolkit.base.api.math.matrices.Matrix;
import jdplus.toolkit.base.api.stats.StatisticalTest;

/**
 *
 * @author Jean Palate
 */
public class LtdDictionaries {

    public static final String PARAMETERS_FIXED = "pfixed", PARAMETERS_ALL = "pall", PARAMETERS_MEAN = "pmean", PARAMETERS_DELTA = "pdelta",
            PARAMETERS = "parameters",
            VAR = "var",
            PARAMETERS_FIXED_COV = "pfixed_cov", PARAMETERS_COV = "pall_cov", PARAMETERS_NAMES = "pnames",
            PARAMETERS_DERIVED = "pderived", PARAMETERS_DERIVED_COV = "pderived_cov", PARAMETERS_DERIVED_NAMES = "pderived_names",
            TEST_STATIONARITY = "test_stationarity", LRTEST = "lrtest";

    public static final String REGS_COV = "cov", REGS_C = "c",
            REGS_EFFECT = "effect", Y_LIN = "y_lin";

    public static final String MODEL = "model", RESIDUALS = "residuals", REGRESSION = "regression";

    public static final String SARIMA = "sarima", LTDSARIMA = "ltdsarima", INITIAL = "initial", FINAL = "final";

    public final Dictionary LTDARIMA_MODEL = AtomicDictionary.builder()
            .name("ltdarima")
            .item(AtomicDictionary.Item.builder().name(PARAMETERS).description("arima parameters").outputClass(double[].class).build())
            .item(AtomicDictionary.Item.builder().name(PARAMETERS_NAMES).description("names of the parameters of the model").outputClass(String.class).build())
            .item(AtomicDictionary.Item.builder().name(PARAMETERS_COV).description("covariance of the parameters of the model").outputClass(Matrix.class).build())
            .item(AtomicDictionary.Item.builder().name(PARAMETERS_DERIVED).description("derived parameters of the time-dependent model").outputClass(Double.class).build())
            .item(AtomicDictionary.Item.builder().name(PARAMETERS_DERIVED_NAMES).description("names of the derived parameters of the time-dependent model").outputClass(String.class).build())
            .item(AtomicDictionary.Item.builder().name(PARAMETERS_DERIVED_COV).description("covariance of the derived parameters of the time-dependent model").outputClass(Matrix.class).build())
            .item(AtomicDictionary.Item.builder().name(PARAMETERS_MEAN).description("mean of the arima parameters").outputClass(double[].class).build())
            .item(AtomicDictionary.Item.builder().name(PARAMETERS_DELTA).description("delta of the arima parameters").outputClass(double[].class).build())
            .item(AtomicDictionary.Item.builder().name(VAR).description("variance of the innovation at the end of the time span").outputClass(Double.class).build())
            .item(AtomicDictionary.Item.builder().name(TEST_STATIONARITY).description("test of the stationarity of the time-dependent parameters").outputClass(StatisticalTest.class).build())
            .item(AtomicDictionary.Item.builder().name(LRTEST).description("likelihood ratio test of the time-dependent model against the fixed model").outputClass(StatisticalTest.class).build())
            .build();

    public final Dictionary LTDARIMA_REG = AtomicDictionary.builder()
            .name("ltdarima_regresion")
            .item(AtomicDictionary.Item.builder().name(REGS_C).description("regression coefficients of the model").outputClass(double[].class).build())
            .item(AtomicDictionary.Item.builder().name(REGS_COV).description("covariance of the regression coefficients").outputClass(Matrix.class).build())
            .item(AtomicDictionary.Item.builder().name(REGS_EFFECT).description("regression effect").outputClass(double[].class).build())
            .item(AtomicDictionary.Item.builder().name(Y_LIN).description("linearized series").outputClass(double[].class).build())
            .build();

    public final Dictionary LTDARIMA = ComplexDictionary.builder()
            .dictionary(new PrefixedDictionary(MODEL, LTDARIMA_MODEL))
            .dictionary(new PrefixedDictionary(REGRESSION, LTDARIMA_REG))
            .dictionary(new PrefixedDictionary(RESIDUALS, ResidualsDictionaries.RESIDUALS_DEFAULT))
            .build();

}
