package com.zebra.demo;

import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(DataProcessActivity activity);
    void inject(DetailActivity activity);

    void inject(JsonConverter jsonConverter);

    void inject(NetworkChangeReceiver reciver);

    void inject(MyForegroundService service);



}