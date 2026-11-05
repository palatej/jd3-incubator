/*
 * Copyright 2026 JDemetra+.
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

import jdplus.sa.base.api.ComponentType;
import jdplus.sa.base.api.DecompositionMode;
import jdplus.sa.base.api.SaVariable;
import jdplus.sa.base.api.SeriesDecomposition;
import jdplus.toolkit.base.api.modelling.ComponentInformation;
import jdplus.toolkit.base.api.processing.ProcessingLog;
import jdplus.toolkit.base.api.timeseries.TsData;
import jdplus.toolkit.base.api.timeseries.TsPeriod;
import jdplus.toolkit.base.core.regsarima.regular.RegSarimaModel;
import jdplus.toolkit.base.core.ssf.akf.AkfToolkit;
import jdplus.toolkit.base.core.ssf.akf.QAugmentation;
import jdplus.toolkit.base.core.ssf.arima.SsfUcarima;
import jdplus.toolkit.base.core.ssf.composite.CompositeSsf;
import jdplus.toolkit.base.core.ssf.univariate.DefaultSmoothingResults;
import jdplus.toolkit.base.core.ssf.univariate.SsfData;
import jdplus.toolkit.base.core.ucarima.UcarimaModel;

/**
 *
 * @author Jean Palate
 */
public class TdDecompositionKernel {

    public TdDecompositionResults process(RegSarimaModel regarima, TdSarimaResults rslts, ProcessingLog log) {
        TdDecompositionResults.Builder dbuilder = TdDecompositionResults.builder();
        processInitial(regarima, dbuilder, log);
        //        if (rslts != null) {
        //            IntFunction<IArimaModel> fmodels = rslts.getModel().models();
        //            TdArimaDecomposer ucmKernel = new TdArimaDecomposer(s.getAnnualFrequency(), s.length(), fmodels,
        // log);
        //            dbuilder.models(Arrays.asList(ucmKernel.ucarimaModels()));
        //        }
        return dbuilder.build();
    }

    private void processInitial(RegSarimaModel model, TdDecompositionResults.Builder dbuilder, ProcessingLog log) {
        boolean mul = model.getDescription().isLogTransformation();
        TsData series = model.interpolatedSeries(false);
        TsData det = model.deterministicEffect(null, v -> !SaVariable.isRegressionEffect(v, ComponentType.Undefined));
        det = model.backTransform(det, true);
        // we remove all the regression effects except the undefined ones (which will be included in the different
        // components)
        TsData ls;
        if (mul) {
            series = TsData.divide(series, det);
            ls = series.log();
        } else {
            series = TsData.subtract(series, det);
            ls = series;
        }

        UcarimaModel ucm = TdArimaDecomposer.ucm(model.arima(), series.getAnnualFrequency());
        CompositeSsf ssf = SsfUcarima.of(ucm);

        DefaultSmoothingResults srslts =
                AkfToolkit.smooth(ssf, new SsfData(ls.getValues()), true, true, true, QAugmentation.QType.NORMAL);
        int[] cmps = ssf.componentsPosition();
        TsPeriod start = ls.getStart();
        TsData trend = TsData.of(start, srslts.getComponent(cmps[0]));
        TsData seas = TsData.of(start, srslts.getComponent(cmps[1]));
        TsData irr = TsData.of(start, srslts.getComponent(cmps[2]));
        TsData etrend = TsData.of(start, srslts.getComponentVariance(cmps[0])).fn(x -> x < 0 ? 0 : Math.sqrt(x));
        TsData eseas = TsData.of(start, srslts.getComponentVariance(cmps[1])).fn(x -> x < 0 ? 0 : Math.sqrt(x));
        TsData eirr = TsData.of(start, srslts.getComponentVariance(cmps[2])).fn(x -> x < 0 ? 0 : Math.sqrt(x));
        TsData sa = TsData.subtract(ls, seas);
        SeriesDecomposition decomp = SeriesDecomposition.builder(DecompositionMode.Additive)
                .add(ls, ComponentType.Series)
                .add(trend, ComponentType.Trend)
                .add(seas, ComponentType.Seasonal)
                .add(irr, ComponentType.Irregular)
                .add(sa, ComponentType.SeasonallyAdjusted)
                .add(etrend, ComponentType.Trend, ComponentInformation.Stdev)
                .add(eseas, ComponentType.Seasonal, ComponentInformation.Stdev)
                .add(eirr, ComponentType.Irregular, ComponentInformation.Stdev)
                .add(eseas, ComponentType.SeasonallyAdjusted, ComponentInformation.Stdev)
                .build();
        dbuilder.stochasticSeries(decomp);
        if (mul) {
            decomp = SeriesDecomposition.builder(DecompositionMode.Multiplicative)
                    .add(series, ComponentType.Series)
                    .add(trend.exp(), ComponentType.Trend)
                    .add(seas.exp(), ComponentType.Seasonal)
                    .add(irr.exp(), ComponentType.Irregular)
                    .add(sa.exp(), ComponentType.SeasonallyAdjusted)
                    .build();
        }
        dbuilder.components(decomp);
    }
}
