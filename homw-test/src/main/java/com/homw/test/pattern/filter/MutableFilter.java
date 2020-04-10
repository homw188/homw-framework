package com.homw.test.pattern.filter;

/**
 * @description 线程不安全的过滤器
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
public class MutableFilter extends AbstractFilter {
	public MutableFilter(Filter next) {
		super(next);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void action(Context context) throws Exception {
		Object obj = context.get();
		if (obj instanceof Message) {
			Message msg = (Message) obj;
			// msg.setMsgId((int) (Math.random() * 100));
			msg.setMsgId(msg.getMsgId() + 1);
		}

		// simulate a processing
		Thread.sleep((long) (Math.random() * 100));

		System.out.println(Thread.currentThread().getName() + "#middle:" + context.get());
		super.action(context);
	}

}
