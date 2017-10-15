package com.nrs.nsnik.architecturecomponents.dagger.components;

import com.nrs.nsnik.architecturecomponents.dagger.modules.ContextModule;
import com.nrs.nsnik.architecturecomponents.dagger.modules.DatabaseModule;
import com.nrs.nsnik.architecturecomponents.utiil.DbUtil;

import dagger.Component;

@Component(modules = {ContextModule.class,DatabaseModule.class})
public interface DatabaseComponent {
    DbUtil getDbUtil();
}
