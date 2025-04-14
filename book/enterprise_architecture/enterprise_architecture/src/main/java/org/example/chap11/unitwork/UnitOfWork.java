package org.example.chap11.unitwork;

import org.example.common.Assert;
import org.example.common.DomainObject;
import org.example.common.MapperRegistry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UnitOfWork {
    private List<DomainObject> newObjects = new ArrayList<>();
    private List<DomainObject> dirtyObjects = new ArrayList<>();
    private List<DomainObject> removedObjects = new ArrayList<>();

    public void registerNew(DomainObject obj) {
        Assert.notNull("id not null", obj.getId());
        Assert.isTrue("object not dirty", !dirtyObjects.contains(obj));
        Assert.isTrue("object not removed", !removedObjects.contains(obj));
        Assert.isTrue("object not already registered new", !newObjects.contains(obj));
        newObjects.add(obj);
    }

    public void registerDirty(DomainObject obj) {
        Assert.notNull("id not null", obj.getId());
        Assert.isTrue("object not removed", !removedObjects.contains(obj));
        if (!dirtyObjects.contains(obj) && !newObjects.contains(obj)) {
            dirtyObjects.add(obj);
        }
    }

    public void registerRemoved(DomainObject obj) {
        Assert.notNull("id not null", obj.getId());
        if (newObjects.remove(obj)) {
            return;
        }
        dirtyObjects.remove(obj);
        if (!removedObjects.contains(obj)) {
            removedObjects.add(obj);
        }
    }

    public void registerClean(DomainObject obj) {
        Assert.notNull("id not null", obj.getId());
    }

    public void commit() {
        insertNew();
        updateDirty();
        deleteRemoved();
    }

    private void insertNew() {
        for (Iterator<DomainObject> objects = newObjects.iterator(); objects.hasNext(); ) {
            DomainObject obj = objects.next();
            MapperRegistry.getMapper(obj.getClass()).insert(obj);
        }
    }

    private void updateDirty() {
        for (Iterator<DomainObject> objects = dirtyObjects.iterator(); objects.hasNext(); ) {
            DomainObject obj = objects.next();
            MapperRegistry.getMapper(obj.getClass()).update(obj);
        }
    }

    private void deleteRemoved() {
        for (Iterator<DomainObject> objects = removedObjects.iterator(); objects.hasNext(); ) {
            DomainObject obj = objects.next();
            MapperRegistry.getMapper(obj.getClass()).delete(obj);
        }
    }

    public static class CurrentUnitOfWorkThreadLocal {
        private static final ThreadLocal<UnitOfWork> current = new ThreadLocal<>();

        public static void newCurrent() {
            setCurrent(new UnitOfWork());
        }

        public static void setCurrent(UnitOfWork uow) {
            current.set(uow);
        }

        public static UnitOfWork getCurrent() {
            return current.get();
        }

        public static void remove() {
            current.remove();
        }
    }
}
