<%@tag language="java" pageEncoding="UTF-8"%>
<%@attribute name="label" required="true" type="java.lang.String" description="提示符"%>
<%@attribute name="name" required="true" type="java.lang.String" description="名称"%>
<%@attribute name="type" required="true" type="java.lang.String" description="类型"%>
<%@attribute name="value" required="false" type="java.lang.String" description="值"%>
<%@attribute name="placeholder" required="false" type="java.lang.String" description="占位符"%>
<form action=""></form>
<label>${label }：</label>
<input type="${type }" name="${name }" placeholder="${placeholder }" value="${value }"/>