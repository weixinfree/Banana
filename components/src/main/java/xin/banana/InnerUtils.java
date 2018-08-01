package xin.banana;

import android.app.Application;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

import xin.banana.components.Component;
import xin.banana.stream.Stream;

/**
 * 内部工具类
 * Created by wangwei on 2018/07/30.
 */
public class InnerUtils {

    private InnerUtils() {
        //no instance
    }

    public static Stream<String> readAssetsFiles(Application app, String path) {
        final AssetManager assets = app.getResources().getAssets();
        try {
            Stream.of(assets.list(path))
                    .map(name -> path + "/" + name)
                    .map(file -> readSingleAssetsFile(assets, file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Stream.empty();
    }

    private static String readSingleAssetsFile(AssetManager assetManager, String name) {
        try (final Reader reader = new BufferedReader(new InputStreamReader(assetManager.open(name)))) {
            final StringWriter writer = new StringWriter(1024);
            final char[] cache = new char[1024];
            int readCount;
            while ((readCount = reader.read(cache)) > 0) {
                writer.write(cache, 0, readCount);
            }

            return writer.toString();
        } catch (IOException e) {
            return "";
        }
    }

    public static Component newInstance(String className) {
        try {
            return ((Component) Class.forName(className).newInstance());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
