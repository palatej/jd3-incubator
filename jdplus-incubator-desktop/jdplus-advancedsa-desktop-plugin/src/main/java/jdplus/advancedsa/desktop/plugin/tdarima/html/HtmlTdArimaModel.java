/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
