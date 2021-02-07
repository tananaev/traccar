/*
 * Copyright 2017 - 2020 Anton Tananaev (anton@traccar.org)
 * Copyright 2017 Andrey Kunitsyn (andrey@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.traccar.database;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.traccar.model.BaseModel;

public class BaseObjectManager<T extends BaseModel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseObjectManager.class);

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final DataManager dataManager;

    private final Class<T> baseClass;
    private Map<Long, T> items;

    protected BaseObjectManager(DataManager dataManager, Class<T> baseClass) {
        this.dataManager = dataManager;
        this.baseClass = baseClass;
        refreshItems();
    }

    protected final void readLock() {
        lock.readLock().lock();
    }

    protected final void readUnlock() {
        lock.readLock().unlock();
    }

    protected final void writeLock() {
        lock.writeLock().lock();
    }

    protected final void writeUnlock() {
        lock.writeLock().unlock();
    }

    protected final DataManager getDataManager() {
        return dataManager;
    }

    protected final Class<T> getBaseClass() {
        return baseClass;
    }

    public T getById(long itemId) {
        try {
            readLock();
            return items.get(itemId);
        } finally {
            readUnlock();
        }
    }

    public void refreshItems() {
        if (dataManager != null) {
            try {
                writeLock();
                Collection<T> databaseItems = dataManager.getObjects(baseClass);
                if (items == null) {
                    items = new ConcurrentHashMap<>(databaseItems.size());
                }
                Set<Long> databaseItemIds = new HashSet<>();
                for (T item : databaseItems) {
                    databaseItemIds.add(item.getId());
                    if (items.containsKey(item.getId())) {
                        updateCachedItem(item);
                    } else {
                        addNewItem(item);
                    }
                }
                for (Long cachedItemId : items.keySet()) {
                    if (!databaseItemIds.contains(cachedItemId)) {
                        removeCachedItem(cachedItemId);
                    }
                }
            } catch (SQLException error) {
                LOGGER.warn("Error refreshing items", error);
            } finally {
                writeUnlock();
            }
        }
    }

    protected void addNewItem(T item) {
        try {
            writeLock();
            items.put(item.getId(), item);
        } finally {
            writeUnlock();
        }
    }

    public void addItem(T item) throws SQLException {
        dataManager.addObject(item);
        addNewItem(item);
    }

    protected void updateCachedItem(T item) {
        try {
            writeLock();
            items.put(item.getId(), item);
        } finally {
            writeUnlock();
        }
    }

    public void updateItem(T item) throws SQLException {
        dataManager.updateObject(item);
        updateCachedItem(item);
    }

    protected void removeCachedItem(long itemId) {
        try {
            writeLock();
            items.remove(itemId);
        } finally {
            writeUnlock();
        }
    }

    public void removeItem(long itemId) throws SQLException {
        BaseModel item = getById(itemId);
        if (item != null) {
            dataManager.removeObject(baseClass, itemId);
            removeCachedItem(itemId);
        }
    }

    public final Collection<T> getItems(Set<Long> itemIds) {
        Collection<T> result = new LinkedList<>();
        for (long itemId : itemIds) {
            T item = getById(itemId);
            if (item != null) {
                result.add(item);
            }
        }
        return result;
    }

    public Set<Long> getAllItems() {
        try {
            readLock();
            return items.keySet();
        } finally {
            readUnlock();
        }
    }
}
