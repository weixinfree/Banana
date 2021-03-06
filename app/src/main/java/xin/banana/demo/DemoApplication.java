package xin.banana.demo;

import android.app.Application;

import xin.banana.Banana;

/**
 * Created by wangwei on 2018/08/02.
 */
public class DemoApplication extends Application {

    /*

    移动开发的几个重要方面:
    1. 组件化/解耦
        - 移动端最大的挑战在于快速的变化
        - 编程最大的敌人是复杂度
        - 移动端的复杂度主要来源于：业务纷繁复杂，业务需求快速变化
        - 业务的变化是发散的，倾向于弥漫发散，探索性质。需求特性和系统的整体性，内聚性是相悖的
        - 移动端的业务深度比较浅，数据经过后台接口的整合里，距离ui非常近了，业务逻辑会相对简单
        - 简单的业务逻辑，应该用简单直白的代码实现（典型的mvp模板套路太多，喧宾夺主）
        - 独立是应对变化的大杀器
        - 快速变化的另一个推论是: 可理解,可维护性十分重要
        - 移动端编码第一原则是 清晰，而后是简单
        - 在快速的变化中保持清晰
            1. 结构上要模块独立，以不变应对变化，降低关联改动风险
            2. DRY 优先级要降低，适度的copy 要比 不清晰的依赖关系更可取
            3. 独立，业务代码不要追求通用性，满足当前的需求，自己的需求即可
            4. 因为快速的发散式变化，移动业务几乎不需要为预想的需求预留设计冗余
            5. 越特殊的问题，越容易解决，假设更多，解集空间更小, 变化更少. 移动业务代码应该利用好这一点
        - 类比: 移动端类似颗粒物理 - 例如：沙堆
            - 看似独立的颗粒数量非常多 / 业务繁杂, 几乎所有的入口
            - 系统不够稳定，颗粒之间关系松散，多变 / 快速的变化
            - 系统可预测性比较低 / 业务需求演变倾向于探索式 和 发散变化
            - 长程关联很少出现（后台业务），各种尺度的短程关联动态变化 / 各组件之间会相互关联，但深度关联较少

        => 组件化解决方案: Components + ServiceFetcher （微信方案）
    2. 非四大组件上下文中对生命周期的需求
        - mixin 是组合的一种简化形式
        - mixin 模式是最适合的生命周期注入方式
        => 生命周期注入方案: LifecycleAwareMixin
    3. 单一module内的代码组织和职责划分
        - reactive view 趋势性, vue, react, flutter ... 等等
        - reactive view 模式 相较于 passive View 模式更简洁清晰，生产力更高 => Binding / Muggle
        - 复杂业务下，单向数据流更容易管理和理解 => StoreMixin
        - 系统内业务的另一个倾向是相互关联，store center 提供了简洁可控的 数据/状态层面的关联基础 => StoreMixin

    其它技术选型倾向 (简洁,清晰,高实现质量 最重要，性能次之，功能多/复杂度高的排除):
        1. 网络库 => retrofit
        2. 图像库 => glide
        3. 持久化 => json-base 或者方便的 Room，sugar等
        4. 存储管理 => storage
        5. 线程管理 => bolts-task
        6. 函数式编程 => stream

    生产力TODO:
        1. view 的在线预览，调试
        2. 部分动态化
     */

    @Override
    public void onCreate() {
        super.onCreate();
        Banana.install(this);
    }
}
