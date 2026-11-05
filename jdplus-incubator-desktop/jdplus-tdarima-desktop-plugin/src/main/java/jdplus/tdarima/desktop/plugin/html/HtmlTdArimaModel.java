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
package jdplus.tdarima.desktop.plugin.html;

import java.io.IOException;
import jdplus.advancedsa.base.core.tdarima.TdSarimaResults;
import jdplus.toolkit.desktop.plugin.html.HtmlStream;
import jdplus.toolkit.desktop.plugin.html.HtmlTag;

/**
 *
 * @author Jean Palate
 */
public class HtmlTdArimaModel extends HtmlTdArima {

    private final TdSarimaResults model;
    boolean summary;

    public HtmlTdArimaModel(TdSarimaResults model, boolean summary) {
        this.model = model;
        this.summary = summary;
    }

    @Override
    public void write(HtmlStream stream) throws IOException {
        stream.write(HtmlTag.HEADER1, "TD Arima model");
        stream.write(HtmlTag.LINEBREAK);
        writeTdArima(stream, model, false);
        stream.write(HtmlTag.LINEBREAK);
    }
}
