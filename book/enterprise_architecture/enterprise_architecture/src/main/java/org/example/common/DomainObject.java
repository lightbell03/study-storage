package org.example.common;

import org.example.chap11.unitwork.UnitOfWork;

public abstract class DomainObject {
    private Object id;

    public Object getId() {
        return this.id;
    }

    protected void markNew() {
        UnitOfWork.CurrentUnitOfWorkThreadLocal.getCurrent().registerNew(this);
    }

    protected void markDirty() {
        UnitOfWork.CurrentUnitOfWorkThreadLocal.getCurrent().registerDirty(this);
    }

    protected void markRemoved() {
        UnitOfWork.CurrentUnitOfWorkThreadLocal.getCurrent().registerRemoved(this);
    }
}
