package com.sanq.product.config.utils.mvc.utils;

import com.google.common.collect.Lists;
import com.sanq.product.config.utils.mvc.utils.interfaces.BeanCopyCallback;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * com.sanq.product.config.utils.mvc.utils.BeanCopyUtils
 *
 * @author sanq.Yan
 * @date 2020/1/13
 */
public class BeanCopyUtils extends BeanUtils {

    /**
     * copy类型不一致的字段
     *
     * @param source   源
     * @param target   目标
     * @param callback 在回调中对类型不一致的字段进行处理
     */
    public static <S, T> void copyProperties(S source, T target, BeanCopyCallback<S, T> callback) {
        copyProperties(source, target);

        if (null != callback) {
            callback.callback(source, target);
        }
    }


    /**
     * copy集合
     *
     * @param sources 源集合
     * @param target  转换目标
     * @return 转换完的list
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        return copyListProperties(sources, target, null);

    }

    /**
     * copy集合 对类型不一致的字段进行处理
     *
     * @param sources 源集合
     * @param target  转换目标
     * @return 转换完的list
     */
    private static <T, S> List<T> copyListProperties(List<S> sources, Supplier<T> target, BeanCopyCallback<S, T> callback) {
        if (sources != null && !sources.isEmpty())
            return Lists.newArrayList();

        List<T> list = new ArrayList<>(sources.size());
        sources.forEach(s -> {
            T t = target.get();
            copyProperties(s, t);
            list.add(t);
            if (null != callback)
                callback.callback(s, t);
        });
        return list;
    }
}
