package xin.banana.mixin.dynamic;

import org.junit.Test;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * $end$
 * Created by wangwei on 2018/07/29.
 */
public class DynamicPropMixinTest {

    static class Any implements DynamicPropMixin {
    }

    @Test
    public void test() {
        final Any person = new Any();
        person.setProp("name", "xiaoming");
        person.setProp("age", 10);
        System.out.println(person.dump());

        assertThat(person.props().keySet(), containsInAnyOrder("name", "age"));

        assertThat(person.hasProp("name"), is(true));
        assertThat(person.getProp("name"), equalTo("xiaoming"));

        assertThat(person.hasProp("age"), is(true));
        assertThat(person.getProp("age"), equalTo(10));

        assertThat(person.delProp("name"), is(true));
        assertThat(person.hasProp("name"), is(not(true)));
        assertThat(person.delProp("name"), is(false));

        assertThat(person.props().keySet(), not(contains("name")));

        assertThat(person.delProp("non exsits"), is(false));
        System.out.println(person.dump());
    }

}