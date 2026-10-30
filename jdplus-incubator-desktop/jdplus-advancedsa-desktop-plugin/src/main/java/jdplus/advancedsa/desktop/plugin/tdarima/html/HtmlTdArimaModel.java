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
package jdplus.advancedsa.desktop.plugin.tdarima.html;

import java.io.IOException;
import jdplus.advancedsa.base.core.tdarima.TdSarimaResults;
import jdplus.toolkit.desktop.plugin.html.AbstractHtmlElement;
import jdplus.toolkit.desktop.plugin.html.HtmlStream;

/**
 *
 * @author Jean Palate
 */
public class HtmlTdArimaModel extends AbstractHtmlElement {

    private final TdSarimaResults model;
    boolean summary;

    public HtmlTdArimaModel(TdSarimaResults model, boolean summary) {
        this.model = model;
        this.summary = summary;
    }

    @Override
    public void write(HtmlStream stream) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
