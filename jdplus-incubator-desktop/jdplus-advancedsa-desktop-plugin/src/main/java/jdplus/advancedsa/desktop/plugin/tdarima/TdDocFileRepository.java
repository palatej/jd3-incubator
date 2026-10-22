/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jdplus.advancedsa.desktop.plugin.tdarima;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import jdplus.advancedsa.base.core.tdarima.TdArimaDocument;
import jdplus.toolkit.base.api.DemetraVersion;
import jdplus.toolkit.base.tsp.TsMeta;
import jdplus.toolkit.desktop.plugin.workspace.AbstractFileItemRepository;
import jdplus.toolkit.desktop.plugin.workspace.WorkspaceItem;
import jdplus.toolkit.desktop.plugin.workspace.WorkspaceItemRepository;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Jean Palate
 */
@ServiceProvider(service = WorkspaceItemRepository.class)
public final class TdDocFileRepository extends AbstractFileItemRepository<TdArimaDocument> {

    @Override
    public boolean load(WorkspaceItem<TdArimaDocument> item) {
        return loadFile(item, (TdArimaDocument o) -> {
            o.setLocked(true);
            item.setElement(o);
            item.resetDirty();
        });
    }

    @Override
    public boolean save(WorkspaceItem<TdArimaDocument> doc, DemetraVersion version) {
        TdArimaDocument element = doc.getElement();

        Map<String, String> meta = new HashMap<>(element.getMetadata());
        TsMeta.TIMESTAMP.store(meta, LocalDateTime.now(Clock.systemDefaultZone()));
        element.updateMetadata(meta);

        return storeFile(doc, element, version, doc::resetDirty);
    }

    @Override
    public boolean delete(WorkspaceItem<TdArimaDocument> doc) {
        return deleteFile(doc);
    }

    @Override
    public Class<TdArimaDocument> getSupportedType() {
        return TdArimaDocument.class;
    }
}
