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
package jdplus.advancedsa.desktop.plugin.tdarima.ui;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import jdplus.advancedsa.base.api.tdarima.TdModelSpec;
import jdplus.toolkit.desktop.plugin.descriptors.EnhancedPropertyDescriptor;
import jdplus.toolkit.desktop.plugin.descriptors.IPropertyDescriptors;
import org.openide.util.NbBundle;

/**
 *
 * @author Jean Palate
 */
public class TdSarimaSpecUI implements IPropertyDescriptors {

    private final TdModelSpecRoot root;

    public TdSarimaSpecUI(TdModelSpecRoot root) {
        this.root = root;
    }

    public TdModelSpec core() {
        return root.getCore();
    }

    @Override
    public List<EnhancedPropertyDescriptor> getProperties() {
        ArrayList<EnhancedPropertyDescriptor> descs = new ArrayList<>();
        EnhancedPropertyDescriptor desc = preprocessingDesc();
        if (desc != null) {
            descs.add(desc);
        }
        return descs;
    }

    public boolean isPreprocessing() {
        return root.isUsingPreprocessing();
    }

    public void setPreprocessing(boolean p) {
        if (p != root.isUsingPreprocessing()) {
            root.setUsingPreprocessing(p);
        }
    }

    @NbBundle.Messages("TdModelSpecUI.getDisplayName=TDSarima model")
    @Override
    public String getDisplayName() {
        return Bundle.TdModelSpecUI_getDisplayName();
    }

    private EnhancedPropertyDescriptor preprocessingDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("usingPreprocessing", this.getClass());
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, P_ID);
            desc.setDisplayName(P_NAME);
            desc.setShortDescription(P_DESC);
            return edesc;
        } catch (IntrospectionException ex) {
            return null;
        }
    }

    private static final int P_ID = 0;
    private static final String P_NAME = "Use preprocessing";
    private static final String P_DESC = "Use preprocessing";
}
