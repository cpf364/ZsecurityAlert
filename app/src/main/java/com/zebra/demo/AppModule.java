package com.zebra.demo;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Module
public class AppModule {

    @Provides
    @Singleton
    Map<String, AppLogData> provideAppLogMap() {
        return new HashMap<>();
    }


    @Provides
    @Singleton
    Map<String, AppManifestScanData> provideAppManifestScanInfo(){
        return new HashMap<>();
    }

    @Provides
    @Singleton
    List<CrashInfo> provideCrashInfoList(){
        return new ArrayList<>();
    }

    @Provides
    @Singleton
    Set<String> provideuniqueSSIDs(){
        return new LinkedHashSet<>();
    }



}