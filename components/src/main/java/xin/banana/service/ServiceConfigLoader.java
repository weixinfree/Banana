package xin.banana.service;

import android.app.Application;

import org.json.JSONArray;
import org.json.JSONException;

import xin.banana.base.Objects;
import xin.banana.base.Supplier;
import xin.banana.base.Suppliers;
import xin.banana.base.Tuple2;
import xin.banana.base.Tuple3;
import xin.banana.stream.Stream;

import static xin.banana.InnerUtils.newInstance;
import static xin.banana.InnerUtils.readAssetsFiles;

class ServiceConfigLoader {


    private Stream<Tuple3<String, String, Boolean>> parse(String json) {
        try {
            final JSONArray array = new JSONArray(json);

            final int length = array.length();
            return Stream.range(0, length)
                    .map(array::optJSONObject)
                    .filter(Objects::nonNull)
                    .map(obj -> {
                        try {
                            return new Tuple3<>(
                                    obj.getString("service"),
                                    obj.getString("impl"),
                                    obj.optBoolean("singleton", false));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return null;
                    })
                    .filter(Objects::nonNull);

        } catch (JSONException e) {
            return Stream.empty();
        }
    }

    Stream<Tuple2<Class<?>, Supplier<?>>> loadServicesFromAssetsConfig(Application application) {
        return readAssetsFiles(application, ServiceFetcher.sServiceAssetsDir)
                .flatMap(this::parse)
                .map(service -> {
                    final String clazz = service.first;
                    final String impl = service.second;
                    final Boolean isSingleton = service.third;

                    Supplier<?> supplier = () -> newInstance(impl);
                    if (isSingleton) {
                        supplier = Suppliers.singleton(supplier);
                    }

                    try {
                        return new Tuple2<>(Class.forName(clazz), supplier);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
