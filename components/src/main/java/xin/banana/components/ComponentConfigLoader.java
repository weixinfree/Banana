package xin.banana.components;

import android.app.Application;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Collections;
import java.util.List;

import xin.banana.InnerUtils;
import xin.banana.base.Objects;
import xin.banana.stream.Comparators;
import xin.banana.stream.Stream;

import static xin.banana.InnerUtils.readAssetsFiles;
import static xin.banana.stream.Stream.forEach_;

class ComponentConfigLoader {

    private Stream<Comp> parse(String json) {
        final JSONArray array;
        try {
            array = new JSONArray(json);
        } catch (JSONException e) {
            return Stream.empty();
        }

        final int length = array.length();
        return Stream.range(0, length)
                .map(array::optJSONObject)
                .filter(Objects::nonNull)
                .map(obj -> {
                    try {
                        return new Comp(obj.getString("component"),
                                obj.optInt("initOrder", Integer.MAX_VALUE));
                    } catch (JSONException e) {
                        throw new AssertionError(e);
                    }
                });
    }

    List<Component> loadComponentsFromAssetsConfig(Application application) {
        final List<Comp> comps = readAssetsFiles(application, Components.sComponentAssetsDir)
                .flatMap(this::parse)
                .toList();

        Collections.sort(comps, Comparators.compareInt(comp -> comp.initOrder));

        final List<Component> components = Stream.of(comps)
                .map(comp -> comp.name)
                .map(InnerUtils::newInstance)
                .filter(Objects::nonNull)
                .toList();

        forEach_(components, application::registerComponentCallbacks);
        forEach_(components, component -> component.onAppCreate(application));

        return components;
    }

    private static class Comp {
        final String name;
        final int initOrder;

        Comp(String name, int priority) {
            this.name = name;
            this.initOrder = priority;
        }
    }
}
