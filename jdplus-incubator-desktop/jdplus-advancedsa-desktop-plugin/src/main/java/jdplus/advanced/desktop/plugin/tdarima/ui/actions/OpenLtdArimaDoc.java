/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jdplus.advanced.desktop.plugin.tdarima.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import jdplus.advancedsa.base.core.tdarima.TdArimaDocument;
import jdplus.advancedsa.desktop.plugin.tdarima.TdArimaDocumentManager;
import jdplus.toolkit.desktop.plugin.workspace.DocumentUIServices;
import jdplus.toolkit.desktop.plugin.workspace.WorkspaceItem;
import jdplus.toolkit.desktop.plugin.workspace.nodes.WsNode;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;

@ActionID(category = "Tools", id = "jdplus.advancedsa.desktop.plugin.tdarima.ui.action.OpenTdArimaDoc")
@ActionRegistration(displayName = "#CTL_OpenTdArimaDoc")
@ActionReferences({@ActionReference(path = TdArimaDocumentManager.ITEMPATH, position = 1600, separatorBefore = 1590)})
@NbBundle.Messages("CTL_OpenTdArimaDoc=Open")
public class OpenLtdArimaDoc implements ActionListener {

    private final WsNode context;

    public OpenLtdArimaDoc(WsNode context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        WorkspaceItem<TdArimaDocument> doc =
                context.getWorkspace().searchDocument(context.lookup(), TdArimaDocument.class);
        DocumentUIServices ui = DocumentUIServices.forDocument(TdArimaDocument.class);
        if (ui != null) {
            ui.showDocument(doc);
        }
    }
}
