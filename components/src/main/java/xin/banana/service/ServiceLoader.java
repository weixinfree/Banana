package xin.banana.service;

import org.json.JSONException;
import org.json.JSONObject;

import static xin.banana.base.Objects.requireNonNull;

/**
 * $end$
 * Created by wangwei on 2018/08/01.
 */
class ServiceLoader {


    private static class RegisterItem {
        final Class<?> serviceClass;
        final Class<?> implClass;

        static RegisterItem fromJson(JSONObject obj) {
            try {
                final Class<?> service = Class.forName(obj.getString("service"));
                final Class<?> impl = Class.forName(obj.getString("impl"));

                requireNonNull(service);
                requireNonNull(impl);

                return new RegisterItem(service, impl);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        RegisterItem(Class<?> serviceClass, Class<?> implClass) {
            this.serviceClass = serviceClass;
            this.implClass = implClass;
        }
    }
}
