package com.sanq.product.config.utils.mvc.utils.interfaces;

/**
 * com.sanq.product.config.utils.mvc.utils.interfaces.BeanCopyCallback
 *
 * @author sanq.Yan
 * @date 2020/1/13
 */
@FunctionalInterface
public interface BeanCopyCallback<S, T> {

    void callback(S s, T t);

}
