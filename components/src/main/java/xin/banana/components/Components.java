package xin.banana.components;

import android.app.Application;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Collections;
import java.util.List;

import xin.banana.base.Objects;
import xin.banana.stream.Comparators;
import xin.banana.stream.Stream;

import static xin.banana.Utils.readAssetsFiles;
import static xin.banana.base.Objects.requireNonNull;
import static xin.banana.stream.Stream.forEach_;

/**
 * 组件
 * Created by wangwei on 2018/07/30.
 */
public class Components {

    private Components() {
        //no instance
    }

    /**
     * [
     * {
     * "component":"com.test.UserComponent",
     * "priority": 1
     * },
     * {
     * "component":"com.test.StorageComponent",
     * "priority": 2
     * }
     * ]
     */
    public static String sComponentAssetsDir = "components";

    private static class Comp {
        final String name;
        final int priority;

        Comp(String name, int priority) {
            this.name = name;
            this.priority = priority;
        }
    }

    private static Stream<Comp> parse(String json) {
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
                                obj.optInt("priority", Integer.MAX_VALUE));
                    } catch (JSONException e) {
                        throw new AssertionError(e);
                    }
                });
    }

    private static volatile List<Component> sComponents = Collections.emptyList();

    public static void install(Application application) {
        requireNonNull(application);

        final List<Comp> comps = readAssetsFiles(application, sComponentAssetsDir)
                .flatMap(Components::parse)
                .toList();

        Collections.sort(comps, Comparators.compareInt(comp -> comp.priority));

        final List<Component> components = Stream.of(comps)
                .map(comp -> comp.name)
                .map(componentName -> {
                    try {
                        return Class.forName(componentName);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .map(aClass -> {
                    try {
                        return ((Component) aClass.newInstance());
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();


        forEach_(components, application::registerComponentCallbacks);
        forEach_(components, component -> component.onAppCreate(application));

        sComponents = components;
    }

    public static void onUserLogin() {
        forEach_(sComponents, Component::onUserLogin);
    }

    public static void onUserLogout() {
        forEach_(sComponents, Component::onUserLogout);
    }
}
