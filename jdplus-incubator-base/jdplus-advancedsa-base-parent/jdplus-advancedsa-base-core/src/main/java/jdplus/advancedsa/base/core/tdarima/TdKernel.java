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

import jdplus.advancedsa.base.api.tdarima.TdModelSpec;
import jdplus.advancedsa.base.api.tdarima.TdSarimaSpec;
import jdplus.advancedsa.base.core.regarima.FastKernel;
import jdplus.sa.base.core.CholetteProcessor;
import jdplus.sa.base.core.PreliminaryChecks;
import jdplus.toolkit.base.api.data.DoubleSeq;
import jdplus.toolkit.base.api.modelling.regular.SeriesSpec;
import jdplus.toolkit.base.api.processing.ProcessingLog;
import jdplus.toolkit.base.api.timeseries.TsData;
import jdplus.toolkit.base.api.timeseries.TsDomain;
import jdplus.toolkit.base.api.timeseries.regression.ModellingContext;
import jdplus.toolkit.base.core.math.matrices.FastMatrix;
import jdplus.toolkit.base.core.regsarima.regular.RegSarimaModel;

/**
 * @author Jean Palate
 */
public class TdKernel {

    private final TdModelSpec spec;
    private final PreliminaryChecks.Tool preliminary;
    private final FastKernel preprocessor;
    private final TdDecompositionKernel kernel;
    private final CholetteProcessor cholette;

    private static PreliminaryChecks.Tool of(TdModelSpec spec) {

        SeriesSpec series = spec.getPreprocessing().getSeries();
        return (s, logs) -> {
            TsData sc = s.select(series.getSpan());
            if (series.isPreliminaryCheck()) {
                PreliminaryChecks.testSeries(sc);
            }
            if (!spec.getPreprocessing().isEnabled()) {
                return s.select(series.getSpan());
            } else {
                return s;
            }
        };
    }

    public static TdKernel of(TdModelSpec spec, final ModellingContext context) {
        PreliminaryChecks.Tool check = of(spec);
        FastKernel preprocessor = FastKernel.of(spec.getPreprocessing(), context);
        TdDecompositionKernel tddecomp = TdDecompositionKernel.of(spec.getTdarima());
        return new TdKernel(spec, check, preprocessor, tddecomp, CholetteProcessor.of(spec.getBenchmarking()));
    }

    private TdKernel(
            TdModelSpec spec,
            final PreliminaryChecks.Tool check,
            FastKernel preprocessor,
            TdDecompositionKernel tddecomp,
            CholetteProcessor bench) {
        this.spec = spec;
        this.preliminary = check;
        this.preprocessor = preprocessor;
        this.kernel = tddecomp;
        this.cholette = bench;
    }

    //    private DoubleSeq curP, curDelta;
    //    private FastMatrix curPCov, curDeltaCov;
    //
    /**
     * Raw estimation
     *
     * @param s variable
     * @param log
     * @return
     */
    public TdModellingResults process(TsData s, ProcessingLog log) {
        if (log == null) {
            log = ProcessingLog.dummy();
        }
        try {
            // Step 0. Preliminary checks
            TsData sc = preliminary.check(s, log);
            // Step 1. Preprocessing
            RegSarimaModel preprocessing = null;
            FastMatrix X = null;
            DoubleSeq y = s.getValues();
            int period = s.getAnnualFrequency();
            //            X12plusPreadjustment preadjustment;
            // TODO : backcasts/forecasts
            TdSarimaSpec.Orders orders = TdSarimaSpec.Orders.airline(period);
            if (preprocessor != null) {
                // we have to remove fixed effects of the transformed series
                preprocessing = preprocessor.process(sc, log);
                sc = preprocessing.transformedSeries();
                TsDomain domain = s.getDomain();
                TsData preadj = preprocessing.preadjustmentEffect(domain, v -> true);
                sc = TsData.subtract(sc, preadj);
                X = preprocessing.regressionMatrix(domain);
                y = sc.getValues();

                orders = TdSarimaSpec.Orders.of(preprocessing.specification());
            }
            TdResults drslt = kernel.process(y, true, X, orders);

            return TdModellingResults.builder()
                    .preprocessing(preprocessing)
                    .tdarima(drslt)
                    //                    .benchmarking(bench)
                    //                    .diagnostics(X13plusDiagnostics.of(preprocessing, preadjustment, xr, finals))
                    .log(log)
                    .build();

        } catch (Exception err) {
            log.error(err);
            return null;
        }
    }
}
