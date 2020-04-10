package com.homw.test.pattern.filter;

/**
 * @description 过滤器链实现
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
public abstract class AbstractFilter implements Filter {
	protected final Filter next;

	public AbstractFilter(Filter next) {
		this.next = next;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void action(Context context) throws Exception {
		if (next != null) {
			next.action(context);
		}
	}

	public final Filter getNext() {
		return next;
	}
}
