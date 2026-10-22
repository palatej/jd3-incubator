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
package jdplus.advancedsa.base.core.tdarima;

import java.util.Map;
import jdplus.advancedsa.base.api.tdarima.TdEstimationSpec;
import jdplus.advancedsa.base.api.tdarima.TdSarimaSpec;
import jdplus.toolkit.base.api.timeseries.TsData;
import org.junit.jupiter.api.Test;
import tck.demetra.data.Data;

/**
 * @author Jean Palate
 */
public class TdDecompositionKernelTest {

    public TdDecompositionKernelTest() {}

    @Test
    public void testDetails() {

        TdSarimaSpec.Orders arima = TdSarimaSpec.Orders.airline(0);
        TdSarimaSpec modelspec = TdSarimaSpec.builder()
                .orders(arima)
                .vTheta(true)
                .vBtheta(true)
                //                .vVar(true)
                .build();
        TdEstimationSpec spec = TdEstimationSpec.builder()
                .parametrization(TdEstimationSpec.Parametrization.MEAN_DELTA)
                .build();

        TdSarimaMapping.ParametersDetails details =
                new TdSarimaMapping.ParametersDetails(modelspec, spec.getParametrization());
        System.out.println(details);
        modelspec = modelspec.toBuilder().vVar(true).build();
        details = new TdSarimaMapping.ParametersDetails(modelspec, spec.getParametrization());
        System.out.println(details);
        arima = arima.toBuilder().p(3).build();
        modelspec = modelspec.toBuilder().orders(arima).build();
        details = new TdSarimaMapping.ParametersDetails(modelspec, spec.getParametrization());
        System.out.println(details);
        modelspec = modelspec.toBuilder().vVar(false).build();
        details = new TdSarimaMapping.ParametersDetails(modelspec, spec.getParametrization());
        System.out.println(details);
    }

    @Test
    public void testAirline() {
        TsData[] s = Data.retail_us();

        TdEstimationSpec spec = TdEstimationSpec.builder()
                .parametrization(TdEstimationSpec.Parametrization.START_END)
                .simplificationThreshold(0.05)
                .tdMovingAverage(true)
                .tdInnovation(true)
                .build();

        TdDecompositionKernel kernel = TdDecompositionKernel.of(spec);

        long t0 = System.currentTimeMillis();
        for (int i = 0; i < s.length; ++i) {
            TdResults result = kernel.process(
                    s[i].getValues(), false, null, TdSarimaSpec.Orders.airline(s[i].getAnnualFrequency()));
            if (result.isTimeVarying()) {
                result.getData("final.regression.effect", double[].class);
            }
            //
            // System.out.print(result.initialResults().getLikelihood().getLogLikelihood());
            //            System.out.print('\t');
            //
            // System.out.print(result.finalResults().getLikelihood().getLogLikelihood());
            //            System.out.print('\t');
            //            System.out.print(result.getStart().getLikelihood().getLogLikelihood());
            //            System.out.print('\t');
            //            System.out.print(result.getLtdResults().size());
            //            System.out.print('\t');
            //            System.out.print(result.getStart().parameters());
            //            StatisticalTest test = result.finalResults(). .getStationaryTest();
            //            System.out.print(test == null ? Double.NaN : test.getPvalue());
            //            System.out.print('\t');
            //            System.out.print(result.getLtd().getLikelihoodRatioTest().getPvalue());
            //            System.out.print('\t');
            //            System.out.print(s[i].length());
            //            System.out.print('\t');
            //
            //            System.out.println(result.getMax().getScore());
            //
            //            DoubleSeq t = DoublesMath.divide(result.getLtd().getParameters(),
            // result.getLtd().getParametersCovariance().diagonal().sqrt());
            //            System.out.println();
            //            DoubleSeq t = result.finalResults().getParameters();
            //            System.out.println(t);
        }
        long t1 = System.currentTimeMillis();
        System.out.println(t1 - t0);
    }

    @Test
    public void test311011() {
        TsData[] s = Data.retail_us();
        TdSarimaSpec.Orders airline = TdSarimaSpec.Orders.airline(0);
        TdSarimaSpec.Orders aspec = airline.toBuilder().p(3).build();

        TdEstimationSpec spec = TdEstimationSpec.builder()
                .parametrization(TdEstimationSpec.Parametrization.MEAN_DELTA)
                .simplificationThreshold(0)
                .tdMovingAverage(true)
                .tdAutoregressive(true)
                .tdInnovation(false)
                .build();

        TdDecompositionKernel kernel = TdDecompositionKernel.of(spec);

        long t0 = System.currentTimeMillis();
        for (int i = 0; i < s.length; ++i) {
            TdResults result =
                    kernel.process(s[i].getValues(), false, null, aspec.withPeriod(s[i].getAnnualFrequency()));
            //
            // System.out.print(result.initialResults().getLikelihood().getLogLikelihood());
            //            System.out.print('\t');
            //
            // System.out.print(result.finalResults().getLikelihood().getLogLikelihood());
            //            System.out.print('\t');
            //            System.out.print(result.getStart().getLikelihood().getLogLikelihood());
            //            System.out.print('\t');
            //            System.out.print(result.getLtdResults().size());
            //            StatisticalTest test = result.getLtd().getStationaryTest();
            //            System.out.print('\t');
            //            System.out.print(test == null ? Double.NaN : test.getPvalue());
            //            System.out.print('\t');
            //            System.out.println(result.getLtd().getLikelihoodRatioTest().getPvalue());
            //            System.out.println(result.getStart().parameters());
            //            System.out.println(result.getModel().getP0());
            //            System.out.println(result.getModel().getP1());
            //
            //            System.out.println(result.getMax().getParameters());
            //            System.out.println(result.getMax().getScore());
            //
            //            DoubleSeq t = DoublesMath.divide(result.getMax().getParameters(),
            // result.getMax().asymptoticCovariance().diagonal().sqrt());
            //            System.out.println();
            //            //            System.out.println(t);
        }
        long t1 = System.currentTimeMillis();
        System.out.println(t1 - t0);
    }

    public static void main(String[] args) {
        TsData s = Data.TS_PROD;
        TdSarimaSpec.Orders airline = TdSarimaSpec.Orders.airline(s.getAnnualFrequency());

        TdEstimationSpec spec = TdEstimationSpec.builder()
                .parametrization(TdEstimationSpec.Parametrization.MEAN_DELTA)
                .tdMovingAverage(true)
                .tdInnovation(false)
                .build();

        TdDecompositionKernel kernel = TdDecompositionKernel.of(spec);

        TdResults result = kernel.process(s.getValues(), false, null, airline);
        Map<String, Class> dic = result.getDictionary();
        for (String e : dic.keySet()) {
            System.out.println(e);
        }
    }
}
